package com.server;

import com.objects.User;
import com.ptsesd.groepb.shared.AsteroidType;
import java.sql.*;
import com.ptsesd.groepb.shared.SpaceshipComm;

/**
 *
 * @author Administrator
 */
public class DbConnector {

    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    private static final String db_location = "192.168.24.78";
    private static final String db_name = "inspaceserverdb";
    private static final String db_username = "inspaceserver";
    private static final String db_password = "toor";
    private static final String connectionString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s", new Object[]{db_location, db_name, db_username, db_password});

    public static void readDataBase() throws Exception {
        try {
            System.out.println(connectionString);
            System.out.println("---[DATABASE] Setting up connection...");
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(connectionString);
            System.out.println("---[DATABASE] Connection established");
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    public static boolean SetUser(User obj) {
        try {
            System.out.println("---[DATABASE] Setting up connection...");
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(connectionString);
            System.out.println("---[DATABASE] Connection established");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
        return false;
    }

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
            connect = DriverManager.getConnection(connectionString);

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
     *
     * @param username
     * @return
     * @throws Exception
     */
    public static SpaceshipComm getSpaceship(String username) throws Exception {
        SpaceshipComm result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(connectionString);

            preparedStatement = connect.prepareStatement("SELECT * FROM spaceship s WHERE s.id = (SELECT u.spaceship_id FROM account u WHERE u.username = ?)");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                float x = resultSet.getFloat("position_x");
                float y = resultSet.getFloat("position_y");
                int direction = resultSet.getInt("direction");
                int id = resultSet.getInt("id");
                int[] resources = new int[3];

                resources[0] = resultSet.getInt("resource_common");
                resources[1] = resultSet.getInt("resource_magic");
                resources[2] = resultSet.getInt("resource_rare");

                result = new SpaceshipComm("SpaceshipComm", id, x, y, direction, resources, false);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
        //returns null if doesnt exist, else return spaceship object;
        return result;
    }

    public static int getSpaceshipResourceAmount(int clientID) {
        System.out.println("---[DATABASE] get spaceship resource amount");
        return -1;
    }

    public static void setSpaceshipResourceAmount(int clientID, int resourceAmount, AsteroidType type) {
        try {
            //System.out.println(connectionString);
            //System.out.println("Setting up connection...");
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(connectionString);
            //System.out.println("Connection established");
            preparedStatement = null;
            if (type == AsteroidType.common) {
                preparedStatement = connect.prepareStatement("UPDATE spaceship SET resource_common = ? WHERE id = ?");
            } else if (type == AsteroidType.magic) {
                preparedStatement = connect.prepareStatement("UPDATE spaceship SET resource_magic = ? WHERE id = ?");
            } else if (type == AsteroidType.rare) {
                preparedStatement = connect.prepareStatement("UPDATE spaceship SET resource_rare = ? WHERE id = ?");
            }

            preparedStatement.setInt(1, resourceAmount);
            preparedStatement.setInt(2, clientID);

            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(String.format("---[DATABASE] Error in DbConnector.setSpaceshipResourceAmount() - %s", ex.getMessage()));
        }
    }

    static void updateSpaceship(int id, int posX, int posY, int direction, int resourceCommon, int resourceMagic, int resourceRare) {
        try {
            //System.out.println(connectionString);
            //System.out.println("Setting up connection...");
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(connectionString);
            //System.out.println("Connection established");

            preparedStatement = connect.prepareStatement("UPDATE spaceship SET position_x = ?, position_y = ?, direction = ?, resource_common = ?, resource_magic = ?, resource_rare = ? WHERE id = ?");

            preparedStatement.setInt(1, posX);
            preparedStatement.setInt(2, posY);
            preparedStatement.setInt(3, direction);
            preparedStatement.setInt(4, resourceCommon);
            preparedStatement.setInt(5, resourceMagic);
            preparedStatement.setInt(6, resourceRare);
            preparedStatement.setInt(7, id);

            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            System.err.println(String.format("---[DATABASE] Error in DbConnector.updateSpaceship() - %s", ex.getMessage()));
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
