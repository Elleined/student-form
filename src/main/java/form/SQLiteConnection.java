package form;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public interface SQLiteConnection {
    String myOwnGuiDbURL = "jdbc:sqlite:C:\\Users\\Administrator\\IdeaProjects\\Form\\src\\main\\resources\\myOwnGUIwithDB.db";
    String loginDbUrl = "jdbc:sqlite:C:\\Users\\Administrator\\IdeaProjects\\Form\\src\\main\\resources\\login.db";

    static Connection myOwnGuiDbConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(myOwnGuiDbURL);
            System.out.println("Connection established! Connected to sampleDB.db");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Occurred! Connection failed");
        }
        return conn;
    }

    static Connection loginDbConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(loginDbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Occurred! Cannot connect to Database!");
        }
        return conn;
    }
}
