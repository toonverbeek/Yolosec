/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.util;

/**
 *
 * @author user
 */
public class ConnectionString {
    private static final String db_location = "192.168.24.78";
    private static final String db_name = "inspaceserverdb";
    private static final String db_username = "inspaceserver";
    private static final String db_password = "toor";
    private static final String connectionString = String.format("jdbc:mysql://%s/%s?user=%s&password=%s", new Object[]{db_location, db_name, db_username, db_password});
    
    public static String getConnectionString(){
        return connectionString;
    }
}
