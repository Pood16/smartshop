# SmartShop

## Context:
SmartShop is a commercial management web application designed for MicroTech Morocco, a B2B distributor of computer equipment based in Casablanca. The application will manage a portfolio of 650 active clients with a progressive discount loyalty system and multi-payment methods for split payments per invoice. The system will ensure complete traceability of all financial events through an immutable history and optimize cash flow management.

## Important Notes:
* This application is purely backend REST (API only)
* No graphical interface
* Tests and demonstrations will be done via an API tester (Postman or Swagger)
* Authentication via HTTP Session (login/logout)
* No JWT, No Spring Security
* Role management: ADMIN (MicroTech employee who uses the SmartShop application) and CLIENT (customer companies that purchase from MicroTech)

## FUNCTIONAL REQUIREMENTS

### 1. Client Management
* Create a client
* View client information
* Update client data (personal information)
* Delete a client
* Automatically track:
  * Statistics: Total number of orders + Cumulative amount: cumulative total of confirmed orders
  * Date of first and last order
  * View order history by displaying the list of orders made by a specific client:
    * Order identifier
    * Order creation date
    * Total amount including tax
    * Order status (PENDING, CONFIRMED, CANCELED, REJECTED)

### 2. Automatic Loyalty System
* Automatic level calculation based on client history:
  * BASIC: Default client (0 orders)
  * SILVER: From 3 orders OR 1,000 cumulative
  * GOLD: From 10 orders OR 5,000 cumulative
  * PLATINUM: From 20 orders OR 15,000 cumulative
* Level update after each confirmed order
* Apply discounts according to current level:
  * SILVER: 5% if subtotal ≥ 500
  * GOLD: 10% if subtotal ≥ 800
  * PLATINUM: 15% if subtotal ≥ 1200

Subtotal = Pre-tax order amount (before discounts)

#### DETAILED EXPLANATIONS:

**PART 1: How to OBTAIN a level**

The level is calculated automatically according to the TOTAL orders and client expenses:
* Total number of orders placed (confirmed by admin)
* Total amount spent since registration (client creation)

**PART 2: How to USE this level**

Once the level is acquired, it entitles to discounts ON FUTURE ORDERS if they reach a certain amount

#### COMPLETE EXAMPLE: Client History

**Amine registers → Level: BASIC**

**January - February: Acquiring SILVER level**

| Order 1: 250DH | Order 2: 350DH | Order 3: 450DH |
|----------------|----------------|----------------|
| Current level: BASIC<br>Discount: 0% (BASIC client has no discount)<br>After ADMIN confirmation: 1 order, 250DH cumulative → remains BASIC | Current level: BASIC<br>Discount: 0%<br>After ADMIN confirmation: 2 orders, 600DH cumulative → remains BASIC | Current level: BASIC (not yet validated)<br>Discount: 0%<br>After ADMIN confirmation: 3 orders, 1050DH cumulative<br>BECOMES SILVER |

**March - April: Using SILVER benefits**

| Order 4: 600DH | Order 5: 3500DH | Order 6: 900DH |
|----------------|-----------------|----------------|
| Current level: SILVER<br>Discount: 5% = -30DH (SILVER + order ≥ 500DH)<br>Final price: 570DH<br>After confirmation: 4 orders, 1650DH cumulative → remains SILVER | Current level: SILVER<br>Discount: 5% = -175DH<br>Final price: 3325DH<br>After ADMIN confirmation: 5 orders, 5150DH cumulative → BECOMES GOLD | Current level: GOLD<br>Discount: 10% = -90DH (GOLD + order ≥ 800DH)<br>Final price: 810DH<br>After ADMIN confirmation: ... etc |

### 3. Product Management
* Add products
* Modify product information
* Delete products (soft delete if existing orders)
* View product list with filters and pagination

**Note:** If a product is used in existing orders and you want to delete it, it is marked as "deleted" (soft delete). It no longer appears in the product list but remains visible in old orders.

### 4. Order Management
* Create a multi-product order with quantities
* Validate prerequisites:
  * Stock availability for each product
* Apply cumulative discounts:
  * Loyalty discount according to client level
  * Promo code PROMO-XXXX (+5% if valid)
* Automatically calculate:
  * Pre-tax subtotal: sum of (pre-tax price × product quantity)
  * Total discount amount (promo code or loyalty level)
  * Pre-tax amount after discount = Pre-tax subtotal - Discount amount
  * VAT 20% (configurable) = Pre-tax amount after discount × 20%
  * Total with tax = Pre-tax amount after discount + VAT

**Note:** VAT is calculated on the amount AFTER discount (standard in Morocco)

**Example:** Subtotal 1,000 DH - Discount 100 DH = 900 DH → VAT 180 DH → Total 1,080 DH

* Update after validation:
  * Decrement product stock
  * Update client statistics (totalOrders, totalSpent)
  * Recalculate loyalty level
* Manage statuses:
  * PENDING: awaiting validation
  * CONFIRMED: order validated by ADMIN (after full payment)
  * CANCELED: manually canceled by ADMIN
  * REJECTED: refused (insufficient stock)

### 5. Multi-Payment System

#### Accepted Payment Methods

| CASH | CHECK | TRANSFER |
|------|-------|----------|
| Legal limit: 20,000 DH maximum per payment (Art. 193 CGI)<br>Immediate payment<br>Possible statuses: Collected<br>Requires receipt | Can be deferred (future due date)<br>Possible statuses: Pending / Collected / Rejected<br>Requires: number, bank, due date | Immediate or deferred payment<br>Possible statuses: Pending / Collected / Rejected<br>Requires: reference, bank |

An order can be paid in installments with different payment methods.

**Important rule:** An order must be fully paid (remaining_amount = 0) before an ADMIN can validate it and change it to CONFIRMED status.

#### Split Payment Example:

**Order of 10,000 DH**

| Payment 1 - 05/11/2025 | Payment 2 - 08/11/2025 | Payment 3 - 12/11/2025 |
|------------------------|------------------------|------------------------|
| Amount: 6,000 DH<br>Method: CASH<br>Reference: REÇU-001<br>Status: Collected<br>Remaining due: 4,000 DH<br>→Order remains PENDING | Amount: 3,000 DH<br>Method: CHECK<br>Reference: CHQ-7894561<br>Bank: BMCE Bank<br>Due date: 20/11/2025<br>Status: Awaiting collection<br>Remaining due: 1,000 DH<br>→Order remains PENDING | Amount: 1,000 DH<br>Method: TRANSFER<br>Reference: VIR-2025-11-12-4521<br>Bank: Attijariwafa Bank<br>Status: Collected<br>Remaining due: 0 DH<br>→Order can now be validated by ADMIN → CONFIRMED |

### 6. Critical Business Rules
* Stock validation: requested_quantity ≤ available_stock
* Rounding: all amounts to 2 decimals
* Promo codes: strict format PROMO-XXXX, single use possible @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
* VAT rate: 20% by default (configurable via configuration)

#### Business Validations
* An order without a client or without items is refused
* An order can only be validated (CONFIRMED) by admin if fully paid
* Error messages must be clear

## TECHNICAL REQUIREMENTS:

### 1. Architecture & Technical Stack
* Application type: Backend REST API only (no frontend)
* Testing and simulation: Via Postman or Swagger
* Framework: Spring Boot
* Java: Version 8 or higher
* API: REST with JSON
* Database: PostgreSQL or MySQL
* ORM: Spring Data (JPA/Hibernate)
* Unit Tests: JUnit, Mockito
* Layered architecture (Controller, Service, Repository, Entity, DTO, …)
* Data validation with annotations
* Use of interfaces and implementation
* Centralized exception handling
* Lombok and Builder Pattern to simplify entity management
* MapStruct for conversion between entities, DTOs and View Models
* Fundamental Java concepts: Stream API, Java Time API, Lambda expressions

#### Authentication: HTTP Session
* No JWT, No Spring Security, Simple session with login/logout

### 2. Data Model
* **User**: id, username, password, role (ADMIN/CLIENT)
* **Client**: id, name, email, loyalty level
* **Product**: id, name, unit price, available stock
* **Order**: id, client, list of items, date, subtotal, discount, VAT, total, promo code, status, remaining_amount
* **OrderItem**: id, product, quantity, unit price, line total
* **Payment**: id, order_id, payment_number: Sequential payment number for this invoice (1, 2, 3...), amount, payment_type, payment_date: Date when the client made the payment, collection_date: Actual collection date

**Note:** You can add other relevant attributes according to your analysis

### 3. System Enums
* **UserRole**: represents the user's role:
  * ADMIN: MicroTech employee (full system management)
  * CLIENT: Customer company (consultation only)
* **CustomerTier**: represents the client's loyalty level:
  * BASIC
  * SILVER
  * GOLD
  * PLATINUM
* **OrderStatus**: represents an order's status:
  * PENDING: order awaiting ADMIN validation
  * CONFIRMED: order validated by ADMIN (after full payment)
  * CANCELED: order manually canceled by ADMIN (only if PENDING)
  * REJECTED: order refused due to insufficient stock
* **PaymentStatus**: represents a payment's status:
  * EN_ATTENTE (PENDING): payment received but not yet collected
  * ENCAISSÉ (COLLECTED): amount actually received
  * REJETÉ (REJECTED): payment rejected

### 4. Order Status Management:

#### Automatic transitions (managed by the system):
* PENDING → REJECTED: if insufficient stock at order creation time

#### Manual transitions (via API endpoint - ADMIN only):
* PENDING → CONFIRMED: validation by ADMIN (after full payment verification)
* PENDING → CANCELED: cancellation by ADMIN
* Final statuses: CONFIRMED, REJECTED, CANCELED (no order modification will be possible)

#### Permission Matrix

| CLIENT can only: | ADMIN can do everything: |
|------------------|--------------------------|
| Log in<br>View THEIR OWN data:<br>• Profile and loyalty level<br>• Order history<br>• Statistics (number of orders, cumulative amount)<br>View product list (read-only)<br>CANNOT create, modify, delete anything<br>CANNOT see other clients' data | All operations (full CRUD)<br>View all clients<br>Create orders for any client<br>Record payment<br>Validate, cancel and reject orders |

### 5. Error Handling
* The @ControllerAdvice class centralizes exception handling
* Errors must be translated into consistent HTTP codes:
  * 400 → validation error
  * 401 → not authenticated
  * 403 → access denied (insufficient permissions)
  * 404 → resource not found
  * 422 → business rule violated (insufficient stock, order already validated, etc.)
  * 500 → internal error
* Each JSON response must include:
  * A timestamp
  * The HTTP code
  * The error type
  * The explanatory message
  * The request path

## Pedagogical Modality
Individual brief
* Launch date: 11/24/2025
* Deadline: 11/28/2025
* Duration: 5 days

## Evaluation Modality
45 minutes divided as follows:
* 05 minutes: Presentation + demonstration of application features
* 05 minutes: Code explanation and organization
* 20 minutes: Knowledge assessment (Q&A)
* 15 minutes: Practical situation

## Expected Deliverables
* GitHub link to complete source code
* UML class diagram (image format)
* JIRA project to show on evaluation day
* A good README

## Success Criteria
* Application starts without errors and connects correctly to the database
* Validations, discounts, VAT and stock are correctly managed
* Errors are properly handled and sent in consistent JSON format
* Architecture (Controller-Service-Repository-DTO-Mapper) is clear and clean

## Targeted Technical Skills
* C1 - N2: Install and configure work environment according to the project
* C3 - N2: Develop business components
* C4 - N2: Contribute to IT project management
* C6 - N2: Define software architecture of an application
* C7 - N2: Design and implement a relational database
* C8 - N2: Develop data access components for SQL and NoSQL
* C9 - N2: Prepare and execute application test plans

## Targeted Transversal Skills
* C1 - N2: Plan work to be done individually and in teams to optimize work necessary to achieve the target objective N2
* C6 - N2: Present completed work by synthesizing results, approach and answering questions to return it to the client
* C8-N2: Interact in a professional context in a respectful and constructive manner to promote collaboration

## Instructions:

### Before the start of the defense:
* Prepare your IDE (Open the project)
* Prepare your class diagram and JIRA
* Prepare the database with at least 5 records in each database table
* Open your GitHub repository
* Prepare and open simple and clear presentation slides

### Defense procedure:
* Introduction: Start with a brief presentation of the project, its objective, its usefulness and the technologies
* Have a Postman collection of all requested endpoints, or API documentation via Swagger
* Explain the overall architecture of the project, including entity structure, relationships between them and other layers
* Demonstration with your API Tester: Creation, Addition, modification, automatic calculation...
* Knowledge assessment: The evaluator will ask you questions to assess your level of mastery of the concepts and technologies covered
* Practical situation: You will be asked to code a method or make a modification to your code (business logic, introduce a new service implementation, etc.)