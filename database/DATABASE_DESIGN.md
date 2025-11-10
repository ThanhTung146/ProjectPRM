# Books Selling App - Database Design

## Database Overview
- **Database Name**: bookstore_db
- **Database Type**: MySQL
- **Character Set**: utf8mb4
- **Collation**: utf8mb4_unicode_ci

## Tables Structure

### 1. users
Stores user information for both customers and admins.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| user_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| email | VARCHAR(255) | NOT NULL, UNIQUE | User email for login |
| password_hash | VARCHAR(255) | NOT NULL | Hashed password (BCrypt) |
| full_name | VARCHAR(255) | NOT NULL | User's full name |
| phone_number | VARCHAR(20) | NULL | Contact phone number |
| address | TEXT | NULL | Delivery address |
| role | ENUM | DEFAULT 'CUSTOMER' | User role (CUSTOMER, ADMIN) |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Account creation time |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update time |
| is_active | BOOLEAN | DEFAULT TRUE | Account status |

**Indexes**: email, role

---

### 2. categories
Book categories/genres.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| category_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique category identifier |
| category_name | VARCHAR(100) | NOT NULL, UNIQUE | Category name |
| description | TEXT | NULL | Category description |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Creation time |

**Indexes**: category_name

---

### 3. books
Book inventory and details.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| book_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique book identifier |
| title | VARCHAR(255) | NOT NULL | Book title |
| author | VARCHAR(255) | NOT NULL | Book author |
| category_id | INT | FOREIGN KEY → categories | Book category |
| description | TEXT | NULL | Book description |
| price | DECIMAL(10,2) | NOT NULL | Book price in VND |
| stock_quantity | INT | NOT NULL, DEFAULT 0 | Available stock |
| isbn | VARCHAR(20) | UNIQUE | ISBN number |
| publisher | VARCHAR(255) | NULL | Publisher name |
| publication_year | INT | NULL | Year of publication |
| pages | INT | NULL | Number of pages |
| language | VARCHAR(50) | DEFAULT 'English' | Book language |
| cover_image_url | VARCHAR(500) | NULL | Cover image URL |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Creation time |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update time |
| is_active | BOOLEAN | DEFAULT TRUE | Book availability status |

**Indexes**: title, author, category_id, price

---

### 4. cart_items
Shopping cart items for users.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| cart_item_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique cart item identifier |
| user_id | INT | FOREIGN KEY → users | User who owns the cart |
| book_id | INT | FOREIGN KEY → books | Book in cart |
| quantity | INT | NOT NULL, DEFAULT 1 | Quantity of books |
| added_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Time added to cart |

**Unique Constraint**: (user_id, book_id)  
**Indexes**: user_id

---

### 5. orders
Customer orders.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| order_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique order identifier |
| user_id | INT | FOREIGN KEY → users | Customer who placed order |
| order_date | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Order placement time |
| total_amount | DECIMAL(10,2) | NOT NULL | Total order amount |
| status | ENUM | DEFAULT 'PENDING' | Order status |
| payment_method | ENUM | NOT NULL | Payment method used |
| payment_status | ENUM | DEFAULT 'UNPAID' | Payment status |
| shipping_address | TEXT | NOT NULL | Delivery address |
| phone_number | VARCHAR(20) | NOT NULL | Contact number |
| notes | TEXT | NULL | Additional notes |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Creation time |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update time |

**Order Status Values**: PENDING, CONFIRMED, PROCESSING, SHIPPED, DELIVERED, CANCELLED  
**Payment Method Values**: COD, VNPAY, MOMO  
**Payment Status Values**: UNPAID, PAID, REFUNDED

**Indexes**: user_id, status, order_date

---

### 6. order_items
Items within each order.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| order_item_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique order item identifier |
| order_id | INT | FOREIGN KEY → orders | Associated order |
| book_id | INT | FOREIGN KEY → books | Book ordered |
| quantity | INT | NOT NULL | Quantity ordered |
| price_at_purchase | DECIMAL(10,2) | NOT NULL | Price at time of purchase |
| subtotal | DECIMAL(10,2) | NOT NULL | Item subtotal (price × quantity) |

**Indexes**: order_id, book_id

---

### 7. reviews
Book reviews and ratings from customers.

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| review_id | INT | PRIMARY KEY, AUTO_INCREMENT | Unique review identifier |
| book_id | INT | FOREIGN KEY → books | Book being reviewed |
| user_id | INT | FOREIGN KEY → users | User who wrote review |
| rating | INT | NOT NULL, CHECK (1-5) | Star rating (1-5) |
| comment | TEXT | NULL | Review text |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Review creation time |
| updated_at | TIMESTAMP | AUTO UPDATE | Last update time |

**Unique Constraint**: (user_id, book_id) - One review per user per book  
**Indexes**: book_id, user_id, rating

---

## Entity Relationships

```
users (1) ──────< (M) cart_items
users (1) ──────< (M) orders
users (1) ──────< (M) reviews

categories (1) ──────< (M) books

books (1) ──────< (M) cart_items
books (1) ──────< (M) order_items
books (1) ──────< (M) reviews

orders (1) ──────< (M) order_items
```

## Database Setup Instructions

1. **Create Database**:
```sql
CREATE DATABASE bookstore_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bookstore_db;
```

2. **Run Schema**:
```bash
mysql -u root -p bookstore_db < schema.sql
```

3. **Verify Tables**:
```sql
SHOW TABLES;
```

## Sample Data Included

- **10 Categories**: Fiction, Non-Fiction, Science, History, Business, etc.
- **1 Admin User**: admin@bookstore.com (password: admin123)
- **8 Sample Books**: Classic and modern books with real ISBNs

## Notes

- All prices are in VND (Vietnamese Dong)
- Passwords are hashed using BCrypt ($2a$10$...)
- Foreign key constraints prevent data inconsistency
- Cascade deletes for cart_items and order_items
- Indexes optimize query performance
- Book cover images use Open Library Cover API
