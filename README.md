# Events Management Application

## Overview

This Events Management application is designed to facilitate the creation, management, and analysis of events. The application follows a microservices architecture, allowing for scalability, modularity, and ease of maintenance. The application is fully hosted on docker containers.

## Technologies Used

- **Microservices Framework:** [Micronaut](https://micronaut.io/)
- **Messaging System:** [Apache Kafka](https://kafka.apache.org/)
- **Containerization:** [Docker](https://www.docker.com/)
- **Database:** [Mariadb](https://mariadb.org/)

## Microservices

1. **Event Microservice:**
   - Handles CRUD operations for users, events and businesses.
   - Technologies: Micronaut, Kafka, Docker.

2. **Registration Microservice:**
   - Manages user registration and unregistration for events.
   - Technologies: (To be implemented)

3. **Analytics Microservice:**
   - Provides insights into trending events and analytics.
   - Technologies: (To be implemented)

4. **Recommendation Microservice:**
   - Prvides personalized event recommendations to users.
   - Technologies: (To be implemented)

6. **Notification Microservice:**
   - Handles notifications, such as email notifications to users and entities.
   - Technologies: (To be implemented)

7. **Rating and Review Microservice:**
   - Manages ratings and reviews from users for events.
   - Technologies: (To be implemented)
  
## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/)

### Building and Running the Microservices

1. Clone the repository: git clone https://github.com/LabibZabaneh/Events-Management-Application.git
2. Run the application using docker compose: docker compose up -d
3. Access the event-microservice at: http://localhost:8080

## Acknowledgments

- The event-microservice is currently implemented; however, other microservices are yet to be built
