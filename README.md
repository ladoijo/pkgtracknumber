# Track Number Generator

A Spring Boot-based microservice for generating and managing tracking numbers with validation and caching capabilities.

## 🚀 Tech Stack

- **Java 21** - Primary programming language
- **Spring Boot 3.5.4** - Application framework
- **Spring Web** - RESTful web services
- **Spring Validation** - Request validation
- **MongoDB** - Database for storing track numbers and other data (NoSQL)
- **Redis** - In-memory key-value store for caching track numbers (NoSQL)
- **Lombok** - Reduced boilerplate code
- **Actuator** - Application monitoring

## 📋 Prerequisites

- JDK 21 or higher
- Gradle 8.0+ (included in the project as `gradlew`)
- Redis (for caching track numbers)
- MongoDB (for storing track numbers and other data)
- Internet connection (for downloading dependencies)

## ⚙️System Design
### Get Existing Package Track Number from Cache
![Cache](https://drive.google.com/file/d/1oWKNyoJUQYXyZhyMgSkYd1c5HL8sQJ1g/view?usp=drive_link)

### Get Existing Package Track Number from DB
![DB](https://drive.google.com/file/d/1aMBZJbERbiS8zcUKfzj4jUi1x28mzy-i/view?usp=drive_link)

### Insert New Package Track Number into DB and Cache
![Insert](https://drive.google.com/file/d/1xkm0CL6FxY2bpZVf_yqdpADzLdRf2xft/view?usp=drive_link)

## 🛠️ Setup

**Clone the repository**

   ```bash
   git clone https://github.com/ladoijo/pkgtracknumber.git
   cd pkgtracknumber
   ```

### How to Run

1. Run the Redis server
   ```bash
   docker run -d --name YOUR_REDIS_NAME -p 6379:6379 redis
   ```
   
2. Run the MongoDB server, or MongoDB atlas to run on cloud
   ```bash
   docker run -d --name YOUR_MONGODB_NAME -p 27017:27017 mongo
   ```
   
3. **Build the project**
   ```bash
   ./gradlew build
   ```

2. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

The application will start on `http://localhost:8080` by default.

## 🚦 API Endpoints

### Generate Track Number

```
GET /api/v1/next-tracking-number

# Example
GET http://localhost:8080/api/v1/next-tracking-number?origin_country_id=ID&destination_country_id=MY&weight=4&created_at=2025-06-24T15%3A30%3A45%2B07%3A00&customer_id=de619854-b59b-425e-9db4-943979e1bd49&customer_name=RedBox Logistics&customer_slug=redbox-logistics
```

**Request Parameters:**

| Parameter              | Type          | Required | Description                                                  | Example                                |
|------------------------|---------------|----------|--------------------------------------------------------------|----------------------------------------|
| origin_country_id      | String        | Yes      | The order’s origin country code in ISO 3166-1 alpha-2 format | MY                                     |
| destination_country_id | String        | Yes      | The order’s origin country code in ISO 3166-1 alpha-2 format | ID                                     |
| weight                 | Decimal       | Yes      | The order’s weight in kilograms, up to three decimal places  | "2.123"                                |
| created_at             | String)       | Yes      | The order’s creation timestamp in RFC 3339 format            | "2024-06-25T01:20:30+07:00"            |
| customer_id            | String (UUID) | Yes      | The customer’s UUID                                          | "de619854-b59b-425e-9db4-943979e1bd49" |
| customer_name          | String        | Yes      | Full name of the customer                                    | "John Doe"                             |
| customer_slug          | String        | Yes      | The customer’s name in slug-case/kebab-case                  | "john-doe"                             |

**Response:**

```json
{
  "code": 200,
  "status": "OK",
  "message": "Request successful",
  "timestamp": "2025-06-24T17:32:10.942421Z",
  "data": {
    "track_number": "7GRHFD5K9EHZEHPE",
    "created_at": "2025-06-25T00:32:10.942398+07:00"
  },
  "errors": null
}
```

## 🔍 Project Structure

```
src/
├── main/
│   ├── java/com/hadyan/tracknumbergenerator/
│   │   ├── cache/          # Caching implementation
│   │   ├── configuration/  # Configuration
│   │   ├── constant/       # Constants
│   │   ├── controller/     # REST controllers
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entities
│   │   ├── exception/      # Handler or custom exceptions
│   │   ├── repository/     # Repositories
│   │   ├── usecase/        # Business logic
│   │   ├── service/        # usable functionality
│   │   ├── util/           # Utility classes
│   │   └── PkgTrackNumberApplication.java  # Main application class
└── └── resources/
        └── application.properties  # Application configuration
```

## ⚙️ Configuration

Application properties can be configured in `src/main/resources/application.properties`.

## 🌐 Live Demo

You can try out the live API demo here: [https://pkgtracknumber.et.r.appspot.com](https://pkgtracknumber.et.r.appspot.com)