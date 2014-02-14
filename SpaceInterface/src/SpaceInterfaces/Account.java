package SpaceInterfaces;

/**
 *
 * @author Peter
 */
public interface Account {
    String username = "";
    String password = "";
    
    public boolean SetUsername(String name);
    public boolean SetPassword(String password);
}
