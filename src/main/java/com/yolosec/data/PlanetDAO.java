package com.yolosec.data;

import java.util.List;
import com.ptsesd.groepb.shared.PlanetComm;

/**
 *
 * @author Administrator
 */
public interface PlanetDAO {
        List<PlanetComm> findAll();
}
