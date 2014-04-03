/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.service;

import com.ptsesd.groepb.shared.SpaceshipComm;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author user
 */
public interface SpaceshipService {    
    
    void addOnlineSpaceship(SpaceshipComm ship);
    
    void removeOnlineSpaceship(int spaceshipId);
    
    SpaceshipComm getOnlineSpaceship(int spaceshipId);
    
    SpaceshipComm getDatabaseSpaceship(String username) throws ClassNotFoundException, SQLException;
    
    List<SpaceshipComm> getAllOnlineSpaceships(int requestorId);
    
    List<SpaceshipComm> getAllOnlineSpaceships();
    
    void updateSpaceship(SpaceshipComm spaceship);
    
    void updateSpaceshipDatabase(SpaceshipComm spaceship) throws ClassNotFoundException, SQLException;
    
    
    
}
