# MOM & POP’S Pizzaria’s Pizza System

This is the SWE 3313 team project to design a pizza order and delivery system for a new "Mom and Pop" style pizza shop. The system manages customers, takes orders, processes payments, and tracks inventory.

## Team: Stack UnderFlow

* **Jake Cashion:** Project Manager & Full Stack Developer 
* **Khason Murphys:** Front-End Development 
* **Amaya Cruz:** Front-End Development 
* **Arad Nasre Azadani:** Back-End Development 

## Project Structure

* `/assets`: Contains GUI images (icons, menu item photos, etc.)
* `/db`: Contains the Microsoft Access database file (`MomAndPops.accdb`).
* `/docs`: Contains all project planning, requirements, and design documents.
* `/src`: Contains all Java source code.
    * `/com/stackunderflow/pizzasystem/data`: Database connection, Seeding, and Data Managers.
    * `/com/stackunderflow/pizzasystem/model`: Data classes (Customer, Order, Pizza, etc.).
    * `/com/stackunderflow/pizzasystem/ui`: All GUI screens and Controllers.
    * `/com/stackunderflow/pizzasystem/util`: Helper classes (Password Hashing, Seeder).

## How to Run

1.  **Database Setup:**
    * Ensure `MomAndPops.accdb` is located in the `/db` folder at the project root.
    * Run the `src/com/stackunderflow/pizzasystem/util/DatabaseSeeder.java` file **once** to populate the database with the menu, inventory, and test users.

2.  **Launch the Application:**
    * Navigate to `src/com/stackunderflow/pizzasystem/ui/customer/AppLauncher.java`.
    * Run the `main` method to start the application.

## Test Credentials

Use these accounts to test the system features without signing up manually.

### **Customer Login**
* **Username:** `customer`
* **Password:** `password123`
* **Phone:** `5551234567` (Used for alternative login)

### **Manager/Employee Login**
* **Username:** `manager`
* **Password:** `admin123`
* **Role:** Manager
