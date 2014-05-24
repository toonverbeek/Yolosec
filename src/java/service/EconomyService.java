package service;

import com.ptsesd.groepb.shared.ItemComm;
import com.ptsesd.groepb.shared.jms.ResourceMessage;
import com.ptsesd.groepb.shared.socket.InventoryReply;
import com.ptsesd.groepb.shared.socket.InventoryRequest;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import jms.GameserverGateway;
import jms.InventoryGateway;
import jms.ResourceGateway;

public class EconomyService implements Serializable {

    private AuctionHouse ah;
    private GameserverGateway gameserverGateway;
    private InventoryGateway inventoryGateway;
    private ResourceGateway resourceGateway;

    public EconomyService(EntityManager em) {
        ah = new AuctionHouse(em);
    }

    public void startEconomyServervice() {
        inventoryGateway = new InventoryGateway() {

            @Override
            public void processRequest(InventoryRequest request) {
                InventoryReply reply = null;
                if (request.isAuctionHouse()) {
                    List<ItemComm> auctionHouse = ah.getAuctionHouse(request.getSpaceshipId());
                    reply = new InventoryReply(InventoryReply.class.getSimpleName(), request.getSpaceshipId(), auctionHouse, request.isAuctionHouse());
                } else {
                    List<ItemComm> inventory = ah.getInventory(request.getSpaceshipId());
                    reply = new InventoryReply(InventoryReply.class.getSimpleName(), request.getSpaceshipId(), inventory, request.isAuctionHouse());
                }
                System.out.println("---[EconomyService] inventoryItems: " + reply.getItems().size() + " is auctionhouse "+ reply.isAuctionHouse());
                inventoryGateway.sendReply(reply);

            }
        };
        
        resourceGateway = new ResourceGateway() {

            @Override
            public void processRequest(ResourceMessage resRep) {
                int resource_magic = resRep.getResource_magic();
                int resource_normal = resRep.getResource_normal();
                int resource_rare = resRep.getResource_rare();
                long userId = resRep.getUserId();
                
                if(resource_magic == -1 && resource_normal == -1 && resource_rare == -1){
                    int[] resources = ah.getResources(userId);
                    
                    ResourceMessage message = new ResourceMessage(userId, resources[0], resources[1], resources[2]);
                    resourceGateway.sendResourceReply(message);
                } else {
                    ah.saveResources(userId, resource_normal, resource_magic, resource_rare);
                }  
            }
        };
        

        gameserverGateway = new GameserverGateway() {

            @Override
            public void processRequest(ItemComm incomingItem) {
                switch (incomingItem.getRequestType().toString()) {
                    case "BUY":
                        System.out.println("BUY");
                        if (ah.buyItem(incomingItem)) {
                            InventoryReply invReply = new InventoryReply(InventoryReply.class.getSimpleName(), incomingItem.getRequestorId(), ah.getInventory(incomingItem.getRequestorId()), false);
                            //return the auction house items
                            InventoryReply ahReply = new InventoryReply("InventoryReply", incomingItem.getRequestorId(), ah.getAuctionHouse(incomingItem.getRequestorId()), true);
                            gameserverGateway.sendRefresh(invReply, ahReply);
                        }
                        break;
                    case "SELL":
                        System.out.println("SELL");
                        if (ah.sellItem(incomingItem)) {
                            InventoryReply invReply = new InventoryReply(InventoryReply.class.getSimpleName(), incomingItem.getRequestorId(), ah.getInventory(incomingItem.getRequestorId()), false);
                            //return the auction house items
                            InventoryReply ahReply = new InventoryReply("InventoryReply", incomingItem.getRequestorId(), ah.getAuctionHouse(incomingItem.getRequestorId()), true);
                            gameserverGateway.sendRefresh(invReply, ahReply);
                        }
                        break;
                    case "CANCEL":
                        System.out.println("CANCEL");
                        if (ah.cancelItem(incomingItem)) {
                            InventoryReply invReply = new InventoryReply(InventoryReply.class.getSimpleName(), incomingItem.getRequestorId(), ah.getInventory(incomingItem.getRequestorId()), false);
                            //return the auction house items
                            InventoryReply ahReply = new InventoryReply("InventoryReply", incomingItem.getRequestorId(), ah.getAuctionHouse(incomingItem.getRequestorId()), true);
                            gameserverGateway.sendRefresh(invReply, ahReply);
                        }
                        break;
                    default:
                        System.out.println("derp");
                        break;
                }
            }
        };
    }
}
