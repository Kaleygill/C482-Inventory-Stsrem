/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kgjavafxmlapplication;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;

/** This class Start the Program.
 *
 * @author Koala
 */
public class KGJavaFXMLApplication extends Application {
    
    @Override
    /**Launching Main Screen Method. This opens the Main Menu.*/
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**Main Method. This sets initial data and populates Appropriate Table Views for the Main menu
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        
        InHouse inHousePart1 = new InHouse(1, "Hard Drive SSD 8TB", 399.99, 5, 0, 2, 1);
        InHouse inHousePart2 = new InHouse(2, "Cables CAT6 50ft", 199.99, 10, 2, 10, 2);
      
        Outsourced outsourcedPart1 = new Outsourced(3, "LCD Display", 299.99, 5, 0, 2, "Dell");
        
        Product product1 = new Product(1, "Desktop", 125.99, 50, 5, 20);
        Product product2 = new Product(2, "Laptop", 175.99, 50, 5, 20);
        
        Inventory.addPart(inHousePart1);
        Inventory.addPart(inHousePart2);
        
        Inventory.addPart(outsourcedPart1);
        
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        
        product1.addAssociatedPart(inHousePart1);
        product2.addAssociatedPart(inHousePart2);
       
        
        
        launch(args);
    }
    
    
}
