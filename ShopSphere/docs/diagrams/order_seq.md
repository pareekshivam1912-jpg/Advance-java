# ShopSphere — Atomic Checkout & Order Placement Sequence Diagram

This sequence diagram depicts the transaction boundary and execution flow during checkout, illustrating how `CheckoutService` orchestrates multiple DAOs within a single ACID transaction (`setAutoCommit(false)`, `commit()`, `rollback()`).

```mermaid
sequenceDiagram
    autonumber
    actor Customer as Authenticated Customer
    participant AF as AuthenticationFilter
    participant CS as CheckoutServlet
    participant ChkSvc as CheckoutService
    participant Conn as JDBC Connection
    participant CartDAO as CartDAO
    participant ProdDAO as ProductDAO
    participant OrderDAO as OrderDAO
    participant ItemDAO as OrderItemDAO
    participant PayDAO as PaymentDAO
    participant CpnDAO as CouponDAO
    participant DB as MySQL Database

    Customer->>AF: POST /checkout (addressId, paymentMethod, couponCode)
    AF->>AF: Verify session.getAttribute("currentUser") != null
    AF->>CS: doPost(req, resp)

    CS->>ChkSvc: processCheckout(userId, addressId, couponCode, paymentMethod)
    ChkSvc->>Conn: DBUtil.getConnection()
    ChkSvc->>Conn: setAutoCommit(false) [BEGIN TRANSACTION]

    ChkSvc->>CartDAO: findByUserId(conn, userId)
    CartDAO->>DB: SELECT * FROM cart WHERE user_id=?
    DB-->>CartDAO: List<CartItem>
    CartDAO-->>ChkSvc: cartItems

    loop For each CartItem
        ChkSvc->>ProdDAO: findByIdForUpdate(conn, item.getProductId())
        ProdDAO->>DB: SELECT * FROM products WHERE id=? FOR UPDATE
        DB-->>ProdDAO: Product row (current stock)
        ProdDAO-->>ChkSvc: product
        ChkSvc->>ChkSvc: Validate item.getQuantity() <= product.getStock()
        ChkSvc->>ProdDAO: updateStock(conn, id, -item.getQuantity())
        ProdDAO->>DB: UPDATE products SET stock_quantity = stock_quantity - ? WHERE id=?
    end

    ChkSvc->>ChkSvc: Calculate subtotal, discount, tax (18% GST), shipping, net total
    ChkSvc->>OrderDAO: create(conn, order)
    OrderDAO->>DB: INSERT INTO orders (...) VALUES (...)
    DB-->>OrderDAO: Generated Order ID
    OrderDAO-->>ChkSvc: orderWithId

    loop For each item
        ChkSvc->>ItemDAO: create(conn, orderItem)
        ItemDAO->>DB: INSERT INTO order_items (...) VALUES (...)
    end

    ChkSvc->>PayDAO: create(conn, payment)
    PayDAO->>DB: INSERT INTO payments (...) VALUES (...)

    opt Coupon Code Present
        ChkSvc->>CpnDAO: recordUsage(conn, couponId, userId, orderId)
        CpnDAO->>DB: INSERT INTO coupon_usage ...; UPDATE coupons SET times_used=times_used+1
    end

    ChkSvc->>CartDAO: clearCart(conn, userId)
    CartDAO->>DB: DELETE FROM cart WHERE user_id=?

    ChkSvc->>Conn: commit() [TRANSACTION SUCCESSFUL]
    ChkSvc->>Conn: setAutoCommit(true) & close()
    ChkSvc-->>CS: completedOrder

    CS-->>Customer: 302 Redirect to /order/details?id={orderId} (Order Confirmation)
```
