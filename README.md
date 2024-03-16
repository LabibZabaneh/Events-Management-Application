# Events Management Application

## Overview

This Events Management application is designed to facilitate the creation, management, and analysis of events. The backend application follows a microservices architecture, allowing for scalability, modularity, and ease of maintenance. The application is fully hosted on Docker containers.

## Technologies Used

- **Microservices Framework:** [Micronaut](https://micronaut.io/)
- **Messaging System:** [Apache Kafka](https://kafka.apache.org/)
- **Containerization:** [Docker](https://www.docker.com/)
- **Database:** [MariaDB](https://mariadb.org/)

## Microservices

1. **Event Microservice:**
   - Handles CRUD operations for users, events, and businesses.

2. **Registration Microservice:**
   - Handles user registration to events.
   - Enables users to follow organizers

3. **Analytics Microservice:**
   - Provides insights into trending events and analytics.
   - (Under implementation)

4. **Recommendation Microservice:**
   - Provides personalized event recommendations to users.
   - (To be implemented)

6. **Notification Microservice:**
   - Handles notifications, such as email notifications to users and entities.
   - (To be implemented)

7. **Rating and Review Microservice:**
   - Manages ratings and reviews from users for events.
   - (To be implemented)
  
## Getting Started

### Prerequisites

- [Docker](https://www.docker.com/)

### Building and Running the Microservices

1. Clone the repository: `git clone https://github.com/LabibZabaneh/Events-Management-Application.git`
2. Navigate to the directory: `cd Events-Management-Application`
3. Run the application using Docker Compose: `docker-compose up -d`
4. Access the event-microservice at: [http://localhost:8080](http://localhost:8080) (must manually run the microservice)
5. Access the registration-microservice at: [http://localhost:8081](http://localhost:8081) (must manually run the microservice)

## Acknowledgments

- The event-microservice and registration-microservice are currently implemented; however, other microservices are yet to be built.
- There is no user interface yet; the development of the frontend will start after the full completion of the backend.
