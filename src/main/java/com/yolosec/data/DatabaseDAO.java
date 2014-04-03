package com.yolosec.data;

import com.yolosec.domain.User;
import com.ptsesd.groepb.shared.AsteroidType;
import java.sql.*;
import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.util.ConnectionString;

/**
 * Used for util methods 
 * @author Administrator
 */
public class DatabaseDAO {

    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    /**
     * Checks if a username exists, if so returns the password. Else returns
     * null value.
     *
     * @param username String username
     * @return If user != exists then null, else password
     * @throws java.lang.Exception
     */
    public static String identifyUser(String username) throws Exception {
        String result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(ConnectionString.getConnectionString());

            preparedStatement = connect.prepareStatement("SELECT password FROM account WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result = resultSet.getString("password");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
        //returns null if doesnt exist, else return password;
        return result;
    }

    /**
     * close method to end all open connections
     */
    private static void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
