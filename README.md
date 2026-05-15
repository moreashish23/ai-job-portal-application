# AI Job Portal System

An AI-powered Full Stack Job Portal built using Microservices Architecture with Spring Boot, React, PostgreSQL, and Google Gemini AI.

This project is designed to provide a production-level scalable job portal platform where candidates can create resumes, employers can post jobs, and AI can analyze resumes to provide career feedback and improvement suggestions.

Current Status:
- Job Category Service API Completed
- Other Services & Features are Under Development

---

# Features

## Candidate Features
- User Registration & Login
- Resume Builder
- Multiple Resume Templates
- AI Resume Feedback
- Job Application Management
- Profile Management

## Employer Features
- Company Profile Management
- Job Posting Management
- Job Search & Filtering
- Applicant Tracking

## AI Features
- AI Resume Analysis
- Resume Improvement Suggestions
- Career Recommendations
- Missing Skills Detection
- Target Job Suggestions

---

# Architecture

This project follows:

- Microservices Architecture
- Maven Multi-Module Structure
- API Gateway Pattern
- Event-Driven Architecture
- JWT Authentication
- Service Discovery Architecture

---

# Tech Stack

## Backend
- Java 21
- Spring Boot 4
- Spring Security
- Spring Cloud
- Spring Data JPA
- Spring Cloud Gateway
- PostgreSQL
- JWT Authentication
- Apache Kafka
- OpenFeign
- Lombok
- Redis
- Spring Mail
- Google Gemini AI SDK

## Frontend
- React 19
- Vite
- Redux Toolkit
- React Router DOM
- Axios
- Tailwind CSS
- ShadCN UI
- Radix UI

## DevOps & Infrastructure
- Docker
- Docker Compose
- Spring Cloud Config
- Eureka Service Discovery

---

# Project Structure

```bash
job-portal-system/
│
├── cloud/
│   ├── discovery-service
│   ├── config-server
│   └── api-gateway
│
├── common-library/
│
├── services/
│   ├── user-service
│   ├── company-service
│   ├── job-service
│   ├── resume-service
│   ├── application-service
│   └── job-category-service
│
└── frontend/
```

---

# Completed Modules

## Job Category Service API

Implemented APIs:
- Create Job Category
- Get All Categories
- Active Category Handling

Example Categories:
- Java Developer
- Python Developer
- JavaScript Developer
- HR Manager

---

# Modules Under Development

- Authentication Service
- Resume Service
- AI Feedback Service
- Company Service
- Job Service
- Application Service
- Notification Service
- API Gateway Security
- Frontend Dashboard
- Kafka Event Integration

---

# AI Resume Analysis Features

The AI module analyzes resumes and provides:

- Resume Shortlisting Reasons
- Missing Skills Detection
- Grammar & Spelling Improvements
- Technical Depth Suggestions
- Education Timeline Issues
- Recommended Target Roles

Example:
- Junior Frontend Developer
- Full Stack Developer Trainee
- Java Backend Developer Intern

---

# Security

- JWT Authentication
- Role-Based Authorization
- Spring Security
- API Gateway Protection

---

# Database

- PostgreSQL
- Separate Database per Microservice
- JPA & Hibernate ORM

---

# Maven Multi-Module Setup

The project uses Maven Multi-Module architecture to manage all microservices under one parent project.

Benefits:
- Centralized Dependency Management
- Easy Version Control
- Single Build Command
- Better Project Organization

---

# Run the Project

## Backend

```bash
mvn clean install
```

Run individual services:

```bash
mvn spring-boot:run
```

---

## Frontend

```bash
cd frontend
npm install
npm run dev
```

---

# Example Service Ports

| Service | Port |
|---|---|
| API Gateway | 8080 |
| User Service | 5001 |
| Company Service | 5002 |
| Job Service | 5003 |
| Resume Service | 5004 |

---

# Learning Objectives

This project demonstrates:
- Production-Level Microservices
- Full Stack Development
- AI Integration
- Spring Boot Best Practices
- Scalable System Design
- REST API Development
- React State Management
- Secure Authentication

---

# Future Enhancements

- AI Interview Preparation
- Real-Time Notifications
- Video Resume Support
- Resume PDF Export
- Advanced Job Recommendation Engine
- Docker Deployment
- Kubernetes Deployment
- CI/CD Pipeline

---

# Author

Ashish More

- Full Stack Java Developer
- MERN Stack Developer
- React Developer
- Spring Boot Developer

---

# Project Status

In Progress — Building Production-Level AI Job Portal System
