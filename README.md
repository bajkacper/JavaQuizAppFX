# Java Test FX and Spring Project

## Overview

This project is a comprehensive application combining JavaFX for the frontend and Spring Boot for the backend. It aims to provide a seamless integration between a desktop application interface and a robust server-side architecture.

## Project Structure

### FX (Frontend)

The `FX` module contains the JavaFX application code. This module is responsible for the user interface and interaction. It includes several controllers and entity classes that manage the application's UI logic and data representation.

Key Components:
- **Controllers**: Handle user interactions and update the UI.
- **Entities**: Represent the data models used within the application.

### QuizApplication (Backend)

The `QuizApplication` module contains the Spring Boot application code. This module is responsible for the backend logic, including handling HTTP requests, managing database interactions, and providing RESTful APIs for the frontend.

Key Components:
- **Controllers**: Handle HTTP requests and define RESTful endpoints.
- **Services**: Contain business logic and interact with repositories.
- **Repositories**: Interface with the database to perform CRUD operations.
- **Entities**: Define the data models and map them to database tables.

## Technologies Used

### Frontend
- **JavaFX**: For building the user interface.
- **Maven**: For dependency management and building the project.

### Backend
- **Spring Boot**: For building the RESTful API and managing the backend logic.
- **Hibernate**: For ORM (Object-Relational Mapping) to interact with the database.
- **PostgreSQL**: As the relational database system.
- **Maven**: For dependency management and building the project.

## Features

- **User Management**: Includes functionalities for user authentication and authorization.
- **Quiz Management**: Allows administrators to create, update, and manage quizzes.
- **Real-time Results**: Provides real-time updates on quiz submissions and results.
- **RESTful APIs**: Exposes various endpoints for frontend interaction.

## Authors

- **Author Name**: [Kacper Baj](https://github.com/bajkacper)
  

