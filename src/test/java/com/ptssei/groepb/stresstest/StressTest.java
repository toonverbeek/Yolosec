package com.ptssei.groepb.stresstest;

import com.yolosec.util.ConnectionString;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author user
 */
public class StressTest extends TestCase {

    private ExecutorService executor;
    private final int amountOfClients = 50;
    private int amountLoggedIn = 0;
    private static final int USERIDBASE = 2000000000;
    private static final int WAITTIME = 5000;
    private static final String IP_ADDRESS = "127.0.0.1";
//    private static final String IP_ADDRESS = "192.168.24.78‏";
//    private static final String IP_ADDRESS = "145.93.57.150";
    private static HashMap<Integer, String> userList;
    private static boolean hasData = false;
    private static int testsCompleted = 0;
    private static final int numberOfTests = 1;

    private boolean running = true;

    public StressTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Instantiate variables
        if (!hasData) {
            executor = Executors.newFixedThreadPool(amountOfClients);

            //Add users to the database
            userList = new HashMap<>();
            Exception ErrM = null;
            Connection connect = null;
            PreparedStatement preparedStatementSpaceship = null;
            PreparedStatement preparedStatementUser = null;
            try {
                System.out.println(String.format("---[DATABASE] %s", ConnectionString.getConnectionString()));
                System.out.println("---[DATABASE] Setting up connection...");
                Class.forName("com.mysql.jdbc.Driver");
                connect = DriverManager.getConnection(ConnectionString.getConnectionString());
                System.out.println("---[DATABASE] Connection established");
                //clear database of old testdata
                preparedStatementUser = connect.prepareStatement("DELETE FROM account WHERE spaceship_id >= ?;");
                preparedStatementSpaceship = connect.prepareStatement("DELETE FROM spaceship WHERE id >= ?;");
                preparedStatementUser.setInt(1, USERIDBASE);
                preparedStatementSpaceship.setInt(1, USERIDBASE);
                preparedStatementUser.executeUpdate();
                preparedStatementSpaceship.executeUpdate();
                //end 
                String username;
                Integer spaceshipid;
                Random r = new Random();
                preparedStatementSpaceship = connect.prepareStatement("INSERT INTO spaceship (id, position_x, position_y) VALUES (?, ?, ?);");
                preparedStatementUser = connect.prepareStatement("INSERT INTO account (username, password, spaceship_id) VALUES (?, ?, ?);");
                for (int i = 0; i < amountOfClients; i++) {
                    username = "testuser" + i;
                    spaceshipid = USERIDBASE + i;

                    preparedStatementSpaceship.setInt(1, spaceshipid);
                    preparedStatementSpaceship.setInt(2, r.nextInt(65535));
                    preparedStatementSpaceship.setInt(3, r.nextInt(65535));
                    preparedStatementUser.setString(1, username);
                    preparedStatementUser.setString(2, username);
                    preparedStatementUser.setInt(3, spaceshipid);

                    preparedStatementSpaceship.executeUpdate();
                    preparedStatementUser.executeUpdate();
                    userList.put(spaceshipid, username);
                }
            } catch (ClassNotFoundException | SQLException e) {
                ErrM = e;
            } finally {
                preparedStatementUser.close();
                preparedStatementSpaceship.close();
                connect.close();
            }
            //check if an error was thrown while creating the users
            System.out.println("---" + amountOfClients + " Users created---");
            if (ErrM != null) {
                System.out.println("Errors occurred: " + ErrM.getMessage());
            }
            hasData = true;
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        testsCompleted++;

        if (testsCompleted == numberOfTests) {
            Exception ErrM = null;
            Connection connect = null;
            PreparedStatement preparedStatementSpaceship = null;
            PreparedStatement preparedStatementUser = null;
            try {
                System.out.println(String.format("---[DATABASE] %s", ConnectionString.getConnectionString()));
                System.out.println("---[DATABASE] Setting up connection...");
                Class.forName("com.mysql.jdbc.Driver");
                connect = DriverManager.getConnection(ConnectionString.getConnectionString());
                System.out.println("---[DATABASE] Connection established");
                Integer spaceshipid;
                preparedStatementUser = connect.prepareStatement("DELETE FROM account WHERE spaceship_id = ?;");
                preparedStatementSpaceship = connect.prepareStatement("DELETE FROM spaceship WHERE id = ?;");

                for (int i = 0; i < amountOfClients; i++) {
                    spaceshipid = USERIDBASE + i;

                    preparedStatementUser.setInt(1, spaceshipid);
                    preparedStatementSpaceship.setInt(1, spaceshipid);

                    preparedStatementUser.executeUpdate();
                    preparedStatementSpaceship.executeUpdate();
                    userList.remove(spaceshipid);
                }
            } catch (ClassNotFoundException | SQLException e) {
                ErrM = e;
            } finally {
                preparedStatementUser.close();
                preparedStatementSpaceship.close();
                connect.close();
            }
            //check if an error was thrown while removing the users
            assertNull(ErrM);
            assertTrue(userList.isEmpty());
            assertEquals(amountOfClients, amountLoggedIn);

            System.out.println("---Users removed---");
            if (ErrM != null) {
                System.out.println("Errors occurred: " + ErrM.getMessage());
            }
        }
    }

    /**
     * Test how many of the TestClient are initialised. Also start the clients
     * for the next test
     *
     * @return Returns thrown errors.
     */
    public Exception initializeTestClient() {
        Exception ErrM = null;
        for (int i = 0; i < amountOfClients; i++) {
            boolean initiate = false;
            try {
                TestClientRunnable r = new TestClientRunnable(userList.get(USERIDBASE + i), IP_ADDRESS);
                initiate = r.initiate();
                executor.execute(r);
            } catch (SocketException ex) {
                System.out.println(String.format("---[STRESSTEST] SocketException %s", ex.getMessage()));
                ErrM = ex;
            } catch (Exception ex) {
                System.out.println(String.format("---[STRESSTEST] Exception %s", ex.getMessage()));
                ErrM = ex;
            }
            if (initiate) {
                amountLoggedIn++;
            }
            if (ErrM != null) {
                ErrM = new Exception("Initiation failed.");
            }
        }
        return ErrM;
    }

    /**
     * Shutdown all the clients
     *
     * @return Returns thrown errors.
     */
    public Exception shutDownTestClient() {
        System.out.println("---[STRESSTEST] Shutting down");
        Exception ErrM = null;
        try {
            executor.shutdown();

            while (!executor.isTerminated()) {
                //wait
                //System.out.println("Waiting...");
            }
        } catch (Exception ex) {
            ErrM = ex;
        }
        System.out.println("---[STRESSTEST] Shutted Down");
        return ErrM;
    }

    /**
     *
     */
    @Test
    public synchronized void testClient() {
        System.out.println("---[STRESSTEST] Start testClient");
        Exception ErrM;

        ErrM = initializeTestClient();
        assertNull(ErrM);

        try {
            wait(WAITTIME);
        } catch (Exception e) {
            ErrM = e;
        }
        assertNull(ErrM);

        ErrM = shutDownTestClient();
        assertNull(ErrM);
        System.out.println("---[STRESSTEST] Stop testClient");
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}
}
