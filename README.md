# Checkpoint

A modern hotel booking and management platform built using Spring Boot.

Checkpoint provides a secure backend for managing hotels, rooms, inventory, bookings, users, and online payments through RESTful APIs. The application focuses on scalability, maintainability, and clean architecture while implementing industry-standard backend development practices.

---

## Overview

Checkpoint is designed to simplify hotel operations by providing a centralized platform where users can browse hotels, search available rooms, make bookings, and complete secure online payments. Administrators can manage hotels, room inventories, pricing, and bookings through dedicated APIs.

The project follows a layered architecture using Spring Boot and Spring Security, with JWT-based authentication, PostgreSQL for data persistence, and Stripe for payment processing.

---

## Key Features

### Authentication & Authorization

- User Registration
- User Login
- JWT Authentication
- Role-Based Access Control
- Password Encryption using Spring Security

---

### Hotel Management

- Create Hotels
- Update Hotel Information
- Browse Hotels
- Search Hotels
- Hotel Pricing Information

---

### Room Management

- Add Rooms
- Update Rooms
- Room Availability
- Room Pricing

---

### Inventory Management

- Daily Inventory Tracking
- Room Availability Management
- Inventory Updates

---

### Booking Management

- Hotel Booking
- Booking Status Tracking
- Guest Information
- Booking History

---

### Dynamic Pricing

Checkpoint uses the **Strategy Design Pattern** to calculate room prices dynamically.

Implemented pricing strategies include:

- Base Pricing
- Occupancy Pricing
- Holiday Pricing
- Surge Pricing
- Urgency Pricing

---

### Payment Integration

- Stripe Checkout
- Stripe Webhooks
- Payment Status Tracking

---

### API Features

- RESTful APIs
- DTO-Based Communication
- Global Exception Handling
- Standardized API Responses
- Validation Support

---

# Technology Stack

## Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

## Database

- PostgreSQL

## Authentication

- JSON Web Token (JWT)

## Payment Gateway

- Stripe

## API Documentation

- Swagger / OpenAPI

## Utilities

- Lombok
- ModelMapper

---

# Project Architecture

```
Client
    │
    ▼
REST Controllers
    │
    ▼
Business Services
    │
    ▼
Repositories
    │
    ▼
PostgreSQL Database
```

---

# Project Structure

```
src
│
├── advice
│
├── config
│
├── controller
│
├── dto
│
├── entity
│
├── exception
│
├── repository
│
├── security
│
├── service
│
├── strategy
│
└── util
```

---

# Design Patterns

The project incorporates several software engineering practices and design patterns.

### Strategy Pattern

Used for dynamic room pricing.

Implemented strategies include:

- Base Pricing
- Occupancy Pricing
- Holiday Pricing
- Surge Pricing
- Urgency Pricing

### Repository Pattern

Provides abstraction over data persistence.

### Service Layer Pattern

Separates business logic from controllers.

### DTO Pattern

Used for request and response communication.

---

# API Modules

| Module | Description |
|----------|------------|
| Authentication | User registration and login |
| Users | User profile management |
| Hotels | Hotel CRUD operations |
| Rooms | Room management |
| Inventory | Inventory management |
| Bookings | Booking workflow |
| Payments | Stripe integration |
| Webhooks | Stripe webhook handling |

---

# Security

Checkpoint uses Spring Security with JWT Authentication.

Features include:

- Stateless Authentication
- Password Encryption
- Role-Based Authorization
- Protected REST APIs

---

# Getting Started

## Prerequisites

- Java 21
- Maven
- PostgreSQL

---

## Clone Repository

```bash
git clone https://github.com/Mksthatsall/checkpoint.git
```

---

## Configure Application

Create

```
src/main/resources/application.properties
```

Example:

```properties
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=

jwt.secret=

stripe.secret.key=
```

---

## Build Project

```bash
mvn clean install
```

---

## Run

```bash
mvn spring-boot:run
```

or

```bash
java -jar target/checkpoint.jar
```

---

# API Documentation

Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

# Future Improvements

- React Frontend
- Docker Support
- AWS Deployment
- CI/CD Pipeline
- Redis Caching
- Email Notifications
- Unit Testing
- Integration Testing

---

# Contributing

Contributions are welcome.

Please fork the repository, create a feature branch, commit your changes, and submit a pull request.

---

# License

This project is available for educational and learning purposes.

---

# Author

**Mridul Krishna Sharma**

B.Tech Computer Science & Engineering

