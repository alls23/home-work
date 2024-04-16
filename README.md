

<!-- ABOUT THE PROJECT -->
## About The Project
This project involves the development of a parking reservation service, which allows users to reserve parking spots in advance. The system comprises two main microservices:
* User Management Service: This microservice is responsible for handling user profile management. 
* Parking Reservation Service: This microservice enables users to reserve parking spots for specific times and vehicles.

### Prerequisites

* Docker

### Installation
Run from root of project:
  ```
  docker compose up -d
  ```

<!-- USAGE EXAMPLES -->
## Usage

* Get JWT token from Keycloak
 ```
  GET http://localhost:8082/realms/demo/protocol/openid-connect/token
  ```
Then using JWT you can do more reqeust like:
* Registre a car
  ```
  POST http://localhost:8080/cars/registre
  {
    "plateNumber": "EP855JL",
    "type": "CAR",
    "brand": "Fiat",
    "model": "Punto"
  }
  ```
* Upload some photo
  ```
  POST http://localhost:8080/cars/photo
  file via form-data
  ```
* Get avalible parking lots
  ```
  GET http://localhost:8080/parking/1
  ```
* Book parking lot
 ```
  POST http://localhost:8080/parking/book
  {
    "parkingId": 123,
    "carId": 456,
    "spaceId": 789,
    "startTime": "2024-04-15T10:00:00",
    "endTime": "2024-04-15T18:00:00"
  }
  ```

## Design
[![Product design][product-screenshot]]


<!-- MARKDOWN LINKS & IMAGES -->
[product-screenshot]: local/images/diagram.png





