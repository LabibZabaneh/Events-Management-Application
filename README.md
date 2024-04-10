# Events Management Application

## Overview

This Events Management application is designed to facilitate the creation, management, and analysis of events. The backend application follows a microservices architecture, allowing for scalability, modularity, and ease of maintenance. The application is fully hosted on Docker containers

## Technologies Used

- **Microservices Framework:** [Micronaut](https://micronaut.io/)
- **Messaging System:** [Apache Kafka](https://kafka.apache.org/)
- **Containerization:** [Docker](https://www.docker.com/)
- **Database:** [MariaDB](https://mariadb.org/)
- **Payment Gateway:** [Stripe](https://stripe.com/)

## Microservices

1. **Event Microservice:**
   - Handles CRUD operations for users, events, and organizers

2. **Registration Microservice:**
   - Handles user registration to events
   - Enables users to follow organizers
   - Generates tickets upon registration

3. **Analytics Microservice:**
   - Provides insights into trending events and analytics

4. **Payment Microservice:**
   - Facilitates users buying tickets for events through Stripe's API

5. **Notification Microservice:**
   - Sends conformation via email to users to activate their accounts
   - Sends tickets to users via email upon successful registration for an event
   - (To be implemented)
  
## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/)

### Building and Running the Microservices

1. Clone the repository: `git clone https://github.com/LabibZabaneh/Events-Management-Application.git`
2. Navigate to the directory: `cd Events-Management-Application`
3. Run the application using Docker Compose: `docker-compose up -d`
4. Access the registration-microservice at: [http://localhost:8081](http://localhost:8081) (must manually run the microservice)
5. Access the event-microservice at: [http://localhost:8080](http://localhost:8080) (must manually run the microservice)
6. Access the analytics-microservice at: [http://localhost:8082](http://localhost:8082) (must manually run the microservice)

## Acknowledgments

- There is no user interface yet; the development of the frontend will start after the full completion of the backend
- Run the registration-microservice before running the event-microservice, there is a minor bug in the automatic generation of kafka topics in the event-microservice
- I am working on a fix for that bug
