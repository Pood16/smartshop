# SmartShop 🛒

A modern commercial management web application built with Spring Boot, providing comprehensive e-commerce functionality with secure authentication and RESTful APIs.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Testing](#testing)
- [Contributing](#contributing)
- [License](#license)

## ✨ Features

- **User Authentication & Authorization**: JWT-based secure authentication system
- **Product Management**: CRUD operations for products
- **Client Management**: Customer information and profile management
- **Order Processing**: Complete order lifecycle management
- **Payment Integration**: Payment processing and tracking
- **RESTful API**: Well-structured REST endpoints
- **API Documentation**: Interactive Swagger UI documentation
- **Data Validation**: Input validation using Bean Validation
- **Security**: Spring Security integration with role-based access control

## 🛠 Tech Stack

- **Framework**: Spring Boot 3.5.8
- **Language**: Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA / Hibernate
- **Security**: Spring Security + JWT (JSON Web Tokens)
- **API Documentation**: SpringDoc OpenAPI 3
- **Object Mapping**: MapStruct 1.5.5
- **Build Tool**: Maven
- **Additional Libraries**:
  - Lombok (Boilerplate reduction)
  - Bean Validation
  - MySQL Connector

## 📦 Prerequisites

Before running this application, ensure you have:

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## 🚀 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Pood16/smartshop.git
   cd smartshop
   ```

2. **Create MySQL database**
   ```sql
   CREATE DATABASE smartshopdb;
   ```

3. **Configure environment variables Based on the properties file**

```properties
# Database Configuration
DB_URL=your-data-base-url
DB_USERNAME=your-username
DB_PASSWORD=your_password

# JWT Configuration
JWT_SECRET=your-secret-key
JWT_EXPIRATION=86400000

```

4. **Install dependencies**
   ```bash
   mvn clean install
   ```
### Important Notes:
- `JWT_SECRET`: Must be at least 256 bits (32 characters) for HS256 algorithm
- `JWT_EXPIRATION`: Token expiration time in milliseconds (default: 24 hours)


## 🏃 Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using Java

```bash
mvn clean package
java -jar target/smartshop-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Main API Endpoints

#### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

#### Products
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

#### Clients
- `GET /api/clients` - Get all clients
- `GET /api/clients/{id}` - Get client by ID
- `POST /api/clients` - Create new client
- `PUT /api/clients/{id}` - Update client
- `DELETE /api/clients/{id}` - Delete client

#### Orders
- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create new order
- `PUT /api/orders/{id}` - Update order
- `DELETE /api/orders/{id}` - Delete order

#### Payments
- `GET /api/payments` - Get all payments
- `GET /api/payments/{id}` - Get payment by ID
- `POST /api/payments` - Process payment

## 📁 Project Structure

```
smartshop/
├── src/
│   ├── main/
│   │   ├── java/com/ouirghane/smartshop/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── mapper/          # MapStruct mappers
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── security/        # Security configurations
│   │   │   ├── service/         # Business logic
│   │   │   └── SmartshopApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/                    # Test classes
├── docs/                        # API documentation
├── .env.example                 # Environment variables template
├── pom.xml                      # Maven configuration
└── README.md
```

## 🧪 Testing

Run the test suite:

```bash
mvn test
```
## 👥 Authors

- **LAHCEN OUIRGHANE** - [GitHub Profile](https://github.com/pood16)


