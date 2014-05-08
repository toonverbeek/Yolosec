package com.yolosec.data;

import com.yolosec.domain.Position;
import com.yolosec.domain.GameClient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import com.ptsesd.groepb.shared.AsteroidComm;
import com.ptsesd.groepb.shared.AsteroidType;
import com.ptsesd.groepb.shared.Serializer;
import com.yolosec.service.GameService;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author user
 */
public class AsteroidDAOImpl implements AsteroidDAO {

    private final int unitSizeX;
    private final int unitSizeY;

    private int[][] map;
    private List<Position> occupiedPositions;

    private List<AsteroidComm> asteroids;

    private final int amountOfAsteroids = 50;

    public AsteroidDAOImpl(int mapSizeX, int mapSizeY) {
        unitSizeX = mapSizeX / 10;
        unitSizeY = mapSizeY / 10;
        map = new int[unitSizeX][unitSizeY];
        occupiedPositions = new ArrayList<>();
        asteroids = new ArrayList<>();
        generateAsteroids();
    }

    @Override
    public List<AsteroidComm> findAll() {
        return this.asteroids;
    }

    @Override
    public void resetAsteroids() {
        generateAsteroids();
    }

    @Override
    public void updateAsteroid(AsteroidComm asteroid) {
        for (AsteroidComm ast : asteroids) {
            if (ast.getX() == asteroid.getX() && ast.getY() == asteroid.getY()) {
                ast.setResourceAmount(asteroid.getResourceAmount());
                if (GameService.debug) {
                    //System.out.println(String.format("---[ASTEROID] X1 [ %s ] X2 [ %s ] Y1 [ %s ] Y2 [ %s ] NEW RESOURCE AMOUNT [ %s ]", ast.getX(), asteroid.getX(), ast.getY(), asteroid.getY(), asteroid.getResourceAmount()));
                    //System.out.println(String.format("---[ASTEROID] Setted asteroid resource amount %s \n", asteroid.getResourceAmount()));
                }
                break;
            }
        }
    }

    /**
     * send all the current asteroids
     *
     * @param clients
     */
    public void sendAsteroidComms(List<GameClient> clients) {
        PrintWriter writer = null;
        for (GameClient client : clients) {
            try {
                writer = new PrintWriter(client.getSocket().getOutputStream(), true);

                for (AsteroidComm asteroidComm : asteroids) {
                    String aster = Serializer.serializeAsteroidAsGamePacket(asteroidComm.getHeader(), asteroidComm.getType(), asteroidComm.getResourceAmount(), (int) asteroidComm.getX(), (int) asteroidComm.getY());
                    //System.out.println("Aster" + aster);
                    writer.println(aster);
                }
            } catch (IOException ex) {
                System.err.println(String.format("Error in PlayerLocationModule.broadcastAsteroids() - %s", ex.getMessage()));
            }
        }
    }

    private void generateAsteroids() {
        List<AsteroidComm> _asteroids = new ArrayList<>();

        while (_asteroids.size() < amountOfAsteroids) {
            Random r = new Random();
            //Remove 20 units from total range and add 10 to it
            //This way the asteroids cannot spawn 10 unit (==100pixels) from the map borders
            int rUnitSizeX = unitSizeX - 20;
            int rMapUnitX = r.nextInt(rUnitSizeX);
            rMapUnitX += 10;

            int rUnitSizeY = unitSizeY - 20;
            int rMapUnitY = r.nextInt(rUnitSizeY);
            rMapUnitY += 10;

            //Determine asteroid type
            AsteroidType atype = AsteroidType.common; //default common, chances on making it rare or magic
            int chanceMagic = 10;
            int chanceRare = chanceMagic + 20;
            int atypeChance = r.nextInt(100);
            if (atypeChance < chanceMagic) {
                atype = AsteroidType.magic;
            } else if (atypeChance < chanceRare) {
                atype = AsteroidType.rare;
            }

            //Create random resource amount
            int rResourceAmount = 0;
            switch (atype) {
                case common:
                    rResourceAmount = 80 + r.nextInt(20);
                    break;
                case magic:
                    rResourceAmount = 40 + r.nextInt(25);
                    break;
                case rare:
                    rResourceAmount = 10 + r.nextInt(15);
                    break;
            }

            //Check if is colliding with current rendered asteroids
            if (!isColliding(rMapUnitX, rMapUnitY, rResourceAmount, _asteroids) || _asteroids.isEmpty()) {
                float rmapX = rMapUnitX * 10;
                float rmapY = rMapUnitY * 10;
                _asteroids.add(new AsteroidComm(AsteroidComm.class.getSimpleName(), atype, rResourceAmount, rmapX, rmapY));
            }
        }

        this.asteroids = _asteroids;
    }

    private boolean isColliding(int posUnitX, int posUnitY, int resourceAmount, Collection<AsteroidComm> checkAsteroids) {
        boolean isColliding = false;
        int newAstSize = calculateSizeFromResource(resourceAmount);
        Position newAsteroidPos = new Position(posUnitX, posUnitY, newAstSize);
        for (AsteroidComm asteroid : checkAsteroids) {
            //get the position parameters
            int astPosX = (int) asteroid.getX();
            int astPosY = (int) asteroid.getY();
            int size = calculateSizeFromResource(asteroid.getResourceAmount());
            //create the position from the exisitng asteroid
            Position p = new Position(astPosX, astPosY, size);

            //check if the positions are colliding
            if (positionsColliding(newAsteroidPos, p)) {
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

    private int calculateSizeFromResource(int resourceAmount) {
        return resourceAmount;
    }
}
