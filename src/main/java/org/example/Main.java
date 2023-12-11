package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class Main
{
    public static void main(String[] args)
    {

        //test databaseConnection

        try (Connection connection = databaseConnection.getConnection())
        {
            if (connection != null)
            {
                System.out.println("Successfully connected to the database!");
            }
            else
            {
                System.out.println("Failed to connect to the database.");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Error occurred while connecting to the database.");
        }


    }







}
