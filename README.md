# Spring Microservices Architecture

This project demonstrates a **Spring Boot microservices architecture** with multiple services and an API Gateway. It includes features like service routing, 
Resilience4j circuit breakers, and Swagger API documentation aggregation.

---

## Project Structure

- **api-gateway**: Spring Cloud Gateway handling routing, Swagger aggregation, and circuit breakers.  
- **product-service**: Service managing product-related data and APIs.  
- **order-service**: Service managing order-related data and APIs.  
- **inventory-service**: Service managing inventory-related data and APIs.  

---

## Features

- **Spring Boot 3.5+** microservices with REST APIs  
- **Spring Cloud Gateway (WebMVC)** for routing  
- **Resilience4j Circuit Breakers** for fault tolerance  
- **Swagger/OpenAPI** integration for API documentation  
- **Spring Boot Actuator** for monitoring health and metrics  
- **Secure endpoints** ready for OAuth2 Resource Server  
- **Docker-ready** for containerized deployments  

---

## Running the Project

1. Make sure you have **Java 21** installed.  
2. Start each microservice individually:  

bash
cd product-service
mvn spring-boot:run

cd order-service
mvn spring-boot:run

cd inventory-service
mvn spring-boot:run

cd api-gateway
mvn spring-boot:run
