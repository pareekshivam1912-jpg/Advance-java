# ShopSphere — Desktop Administration Subsystem (`swing-admin`)

This document details the **Java Swing Desktop Administration** application of ShopSphere, covering the RTU Advanced Java syllabus: Java Swing components, Model-View-Controller (MVC) architecture, Event Dispatch Thread (EDT) safety with `javax.swing.SwingWorker`, and Pluggable Look and Feel (PLAF).

---

## 1. Architecture Overview: Swing MVC

The desktop application is strictly decomposed according to the **Model-View-Controller (MVC)** design pattern, sharing the business services and JDBC DAO layer with the web application (`shopsphere-core`):

```
+--------------------------------------------------------------------------+
|  VIEW (com.shopsphere.swing.view)                                        |
|  - LoginFrame (JFrame, JTextField, JPasswordField, JCheckBox, JButton)   |
|  - AdminDashboard (JMenuBar, JTabbedPane, Status Bar)                    |
|  - ProductPanel (JTable, JSplitPane, JComboBox, JRadioButton, JTextArea) |
|  - CategoryPanel, CustomerPanel, OrderPanel, InventoryPanel              |
|  * Strictly NO SQL or Database code inside views *                       |
+--------------------------------------------------------------------------+
                     ▲                                      │
       Data Binding  │                                      │ User Actions
       & Events      │                                      ▼
+--------------------------------------------------------------------------+
|  CONTROLLER (com.shopsphere.swing.controller)                            |
|  - LoginController, ProductController, CategoryController                |
|  - CustomerController, OrderController, InventoryController              |
|  * Executes DB tasks asynchronously off the EDT via SwingWorker *        |
+--------------------------------------------------------------------------+
                     │                                      ▲
                     ▼                                      │
+--------------------------------------------------------------------------+
|  MODEL (com.shopsphere.swing.model & core)                               |
|  - ProductTableModel, CategoryTableModel, CustomerTableModel             |
|  - OrderTableModel, InventoryTableModel (AbstractTableModel)             |
|  - Shared Domain POJOs: Product, Category, User, Order, OrderItem        |
+--------------------------------------------------------------------------+
                     │
                     ▼
+--------------------------------------------------------------------------+
|  SHARED SERVICE & DAO LAYER (com.shopsphere.service / dao)               |
|  - ServiceFactory, ProductService, UserService, OrderService             |
|  - PreparedStatement JDBC queries -> MySQL localhost:3306/shopsphere     |
+--------------------------------------------------------------------------+
```

---

## 2. Syllabus Swing Components Verified

The application explicitly utilizes all standard Swing components mandated by the syllabus:

| Component | Class Name | Location in Project | Purpose / Demonstration |
|---|---|---|---|
| **Top-Level Window** | `JFrame` | `LoginFrame`, `AdminDashboard` | Main application windows with custom title, size, and close operations. |
| **Containers & Panels** | `JPanel`, `JSplitPane`, `JTabbedPane` | `AdminDashboard`, `ProductPanel` | Modular layout organization, tabbed views, and split form/table editors. |
| **Labels & Text** | `JLabel`, `JTextField`, `JPasswordField`, `JTextArea` | `LoginFrame`, `ProductPanel`, `CategoryPanel` | User prompts, text fields, password masking with echo char toggling, and multi-line descriptions. |
| **Buttons & Selectors** | `JButton`, `JCheckBox`, `JRadioButton`, `ButtonGroup`, `JComboBox` | Across all views | Form submission, password show/hide, active/inactive radio groups, category dropdowns. |
| **Data Grid & Tables** | `JTable` + custom `AbstractTableModel` | All 5 panels | Tabular display of catalog, orders, users, and low-stock products with `fireTableDataChanged()`. |
| **Scrolling** | `JScrollPane` | All panels | Smooth vertical/horizontal scrolling for tables and multi-line text areas. |
| **Menus** | `JMenuBar`, `JMenu`, `JMenuItem` | `AdminDashboard` | Standard application menu bar with File, Look & Feel, and Help submenus. |
| **Dialogs** | `JOptionPane` | Across all controllers/views | Informational, confirmation, warning, and error dialog popups. |
| **Layout Managers** | `BorderLayout`, `GridBagLayout`, `GridLayout`, `FlowLayout` | Across all views | Responsive component positioning adhering to modern layout standards. |

---

## 3. Concurrency: Event Dispatch Thread (EDT) Safety

Swing is **single-threaded**. All GUI rendering and event handling happens on the **Event Dispatch Thread (EDT)**. If a slow database query runs on the EDT, the entire user interface freezes.

ShopSphere guarantees EDT safety:
1. **Startup**: Initiated via `SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));`.
2. **Database Queries**: All database operations in controllers execute inside `javax.swing.SwingWorker.doInBackground()` on a background thread.
3. **UI Updates**: Results return to the UI inside `SwingWorker.done()`, which is guaranteed by the JVM to execute safely on the EDT.

---

## 4. Pluggable Look and Feel (PLAF)

The Look and Feel can be switched dynamically at runtime without restarting the application via the **Look & Feel** menu:
- **System Native Look & Feel**: `UIManager.getSystemLookAndFeelClassName()`
- **Nimbus Look & Feel**: `"javax.swing.plaf.nimbus.NimbusLookAndFeel"`
- **Metal (Java Default)**: `UIManager.getCrossPlatformLookAndFeelClassName()`

Implementation:
```java
UIManager.setLookAndFeel(lafClassName);
SwingUtilities.updateComponentTreeUI(this);
```

---

## 5. How to Run the Swing Admin Application

### Option A: Via Maven Exec Plugin (Development)
```powershell
mvn exec:java -pl swing-admin -Dexec.mainClass="com.shopsphere.swing.SwingAdminApp"
```

### Option B: Standalone Fat Executable JAR
```powershell
java -jar swing-admin/target/shopsphere-swing-admin-1.0.0-jar-with-dependencies.jar
```

### Default Login Credentials:
- **Email:** `admin@shopsphere.com`
- **Password:** `Admin@123`
*(Or click the **"Use Seed Admin"** button on `LoginFrame` to autofill!)*
