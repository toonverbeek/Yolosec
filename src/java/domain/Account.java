/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.*;

@Entity
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username")
    private String username;
    
    @Column(name = "password")
    private String password;
    
    @OneToOne(cascade = CascadeType.ALL)
    private Spaceship spaceship;
    
    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<Resource> resources;

    public Account() {
    }

    /**
     *
     * @param username
     * @param password
     * @param spaceship
     * @param resources
     */
    public Account(String username, String password, Spaceship spaceship, Collection<Resource> resources) {
        this.username = username;
        this.password = password;
        this.spaceship = spaceship;
        this.resources = resources;
    }

    /**
     * Get the username
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set the username
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get the password of the user
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password of the user
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the spaceship of the user
     *
     * @return
     */
    public Spaceship getSpaceship() {
        return spaceship;
    }

    /**
     * Set the spaceship of the user
     *
     * @param spaceship
     */
    public void setSpaceship(Spaceship spaceship) {
        this.spaceship = spaceship;
    }

    /**
     * Get the resources of the user
     *
     * @return
     */
    public Collection<Resource> getResources() {
        return resources;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Add a resource to the player
     *
     * @param resource the resource to add
     * @return
     */
    public boolean addResource(Resource resource) {
        if (resource != null) {
            return this.resources.add(resource);
        }
        return false;
    }

    /**
     * Update a resource
     *
     * @param resource the resource to update
     * @param amount the amount to add to the resource
     * @return
     */
    public boolean updateResource(Resource resource, long amount) {
        if (resource != null) {
            if (resources.contains(resource)) {
                resources.remove(resource);
                resource.increaseAmount(amount);
                resources.add(resource);
                return true;
            }
        }
        return false;
    }

    /**
     * Set the resources of the user
     *
     * @param resources
     */
    public void setResources(Collection<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public String toString() {
        return String.format("(id: " + id + " user: " + username + " pass: " + password + ")");
    }

    public void addItemToSpaceShipInventory(Item item) {
        this.spaceship.addItemToInventory(item);
    }

    public List<Item> getSpaceShipItems() {
        return new ArrayList<>(this.spaceship.getAllItems());
    }

}
