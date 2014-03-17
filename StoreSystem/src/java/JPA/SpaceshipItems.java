/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JPA;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 *
 * @author Jamy
 */
public class SpaceshipItems {

    @Id
    private long id;
    
    @Id
    private long spaceship_id;

    @Id
    @Column()
    private long item_id;
}
