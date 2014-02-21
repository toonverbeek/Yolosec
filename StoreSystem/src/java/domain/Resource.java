/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

public class Resource {

    private String type;
    private long amount;

    public Resource(String type, long amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Get the type of the resource
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the resource
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Set the amount of the resource
     *
     * @return
     */
    public long getAmount() {
        return amount;
    }

    /**
     * Set the amount of the resource
     *
     * @param amount
     */
    public void setAmount(long amount) {
        this.amount = amount;
    }
    
    /**
     * Increase the amount with <amount> 
     * @param amount 
     */
    public void increaseAmount(long amount){
        this.amount += amount;
    }

}
