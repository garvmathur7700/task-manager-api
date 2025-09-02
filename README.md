# 📝 Task Management REST API

A comprehensive backend-only Task Management System built with **Java Spring Boot**, featuring secure user authentication and full CRUD operations for task management.

## 🎯 Project Overview

This is a production-ready REST API that allows users to register, log in, and manage their personal tasks. The system is designed with modern backend development practices, including JWT-based authentication, MongoDB integration, and proper security measures.

### ✨ Key Features

- 🔐 **Secure Authentication** - JWT-based user authentication and authorization
- 📋 **Task Management** - Complete CRUD operations for tasks
- 👤 **User Isolation** - Each user can only access their own tasks
- 🏗️ **Clean Architecture** - Proper separation of concerns with controllers, services, and repositories
- 🛡️ **Security** - BCrypt password hashing and JWT token validation
- 📊 **Monitoring** - Built-in health checks and metrics via Spring Boot Actuator
- 🚀 **Production Ready** - Error handling, validation, and proper HTTP status codes

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| **Java 17+** | Programming language |
| **Spring Boot 3.5.5** | Framework for building the REST API |
| **Spring Security + JWT** | Authentication and authorization |
| **MongoDB** | NoSQL database for storing users and tasks |
| **Spring Data MongoDB** | Database abstraction layer |
| **Lombok** | Reducing boilerplate code |
| **Maven** | Dependency management and build tool |

## 📂 Project Structure

```
src/main/java/com/garv/task_api/
├── controller/         # REST controllers
│   └── TaskController.java
├── dto/               # Data Transfer Objects
│   └── AuthRequest.java
├── exception/         # Custom exceptions
│   ├── Forbidden.java
│   └── NotFound.java
├── model/             # Entity classes
│   └── Task.java
├── repository/        # Data access layer
│   └── TaskRepository.java
├── security/          # Security configuration
│   ├── JwtFilter.java
│   ├── JwtUtil.java
│   └── SecurityConfig.java
├── service/           # Business logic layer
│   └── TaskService.java
├── AuthController.java       # Authentication endpoints
├── User.java                # User entity
├── UserRepository.java      # User data access
└── TaskApiApplication.java  # Main application class
```

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.6+**
- **Docker** (for MongoDB)
- **Git**

### 📥 Clone the Repository

```bash
git clone https://github.com/garvmathur7700/task-manager-api.git
cd task-manager-api
```

### 🐳 MongoDB Setup with Docker

The easiest way to run MongoDB for this project is using Docker:

#### Docker Run Command
```bash
# Run MongoDB in a Docker container
docker run -d \
  --name taskdb-mongo \
  -p 27017:27017 \
  -e MONGO_INITDB_ROOT_USERNAME=admin \
  -e MONGO_INITDB_ROOT_PASSWORD=password \
  -e MONGO_INITDB_DATABASE=taskdb \
  mongo:latest
```

#### Verify MongoDB is Running
```bash
# Check if MongoDB container is running
docker ps | grep mongo

# Connect to MongoDB (optional)
docker exec -it taskdb-mongo mongosh
```

### ⚙️ Configuration

1. **Configure MongoDB** in `src/main/resources/application.yml`:
   
   **For Docker MongoDB without authentication:**
   ```yaml
   spring:
     data:
       mongodb:
         uri: mongodb://localhost:27017/taskdb
   ```

2. **Update JWT Secret** (recommended for production):
   ```yaml
   app:
     jwt:
       secret: "your-super-secure-secret-key-here"
       expiration-minutes: 60
   ```

### 🏃‍♂️ Running the Application

1. **Install dependencies and compile**:
   ```bash
   mvn clean compile
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Verify the application is running**:
   ```bash
   curl http://localhost:8080/actuator/health
   ```
   Expected response: `{"status":"UP"}`

## 📚 API Documentation

### 🔓 Public Endpoints (No Authentication Required)

#### User Registration
```http
POST /auth/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securePassword123"
}
```

#### User Login
```http
POST /auth/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "securePassword123"
}
```
**Response**: JWT token string

### 🔐 Protected Endpoints (JWT Token Required)

Add the JWT token to the Authorization header:
```
Authorization: Bearer your_jwt_token_here
```

#### Create Task
```http
POST /tasks
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "Complete project documentation",
  "description": "Write comprehensive API documentation",
  "status": "TODO",
  "dueDate": "2025-12-31"
}
```

#### Get All Tasks
```http
GET /tasks?status=TODO&page=0&size=10
Authorization: Bearer {token}
```

**Query Parameters**:
- `status` (optional): Filter by `TODO`, `IN_PROGRESS`, or `DONE`
- `page` (optional): Page number (default: 0)
- `size` (optional): Items per page (default: 10)

#### Get Specific Task
```http
GET /tasks/{taskId}
Authorization: Bearer {token}
```

#### Update Task
```http
PUT /tasks/{taskId}
Content-Type: application/json
Authorization: Bearer {token}

{
  "title": "Updated task title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "dueDate": "2025-11-30"
}
```

#### Delete Task
```http
DELETE /tasks/{taskId}
Authorization: Bearer {token}
```

### 💚 Health Check
```http
GET /actuator/health
```

## 🔧 Testing with Postman

1. **Import the collection**: Create a new Postman collection for "Task Management API"
2. **Set up environment variables**:
   - `baseUrl`: `http://localhost:8080`
   - `jwtToken`: (paste your token after login)
3. **Test flow**:
   1. Register a new user
   2. Login to get JWT token
   3. Set the token in your environment
   4. Test all CRUD operations

## 🗄️ Database Schema

### User Collection
```javascript
{
  "_id": "ObjectId",
  "username": "string (unique)",
  "password": "string (bcrypt hashed)"
}
```

### Task Collection
```javascript
{
  "_id": "ObjectId",
  "ownerId": "string (username)",
  "title": "string",
  "description": "string",
  "status": "TODO | IN_PROGRESS | DONE",
  "dueDate": "date",
  "createdAt": "timestamp",
  "updatedAt": "timestamp"
}
```

## 🛡️ Security Features

- **Password Hashing**: BCrypt with salt for secure password storage
- **JWT Authentication**: Stateless authentication using JSON Web Tokens
- **Authorization**: Users can only access their own tasks
- **Input Validation**: Comprehensive validation on all endpoints
- **Error Handling**: Proper HTTP status codes and error messages

## 🚀 Production Deployment

### Environment Variables
Set these environment variables in production:

```bash
MONGODB_URI=mongodb://your-production-mongodb-uri
JWT_SECRET=your-super-secure-production-secret
SERVER_PORT=8080
```

### Docker Support (Optional)
Create a `Dockerfile`:

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/task-api-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Build and run:
```bash
mvn clean package
docker build -t task-api .
docker run -p 8080:8080 task-api
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

- GitHub: [@garvmathur7700](https://github.com/garvmathur7700)
- LinkedIn: [garvvmathur](https://linkedin.com/in/garvvmathur)

---

## 📊 API Response Examples

### Successful Task Creation
```json
{
  "id": "507f1f77bcf86cd799439011",
  "title": "Complete project documentation",
  "description": "Write comprehensive API documentation",
  "status": "TODO",
  "dueDate": "2025-12-31",
  "createdAt": "2025-09-02T10:15:30Z",
  "updatedAt": "2025-09-02T10:15:30Z"
}
```

### Task List Response
```json
{
  "tasks": [
    {
      "id": "507f1f77bcf86cd799439011",
      "title": "Complete project documentation",
      "description": "Write comprehensive API documentation",
      "status": "TODO",
      "dueDate": "2025-12-31",
      "createdAt": "2025-09-02T10:15:30Z",
      "updatedAt": "2025-09-02T10:15:30Z"
    }
  ],
  "totalTasks": 1,
  "currentPage": 0,
  "totalPages": 1,
  "hasNext": false,
  "hasPrevious": false
}
```

### Error Response
```json
{
  "error": "Task not found",
  "message": "Task with id 507f1f77bcf86cd799439011 not found",
  "timestamp": "2025-09-02T10:15:30Z",
  "status": 404
}
```


---

**Happy Coding! 🚀**
