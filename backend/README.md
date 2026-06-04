# POS System

A full-stack Point of Sale application built for retail management. Supports product cataloguing, inventory, customer management, cart operations, and order processing with role-based access control.

## Live Demo
> Coming soon

## Tech Stack

**Backend**
- Java 17, Spring Boot 3
- Spring Security with JWT authentication
- Spring Data MongoDB
- Maven

**Frontend**
- Next.js 14 (App Router)
- Tailwind CSS
- Axios

**Database**
- MongoDB

## Features

- **Authentication** — JWT-based login with token validation on every request
- **Role-Based Access Control** — users see only the navigation nodes assigned to their roles
- **Product Management** — products with brand, category, model, unit associations and dynamic dropdowns
- **Inventory** — stock tracking across warehouses, racks, and shelves
- **Customer Management** — customer profiles with address management
- **Cart System** — initialize cart per customer, add/remove products, automatic price and discount calculation
- **Order Processing** — checkout converts cart to a permanent order record with timestamp and status
- **Dashboard** — live stats showing total orders, revenue, active carts and recent order history

## Project Structure

```
POS-System/
├── backend/         # Spring Boot REST API
│   └── src/
│       └── main/java/com/ust/pos/
│           ├── api/         # REST controllers
│           ├── model/       # MongoDB documents
│           ├── dto/         # Data transfer objects
│           └── */service/   # Business logic per entity
└── frontend/        # Next.js application
    └── app/
        ├── components/  # Shared layout, list, form components
        ├── cart/        # Cart list and management pages
        ├── order/       # Orders list
        ├── product/     # Product CRUD
        └── ...          # One folder per entity
```

## Getting Started

### Prerequisites
- Java 17+
- Node.js 18+
- MongoDB (local or Atlas)
- Maven

### Backend Setup

```bash
cd backend
# Update MongoDB connection in src/main/resources/application.properties
# spring.data.mongodb.uri=mongodb://localhost:27017/pos
mvn spring-boot:run
```

Backend runs on `http://localhost:8080`

### Frontend Setup

```bash
cd frontend/pos-next
npm install
npm run dev
```

Frontend runs on `http://localhost:3000`

### Environment

Create a `.env.local` file in the frontend folder:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

## API Overview

| Entity | Base Path |
|---|---|
| Auth | `/api/token` |
| User | `/api/user` |
| Role | `/api/role` |
| Node | `/api/node` |
| Product | `/api/product` |
| Brand | `/api/brand` |
| Category | `/api/category` |
| Stock | `/api/stock` |
| Warehouse | `/api/warehouse` |
| Customer | `/api/customer` |
| Cart | `/api/cart` |
| Cart Entry | `/api/cartEntry` |
| Order | `/api/order` |

All endpoints (except login) require a Bearer token in the Authorization header.

## Screenshots

> Coming soon

## Author

[Fijul Ali](https://github.com/Fijul-Ali)
