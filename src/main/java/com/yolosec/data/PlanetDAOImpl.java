package com.yolosec.data;

import com.ptsesd.groepb.shared.PlanetComm;
import com.ptsesd.groepb.shared.Serializer;
import com.yolosec.domain.GameClient;
import com.yolosec.domain.Position;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Administrator
 */
public class PlanetDAOImpl implements PlanetDAO {

    private final int unitSizeX;
    private final int unitSizeY;
    private int[][] map;
    private List<Position> occupiedPositions;

    private List<PlanetComm> planets;
    private static final int amountOfPlanets = 3;

    public PlanetDAOImpl(int mapSizeX, int mapSizeY) {
        unitSizeX = mapSizeX / 10;
        unitSizeY = mapSizeY / 10;
        map = new int[unitSizeX][unitSizeY];
        occupiedPositions = new ArrayList<>();
        planets = new ArrayList<>();
        generatePlanets();
    }

    @Override
    public List<PlanetComm> findAll() {
        return this.planets;
    }

    /**
     * send all the current asteroids
     *
     * @param clients
     */
    public void sendPlanetComms(List<GameClient> clients) {
        PrintWriter writer = null;
        for (GameClient client : clients) {
            try {
                writer = new PrintWriter(client.getSocket().getOutputStream(), true);

                for (PlanetComm planetComm : planets) {
                    String plan = Serializer.serializePlanetAsGamePacket(planetComm.getHeader(), planetComm.getPlanetName(), planetComm.getSize(), (int) planetComm.getX(), (int) planetComm.getY());
                    writer.println(plan);
                }
            } catch (IOException ex) {
                System.err.println(String.format("Error in PlayerLocationModule.broadcastPlanets() - %s", ex.getMessage()));
            }
        }
    }

    private void generatePlanets() {
        List<PlanetComm> _planets = new ArrayList<>();

        while (_planets.size() < amountOfPlanets) {
            Random r = new Random();
            //Remove 100 units from total range and add 50 to it
            //This way the planets cannot spawn 50 unit (==500pixels) from the map borders
            int rUnitSizeX = unitSizeX - 100;
            int rMapUnitX = r.nextInt(rUnitSizeX);
            rMapUnitX += 50;

            int rUnitSizeY = unitSizeY - 100;
            int rMapUnitY = r.nextInt(rUnitSizeY);
            rMapUnitY += 50;

            int rSize = r.nextInt(10) + 30;

            //Check if is colliding with current rendered planets
            if (!isColliding(rMapUnitX, rMapUnitY, rSize, _planets) || _planets.isEmpty()) {
                float rmapX = rMapUnitX * 10;
                float rmapY = rMapUnitY * 10;
                _planets.add(new PlanetComm(PlanetComm.class.getSimpleName(), "PlanetX", rSize, rmapX, rmapY));
            }
        }
        this.planets = _planets;
    }

    private boolean isColliding(int posUnitX, int posUnitY, int size, Collection<PlanetComm> checkPlanets) {
        boolean isColliding = false;
        Position newPlanetPos = new Position(posUnitX, posUnitY, size);
        for (PlanetComm planet : checkPlanets) {
            //get the position parameters
            int plaPosX = (int) planet.getX();
            int plaPosY = (int) planet.getY();
            //create the position from the exisitng asteroid
            Position p = new Position(plaPosX, plaPosY, size);

            //check if the positions are colliding
            if (positionsColliding(newPlanetPos, p)) {
                //if they are colliding return
                isColliding = true;
                return isColliding;
            }
        }

        return isColliding;
    }

    private boolean positionsColliding(Position a, Position b) {
        boolean posColliding = false;

        int aposX2 = a.getPosX1() + a.getSize();
        int aposY2 = a.getPosY1() + a.getSize();

        int bposX2 = b.getPosX1() + a.getSize();
        int bposY2 = b.getPosY1() + a.getSize();

        //if point bX1 is between point aX1 and point aX2
        if (b.getPosX1() >= a.getPosX1() && b.getPosX1() <= aposX2) {
            //if point bY1 is between point aY1 and point aY2
            if (b.getPosY1() >= a.getPosY1() && b.getPosY1() <= aposY2) {
                posColliding = true;
            }

            //if point bY2 is between point aY1 and point aY2
            if (bposY2 >= a.getPosY1() && bposY2 <= aposY2) {
                posColliding = true;
            }

            //if point bY1 is left of aY1 and bY2 is right of aY2
            if (b.getPosY1() <= a.getPosY1() && bposY2 >= aposY2) {
                posColliding = true;
            }
        }

        //if point bX2 is between point aX1 and point aX2
        if (bposX2 >= a.getPosX1() && bposX2 <= aposX2) {
            //if point bY1 is between point aY1 and point aY2
            if (b.getPosY1() >= a.getPosY1() && b.getPosY1() <= aposY2) {
                posColliding = true;
            }

            //if point bY2 is between point aY1 and point aY2
            if (bposY2 >= a.getPosY1() && bposY2 <= aposY2) {
                posColliding = true;
            }

            //if point bY1 is left of aY1 and bY2 is right of aY2
            if (b.getPosY1() <= a.getPosY1() && bposY2 >= aposY2) {
                posColliding = true;
            }
        }

        //if point bX1 is left of point aX1 and point bX2 is right of point aX2
        if (b.getPosX1() <= a.getPosX1() && bposX2 >= aposX2) {
            //if point bY1 is between point aY1 and point aY2
            if (b.getPosY1() >= a.getPosY1() && b.getPosY1() <= aposY2) {
                posColliding = true;
            }

            //if point bY2 is between point aY1 and point aY2
            if (bposY2 >= a.getPosY1() && bposY2 <= aposY2) {
                posColliding = true;
            }

            //if point bY1 is left of aY1 and bY2 is right of aY2
            if (b.getPosY1() <= a.getPosY1() && bposY2 >= aposY2) {
                posColliding = true;
            }
        }
        return posColliding;
    }

}
