/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**This class contains the Observable lists and various methods to update Parts/ Products and Lookup by ID/Name.
  A feature I would implement that would extend functionality is the capability to track orders and sales. 
 This will allow the user to track which parts sell well with a product and which products overall are selling. 
 @author Koala
 */
public class Inventory {
    
    /**Observable Lists.*/
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> lookupParts = FXCollections.observableArrayList();
    private static ObservableList<Product> lookupProducts = FXCollections.observableArrayList();
    
     /**
     * @return the partIdCount
     */
    private static int partIdCount = 10;
    
     /**
     * @return the productIdCount
     */
    private static int productIdCount = 3;
     
    /**Is Part Used Method. This checks if a part is currently being used by a product before deletion */
     public static boolean isPartUsed(Part part) {
        boolean isUsed = false;
        for (int i = 0; i < allProducts.size(); i++) {
            if (allProducts.get(i).getAssociatedParts().contains(part)) {
                isUsed = true;
            }
        }
        return isUsed;
    }
     
     
     
   
    
    /** * Add Part Method.Sets the parameters to add a Part to the Inventory System
     * @param part*/
    public static void addPart(Part part) {
        allParts.add(part); 
    }
    
    /** * Add Product Method.Sets the parameters to add a Product to the Inventory System
     * @param product*/
    public static void addProduct(Product product) {
        allProducts.add(product);
    }
    
    /**
     * @return the allparts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    /**
     @return the allproucts
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
    /** Lookup Part Method by Id.This allows the user to search a Part by Part ID
      @param id
      @return */
    public static Part lookupPart(int id) {
        for(Part p : allParts) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    /**Lookup Product Method by Id.This allows the user to search a Product by Product ID
    @param id
    @return */
    public static Product lookupProduct(int id) {
        for(Product p : allProducts) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    /** * Look up Part by name.This allows the user to search a Part by Part Name
     * @param partName
     * @return */
    public static ObservableList<Part> lookupPart(String partName){
        lookupParts.clear();
        for(int i = 0; i < allParts.size(); i++){
            Part p = allParts.get(i);
            
            if(p.getName().contains(partName)){
                lookupParts.add(p);
            }
        }
        return lookupParts;   
        }
    
    /** * Look up Product by name.This allows the user to search a Product by Product Name
     * @param productName
     * @return */
    public static ObservableList<Product> lookupProduct(String productName){
        lookupProducts.clear();
        for(int i = 0; i < allProducts.size(); i++){
            Product pr = allProducts.get(i);
            
            if(pr.getName().contains(productName)){
                lookupProducts.add(pr);
            }
        }
        return lookupProducts;
    }
    
    /** * Update Part Method.This updates the window with the selected Part
     * @param index
     * @param selectedPart*/
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    
    /** * Update Product Method.This updates the window with the selected Product
     * @param index
     * @param newProduct*/
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }
   
    
    /** * Delete Part Method.This allows the user to delete the selected part
     * @param selectedPart
     * @return */
    public static boolean deletePart(Part selectedPart){
        allParts.remove(selectedPart);
        return true;
    }
   
    
    /** * Delete Product Method.This allows the user to delete the selected product
     * @param selectedProduct
     * @return */
    public static boolean deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
        return true;
    }
    
    /**This method is used to control the AutoGen I
     * @return D*/
    public static int handlePartIdCount(){
        partIdCount++;
        return partIdCount;
    }
    
    /**This method is used to control the AutoGen I
     * @return D*/
    public static int handleProductIdCount(){
        productIdCount++;
        return productIdCount;
    }
}