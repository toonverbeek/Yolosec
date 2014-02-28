/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author Jamy
 */
@Entity
@Table
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @Column
    private String username;

    public Test() {
        this.username = "USERNAME";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
