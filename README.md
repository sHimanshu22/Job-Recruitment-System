# Job Recruitment System 

A Java-based desktop application that allows employers to post jobs and job seekers to apply. Built using Java Swing and MySQL, 
this system simplifies the recruitment process with an intuitive user interface and structured data management.

## Features

-  Employer Login & Job Posting  
-  Job Seeker Registration & Application Submission  
-  View Applications for Each Job  
-  Admin Panel for Managing Users and Jobs  
-  Authentication and Role Management  

---

## Technologies Used

- Java (Swing) – for the GUI  
- MySQL – for the database  
- JDBC – for connecting Java and MySQL  
- IntelliJ IDEA / NetBeans** – recommended IDEs  

---

1) Clone the Repository

```bash
git clone https://github.com/sHimanshu22/Job-Recruitment-System.git
cd Job-Recruitment-System

2) Set Up MySQL Database
-Open MySQL Workbench or any MySQL client.

Create a new database, e.g., job_recruitment_system.

Create the following tables (see schema below).

Update the database connection settings in your Java project code to match your MySQL username, password, and database name.

Database Schema
1. users
| Column        | Type     | Description                               |
| ------------- | -------- | ----------------------------------------- |
| `id`          | INT (PK) | Unique user ID                            |
| `name`        | VARCHAR  | User's full name                          |
| `email`       | VARCHAR  | User's email address                      |
| `password`    | VARCHAR  | Hashed password                           |
| `phoneNumber` | VARCHAR  | User's contact number                     |
| `role`        | VARCHAR  | Role of user (e.g., employer, job seeker) |


2. jobs
| Column        | Type        | Description               |
| ------------- | ----------- | ------------------------- |
| `id`          | INT (PK)    | Unique job ID             |
| `employerId`  | INT (FK)    | ID of the employer (user) |
| `title`       | VARCHAR     | Job title                 |
| `description` | TEXT        | Job description           |
| `salary`      | DECIMAL/INT | Salary offered            |


3. application
| Column        | Type     | Description                                      |
| ------------- | -------- | ------------------------------------------------ |
| `id`          | INT (PK) | Unique application ID                            |
| `jobId`       | INT (FK) | The job being applied to                         |
| `jobSeekerId` | INT (FK) | ID of the job seeker (user)                      |
| `status`      | VARCHAR  | Application status (pending, accepted, rejected) |
| `appliedDate` | DATE     | Date of application                              |
| `resumePath`  | VARCHAR  | Path to the uploaded resume file                 |


3) Configure Project in Your IDE
Open the project in IntelliJ IDEA or NetBeans.

Make sure your JDK is installed and selected.

Ensure your MySQL JDBC driver is added to the project dependencies/classpath.

Compile and run the project.


## Contributing
Feel free to fork the repository and submit pull requests. Contributions are welcome!
