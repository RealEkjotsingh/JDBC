package com.student.pack;

import java.util.Scanner;
import java.sql.*;

public class JDBCExample {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter number of students to enter data: ");
            int n = scanner.nextInt();
            scanner.nextLine(); // Consume the remaining newline character

            String url = "jdbc:mysql://localhost:3306/Student"; // JDBC URL for MySQL database
            String username = "root"; // Username for the database
            String password = ""; // Password for the database (if any)

            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                System.out.println("Connection to database successful");

                // Truncate the table
                System.out.print("Do you want to truncate the table? (y/n): ");
                String truncateOption = scanner.nextLine();
                if (truncateOption.equalsIgnoreCase("y")) {
                    String truncate = "TRUNCATE TABLE Student";
                    try (PreparedStatement statement = conn.prepareStatement(truncate)) {
                        int rows = statement.executeUpdate();
                        System.out.println(rows + " row(s) truncated");
                    }
                }

                // Insert data into the table
                String insertSql = "INSERT INTO Student (name, roll, course, sub_course) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = conn.prepareStatement(insertSql)) {
                    for (int i = 0; i < n; i++) {
                        System.out.print("Enter student name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter roll number: ");
                        int roll = scanner.nextInt();
                        scanner.nextLine(); // Consume the remaining newline character
                        System.out.print("Enter course: ");
                        String course = scanner.nextLine();
                        System.out.print("Enter sub course: ");
                        String sub_course = scanner.nextLine();

                        statement.setString(1, name);
                        statement.setInt(2, roll);
                        statement.setString(3, course);
                        statement.setString(4, sub_course);

                        int rows = statement.executeUpdate();
                        System.out.println(rows + " row(s) inserted");
                    }
                }

                // Query data from the table and display it
                String selectSql = "SELECT * FROM Student";
                try (Statement statement = conn.createStatement();
                     ResultSet resultSet = statement.executeQuery(selectSql)) {

                    System.out.println("Data from the table:");
                    System.out.println("Name\t\tRoll\t\tCourse\t\tSub Course");

                    while (resultSet.next()) {
                        System.out.println(
                                resultSet.getString("name") + "\t\t" +
                                resultSet.getInt("roll") + "\t\t" +
                                resultSet.getString("course") + "\t\t" +
                                resultSet.getString("sub_course"));
                    }
                }

                System.out.println("Done!");
            } catch (SQLException e) {
                System.out.println("Connection to database failed: " + e.getMessage());
            }
        }
    }
}
