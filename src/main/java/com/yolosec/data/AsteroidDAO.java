/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yolosec.data;

import com.ptsesd.groepb.shared.AsteroidComm;
import java.util.List;

/**
 *
 * @author user
 */
public interface AsteroidDAO {
    
    List<AsteroidComm> findAll();
    
    void resetAsteroids();
    
    void updateAsteroid(AsteroidComm ast);
}
