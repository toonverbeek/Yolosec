package com.ptsesd.groepb.shared;

import java.util.Date;

/**
 *
 * @author Tim
 */
public class MessagingComm extends GamePacket {
    
    private int spaceShipId;
    private String message;
    private String username;
    private Date timestamp;

    public MessagingComm(String header, int spaceShipId, String message, String username, Date timestamp) {
        super(header);
        this.spaceShipId = spaceShipId;
        this.message = message;
        this.username = username;
        this.timestamp = timestamp;
    }

    public int getSpaceShipId() {
        return spaceShipId;
    }

    public void setSpaceShipId(int spaceShipId) {
        this.spaceShipId = spaceShipId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    public Date getTimestamp() {
        return this.timestamp;
    }
}
