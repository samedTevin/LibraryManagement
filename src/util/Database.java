package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private String name = "root";
    private String password = "samed6583";
    private String url = "jdbc:mysql://localhost:3306/library_management";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,name,password);
    }

    public String showError(SQLException exception) {
        return exception.getMessage();
    }
}
