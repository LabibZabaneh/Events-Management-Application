# Events Management Application

## Overview

This Events Management application is designed to facilitate the creation, management, and analysis of events. The application follows a microservices architecture, allowing for scalability, modularity, and ease of maintenance.

## Technologies Used

- **Microservices Framework:** [Micronaut](https://micronaut.io/)
- **Messaging System:** [Apache Kafka](https://kafka.apache.org/)
- **Containerization:** [Docker](https://www.docker.com/)

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

4. **Notification Microservice:**
   - Handles notifications, such as email notifications to users and entities.
   - Technologies: (To be implemented)

5. **Rating and Review Microservice:**
   - Manages ratings and reviews from users for events.
   - Technologies: (To be implemented)
  
## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/)

### Building and Running the Microservices

1. Clone the repository: git clone https://github.com/LabibZabaneh/Events-Management-Application.git
2. Run the application using docker compose: docker compose up -d
3. Access the event-microservice at: http://localhost:8080
4. Open the evnt-cli in a terminal which is a UI for the event-microservice (refer to event-cli README file for usage instruction)

## Acknowledgments

- The event-microservice is currently implemented; however, other microservices are yet to be built
- The event-cli is not yet implemented
- Every microservice will have a corresponding command line client under the clients folder mainly for testing purposes only
