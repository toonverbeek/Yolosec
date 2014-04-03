/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.data;

import com.ptsesd.groepb.shared.SpaceshipComm;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public interface SpaceshipDAO {
    
    SpaceshipComm findDatabaseSpaceship(String username) throws ClassNotFoundException, SQLException;
    
    void updateDatabaseSpaceship(SpaceshipComm ship) throws ClassNotFoundException, SQLException;
    
    
}
