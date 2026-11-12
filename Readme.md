# IT Helpdesk Ticket Management System

A Java-based console application that helps IT support teams manage employee issues, create support tickets, assign engineers, track ticket status, and generate reports. The application uses MySQL to store ticket information permanently.

## 1. Project Overview

The IT Helpdesk Ticket Management System is designed to simplify IT support operations.

Employees can raise tickets for technical issues, and the system automatically assigns priorities and support engineers. The application stores ticket information in a MySQL database.

## 2. Technologies Used

- **Programming Language:** Java
- **Database:** MySQL
- **Database Connectivity:** JDBC
- **Database Driver:** MySQL Connector/J
- **IDE:** Visual Studio Code
- **Terminal:** PowerShell

## 3. Features

1. **Create Ticket** – Create a new support ticket with employee name and issue description.
2. **Automatic Priority Detection** – Assign Critical, High, Medium, or Low priority based on the issue description.
3. **Automatic Engineer Assignment** – Assign tickets to engineers based on their active workload.
4. **View All Tickets** – Display all tickets stored in the database.
5. **Search Ticket** – Search for a ticket using its unique ID.
6. **Update Ticket Status** – Change ticket status to Open, In Progress, or Resolved.
7. **View Open Tickets** – Display tickets that are currently open.
8. **View Resolved Tickets** – Display completed tickets.
9. **Generate Reports** – Display ticket statistics, priority summaries, and engineer workloads.
10. **Resolution Time Tracking** – Calculate the time taken to resolve a ticket.

## 4. Project Folder Structure

```text
ITHelpdeskTicketSystem/
│
├── .vscode/
│   └── settings.json
│
├── lib/
│   └── mysql-connector-j-26.10.0.jar
│
├── src/
│   ├── HelpdeskApp.java
│   ├── Ticket.java
│   ├── Engineer.java
│   ├── DBConnection.java
│   ├── DBTest.java
│   └── TicketDAO.java
│
├── out/
│
└── README.md
```

**Note:** The MySQL Connector/J JAR filename shown above is an example based on the current project setup. Keep the actual JAR filename you downloaded. The `out` folder contains compiled Java class files and can be generated during compilation.

## 5. Prerequisites

Install the following before running the project:

- JDK 17 or later
- Visual Studio Code
- Java Extension Pack in VS Code
- MySQL Server
- MySQL Workbench
- MySQL Connector/J

Verify Java installation:

```powershell
java -version
javac -version
```

Both commands should display the installed Java version.

## 6. Clone or Download the Project

If you have uploaded the project to GitHub:

```powershell
git clone _GITHUB_REPOSITORY_URLYOUR
```

Alternatively, download the project ZIP file and extract it.

Open the project folder in VS Code:

```powershell
cd ITHelpdeskTicketSystem
code .
```

Make sure the terminal is opened in the main project folder, not inside `src` or `lib`.

## 7. MySQL Database Setup

### Step 1: Start MySQL Server

Open MySQL Workbench and connect to your local MySQL Server instance.

### Step 2: Create the Database

Open a new SQL query tab and execute:

```sql
CREATE DATABASE IF NOT EXISTS helpdesk_db;

USE helpdesk_db;
```

### Step 3: Create the Tickets Table

Execute the following SQL query:

```sql
CREATE TABLE IF NOT EXISTS tickets (
    ticket_id INT PRIMARY KEY,
    employee_name VARCHAR(100) NOT NULL,
    issue TEXT NOT NULL,
    priority VARCHAR(20) NOT NULL,
    status VARCHAR(30) NOT NULL,
    engineer VARCHAR(100),
    created_at DATETIME NOT NULL,
    resolved_at DATETIME NULL
);
```

### Step 4: Verify the Database

```sql
USE helpdesk_db;

SHOW TABLES;

SELECT * FROM tickets;
```

You should see the `tickets` table.

## 8. Configure MySQL Connection

Open:

```text
src/DBConnection.java
```

The application connects to MySQL using:

- Database: `helpdesk_db`
- Host: `localhost`
- Port: `3306`
- Username: `root`
- Password: Read from an environment variable.

The password is not hardcoded in the Java source code.

### Set the Database Password

Open the VS Code PowerShell terminal and execute:

```powershell
$env:HELPDESK_DB_PASSWORD = "YOUR_MYSQL_PASSWORD"
```

Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password.

**Important:** Set this environment variable every time you open a new PowerShell terminal. Never upload your actual database password to GitHub.

## 9. Configure MySQL Connector/J

Download MySQL Connector/J and place the actual `.jar` file inside the project's `lib` folder.

Example:

```text
lib/
└── mysql-connector-j-26.10.0.jar
```

Make sure it is a JAR file, not a folder containing extracted source code.

In `.vscode/settings.json`, add:

```json
{
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ],
    "java.configuration.updateBuildConfiguration": "automatic"
}
```

If your Connector/J release requires additional dependencies, such as Protocol Buffers, add the compatible dependency JAR to `lib` as well.

## 10. Compile the Project

Open the VS Code terminal.

Navigate to the project directory:

```powershell
cd "C:\Users\Geethika\OneDrive\Desktop\ITHelpdeskTicketSystem"
```

Create the output folder if it does not exist:

```powershell
mkdir out
```

Compile all Java files:

```powershell
javac -cp "lib\*" -d out src\*.java
```

If compilation succeeds, the compiled `.class` files will be stored in the `out` directory.