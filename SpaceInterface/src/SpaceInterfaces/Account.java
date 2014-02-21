package SpaceInterfaces;

/**
 *
 * @author Peter
 */
public interface Account {
    String username = null;
    String password = null;
    
    public boolean SetUsername(String name);
    public boolean SetPassword(String password);
}