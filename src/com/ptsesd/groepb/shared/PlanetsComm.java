package com.ptsesd.groepb.shared;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tim
 */
public class PlanetsComm {
    private List<PlanetComm> planets = new ArrayList<>();
    public  List<PlanetComm> getPlanets() {
        planets.add(new PlanetComm("Owheon", 900, 1150, 1200));
        planets.add(new PlanetComm("Xovis", 1400, 13000, 12000));
        return planets;
    }
}
