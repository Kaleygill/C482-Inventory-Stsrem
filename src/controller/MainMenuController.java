/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

 


import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import static model.Inventory.deletePart;
import static model.Inventory.getAllParts;
import static model.Inventory.getAllProducts;
import static model.Inventory.isPartUsed;
import static model.Inventory.lookupPart;
import static model.Inventory.lookupProduct;
import model.Outsourced;
import model.Part;
import model.Product;


/** FXML Controller Class. 
 *
 * @author Koala
 */
public class MainMenuController implements Initializable {
 Stage stage;
 Parent scene;
    
    @FXML
    private TableView<Part> invPartTbl;

    @FXML
    private TableColumn<Part, Integer> invIPartDCol;

    @FXML
    private TableColumn<Part, String> invPartNameCol;

    @FXML
    private TableColumn<Part, Integer> invPartInvCol;

    @FXML
    private TableColumn<Part, Double> invPartPriceCol;

    @FXML
    private TableView<Product> invProdTbl;

    @FXML
    private TableColumn<Product, Integer> invProdIDCol;

    @FXML
    private TableColumn<Product, String> invProdNameCol;

    @FXML
    private TableColumn<Product, Integer> invProdInvCol;

    @FXML
    private TableColumn<Product, Double> invProdPriceCol;

    @FXML
    private TextField invPartSearchTxt;

    @FXML
    private TextField invProdSearchTxt;
    
    
    private ObservableList<Part> searchPart = FXCollections.observableArrayList();
    private ObservableList<Product> searchProduct = FXCollections.observableArrayList();
    private static Product selectedProduct;
    private static int indexOfSelectedProduct;
    private static Part selectedPart;
    private static int indexOfSelectedPart;

    /**Get selected Part Method
     * @return .*/
    public static Part getSelectedPart() {
        return selectedPart;
    }

    /**Get selected Part Index Method
     * @return .*/
    public static int getSelectedPartIndex() {
        return indexOfSelectedPart;
    }

    /**Get selected Product Method
     * @return .*/
    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    /**Get selected Product Method
     * @return d*/
    public static int getSelectedProductIndex() {
        return indexOfSelectedProduct;
    }


    /**Exit Program Button Control. This allows the user to close the Inventory System Program. */
    @FXML
    void onActionInvExit(ActionEvent event) {
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This will exit the program.");
        alert.setContentText("Would you like to continue?");
        
        Optional<ButtonType> result = alert.showAndWait(); 
        if (result.get() == ButtonType.OK) {
        System.exit(0);
        }
    }

    /**Add Part Button Control. This allows the user to open the Add part window to add parts to the Inventory System.*/
    @FXML
    void onActionInvPartAdd(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPartInHouse.fxml")); 
        stage.setScene(new Scene(scene));
        stage.show(); 
    }

    /**Delete Part Button Control. This allows the user to Delete a part from the Part Table View and Inventory System.*/
    @FXML
    void onActionInvPartDelete(ActionEvent event) {
        
         Part part = invPartTbl.getSelectionModel().getSelectedItem();
         if (part == null) {
             Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a part to delete!");
            alert.showAndWait();
          
         }
         else if (isPartUsed(part)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("You cannot delete this part");
            alert.setContentText("This part is used in a product; please remove from any products before deletion");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm part deletion");
            alert.setContentText("Please confirm you want to delete this part.");
            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {
                deletePart(part);
                updateParts();
            }
        }
        
    }

    /**Modify Part Button Control. This allows the user to open the Modify Part window to make changes to the selected part.*/
    @FXML
    private void onActionIModPart(ActionEvent event) throws IOException {
       
        
        selectedPart = invPartTbl.getSelectionModel().getSelectedItem();
        indexOfSelectedPart = getAllParts().indexOf(selectedPart);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPartInHouse.fxml"));
            Parent modifyPartPage = loader.load();
            Scene modifyPartScene = new Scene(modifyPartPage);

            
            ModifyPartInHouseController controller = loader.getController();
            if ((invPartTbl.getSelectionModel().getSelectedItem()) instanceof InHouse) {
                controller.retrievePartIn((InHouse) (invPartTbl.getSelectionModel().getSelectedItem()));
            } else {
                controller.retrievePartOut((Outsourced) invPartTbl.getSelectionModel().getSelectedItem());
            }

            Stage applicationStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            applicationStage.setScene(modifyPartScene);
            applicationStage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a part to modify!");
            alert.showAndWait();
        }

    }
    

    /**Add Product Button Control. This allows the user to open the Add Product window to add a product to the Inventory System.*/
    @FXML
    void onActionInvProdAdd(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml")); 
        stage.setScene(new Scene(scene));
        stage.show(); 
    }

    /**Product Delete Button Control. This allows the user to delete a product from the Product Table view and Inventory System.*/
    @FXML
    void onActionInvProdDelete(ActionEvent event) {
        Product productToDelete = invProdTbl.getSelectionModel().getSelectedItem();
        if (productToDelete == null) {
           Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a product to delete!");
            alert.showAndWait();
        }
        else if (!productToDelete.getAllAssociatedParts().isEmpty()) {
        Alert warn = new Alert(Alert.AlertType.WARNING, "You cannot delete a product that has associated Parts. Delete associated parts first.", ButtonType.OK);
        warn.showAndWait();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm part deletion");
            alert.setContentText("Please confirm you want to delete this product.");
            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {
          Inventory.deleteProduct(productToDelete);
          getAllProducts();
            }
      }
       /*Product productToDelete = invProdTbl.getSelectionModel().getSelectedItem();
        if (productToDelete == null) {
           Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a product to delete!");
            alert.showAndWait();
       }
        else if (isPartUsed(productToDelete)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("You cannot delete this part");
            alert.setContentText("This part is used in a product; please remove from any products before deletion");
            alert.showAndWait();
        }
       else {Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm part deletion");
            alert.setContentText("Please confirm you want to delete this product.");
            Optional<ButtonType> confirmation = alert.showAndWait();

            if (confirmation.get() == ButtonType.OK) {
        Inventory.deleteProduct(productToDelete);
          getAllProducts();
            }
       }*/
    }
        
    
        
    /**Product Modify Button Control. This allows the user to open the Modify Product window to make changes to the selected Product. */ 
    @FXML
    void onActionInvProdMod(ActionEvent event) throws IOException {

        selectedProduct = invProdTbl.getSelectionModel().getSelectedItem();
        indexOfSelectedProduct = getAllProducts().indexOf(selectedProduct);

        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select a product to modify!");
            alert.showAndWait();
        } else {
            Parent modifyProductScreen = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Scene modifyProductScene = new Scene(modifyProductScreen);
            Stage windowProductMod = (Stage) ((Node) event.getSource()).getScene().getWindow();
            windowProductMod.setScene(modifyProductScene);
            windowProductMod.show();
        }
    
    }
    
    /**Search Part Button Control. This allows the user to search a part in the Part Table view by ID or Name. */
    @FXML
    void onActionSearchPart(ActionEvent event) {

        String searchedPart = invPartSearchTxt.getText();
        ObservableList<Part> foundParts = Inventory.lookupPart(searchedPart);

        if (foundParts.size() == 0) {
            try {
                int id = Integer.parseInt(searchedPart);
                Part part = lookupPart(id);

                if (part != null) {
                    foundParts.add(part);
                }else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No Part Found!");
            alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No Part Found!");
            alert.showAndWait();
               
            }

        }
        invPartTbl.setItems(foundParts);
        
    }

    /**Search Product Button Control. This allows the user to search for a Product in the Product Table view by ID or Name.*/
    @FXML
    void onActionSearchProduct(ActionEvent event) {
        String searchedProduct = invProdSearchTxt.getText();
        ObservableList<Product> foundProducts = Inventory.lookupProduct(searchedProduct);

        if (foundProducts.size() == 0) {

            try {
                int id = Integer.parseInt(searchedProduct);
                Product product = lookupProduct(id);
                
                if (product != null) {
                    foundProducts.add(product);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No Part Found!");
            alert.showAndWait();
                }
            } catch (NumberFormatException e) {
              
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No Product Found!");
            alert.showAndWait();
    
            }

        }
        invProdTbl.setItems(foundProducts);
    }
    
     public void updateParts() {
        invPartTbl.setItems(getAllParts());
    }

     public void updateProducts() {
        invProdTbl.setItems(getAllProducts());
    }
     
    /**Initialize Method. This populates both table views and holds the getter for AllParts and AllProducts.*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        invPartTbl.setItems(Inventory.getAllParts());
        invProdTbl.setItems(Inventory.getAllProducts());
        
        
        invIPartDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        invPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invPartInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        invPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        invProdIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        invProdNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        invProdInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        invProdPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    

}    