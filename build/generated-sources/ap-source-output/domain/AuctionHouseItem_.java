package domain;

import domain.Item;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-04-23T13:40:25")
@StaticMetamodel(AuctionHouseItem.class)
public class AuctionHouseItem_ { 

    public static volatile SingularAttribute<AuctionHouseItem, Long> id;
    public static volatile SingularAttribute<AuctionHouseItem, Integer> sellerId;
    public static volatile SingularAttribute<AuctionHouseItem, Item> item;
    public static volatile SingularAttribute<AuctionHouseItem, String> asteroidType;
    public static volatile SingularAttribute<AuctionHouseItem, Integer> resourceAmount;

}