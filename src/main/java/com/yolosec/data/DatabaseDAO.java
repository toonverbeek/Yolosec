package com.yolosec.data;

import java.sql.*;
import com.yolosec.util.ConnectionString;

/**
 * Used for util methods
 *
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
     * Checks if a username exists, if so returns the password. Else returns
     * null value.
     *
     * @param username String username
     * @return If user is already logged on, true
     * @throws java.lang.Exception
     */
    public static boolean identifyUserStatus(String username) throws Exception {
        boolean isLoggedOn = false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(ConnectionString.getConnectionString());

            preparedStatement = connect.prepareStatement("SELECT loggedIn FROM account WHERE username = ?;");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                isLoggedOn = resultSet.getBoolean("loggedIn");
            }

            if (!isLoggedOn) {
                //log him in if he wasn't allready
                preparedStatement = connect.prepareStatement("UPDATE account SET loggedIn=1 WHERE username = ?;");
                preparedStatement.setString(1, username);
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
        return isLoggedOn;
    }

    /**
     * Checks if a username exists, if so returns the password. Else returns
     * null value.
     *
     * @param spaceshipId integer spaceshipId
     * @throws java.lang.Exception
     */
    public static void logoutUser(int spaceshipId) throws Exception {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(ConnectionString.getConnectionString());

            preparedStatement = connect.prepareStatement("UPDATE account SET loggedIn=0 WHERE spaceship_id = ?;");
            preparedStatement.setInt(1, spaceshipId);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
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
