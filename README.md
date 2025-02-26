## Prerequisites
Ensure you have the following installed before running the server:
- Java 17+
- Maven
- PostgreSQL
- Docker (Optional, for containerized database)

---

## Running the Server

### 1. Clone the Repository
```sh
git clone <repository-url>
cd ecommerce
```

### 2. Configure the Environment
Update the `.env` file or edit `application.properties` with your database and API keys.

#### For Local PostgreSQL
Ensure you have PostgreSQL running and update these values:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.datasource.driver-class-name=org.postgresql.Driver
```

#### For Docker-Based PostgreSQL
Uncomment the Docker-specific lines:
```properties
spring.datasource.url=jdbc:postgresql://postgres_db:5432/ecommerce
spring.datasource.username=postgres
spring.datasource.password=postgres
```

---

### 3. Set Up the Unsplash API Key
To use the Unsplash API for fetching product images:
- Replace `unsplash.api.key=secret` in `application.properties` with your actual Unsplash API key. You can use mine (_trafNY7drNRDEcDt8o3K6p4oKxJr2pEGP6w7Y2psC4)
- If you don't have one, sign up on [Unsplash Developers](https://unsplash.com/developers) and create an API key.

```properties
unsplash.api.key=secret
unsplash.api.url=https://api.unsplash.com/search/photos
```

---

### 4. Generate a Secure JWT Secret
To ensure secure authentication, generate a strong JWT secret key. You can use:
```sh
openssl rand -base64 64
```
Replace the secret in `application.properties`:
```properties
auth.jwt.secret=<your-generated-secret>
auth.jwt.expiration=3600000
```

---

### 5. Build and Run the Server

#### Run with Maven
```sh
mvn clean install
mvn spring-boot:run
```

#### Run with Docker
```sh
docker-compose up --build
```

---

## API Access
- Swagger Documentation: `http://localhost:8080/swagger-ui/`
- Health Check: `http://localhost:8080/actuator/health`
- Base API Endpoint: `http://localhost:8080/api/products`