@echo off
javac -cp lib/mysql-connector-j.jar src/LibraryManagement.java
java -cp src;lib/mysql-connector-j.jar LibraryManagement
pause
