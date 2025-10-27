import java.sql.*;
import java.util.Scanner;

public class LibraryManagement {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("\nLibrary Management System");
                System.out.println("1. Add Book");
                System.out.println("2. Issue Book");
                System.out.println("3. Return Book");
                System.out.println("4. View Books");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        addBook(conn, scanner);
                        break;
                    case 2:
                        issueBook(conn, scanner);
                        break;
                    case 3:
                        returnBook(conn, scanner);
                        break;
                    case 4:
                        viewBooks(conn);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        conn.close();
                        return;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addBook(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter author: ");
        String author = scanner.nextLine();
        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine();

        String sql = "INSERT INTO books (title, author, isbn) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setString(3, isbn);
        stmt.executeUpdate();
        System.out.println("Book added successfully!");
    }

    private static void issueBook(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter student name: ");
        String studentName = scanner.nextLine();

        String sql = "UPDATE books SET available = FALSE WHERE id = ? AND available = TRUE";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, bookId);
        int rows = stmt.executeUpdate();

        if (rows > 0) {
            String insertSql = "INSERT INTO issued_books (book_id, student_name, issue_date) VALUES (?, ?, CURDATE())";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, bookId);
            insertStmt.setString(2, studentName);
            insertStmt.executeUpdate();
            System.out.println("Book issued successfully!");
        } else {
            System.out.println("Book not available or not found!");
        }
    }

    private static void returnBook(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter book ID: ");
        int bookId = scanner.nextInt();

        String sql = "UPDATE books SET available = TRUE WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, bookId);
        stmt.executeUpdate();

        String updateSql = "UPDATE issued_books SET return_date = CURDATE() WHERE book_id = ? AND return_date IS NULL";
        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
        updateStmt.setInt(1, bookId);
        updateStmt.executeUpdate();

        System.out.println("Book returned successfully!");
    }

    private static void viewBooks(Connection conn) throws SQLException {
        String sql = "SELECT * FROM books";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        System.out.println("\nBooks:");
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") + ", Title: " + rs.getString("title") +
                               ", Author: " + rs.getString("author") + ", Available: " + rs.getBoolean("available"));
        }
    }
}
