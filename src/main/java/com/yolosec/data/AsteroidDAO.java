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
