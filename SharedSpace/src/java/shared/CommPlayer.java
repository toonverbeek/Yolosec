package shared;

/**
 *
 * @author Administrator
 */
public class CommPlayer {
    private String username;
    private String password;
    private Integer id;
    private CommSpaceship spaceship;

    public CommPlayer(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public CommSpaceship getSpaceship() {
        return spaceship;
    }

    public void setSpaceship(CommSpaceship spaceship) {
        this.spaceship = spaceship;
    }
    
    public Integer getId() {
        return this.id;
    }
}
