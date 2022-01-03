/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**This class sets the Variables, Constructors, Observable List, and the getters/ setters
 *
 * @author Koala
 */
public class Product {
    
    /**Part Observable List for associated parts*/
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList(); 
    
    /**Product Variables*/
    private int id;
    private String name; 
    private double price;
    private int stock; 
    private int min;
    private int max; 
    

    /**Product Constructor*/
    public Product(int id, String name, int stock, Double price, int min, int max, ObservableList associatedParts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts.addAll(associatedParts);
    }
    
      public Product(int id, String name, Double price, int inv, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = inv;
        this.min = min;
        this.max = max;
    }
    
    
    public Product() {};
    
    /**
     * @return the associatedParts
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    /**
     Get all associated parts Method. This allowed me to fix the issue of deleting a product with an associated part. 
     * The ability to call All Associated parts and set an alert allowed me to catch this error and display to the user. 
     * @return the allAssociatedParts
     */
    public ObservableList<Part> getAllAssociatedParts() {
            return associatedParts;
    }
    
    /**Setter for associated parts.*/
    public void setAssociatedParts(ObservableList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }
    
    /**Add Associated Parts Method.*/
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }
    
    /**Delete Associated Part Method.*/
     public boolean deleteAssociatedPart(Part selectedAspart){
        return true;
    }
     
    
     /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

   /**
     * @param price the price to set
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    

    
    
    
}
