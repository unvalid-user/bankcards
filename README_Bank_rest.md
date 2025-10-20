## Steps to Setup:

**1. Clone the application**

```bash
git clone git@github.com:unvalid-user/bankcards.git
```

**2. Database Setup**
- create PostgreSQL database
```sql
CREATE DATABASE bankcards
```
- create tables: `db-init/0-init-schema.sql`
- fill the database with data: `db-init/1-init-data.sql`

**3. Change database properties in `src/main/resources/application.yml`**
- port
- username
- password

**4. Run the app using maven**
```bash
mvn spring-boot:run
```

## Start with Docker:
```bash
docker-compose up --build
```

The app will start running at <http://localhost:8080>\
Explore <http://localhost:8080/swagger-ui/index.html> for all available endpoints

## Rest API:
### Auth

| Method | Url           | Description | Role access | Sample Valid Request Body | 
|--------|---------------|-------------|-------------|---------------------------|
| POST   | /auth/sign-in | Log in      | -           | [JSON](#login-request)    |

### Cards
| Method | Url                       | Description               | Role access | Sample Valid Request Body    | 
|--------|---------------------------|---------------------------|-------------|------------------------------|
| GET    | /cards/{id}               | Get user's card by id     | any         |                              |
| GET    | /cards                    | Get user's cards          | any         |                              |
| POST   | /cards/{id}/request-block | Create card block request | any         |                              |
| POST   | /cards                    | Create card               | ADMIN       | [JSON](#create-card-request) |
| GET    | /cards/all                | Get all cards             | ADMIN       |                              |
| DELETE | /cards/{id}               | Delete card by id         | ADMIN       |                              |
| PATCH  | /cards/{id}               | Update card               | ADMIN       | [JSON](#update-card-request) |  

### Card operations

| Method | Url                            | Description                   | Role access | Sample Valid Request Body | 
|--------|--------------------------------|-------------------------------|-------------|---------------------------|
| GET    | /card-operations               | Get all card operations       | ADMIN       |                           |
| GET    | /card-operations/{id}          | Get card operation by id      | ADMIN       |                           |
| PATCH  | /card-operations/{id}/cancel   | Cancel card operation by id   | ADMIN       |                           |
| PATCH  | /card-operations/{id}/complete | Complete card operation by id | ADMIN       |                           |

### Transactions

| Method | Url                | Description                  | Role access | Sample Valid Request Body           | 
|--------|--------------------|------------------------------|-------------|-------------------------------------|
| GET    | /transactions      | Get user's transactions      | any         |                                     |
| GET    | /transactions/{id} | Get user's transaction by id | any         |                                     |
| POST   | /transactions      | Create transaction           | any         | [JSON](#create-transaction-request) |
| GET    | /transactions/all  | Get all transactions         | ADMIN       |                                     |

### Users

| Method | Url         | Description       | Role access | Sample Valid Request Body    | 
|--------|-------------|-------------------|-------------|------------------------------|
| POST   | /users      | Create user       | ADMIN       | [JSON](#create-user-request) |
| GET    | /users/{id} | Get user by id    | ADMIN       |                              |
| GET    | /users/all  | Get all users     | ADMIN       |                              |
| PATCH  | /users      | Update user by id | ADMIN       | [JSON](#update-user-request) |

## Sample Valid JSON Request Bodies:

##### <a id="login-request">Sign In</a>
```json
{
  "phoneNumber": "admin",
  "password": "admin"
}
```

##### <a id="create-card-request">Create Card</a>
```json
{
  "cardNumber": "1234567812345678",
  "ownerPhoneNumber": "89991234455",
  "expirationDate": "2030-10-20"
}
```

##### <a id="update-card-request">Update Card</a>
```json
{
  "expirationDate": "2030-10-20",
  "status": "ACTIVE"
}
```

##### <a id="create-transaction-request">Create Transaction</a>
```json
{
  "sourceCardId": 1,
  "destinationCardId": 2,
  "monetaryAmount": 100.55
}
```

##### <a id="create-user-request">Create User</a>
```json
{
  "phoneNumber": "89991234455",
  "password": "password123",
  "role": "ROLE_USER"
}
```

##### <a id="update-user-request">Update User</a>
```json
{
  "phoneNumber": "89991234455",
  "password": "password123"
}
```