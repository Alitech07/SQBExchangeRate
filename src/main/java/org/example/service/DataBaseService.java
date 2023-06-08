package org.example.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseService {
    private static String url = "jdbc:postgresql://localhost:5432/telegrambot";
    private static String user = "postgres";
    private static String password = "12345";

    public static Connection getDbConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            System.out.println("Error! Not connetion databse.");
        }
        return conn;
    }


}
