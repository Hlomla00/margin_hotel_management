# Margin Hotel Management System

This repository contains the core backend application for **Margin Hotel**, an established hotel group with several branches across South Africa. The project transitions the hotel's traditional paper-based front-desk processes to a modern, digitized platform.

This application is developed as part of the **Applications Development Practice 3 (ADP372S)** capstone group project.

## 🚀 Project Overview

The Margin Hotel Management System delivers a robust, web-enabled backend built from the domain model up to the REST controllers that expose hotel operations as web services.

### System Scope

The scope of this project is **back-end only** (domain layer → REST controllers). The public booking website and the internal staff portal front-end views are out of scope and will be built by another team.

The platform serves two separated audiences across three distinct booking channels with no shared interface:

- **Guests (The Public):** Browse room availability and book rooms directly through a public booking website (`ONLINE` channel).
- **Staff (Internal Portal):**
  - **Receptionists:** Check availability, create bookings on behalf of guests (`WALK_IN` or `TELEPHONIC` channels), and process payments.
  - **Managers:** Oversee overall operations and review critical business data.

The system supports a fixed structure of **1 manager and 5 receptionists**.

## 🛠️ Tech Stack & Architecture

| Category | Choice |
|---|---|
| Language | Java 21 |
| Framework | Java Spring Boot |
| Architecture | Domain-Driven Design (DDD) layers |
| Persistence | Jakarta Persistence API (JPA) / Hibernate |
| Database | MySQL |
| Testing | JUnit 5 (strictly following Test-Driven Development cycles) |
| Build Tool | Maven |
| Design Patterns | Builder Pattern (implemented without Lombok) |

## 🛡️ Core Business & Validation Rules

> 💡 **Crucial Design Rule:** Code to abstraction, not concretion. Strict package boundaries must be observed across layers (`domain`, `factory`, `repository`, `service`, `controller`).

- **Mandatory Fields:** All attributes are strictly mandatory and must be validated before an instance is created, with the sole exception of `Name.middleName`.
- **No Null Saves:** No attribute is allowed to be saved to the database as `null` or as an empty string (except `middleName`).
- **Factory Failure Handling:** On any validation failure, the object factory must return `null` instead of throwing an `IllegalArgumentException`.
- **Anti-Double-Booking Logic:** A room cannot be double-booked. No two bookings for the exact same room may overlap in their dates. This rule is strictly enforced within the service layer, not the database.
- **Payment Settlement:** One `Booking` produces exactly one `Invoice`, and one `Invoice` is settled by exactly one `Payment`. Split or partial payments are not supported.

## 📊 Domain Model UML Diagram

![Margin Hotel UML Diagram](Margin%20Hotel%20UML.jpeg)

This UML diagram illustrates the complete domain model for the Margin Hotel Management System, including all entities, relationships, and value objects used throughout the application.
