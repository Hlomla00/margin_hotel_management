# Margin Hotel Management System
This repository contains the core backend application for **Margin Hotel**, an established hotel group based in South Africa. The project aims to transition the hotel's traditional paper and spreadsheet-based operations into a modern, centralized digital system.
The initial phase focuses on delivering a robust, web-enabled backend that will eventually power both internal staff applications and a public-facing booking website.
## 🚀 Project Overview
This application follows a **Domain-Driven Design (DDD)** approach. It handles end-to-end front-desk operations including room availability tracking, guest bookings, invoicing, and payment processing.
### Key Features
 * **Role-Based Workflows**: Supports specific features for internal roles (1 Manager and 5 Receptionists).
 * **Automated Invoicing**: Every approved guest booking automatically generates a corresponding invoice.
 * **Payment Settlement**: Enforces strict business logic where each invoice is settled by exactly one payment.
 * **Domain Model Alignment**: Built entirely around the engineered domain specifications.
## 🛠️ Tech Stack
 * **Language:** Java
 * **Architecture:** Domain-Driven Design (DDD)
 * **Persistence:** Jakarta Persistence API (JPA) / Hibernate
 * **Database:** MySQL
 * **Type:** Web-enabled backend application
## 👥 User Roles & Permissions
 * **Manager**: Oversees all operations and reviews critical business data including bookings, invoices, and payments.
 * **Receptionist**: Manages day-to-day front-desk tasks such as checking room availability, creating guest bookings, and processing incoming payments.
