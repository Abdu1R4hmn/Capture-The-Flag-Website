
# Capture The Flag (CTF) Web Platform for Cybersecurity Education

A beginner-friendly web-based CTF platform designed to improve cybersecurity practical skills through ethical hacking challenges.

---

## Table of Contents
- [About the Project](#about-the-project)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Technology Stack](#technology-stack)
- [Screenshots](#screenshots)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Testing](#testing)
- [Roadmap / Future Enhancements](#roadmap--future-enhancements)
- [License](#license)
- [Author](#author)

---

## About the Project
Cybersecurity threats continue to increase, yet beginners have limited safe and accessible platforms to develop practical skills.  
This project introduces a Capture The Flag (CTF) learning system with:
- Progressive beginner-focused challenges
- Secure user authentication and role-based features
- Continuous challenge improvement through user feedback

The platform is intended for cybersecurity education, especially at UTM (Universiti Teknologi Malaysia).

---

## Features
- User authentication (login/register)
- Browse and solve cybersecurity challenges
- Hints and solutions for educational learning
- Progress tracking with stars and completion status
- Feedback system for challenge improvement
- Admin and lecturer dashboards for content management

---

## System Architecture
The system follows the MVC structure with secured REST APIs:

```
React Frontend  →  Spring Boot API  →  MySQL Database
```

Role-based access:
- User: challenge interaction
- Lecturer: challenge content management
- Admin: user account management

---

## Technology Stack

### Frontend
- React.js
- Axios

### Backend
- Spring Boot (Java)
- Spring Security
- BCrypt password hashing

### Database
- MySQL

### Tools
- IntelliJ IDEA
- VS Code
- GitHub
- Figma / PlantUML (Design)

---

## Screenshots
> Replace the placeholder images below with actual screenshots from your project.

```
/assets/screens/register.png
/assets/screens/challenges.png
/assets/screens/profile.png
/assets/screens/admin-dashboard.png
```

Example placeholders:

- Registration Page  
- Challenge Listings Page  
- Profile Page  
- Admin Dashboard  

---

## Getting Started

### Prerequisites
Ensure the following are installed:
- Node.js (v16+)
- Java JDK 17+
- MySQL Server
- Git

---

### Installation

#### 1️⃣ Clone Repository
```bash
git clone https://github.com/<your-username>/<repo-name>.git
cd <repo-name>
```

#### 2️⃣ Backend Setup
```bash
cd backend
mvn clean install
```
Update `application.properties` with your MySQL configuration:
```env
spring.datasource.url=jdbc:mysql://localhost:3306/ctf_db
spring.datasource.username=root
spring.datasource.password=yourpassword
```
Run backend:
```bash
mvn spring-boot:run
```

#### 3️⃣ Frontend Setup
```bash
cd frontend
npm install
npm start
```

The app should run at:  
Frontend → `http://localhost:3000`  
Backend API → `http://localhost:8080`

---

## Usage
1. Register as a user
2. Browse available challenges
3. Submit the correct flag to earn stars
4. View progress in profile page
5. (Admin/Lecturer) Manage challenges and users via dashboard

---

## Testing

Performed testing types:
- White-box testing: backend logic & security verification
- Black-box testing: functional behavior and UI checks
- User acceptance testing (usability & accessibility)

---

## Roadmap / Future Enhancements
- Additional challenge categories (Web, Forensics, OSINT)
- Leaderboard and gamification
- OAuth2 login flow
- Improved analytics for instructors

---

## License
This project is intended for **educational use only**.  
(You can add MIT License or others later.)

---

## Author
**Abdulrahman Osama**  
Capture The Flag Web Platform Developer  
Universiti Teknologi Malaysia  

---
