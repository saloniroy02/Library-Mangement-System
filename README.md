# Library Management System

A console-based application to manage books in a library using Java and MySQL.

## Features
- Add books
- Issue books to students
- Return books
- View all books

## Setup
1. Install MySQL and JDK 17.
2. Run `db_setup.sql` to create the database and tables.
3. Place `mysql-connector-j.jar` in the `lib` folder.
4. Compile: `javac -cp lib/mysql-connector-j.jar src/LibraryManagement.java`
5. Run: `java -cp src;lib/mysql-connector-j.jar LibraryManagement`
