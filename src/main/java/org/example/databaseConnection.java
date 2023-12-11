package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class databaseConnection
{
    //root@127.0.0.1:3306
    //jdbc:mysql://127.0.0.1:3306/?user=root

    //final ，无法被修改
    private static final String JDBC_URL = "jdbc:mysql://change to ip/NewsPortalDB";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "change to password";

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }
}
