
# PC Builder Guide CLI App
 PC Builder Guide is an
 application designed to
 help users select the best
 computer components for
 their needs and budget,
 while comparing prices. The
 goal is to make the process
 of building or upgrading a
 PC easier, smarter, and
 more cost-efficient, even
 for users with little
 technical knowledge


## Features

- User login and registration (with PostgreSQL)
- Budget-based PC component selection
- Wikipedia search for component info
-  Save & view favorite builds
- Multithreading for responsive tasks




## Prerequisites
- Java 11 or higher: Ensure you have Java installed on your system.
- Maven: Used for dependency management and building the project.
- Docker & Docker Compose: Required to set up the PostgreSQL database and pgAdmin.
## Setup
1. Clone the Repository
2. Start the PostgreSQL database and pgAdmin using Docker Compose:
    docker compose up


## Example Run:

jdbc:postgresql://localhost:5432/Pc-Builder postgres postgres
Database had been initialized
jdbc:postgresql://localhost:5432/Pc-Builder postgres postgres
Imported 37 components.
jdbc:postgresql://localhost:5432/Pc-Builder postgres postgres

===== User Login =====
1. Register
2. Login
0. Exit
======================

Choose an option: 2

Enter username: fares

Enter password: fares

Login successful. Welcome, fares!

===== PC Component CLI =====
1. Search by component name
2. Filter by component type
3. Get Wikipedia information for a component
4. Build your own PC
0. Exit
== ==========================

Enter your choice: 1

Enter component name to search (Press Enter for All components): asus

jdbc:postgresql://localhost:5432/Pc-Builder postgres postgres
Name                 Brand           Price      Color      Type      
 - - - - - - - - -- - - - - - - - - - - - - - -- - -
Asus PRIME H310M-E   LGA1151         172.07     Silver / Black Motherboard
Asus PRIME B550M-A (WI-FI) AM4             209.50     Black / White Motherboard
Asus PRIME B760M-A D4 LGA1700         269.99     Silver / Black Motherboard
Asus PRIME Z270-P    LGA1151         355.99     Black / Silver Motherboard
Asus TUF Gaming GT501 ATX Mid Tower   154.99     White / Black Case      

===== PC Component CLI =====
1. Search by component name
2. Filter by component type
3. Get Wikipedia information for a component
4. Build your own PC
0. Exit
============================
Enter your choice:

## Unit Testing
We used AI to make a test class that tests the code and check if it's working or not.
