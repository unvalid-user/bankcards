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