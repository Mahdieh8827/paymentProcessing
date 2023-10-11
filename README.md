# Challenge

This repository contains the solution for the Technical Test Inc. payment processing microservice challenge.

## :computer: How to Execute

### Prerequisites

Before running the microservice, ensure you have the following prerequisites installed:

- Java 11
- Docker

### Steps to Run


2. Navigate to the project directory:
3. Start the required Docker containers for PostgreSQL and Kafka: docker-compose up -d
4. Build the Spring Boot application: mvn clean install
5. Run the Spring Boot application: mvn spring-boot:run
6. The microservice should now be running on http://localhost:8080.
7. You can access the basic HTML interface by opening http://localhost:9000 in a web browser.


### Testing Endpoints

- To check if a payment is valid with third-party providers:
- curl -i --header "Content-Type: application/json"
  --request POST
  --data "{'payment_id': 'fdf50f69-a23a-4924-9276-9468a815443a', 'account_id': 1,
  'payment_type': 'online','credit_card': '12345','amount':12}"
  http://localhost:9000/payment
- To store error logs:
- curl -i --header "Content-Type: application/json"
  --request POST
  --data "{'payment_id': 'fdf50f69-a23a-4924-9276-9468a815443a', 'error_type':
  'network', 'error_description': 'Here some description'}"
  http://localhost:9000/log
- To start the technical test and produce payments data: curl http://localhost:9000/start
- To view error logs: curl http://localhost:9000/logs


## :memo: Notes

- The microservice is designed to handle two types of payments: online and offline. <br/>
- It communicates with third-party providers for online payments and stores all payments in a PostgreSQL database.

- When a payment is successfully stored, the account information is updated with the last payment date.

- Errors are logged via a REST call to the Log System.

- The microservice relies on Docker containers for PostgreSQL and Kafka, so make sure they are running before starting the application.

## :pushpin: Things to Improve

If you have more time or want to improve something, consider the following:<br/>
- Implement a more robust data model and database schema if needed for scalability and future requirements.
- Implement application-level monitoring using tools like Prometheus and Grafana to track performance metrics, error rates, and resource utilization.
  Set up automated alerts and notifications to respond to critical issues in real-time.
- Plan for scalability by implementing load balancing strategies to distribute incoming requests across multiple instances of the microservice.
  Explore container orchestration solutions like Kubernetes to manage scaling and deployments.
- Consider using caching mechanisms to reduce the load on the database.


















