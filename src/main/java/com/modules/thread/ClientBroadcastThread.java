package com.modules.thread;

import com.server.ConnectionServer;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class ClientBroadcastThread implements Runnable {

    private boolean isRunning = false;
    private boolean isLogging = false;
    private ConnectionServer serverModule = null;

    //---debug---
    ThreadMXBean bean;
    long tCpu, sum;
    Integer i;
    ArrayList<Integer> tArr;
    //---debug---

    public ClientBroadcastThread() {
        this.isRunning = true;
    }

    /**
     * Set server module to retrieve information for broadcasting
     *
     * @param server
     */
    public void setServerModule(ConnectionServer server) {
        this.serverModule = server;
    }

    public Boolean logCpuTime() {
        if (isLogging) {
            isLogging = false;
        } else {
            tArr = new ArrayList<>(10);
            bean = ManagementFactory.getThreadMXBean();
            isLogging = true;
        }
        return isLogging;
    }

    /**
     * The broadcast module start broadcasting player location to the current
     * connected players
     */
    @Override
    public synchronized void run() {

        while (isRunning) {
            try {
                if (this.serverModule != null) {
                    if (isLogging) {
                        tCpu = bean.getCurrentThreadCpuTime(); //---debug---
                        serverModule.broadcastPositions();
                        log(bean.getCurrentThreadCpuTime() - tCpu); //---debug---
                    } else {
                        serverModule.broadcastPositions();
                    }
                }
                this.wait(16);

            } catch (InterruptedException ex) {
                System.out.println(String.format("---[BROADCAST] ClientBroadcastModule is interrupted : %s", ex.getMessage()));
            }
        }
    }

    private void log(long time) {
        tArr.add((int)time);
        if (tArr.size() > 9) {
            sum = 0;
            for (i = 0; i < tArr.size(); i++) {
                sum += tArr.get(i);
            }
            tArr.clear();
            System.out.println("BROADCAST TIME: " + sum);
        }
    }

}
