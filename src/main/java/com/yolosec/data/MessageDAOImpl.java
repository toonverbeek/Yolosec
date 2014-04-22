package com.yolosec.data;

import com.ptsesd.groepb.shared.MessagingComm;
import com.ptsesd.groepb.shared.Serializer;
import com.yolosec.domain.GameClient;
import com.yolosec.util.ConnectionString;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class MessageDAOImpl implements MessageDAO {

    private List<MessagingComm> messages;

    @Override
    public List<MessagingComm> findAll() {
        return this.messages;
    }

    @Override
    public void resetMessages() {
        messages.clear();
    }

    public void sendMessageComms(List<GameClient> clients) {
        PrintWriter writer = null;
        for (GameClient client : clients) {
            try {
                writer = new PrintWriter(client.getSocket().getOutputStream(), true);

                for (MessagingComm messageComm : messages) {
                    String mess = Serializer.serializeMessageAsGamePacket(messageComm);
                    writer.println(mess);
                }
            } catch (IOException ex) {
                System.err.println(String.format("Error in PlayerLocationModule.broadcastMessages() - %s", ex.getMessage()));
            }
        }
    }

    public boolean addMessage(MessagingComm message) throws Exception {
        Exception ErrM = null;
        Connection connect = null;
        PreparedStatement preparedStatementMessage = null;
        try {
            System.out.println(String.format("---[DATABASE] %s", ConnectionString.getConnectionString()));
            System.out.println("---[DATABASE] Setting up connection...");
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(ConnectionString.getConnectionString());
            System.out.println("---[DATABASE] Connection established");

            preparedStatementMessage = connect.prepareStatement("INSERT INTO messages (spaceship_id, message) VALUES (?, ?);");
            preparedStatementMessage.setInt(1, message.getSpaceShipId());
            preparedStatementMessage.setString(2, message.getMessage());
            preparedStatementMessage.executeUpdate();
            messages.add(message);
        } catch (ClassNotFoundException | SQLException e) {
            ErrM = e;
            System.out.println("-Exception occurred while removing user: " + e.getMessage());
        } finally {
            preparedStatementMessage.close();
            connect.close();
        }
        if (ErrM != null) {
            throw ErrM;
        }
        return ErrM == null;
    }

    public boolean getMessages() throws Exception {
        Exception ErrM = null;
        Connection connect = null;
        PreparedStatement preparedStatementMessage = null;
        try {
            System.out.println(String.format("---[DATABASE] %s", ConnectionString.getConnectionString()));
            System.out.println("---[DATABASE] Setting up connection...");
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(ConnectionString.getConnectionString());
            System.out.println("---[DATABASE] Connection established");

            preparedStatementMessage = connect.prepareStatement("SELECT messages.spaceship_id, messages.message, messages,timestamp, account.username FROM account RIGHT JOIN messages ON account.spaceship_id=messages.spaceship_id;");
            ResultSet rs = preparedStatementMessage.executeQuery();

            while (rs.next()) {
                Integer spaceshipid = rs.getInt("spaceship_id");
                String message = rs.getString("message");
                Date timestamp = rs.getDate("timestamp");
                String username = rs.getString("username");
                MessagingComm mCom = new MessagingComm(MessagingComm.class.getSimpleName(), spaceshipid, message, username, timestamp);
                messages.add(mCom);
            }
            System.out.println("---[DATABASE] GetMessages queried");
        } catch (ClassNotFoundException | SQLException e) {
            ErrM = e;
            System.out.println("-Exception occurred while removing user: " + e.getMessage());
        } finally {
            preparedStatementMessage.close();
            connect.close();
        }
        if (ErrM != null) {
            throw ErrM;
        }
        return ErrM == null;
    }
}
