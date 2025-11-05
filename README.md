
# Capture The Flag (CTF) Web Platform for Cybersecurity Education

A beginner-friendly web-based CTF platform designed to improve cybersecurity practical skills through ethical hacking challenges.

---

## Table of Contents
- [About the Project](#about-the-project)
- [Features](#features)
- [System Architecture](#system-architecture)
- [Technology Stack](#technology-stack)
- [Getting Started](#getting-started)
- [Usage](#usage)
- [Testing](#testing)
- [Roadmap / Future Enhancements](#roadmap--future-enhancements)
- [License](#license)
- [Author](#author)

---

## About the Project

Cybercrime cases continue to rise globally, yet beginners often lack a safe and accessible environment to learn cybersecurity practically.  
This project addresses that challenge by providing a structured learning platform where users complete ethical hacking challenges to build foundational skills.  
The focus is to provide:

- Beginner-friendly cybersecurity challenges  
- Practical exposure to security concepts  
- A safe platform for learning ethical hacking techniques  

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

| Layer | Technology |
|-------|------------|
| Frontend | React.js, Axios |
| Backend | Spring Boot, Spring Security |
| Database | MySQL |
| Development Tools | IntelliJ IDEA, VS Code, GitHub |
| Design Tools | Figma, PlantUML |

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
git clone https://github.com/Abdu1R4hmn/Capture-The-Flag-Website.git
cd Capture-The-Flag-Website
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
npm run dev
```

The app should run at:  
Frontend → http://localhost:5173  
Backend API → http://localhost:8080

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

---

## Author
Developed by **Abdulrahman Osama**

---
