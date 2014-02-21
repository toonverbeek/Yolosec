package SpaceInterfaces;

/**
 *
 * @author Peter
 */
public interface Item {
    String name = null;
    String description = null;
    Double price = null;
    Long id = null;
    
    public boolean SetName(String name);
    public boolean SetDescription(String name);
    public boolean SetPrice(Double price);
    public boolean SetId(Long id);
}
