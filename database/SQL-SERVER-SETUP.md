# SQL Server Database Setup Guide

## Prerequisites

1. **SQL Server** installed (SQL Server 2019 or later, SQL Server Express works fine)
2. **SQL Server Management Studio (SSMS)** or **Azure Data Studio** for running scripts
3. **SQL Server Authentication** enabled (not just Windows Authentication)

## Step-by-Step Setup

### 1. Enable SQL Server Authentication

If you're using SQL Server Express with only Windows Authentication:

1. Open **SQL Server Configuration Manager**
2. Go to **SQL Server Services** → Right-click your SQL Server instance → **Properties**
3. Go to **Security** tab
4. Select **SQL Server and Windows Authentication mode**
5. Click **OK** and **Restart** the SQL Server service

### 2. Create SQL Server Login (SA User)

Open **SSMS** or **Azure Data Studio** and run:

```sql
-- Enable SA account if disabled
ALTER LOGIN sa ENABLE;
GO

-- Set SA password (change to your own strong password)
ALTER LOGIN sa WITH PASSWORD = 'YourStrongPassword123';
GO
```

**Important:** Update the password in `backend/src/main/resources/application.properties` to match:
```properties
spring.datasource.password=YourStrongPassword123
```

### 3. Create Database

```sql
CREATE DATABASE bookstore_db;
GO
```

### 4. Run Schema Script

1. Open `database/schema-sqlserver.sql` in SSMS or Azure Data Studio
2. Make sure you're connected to the `bookstore_db` database:
   ```sql
   USE bookstore_db;
   GO
   ```
3. Execute the entire script (F5 or Execute button)

### 5. Verify Tables Created

```sql
USE bookstore_db;
GO

-- Check all tables
SELECT TABLE_NAME 
FROM INFORMATION_SCHEMA.TABLES 
WHERE TABLE_TYPE = 'BASE TABLE'
ORDER BY TABLE_NAME;

-- Should show: books, cart_items, categories, order_items, orders, reviews, users

-- Check sample data
SELECT * FROM categories;
SELECT * FROM users;
SELECT * FROM books;
```

### 6. Update Application Properties

The `application.properties` file has been updated with SQL Server settings:

```properties
# Database Configuration
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=bookstore_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourStrongPassword123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect
```

**Connection String Parameters:**
- `localhost:1433` - Default SQL Server port
- `encrypt=true` - Enable SSL encryption
- `trustServerCertificate=true` - Trust self-signed certificates (for development)
- Change these if your SQL Server runs on different port or host

### 7. Update Maven Dependencies

The `pom.xml` has been updated to use SQL Server JDBC driver:

```xml
<!-- SQL Server Driver -->
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <scope>runtime</scope>
</dependency>
```

### 8. Clean and Rebuild Backend

```bash
cd backend
mvnw clean package
```

This will download the SQL Server JDBC driver (if not already cached).

### 9. Start Backend Server

```bash
cd backend
start-server.bat
```

Or using PowerShell:
```powershell
cd backend
.\start-server.ps1
```

### 10. Verify Connection

Watch the console output. You should see:

```
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
```

If successful, the backend is now connected to SQL Server!

## Troubleshooting

### Error: "Login failed for user 'sa'"

**Solution:** Check your SA password in `application.properties` matches the one you set in SQL Server.

### Error: "Cannot open database bookstore_db"

**Solution:** Make sure you created the database:
```sql
CREATE DATABASE bookstore_db;
GO
```

### Error: "The TCP/IP connection to the host localhost, port 1433 has failed"

**Solution:** 
1. Open **SQL Server Configuration Manager**
2. Go to **SQL Server Network Configuration** → **Protocols for [your instance]**
3. Enable **TCP/IP** protocol
4. Right-click **TCP/IP** → **Properties** → **IP Addresses** tab
5. Scroll to **IPAll** → Set **TCP Port** to `1433`
6. Restart SQL Server service

### Error: "SSL Security error"

**Solution:** Already handled with `trustServerCertificate=true`. If still having issues, you can disable encryption:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=bookstore_db;encrypt=false
```

## Key Differences from MySQL

### SQL Server vs MySQL Syntax

| Feature | MySQL | SQL Server |
|---------|-------|------------|
| Auto Increment | `AUTO_INCREMENT` | `IDENTITY(1,1)` |
| String Type | `VARCHAR` | `NVARCHAR` (supports Unicode) |
| Text Type | `TEXT` | `NVARCHAR(MAX)` |
| Boolean | `BOOLEAN` / `TINYINT(1)` | `BIT` |
| Timestamp | `TIMESTAMP` | `DATETIME2` |
| Current Time | `CURRENT_TIMESTAMP` | `GETDATE()` |
| Enum | `ENUM('A','B')` | `NVARCHAR(20) CHECK (col IN ('A','B'))` |
| Updated At Trigger | `ON UPDATE CURRENT_TIMESTAMP` | Requires explicit `TRIGGER` |
| String Quotes | Single `'` or double `"` | Single `'` only |
| Batch Separator | `;` | `GO` |

### Entity Classes (No Changes Needed!)

Your JPA entity classes will work with both MySQL and SQL Server without modification:
- `@GeneratedValue(strategy = GenerationType.IDENTITY)` works with both
- `@Enumerated(EnumType.STRING)` handles enum conversion
- Hibernate dialect handles the differences automatically

## Sample Data Included

After running the schema script, you'll have:

- **10 Categories**: Fiction, Non-Fiction, Science, History, Business, etc.
- **1 Admin User**: 
  - Email: `admin@bookstore.com`
  - Password: `admin123`
  - Role: `ADMIN`
- **8 Books**: The Great Gatsby, 1984, Sapiens, Clean Code, Harry Potter, etc.

## Testing the Setup

You can test with this SQL query:

```sql
USE bookstore_db;
GO

-- Test join query
SELECT 
    b.title,
    b.author,
    c.category_name,
    b.price,
    b.stock_quantity
FROM books b
INNER JOIN categories c ON b.category_id = c.category_id
ORDER BY b.title;
GO
```

## Next Steps

1. ✅ SQL Server installed and configured
2. ✅ Database and tables created
3. ✅ Sample data inserted
4. ✅ Backend configured for SQL Server
5. ✅ Maven dependencies updated
6. ➡️ Start backend server and test API endpoints
7. ➡️ Run Android app and verify it connects to SQL Server backend

Your Books Selling App is now using **SQL Server** instead of MySQL! 🎉
