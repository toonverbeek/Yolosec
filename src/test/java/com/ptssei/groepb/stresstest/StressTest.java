package com.ptssei.groepb.stresstest;

import com.yolosec.util.ConnectionString;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author user
 */
public class StressTest extends TestCase {

    private ExecutorService executor;
    private static final int amountOfClients = 100;
    private List<TestClientRunnable> clientList = new ArrayList<>(amountOfClients);
    private int amountInitiated = 0;

    private static final int USERIDBASE = 2000000000;
    private static final int WAITTIME = 30000;
    private static final int LOGINTIMEOUT = 300;
//    private static final String IP_ADDRESS = "127.0.0.1";
    private static final String IP_ADDRESS = "192.168.24.11";
    private static HashMap<Integer, String> userList;
    private static boolean hasData = false;
    private static int testsCompleted = 0;
    private static final int numberOfTests = 2;

    public StressTest(String testName) {
        super(testName);
    }

    // <editor-fold defaultstate="collapsed" desc="SETUP AND TEARDOWN METHODS">
    //setUp() and tearDown() are made to run only once!
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Instantiate variables
        if (!hasData) {
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
            assertTrue(userList.isEmpty());
            System.out.println("---Users removed---");
            assertNull(ErrM);

            if (ErrM != null) {
                System.out.println("Errors occurred: " + ErrM.getMessage());
            }
        }
    }

    /**
     * Initiates the clients and puts them in List<Callable> clientList
     *
     * @return Returns thrown errors.
     */
    public Exception initializeTestClient() {
        System.out.println("---[STRESSTEST] Initiating clients started.");
        executor = Executors.newFixedThreadPool(amountOfClients);

        Exception ErrM = null;
        for (int i = 0; i < amountOfClients; i++) {
            try {
                TestClientRunnable r = new TestClientRunnable(userList.get(USERIDBASE + i), USERIDBASE + i, IP_ADDRESS);
                clientList.add(r);
                amountInitiated++;
            } catch (Exception ex) {
                System.err.println(String.format("---[STRESSTEST] Initiation Exception %s", ex.getMessage()));
                ErrM = ex;
            }
        }

        System.out.println("---[STRESSTEST] Initiating clients completed.");
        return ErrM;
    }

    /**
     * Shutdown all the clients
     *
     * @return Returns thrown errors.
     */
    public Exception shutDownTestClient() {
        System.out.println("---[STRESSTEST] Shutting down clients.");
        Exception ErrM = null;
        try {
            executor.shutdownNow();
            while (!executor.isTerminated()) {
                //wait
            }
            clientList.clear();
        } catch (Exception ex) {
            ErrM = ex;
        }
        System.out.println("---[STRESSTEST] Shuting down clients complete.");
        return ErrM;
    }
//</editor-fold>

    @Test
    public void testLoginClient() {
        System.out.println("---[STRESSTEST] Start testLoginClient");
        System.out.println(String.format("---[STRESSTEST] Number of users: %s", amountOfClients));
        System.out.println(String.format("---[STRESSTEST] User timeout: %s", LOGINTIMEOUT));
        Exception ErrM;
        int amountLoggedIn = 0;

        ErrM = initializeTestClient();
        assertNull(ErrM);
        assertEquals(amountOfClients, amountInitiated);

        for (TestClientRunnable r : clientList) {
            try {
                r.setTimeout(LOGINTIMEOUT);
                FutureTask<Boolean> future = new FutureTask<>(r);
                executor.execute(future);
                amountLoggedIn += future.get() ? 1 : 0;
                if (!future.isDone()) {
                    r.closeSocket();
                    future.cancel(true);
                }
            } catch (InterruptedException | ExecutionException ex) {
                System.err.println(String.format("---[STRESSTEST] Exception %s", ex.getMessage()));
                ErrM = ex;
            }
        }
        System.out.println(String.format("---[STRESSTEST] Clients logged in:  %s/%s", amountLoggedIn, amountOfClients));
        assertNull(ErrM);
        assertEquals(amountOfClients, amountLoggedIn);

        ErrM = shutDownTestClient();
        System.out.println("---[STRESSTEST] Stop testLoginClient");
        assertNull(ErrM);
    }

    @Test
    public void testBroadcastClient() {
        System.out.println("---[STRESSTEST] Start testBroadcastClient");
        System.out.println(String.format("---[STRESSTEST] Number of users: %s", amountOfClients));
        System.out.println(String.format("---[STRESSTEST] Broadcast timeout: %s", WAITTIME));
        Exception ErrM;

        ErrM = initializeTestClient();
        assertNull(ErrM);
        assertEquals(amountOfClients, amountInitiated);
        
        //have amountOfClients of TestClientRunnable in clientList
        //add that information to the list of clients so the server can broadcast them
        //SaceshipServiceImpl - Map<Integer, SpaceshipComm> clientSpaceships
        

//        for (TestClientRunnable r : clientList) {
//            try {
//                r.setTimeout(LOGINTIMEOUT);
//                FutureTask<Boolean> future = new FutureTask<>(r);
//                executor.execute(future);
//                amountLoggedIn += future.get() ? 1 : 0;
//                if (!future.isDone()) {
//                    r.closeSocket();
//                    future.cancel(true);
//                }
//            } catch (InterruptedException | ExecutionException ex) {
//                System.err.println(String.format("---[STRESSTEST] Exception %s", ex.getMessage()));
//                ErrM = ex;
//            }
//        }
//        System.out.println(String.format("---[STRESSTEST] Clients logged in:  %s/%s", amountLoggedIn, amountOfClients));
//        assertNull(ErrM);
//        assertEquals(amountOfClients, amountLoggedIn);
        ErrM = shutDownTestClient();
        System.out.println("---[STRESSTEST] Stop testBroadcastClient");
        assertNull(ErrM);
    }
}
