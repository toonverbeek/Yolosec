/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ptsesd.groepb.shared;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tim
 */
public class PlanetsComm {
    private List<PlanetComm> planets = new ArrayList<PlanetComm>();
    public List<PlanetComm> getPlanets() {
        PlanetComm owheon = new PlanetComm("Owheon", 900, 50, 50);
        PlanetComm xovis = new PlanetComm("Xovis", 1400, 300, 300);
        planets.add(xovis);
        planets.add(owheon);
        return planets;
    }
}
