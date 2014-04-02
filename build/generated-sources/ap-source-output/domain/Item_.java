package domain;

import domain.Resource;
import domain.Stat;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-03-28T15:12:40")
@StaticMetamodel(Item.class)
public class Item_ { 

    public static volatile SingularAttribute<Item, Long> id;
    public static volatile CollectionAttribute<Item, Resource> resources;
    public static volatile SingularAttribute<Item, String> description;
    public static volatile CollectionAttribute<Item, Stat> stats;
    public static volatile SingularAttribute<Item, String> name;
    public static volatile SingularAttribute<Item, String> image;

}