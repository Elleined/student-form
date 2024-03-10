package com.elleined.studentform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface SQLiteConnection {

    static Connection studentFormDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("STUDENT_FORM_DB"));
            System.out.println("Connection established! Connected to student form.db");
        } catch (SQLException e) {
            System.out.println("Error Occurred! Connection failed " + e.getMessage()) ;
        }
        return conn;
    }

    static void createRegisterTable() {
        final String query = """
                CREATE TABLE IF NOT EXISTS REGISTRATION_DETAILS(
                    REGISTRATION_NAME VARCHAR(255),
                    REGISTRATION_USERNAME VARCHAR(255),
                    REGISTRATION_PASSWORD VARCHAR(255),
                    REGISTRATION_NUMBER VARCHAR(255)
                );
                """;
        try (Connection conn = studentFormDB();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Creating Registration Table Failed " + e.getMessage());
        }
    }

    static void studentTable() {
        final String query = """
                CREATE TABLE IF NOT EXISTS STUDENT_DETAILS(
                    STUDENT_ID INT PRIMARY KEY,
                    STUDENT_NAME VARCHAR(255),
                    STUDENT_SEX VARCHAR(10),
                    STUDENT_AGE INT,
                    STUDENT_YEAR_SECTION INT VARCHAR(255)
                );
                """;
        try (Connection conn = studentFormDB();
             PreparedStatement stmt = conn.prepareStatement(query)){
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Creating Registration Table Failed " + e.getMessage());
        }
    }
}
