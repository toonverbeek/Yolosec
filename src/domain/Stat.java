/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity()
public class Stat implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String type;
    private long amount;

    public Stat() {
    }

    public Stat(String type, long amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Get the type of the stat
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the stat
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get the amount of the stat
     *
     * @return
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Set the amount of the stat
     *
     * @param amount
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
