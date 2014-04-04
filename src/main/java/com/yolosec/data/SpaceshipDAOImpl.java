/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.data;

import com.ptsesd.groepb.shared.SpaceshipComm;
import com.yolosec.util.ConnectionString;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author user
 */
public class SpaceshipDAOImpl implements SpaceshipDAO{
    //Query parameters
    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;
    
    public SpaceshipDAOImpl() throws ClassNotFoundException, SQLException{
        try {
            System.out.println(String.format("---[DATABASE] %s", ConnectionString.getConnectionString()));
            System.out.println("---[DATABASE] Setting up connection...");
            
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(ConnectionString.getConnectionString());
            System.out.println("---[DATABASE] Connection established");
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            close();
        }
    }

    @Override
    public SpaceshipComm findDatabaseSpaceship(String username) throws ClassNotFoundException, SQLException {
        SpaceshipComm result = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(ConnectionString.getConnectionString());

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
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            close();
        }
        //returns null if doesnt exist, else return spaceship object;
        return result;
    }

    @Override
    public void updateDatabaseSpaceship(SpaceshipComm ship) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(ConnectionString.getConnectionString());

            preparedStatement = connect.prepareStatement("UPDATE spaceship SET position_x = ?, position_y = ?, direction = ?, resource_common = ?, resource_magic = ?, resource_rare = ? WHERE id = ?");
            int x = (int) ship.getX();
            int y = (int) ship.getY();
            int[] resources = ship.getResources();
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setInt(3, ship.getDirection());
            preparedStatement.setInt(4, resources[0]);
            preparedStatement.setInt(5, resources[1]);
            preparedStatement.setInt(6, resources[2]);
            preparedStatement.setInt(7, ship.getId());

            preparedStatement.executeUpdate();
        } catch (ClassNotFoundException | SQLException ex) {
            throw ex;
        }
    }
    
    /**
     * close method to end all open connections
     */
    private void close() {
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
