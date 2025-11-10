-- Books Selling App - Database Schema
-- SQL Server Database

-- Drop existing tables if they exist (for clean setup)
IF OBJECT_ID('reviews', 'U') IS NOT NULL DROP TABLE reviews;
IF OBJECT_ID('order_items', 'U') IS NOT NULL DROP TABLE order_items;
IF OBJECT_ID('orders', 'U') IS NOT NULL DROP TABLE orders;
IF OBJECT_ID('cart_items', 'U') IS NOT NULL DROP TABLE cart_items;
IF OBJECT_ID('books', 'U') IS NOT NULL DROP TABLE books;
IF OBJECT_ID('categories', 'U') IS NOT NULL DROP TABLE categories;
IF OBJECT_ID('users', 'U') IS NOT NULL DROP TABLE users;
GO

-- Users Table
CREATE TABLE users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    email NVARCHAR(255) NOT NULL UNIQUE,
    password_hash NVARCHAR(255) NOT NULL,
    full_name NVARCHAR(255) NOT NULL,
    phone_number NVARCHAR(20),
    address NVARCHAR(MAX),
    role NVARCHAR(20) DEFAULT 'CUSTOMER' CHECK (role IN ('CUSTOMER', 'ADMIN')),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    is_active BIT DEFAULT 1
);
CREATE INDEX idx_email ON users(email);
CREATE INDEX idx_role ON users(role);
GO

-- Categories Table
CREATE TABLE categories (
    category_id INT PRIMARY KEY IDENTITY(1,1),
    category_name NVARCHAR(100) NOT NULL UNIQUE,
    description NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE()
);
CREATE INDEX idx_category_name ON categories(category_name);
GO

-- Books Table
CREATE TABLE books (
    book_id INT PRIMARY KEY IDENTITY(1,1),
    title NVARCHAR(255) NOT NULL,
    author NVARCHAR(255) NOT NULL,
    category_id INT NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL DEFAULT 0,
    isbn NVARCHAR(20) UNIQUE,
    publisher NVARCHAR(255),
    publication_year INT,
    pages INT,
    language NVARCHAR(50) DEFAULT 'English',
    cover_image_url NVARCHAR(500),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    is_active BIT DEFAULT 1,
    FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
CREATE INDEX idx_title ON books(title);
CREATE INDEX idx_author ON books(author);
CREATE INDEX idx_category ON books(category_id);
CREATE INDEX idx_price ON books(price);
GO

-- Cart Items Table (for shopping cart)
CREATE TABLE cart_items (
    cart_item_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    added_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    CONSTRAINT unique_user_book UNIQUE (user_id, book_id)
);
CREATE INDEX idx_user_cart ON cart_items(user_id);
GO

-- Orders Table
CREATE TABLE orders (
    order_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT NOT NULL,
    order_date DATETIME2 DEFAULT GETDATE(),
    total_amount DECIMAL(10, 2) NOT NULL,
    status NVARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED')),
    payment_method NVARCHAR(20) NOT NULL CHECK (payment_method IN ('COD', 'VNPAY', 'MOMO')),
    payment_status NVARCHAR(20) DEFAULT 'UNPAID' CHECK (payment_status IN ('UNPAID', 'PAID', 'REFUNDED')),
    shipping_address NVARCHAR(MAX) NOT NULL,
    phone_number NVARCHAR(20) NOT NULL,
    notes NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
CREATE INDEX idx_user_orders ON orders(user_id);
CREATE INDEX idx_status ON orders(status);
CREATE INDEX idx_order_date ON orders(order_date);
GO

-- Order Items Table
CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT NOT NULL,
    book_id INT NOT NULL,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10, 2) NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(book_id)
);
CREATE INDEX idx_order ON order_items(order_id);
CREATE INDEX idx_book_order ON order_items(book_id);
GO

-- Reviews Table
CREATE TABLE reviews (
    review_id INT PRIMARY KEY IDENTITY(1,1),
    book_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment NVARCHAR(MAX),
    created_at DATETIME2 DEFAULT GETDATE(),
    updated_at DATETIME2 DEFAULT GETDATE(),
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT unique_user_book_review UNIQUE (user_id, book_id)
);
CREATE INDEX idx_book_reviews ON reviews(book_id);
CREATE INDEX idx_user_reviews ON reviews(user_id);
CREATE INDEX idx_rating ON reviews(rating);
GO

-- Insert sample categories
INSERT INTO categories (category_name, description) VALUES
('Fiction', 'Fictional literature including novels and short stories'),
('Non-Fiction', 'Non-fictional books including biographies and documentaries'),
('Science', 'Books about science, technology, and research'),
('History', 'Historical books and documentaries'),
('Business', 'Business, economics, and management books'),
('Self-Help', 'Personal development and self-improvement books'),
('Technology', 'Programming, IT, and technology books'),
('Children', 'Books for children and young readers'),
('Romance', 'Romance novels and love stories'),
('Mystery', 'Mystery, thriller, and detective novels');
GO

-- Insert sample admin user (password: admin123 - BCrypt hashed)
INSERT INTO users (email, password_hash, full_name, role) VALUES
('admin@bookstore.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Admin User', 'ADMIN');
GO

-- Insert sample books with Open Library cover images
INSERT INTO books (title, author, category_id, description, price, stock_quantity, isbn, publisher, publication_year, pages, cover_image_url) VALUES
('The Great Gatsby', 'F. Scott Fitzgerald', 1, 'A classic American novel set in the Jazz Age', 150000, 50, '9780743273565', 'Scribner', 1925, 180, 'https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg'),
('To Kill a Mockingbird', 'Harper Lee', 1, 'A gripping tale of racial injustice and childhood innocence', 180000, 40, '9780061120084', 'Harper Perennial', 1960, 324, 'https://covers.openlibrary.org/b/isbn/9780061120084-L.jpg'),
('1984', 'George Orwell', 1, 'A dystopian social science fiction novel', 160000, 60, '9780451524935', 'Signet Classic', 1949, 328, 'https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg'),
('Sapiens', 'Yuval Noah Harari', 2, 'A brief history of humankind', 250000, 35, '9780062316097', 'Harper', 2015, 443, 'https://covers.openlibrary.org/b/isbn/9780062316097-L.jpg'),
('Educated', 'Tara Westover', 2, 'A memoir about education and family', 220000, 30, '9780399590504', 'Random House', 2018, 334, 'https://covers.openlibrary.org/b/isbn/9780399590504-L.jpg'),
('The Lean Startup', 'Eric Ries', 5, 'How constant innovation creates radically successful businesses', 280000, 45, '9780307887894', 'Crown Business', 2011, 336, 'https://covers.openlibrary.org/b/isbn/9780307887894-L.jpg'),
('Clean Code', 'Robert C. Martin', 7, 'A handbook of agile software craftsmanship', 350000, 55, '9780132350884', 'Prentice Hall', 2008, 464, 'https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg'),
('Harry Potter and the Philosopher''s Stone', 'J.K. Rowling', 8, 'The first book in the Harry Potter series', 200000, 100, '9780747532699', 'Bloomsbury', 1997, 223, 'https://covers.openlibrary.org/b/isbn/9780747532699-L.jpg');
GO

-- Create trigger for updated_at on users table
CREATE TRIGGER trg_users_updated_at
ON users
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE users
    SET updated_at = GETDATE()
    FROM users u
    INNER JOIN inserted i ON u.user_id = i.user_id;
END;
GO

-- Create trigger for updated_at on books table
CREATE TRIGGER trg_books_updated_at
ON books
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE books
    SET updated_at = GETDATE()
    FROM books b
    INNER JOIN inserted i ON b.book_id = i.book_id;
END;
GO

-- Create trigger for updated_at on orders table
CREATE TRIGGER trg_orders_updated_at
ON orders
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE orders
    SET updated_at = GETDATE()
    FROM orders o
    INNER JOIN inserted i ON o.order_id = i.order_id;
END;
GO

-- Create trigger for updated_at on reviews table
CREATE TRIGGER trg_reviews_updated_at
ON reviews
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;
    UPDATE reviews
    SET updated_at = GETDATE()
    FROM reviews r
    INNER JOIN inserted i ON r.review_id = i.review_id;
END;
GO
