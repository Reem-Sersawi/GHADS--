<div align="center">

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/logo.png" alt="GHADS Logo" width="120"/>

# 🕊️ GHADS
### Gaza Humanitarian Aid Distribution System

**A centralized desktop application to coordinate humanitarian aid distribution for displaced families in Gaza**

[![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![JavaFX](https://img.shields.io/badge/JavaFX-25-0078D4?style=for-the-badge&logo=java&logoColor=white)](https://openjfx.io)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com)
[![JDBC](https://img.shields.io/badge/JDBC-4.2-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/database/technologies/appdev/jdbc.html)
[![License](https://img.shields.io/badge/License-Academic-green?style=for-the-badge)](LICENSE)

---

*The Islamic University of Gaza · Faculty of Information Technology · Programming III Lab · CSCI 2108*

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [The Problem](#-the-problem)
- [The Solution](#-the-solution)
- [Features](#-features)
- [Screenshots](#-screenshots)
- [Architecture](#-architecture)
- [Database Design](#-database-design)
- [Technologies Used](#-technologies-used)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [Default Credentials](#-default-credentials)
- [Design Patterns](#-design-patterns)
- [Bonus Features](#-bonus-features)
- [Developer](#-developer)

---

## 🌍 Overview

**GHADS** (Gaza Humanitarian Aid Distribution System) is a desktop application built with **Java** and **JavaFX** that enables multiple humanitarian organizations to coordinate the distribution of aid to displaced families in Gaza through one shared, centralized database.

> *"We learn tools to solve real problems in our lives — and that is what makes coding truly powerful and meaningful."*
> — Instructor Aya N. Alharazin

---

## ❗ The Problem

When multiple organizations operate independently:

- ✗ The **same family** may receive aid multiple times
- ✗ Another family may receive **nothing at all**
- ✗ No visibility into **who gave what, when, and to whom**
- ✗ Resources are wasted due to **lack of coordination**

---

## ✅ The Solution

GHADS provides a **single shared database** for all organizations, ensuring:

- Every family is registered **once**
- Every aid distribution is **logged and tracked**
- Duplicate distributions are **automatically prevented**
- Families in need are **never forgotten**

---

## ✨ Features

### 👤 Admin Features

| Feature | Description |
|---------|-------------|
| 📊 Dashboard | View system-wide statistics: organizations, coordinators, families, served & unserved counts |
| 🏢 Organization Management | Full CRUD — Add, Edit, Delete organizations with validation |
| 👥 User Management | Create coordinator accounts with photo upload, edit, delete |
| 👨‍👩‍👧‍👦 Family Management | Register, edit, and delete families with national ID uniqueness check |
| 📋 Aid Records | View all distributions across all organizations, filter by organization |
| 🔑 Change Password | Securely update password with current password verification |
| 🚪 Logout | End session and return to login screen |

### 🤝 Coordinator Features

| Feature | Description |
|---------|-------------|
| 📊 Dashboard | View total families, families served by own organization, and unserved count |
| 👨‍👩‍👧‍👦 Family Registration | Register new families with full validation and national ID uniqueness check |
| 📦 Aid Distribution | Record aid distributions with **automatic duplicate check** |
| 🔍 Smart Search | Filter families by Most Vulnerable (HIGH first) or Not Yet Served |
| 👤 Profile | View and edit personal information |
| 🔑 Change Password | Update password securely |
| 🚪 Logout | End session and return to login screen |

### 🛡️ Smart Duplicate Check (Core Feature)

```
Before saving any aid record:

  IF family.vulnerabilityLevel == HIGH
      → ✅ ALLOW  (always, no restrictions)

  IF family.vulnerabilityLevel == MEDIUM or LOW
      AND same aid type was distributed within last 30 days
      → ❌ REJECT with alert showing:
           • Family name
           • Vulnerability level
           • Organization that previously gave aid
           • Date of previous distribution
           • Aid type
```

### 🎨 UI Features

- **Dark / Light theme** toggle from the menu bar
- **Font Size** change (12, 14, 16px)
- **Font Family** change (Arial, Georgia, Verdana, Times New Roman)
- **About** dialog with system info and developer name
- **Professional CSS** with custom style classes throughout

---

## 📸 Screenshots

### Login Screen

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Login.png" width="800"/>

### Admin Dashboard

| Light Theme | Dark Theme |
|:-----------:|:----------:|
| <img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Admin-Dashboard.png" width="380"/> | <img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Admin-Dashboard-dark.png" width="380"/> |

### Organizations Management

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Admin-Org.png" width="800"/>

### Users Management

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Admin-user.png" width="800"/>

### Families Management

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Admin-family.png" width="800"/>

### Aid Records (Admin)

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/admin-Aid.png" width="800"/>

### Coordinator Dashboard

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Coor-Dashboard.png" width="800"/>

### Aid Distribution (Coordinator)

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Coor-Aid.png" width="800"/>

### Family Registration (Coordinator)

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Coor-Family.png" width="800"/>

### Coordinator Profile

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Coor-profile.png" width="800"/>

### Change Password

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/Coor-ChangePass.png" width="800"/>

### About Dialog

<img src="https://raw.githubusercontent.com/Reem-Sersawi/GHADS/main/screenshots/about.png" width="400"/>

---

## 🏗️ Architecture

GHADS follows a strict **MVC + DAO** architecture:

```
┌─────────────────────────────────────────────────────────┐
│                        VIEW LAYER                        │
│              (FXML files built with Scene Builder)       │
│   Login.fxml  AdminDashboard.fxml  CoordinatorDash.fxml  │
└────────────────────────┬────────────────────────────────┘
                         │  user events
                         ▼
┌─────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                      │
│         LoginController  AdminDashboardController        │
│              CoordinatorDashboardController              │
└────────────────────────┬────────────────────────────────┘
                         │  calls
                         ▼
┌─────────────────────────────────────────────────────────┐
│                      DAO LAYER                           │
│     UserDAO  OrganizationDAO  FamilyDao  AidDistDAO      │
│              (all database operations here)              │
└────────────────────────┬────────────────────────────────┘
                         │  JDBC / PreparedStatement
                         ▼
┌─────────────────────────────────────────────────────────┐
│                     MODEL LAYER                          │
│         User  Organization  Family  AidDistribution      │
│                    (Plain Java POJOs)                    │
└─────────────────────────────────────────────────────────┘
                         │
                         ▼
                   [ MySQL Database ]
                      ghads_db
```

---

## 🗄️ Database Design

### Entity Relationship Diagram

```
Organization ──────< User
     │                 │
     │                 │
     └──────< AidDistribution >──── Family
```

### Tables

#### `Organization`

| Column | Type | Description |
|--------|------|-------------|
| org_id | INT PK | Auto increment |
| name | VARCHAR(100) UNIQUE | Organization name |
| type | VARCHAR | NGO / UN / Local / International |
| contact_info | VARCHAR | Phone or email |

#### `User`

| Column | Type | Description |
|--------|------|-------------|
| user_id | INT PK | Auto increment |
| username | VARCHAR UNIQUE | Login username |
| password | VARCHAR | Plain text (course requirement) |
| full_name | VARCHAR | Display name |
| email | VARCHAR UNIQUE | Contact email |
| role | VARCHAR | ADMIN or COORDINATOR |
| org_id | INT FK | → Organization |
| profile_image | BLOB | *Bonus: user photo* |
| profile_image_type | VARCHAR | *Bonus: image format* |

#### `Family`

| Column | Type | Description |
|--------|------|-------------|
| family_id | INT PK | Auto increment |
| household_name | VARCHAR | Family name |
| phone | VARCHAR | Contact number |
| location | VARCHAR | Area or camp |
| family_size | INT | Number of members |
| national_id | VARCHAR UNIQUE | Unique identifier |
| vulnerability_level | VARCHAR | HIGH / MEDIUM / LOW |
| registration_date | DATE | When registered |
| last_aid_date | DATE | Updated after each distribution |

#### `AidDistribution`

| Column | Type | Description |
|--------|------|-------------|
| distribution_id | INT PK | Auto increment |
| family_id | INT FK | → Family |
| org_id | INT FK | → Organization |
| distributed_by | INT FK | → User |
| distribution_date | DATE | When distributed |
| aid_type | VARCHAR | *Bonus: food/water/medicine/tent/blankets/clothes/cash* |

---

## 🛠️ Technologies Used

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17+ | Core programming language |
| JavaFX | 25 | Desktop UI framework |
| Scene Builder | Any | FXML visual design |
| MySQL | 8.0 | Relational database |
| JDBC | 4.2 | Database connectivity |
| CSS | — | Custom UI styling |

---

## 📁 Project Structure

<details>
<summary><b>📁 Click to expand project structure</b></summary>

```
GHADS/
│
├── 📄 Main.java
│
├── 📁 controller/
│   ├── AdminDashboardController.java
│   ├── ChangePasswordController.java
│   ├── CoordinatorDashboardController.java
│   ├── LoginController.java
│   ├── ProfileImageController.java
│   └── RegisterFamilyController.java
│
├── 📁 css/
│   ├── dark-theme.css
│   └── light-theme.css
│
├── 📁 dao/
│   ├── AidDistributionDAO.java
│   ├── DatabaseConnection.java
│   ├── FamilyDAO.java
│   ├── OrganizationDAO.java
│   └── UserDAO.java
│
├── 📁 database/
│   └── ghads_db.sql
│
├── 📁 images/
│   └── (uploaded profile photos stored here)
│
├── 📁 model/
│   ├── AidDistribution.java
│   ├── Family.java
│   ├── Organization.java
│   └── User.java
│
├── 📁 screenshots/
│   ├── logo.png
│   ├── Login.png
│   ├── Admin-Dashboard.png
│   ├── Admin-Dashboard-dark.png
│   ├── Admin-Org.png
│   ├── Admin-user.png
│   ├── Admin-family.png
│   ├── admin-Aid.png
│   ├── Coor-Dashboard.png
│   ├── Coor-Aid.png
│   ├── Coor-Family.png
│   ├── Coor-profile.png
│   ├── Coor-ChangePass.png
│   └── about.png
│
├── 📁 utils/
│   ├── SessionManager.java
│   ├── ThemeManager.java
│   └── ValidationUtils.java
│
└── 📁 views/
    ├── admin_dashboard.fxml
    ├── change_password.fxml
    ├── coordinator_dashboard.fxml
    ├── login.fxml
    ├── profile_image.fxml
    └── register_family.fxml
```
</details>

---

## 🚀 Getting Started

### Prerequisites

- Java JDK 17 or higher
- JavaFX SDK 25
- MySQL Server 8.0
- NetBeans IDE (recommended) or IntelliJ IDEA
- MySQL Connector/J library

### 1. Clone the Repository

```bash
git clone https://github.com/Reem-Sersawi/GHADS.git
cd GHADS
```

### 2. Create the Database

```sql
CREATE DATABASE ghads_db;
USE ghads_db;
```

Then execute the `ghads_db.sql` file located in the `database/` folder.

### 3. Configure Database Connection

Edit `src/dao/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/ghads_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "YOUR_PASSWORD";
```

### 4. Add MySQL Connector/J

- Download MySQL Connector/J from [here](https://dev.mysql.com/downloads/connector/j/)
- Add the `.jar` file to your project's libraries

### 5. Run the Application

Open the project in **NetBeans** → Right-click → **Run**

---

## 🔑 Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| Coordinator | `coord1` | `coord123` |
| Coordinator | `coord2` | `coord123` |

> ⚠️ Change the default passwords after first login.

---

## 🎨 Design Patterns

### 1. Singleton — `DatabaseConnection`

The Singleton pattern ensures only **one database connection** exists throughout the entire application lifetime.

```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}
```

**Singleton is also used in:**
- `SessionManager` → Manages the current user session
- `ThemeManager` → Centralized theme management (Dark/Light)

### 2. MVC (Model-View-Controller)

| Layer | Responsibility | Files |
|-------|----------------|-------|
| **Model** | Data representation | `model/` (User, Family, Organization, AidDistribution) |
| **View** | User interface | `views/` (FXML files) |
| **Controller** | Application logic | `controller/` (AdminDashboard, Login, CoordinatorDashboard) |

### 3. DAO (Data Access Object)

Each database table has its own DAO using **JDBC**:

| DAO | Operations |
|-----|------------|
| `UserDAO` | CRUD + Authentication |
| `OrganizationDAO` | CRUD |
| `FamilyDAO` | CRUD + Vulnerability filters |
| `AidDistributionDAO` | CRUD + Duplicate check |

### 4. Streams & Lambdas

Used throughout for data filtering:

```java
long coordinatorCount = users.stream()
    .filter(u -> u.getRole().equalsIgnoreCase("COORDINATOR"))
    .count();
```

---

## 🌟 Bonus Features

### ✅ Bonus 1: User Profile Photo

- Added `profile_image` (BLOB) and `profile_image_type` columns to the `User` table
- Admin can upload a coordinator's photo when creating their account
- Coordinator can update their own photo from the Profile page
- Image stored as byte array in the database

### ✅ Bonus 2: Aid Type Deduplication

- Added `aid_type` column to `AidDistribution` table
- The duplicate check is now **per aid type**, not just per family:
  - A MEDIUM/LOW family **cannot** receive the same aid type twice within 30 days
  - But can receive **different** aid types in the same period
  - HIGH vulnerability families are **always** allowed regardless of type or date
- Available aid types: `food`, `water`, `medicine`, `tent`, `blankets`, `clothes`, `cash`

---

## 👩‍💻 Developer

| | |
|--|--|
| **Name** | Reem Saeed Alsersawi |
| **University** | The Islamic University of Gaza |
| **Faculty** | Faculty of Information Technology |
| **Course** | Programming III Lab — CSCI 2108 |
| **Instructor** | Aya N. Alharazin |
| **Year** | 2026 |

---

## 📄 License

This project was developed as a final academic project for CSCI 2108 at the Islamic University of Gaza.  
All rights reserved © 2026.

---

<div align="center">

*Built with ❤️ for Gaza*

🕊️

</div>
```


**الآن كل شيء سيعمل بشكل مثالي! 🚀**
