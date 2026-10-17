# ShopSphere — Entity Relationship (ER) Diagram

This diagram visualizes the relational schema of **ShopSphere** (12 tables), showing primary keys, foreign keys, constraints, and cardinalities in third normal form (3NF).

```mermaid
erDiagram
    USERS ||--o{ ADDRESSES : "has"
    USERS ||--o{ ORDERS : "places"
    USERS ||--o{ CART : "owns"
    USERS ||--o{ WISHLIST : "saves"
    USERS ||--o{ REVIEWS : "writes"
    USERS ||--o{ COUPON_USAGE : "redeems"

    CATEGORIES ||--o{ PRODUCTS : "contains"
    PRODUCTS ||--o{ CART : "in"
    PRODUCTS ||--o{ WISHLIST : "in"
    PRODUCTS ||--o{ ORDER_ITEMS : "ordered_in"
    PRODUCTS ||--o{ REVIEWS : "receives"

    ORDERS ||--|{ ORDER_ITEMS : "composed_of"
    ORDERS ||--|| PAYMENTS : "paid_by"
    ORDERS ||--o| ADDRESSES : "delivered_to"
    ORDERS ||--o| COUPONS : "applies"

    COUPONS ||--o{ COUPON_USAGE : "tracked_in"

    USERS {
        int id PK
        varchar email UK
        varchar password_hash
        varchar full_name
        varchar phone
        enum role "CUSTOMER, ADMIN"
        boolean is_active
        timestamp created_at
        timestamp updated_at
    }

    ADDRESSES {
        int id PK
        int user_id FK
        varchar full_name
        varchar phone
        varchar address_line1
        varchar address_line2
        varchar city
        varchar state
        varchar postal_code
        varchar country
        boolean is_default
    }

    CATEGORIES {
        int id PK
        varchar name UK
        varchar slug UK
        text description
        varchar image_url
        boolean is_active
        int display_order
    }

    PRODUCTS {
        int id PK
        int category_id FK
        varchar name
        varchar slug UK
        varchar sku UK
        text description
        decimal price
        decimal compare_price
        int stock_quantity
        varchar image_url
        boolean is_active
        boolean is_featured
    }

    CART {
        int id PK
        int user_id FK
        int product_id FK
        int quantity
        timestamp added_at
    }

    WISHLIST {
        int id PK
        int user_id FK
        int product_id FK
        timestamp added_at
    }

    COUPONS {
        int id PK
        varchar code UK
        enum discount_type "PERCENTAGE, FLAT"
        decimal discount_value
        decimal min_order_amount
        decimal max_discount_amount
        date valid_from
        date valid_until
        int max_uses
        int times_used
        boolean is_active
    }

    ORDERS {
        int id PK
        varchar order_number UK
        int user_id FK
        int address_id FK
        int coupon_id FK
        enum status "PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED"
        decimal subtotal
        decimal discount_amount
        decimal tax_amount
        decimal shipping_fee
        decimal total_amount
        enum payment_method "COD, CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING"
        enum payment_status "PENDING, PAID, FAILED, REFUNDED"
        text notes
        timestamp created_at
    }

    ORDER_ITEMS {
        int id PK
        int order_id FK
        int product_id FK
        varchar product_name
        varchar product_sku
        decimal price
        int quantity
        decimal total_price
    }

    PAYMENTS {
        int id PK
        int order_id FK, UK
        varchar transaction_ref UK
        enum payment_method "COD, CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING"
        decimal amount
        enum status "PENDING, SUCCESS, FAILED"
        text gateway_response
        timestamp created_at
    }

    REVIEWS {
        int id PK
        int product_id FK
        int user_id FK
        int rating "1 to 5"
        varchar title
        text comment
        boolean is_approved
        timestamp created_at
    }

    COUPON_USAGE {
        int id PK
        int coupon_id FK
        int user_id FK
        int order_id FK
        timestamp used_at
    }
```
