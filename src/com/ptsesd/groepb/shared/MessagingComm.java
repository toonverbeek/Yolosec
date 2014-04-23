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

    public MessagingComm(String header, int spaceShipId, String message, String username) {
        super(header);
        this.spaceShipId = spaceShipId;
        this.message = message;
        this.username = username;
    }

    public int getSpaceShipId() {
        return spaceShipId;
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
        if (this.timestamp == null) {
            return new Date(1970, 1, 1);
        }

        return this.timestamp;
    }
    
    public void setTimestamp() {
        this.timestamp = new Date();
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
