package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private String name = "root";
    private String password = "astyana22";
    private String url = "jdbc:mysql://localhost:3306/shelfaware";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url,name,password);
    }

    public String showError(SQLException exception) {
        return exception.getMessage();
    }
}
