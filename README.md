# 📚 Books Selling App - Android & Spring Boot

A complete e-commerce mobile application for selling books, built with **Android (Kotlin)** and **Spring Boot (Java)** backend.

---

## 🎯 Project Overview

This is a full-stack mobile application that allows users to:
- Browse and search books by category
- View detailed book information
- Add books to cart and place orders
- Track order history
- Rate and review books
- Support multiple payment methods (COD, VNPay, MoMo)

**Admin features** (Web interface):
- Manage books and categories
- Process orders
- View sales statistics

---

## 🛠️ Technology Stack

### Frontend (Mobile App)
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Libraries**:
  - Retrofit 2.9.0 - REST API client
  - OkHttp 4.12.0 - HTTP networking
  - Coil 2.5.0 - Image loading
  - Navigation Compose - Screen navigation
  - Coroutines - Asynchronous programming
  - DataStore - Local data storage
  - Material3 - UI components

### Backend (Server)
- **Language**: Java
- **Framework**: Spring Boot 3.x
- **Database**: MySQL 8.x
- **Authentication**: JWT (JSON Web Tokens)
- **Libraries**:
  - Spring Data JPA - Database ORM
  - Spring Security - Authentication & Authorization
  - Lombok - Boilerplate reduction
  - Swagger - API documentation

### Database
- **Type**: MySQL
- **Tables**: 7 main tables
  - users
  - categories
  - books
  - cart_items
  - orders
  - order_items
  - reviews

---

## 📋 Project Requirements

### Development Environment
- ✅ Android Studio (Arctic Fox or later)
- ✅ JDK 11+ (for both Android and Spring Boot)
- ✅ MySQL 8.0+
- ✅ Git

### Minimum Android Version
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 15)

---

## 🚀 Project Phases

### ✅ Phase 1: Design & Setup (COMPLETED)
- [x] Database schema design
- [x] UI/UX mockups and navigation structure
- [x] Android project setup with dependencies
- [x] Documentation and guides

**Deliverables**:
- `database/schema.sql` - Complete database schema
- `database/DATABASE_DESIGN.md` - Database documentation
- `docs/UI_DESIGN.md` - Screen designs and navigation
- `docs/EMULATOR_SETUP.md` - Emulator setup guide
- Updated `build.gradle.kts` with dependencies
- Enhanced `.gitignore`

### 🔄 Phase 2: Backend Development (IN PROGRESS)
- [ ] Spring Boot project structure
- [ ] JPA entities and repositories
- [ ] REST API endpoints
- [ ] JWT authentication
- [ ] MySQL integration
- [ ] Admin web interface (basic)

### 🔄 Phase 3: Integration & Features
- [ ] Implement Android screens
- [ ] Connect Android app to backend API
- [ ] Implement authentication flow
- [ ] Payment gateway integration (VNPay/MoMo)
- [ ] Testing on real device/emulator
- [ ] Bug fixes and optimization

---

## 📁 Project Structure

```
books-selling-app/
├── app/                          # Android application
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/projectprm/
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data/          # Data layer (API, models, repositories)
│   │   │   │   ├── ui/            # UI layer (screens, composables)
│   │   │   │   └── viewmodel/     # ViewModels
│   │   │   └── res/               # Resources (layouts, drawables, etc.)
│   └── build.gradle.kts
│
├── backend/                      # Spring Boot backend (Phase 2)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/bookstore/
│   │   │   │       ├── entity/
│   │   │   │       ├── repository/
│   │   │   │       ├── service/
│   │   │   │       ├── controller/
│   │   │   │       └── config/
│   │   │   └── resources/
│   │   │       └── application.properties
│   └── pom.xml
│
├── database/                     # Database files
│   ├── schema.sql               # MySQL schema with sample data
│   └── DATABASE_DESIGN.md       # Database documentation
│
├── docs/                        # Documentation
│   ├── UI_DESIGN.md            # UI/UX design document
│   ├── EMULATOR_SETUP.md       # Emulator setup guide
│   └── API_DOCUMENTATION.md    # API endpoints (Phase 2)
│
├── .gitignore
├── README.md                    # This file
└── PHASE1_SUMMARY.md           # Phase 1 completion summary
```

---

## 🗄️ Database Setup

### 1. Create Database
```bash
mysql -u root -p
```

```sql
CREATE DATABASE bookstore_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bookstore_db;
source database/schema.sql;
```

### 2. Verify Tables
```sql
SHOW TABLES;
SELECT * FROM categories;
SELECT * FROM books;
```

### 3. Default Admin Credentials
- **Email**: admin@bookstore.com
- **Password**: admin123 (hashed in database)

---

## 📱 Android Setup

### 1. Open Project in Android Studio
```bash
cd d:\FPT\FA25\PRM
# Open folder in Android Studio
```

### 2. Sync Gradle
```
File → Sync Project with Gradle Files
```

### 3. Set up Emulator
- Follow instructions in `docs/EMULATOR_SETUP.md`
- Recommended: Pixel 5, API 34

### 4. Run App
```
Run → Run 'app' (Shift+F10)
```

---

## 🔧 Configuration

### Android App Configuration (Phase 3)
Update `app/src/main/java/com/example/projectprm/utils/Constants.kt`:
```kotlin
object Constants {
    const val BASE_URL = "http://10.0.2.2:8080/api/"  // For emulator
    // const val BASE_URL = "http://localhost:8080/api/"  // For physical device
}
```

### Backend Configuration (Phase 2)
Update `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore_db
spring.datasource.username=root
spring.datasource.password=your_password
```

---

## 📚 API Documentation

API documentation will be available in Phase 2 at:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Documentation**: `docs/API_DOCUMENTATION.md`

### Planned Endpoints:
```
Authentication:
  POST /api/auth/register
  POST /api/auth/login

Books:
  GET  /api/books
  GET  /api/books/{id}
  GET  /api/books/category/{categoryId}
  POST /api/books (Admin)

Cart:
  GET  /api/cart
  POST /api/cart/add
  DELETE /api/cart/remove/{id}

Orders:
  POST /api/orders
  GET  /api/orders/user/{userId}
  GET  /api/orders/{id}

...and more
```

---

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew test
```

### Run Instrumented Tests
```bash
./gradlew connectedAndroidTest
```

---

## 📦 Build & Deploy

### Build Debug APK
```bash
./gradlew assembleDebug
```

### Build Release APK
```bash
./gradlew assembleRelease
```

APK location: `app/build/outputs/apk/`

---

## 🤝 Contributing

This is a student project for FPT University. Follow the development phases and commit regularly.

### Git Workflow
```bash
# Stage changes
git add .

# Commit with meaningful message
git commit -m "Phase X: Description of changes"

# Push to GitHub
git push origin main
```

---

## 📝 License

This project is created for educational purposes at FPT University.

---

## 👥 Team

- **Student**: [Your Name]
- **Course**: PRM (Programming Mobile)
- **Semester**: FA25
- **Institution**: FPT University

---

## 📞 Support

For issues or questions:
1. Check documentation in `docs/` folder
2. Review `PHASE1_SUMMARY.md` for current progress
3. Consult with course instructor

---

## 🎓 Learning Outcomes

This project demonstrates:
- ✅ Android native development with Kotlin
- ✅ Modern UI with Jetpack Compose
- ✅ RESTful API design and implementation
- ✅ Database design and MySQL
- ✅ Spring Boot backend development
- ✅ Full-stack integration
- ✅ Git version control
- ✅ Software development lifecycle

---

**Status**: Phase 1 Complete ✅ | Phase 2 Ready to Start 🚀

**Last Updated**: November 10, 2025
