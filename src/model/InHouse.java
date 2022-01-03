/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**This class contains the variables, getters, and setters for the Inhouse Part Window.
 *
 * @author Koala
 */
public class InHouse extends Part {
    public int machineId; 
    
    //Variables
    public InHouse(int id, String name, Double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the machineid
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}