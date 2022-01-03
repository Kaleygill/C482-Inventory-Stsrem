/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import static controller.MainMenuController.getSelectedProduct;
import static controller.MainMenuController.getSelectedProductIndex;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import static model.Inventory.lookupPart;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author Koala
 */
/**Modify Part Controller creates Dialog Window utilized to make changes to the selected project.*/
public class ModifyProductController implements Initializable {

    @FXML
    private TextField modProductIDTxt;

    @FXML
    private TextField modProductNameTxt;

    @FXML
    private TextField modProductInvTxt;

    @FXML
    private TextField modProductPriceTxt;

    @FXML
    private TextField modProductMaxTxt;

    @FXML
    private TextField modProductMinTxt;

    @FXML
    private TableView<Part> addProdAddTbl;

    @FXML
    private TableColumn<Part, Integer> modProductIDCol;

    @FXML
    private TableColumn<Part, String> modProductNameCol;

    @FXML
    private TableColumn<Part, Integer> modProductInvCol;

    @FXML
    private TableColumn<Part, Double> modProductPriceCol;

    @FXML
    private TableView<Part> addProdRemoveTbl;

    @FXML
    private TableColumn<Part, Integer> modProductIDBCol;

    @FXML
    private TableColumn<Part, String> modProductNameBCol;

    @FXML
    private TableColumn<Part, Integer> modProductInvBCol;

    @FXML
    private TableColumn<Part, Double> modProductPriceBCol;

    @FXML
    private TextField modProductSearchTxt;
    
    Stage stage;
    Parent scene;
    
    Product product;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> partsToTransfer = FXCollections.observableArrayList();
    private Part selectedPart;
    
    
    /**Add Part Button Control. This allows the user to add associated parts to the Product.*/
    @FXML
    void onActionAddPartmod(ActionEvent event) {

        selectedPart = addProdAddTbl.getSelectionModel().getSelectedItem();
        partsToTransfer.add(selectedPart);
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select part to add.");
            alert.showAndWait();
        } else {
            addProdRemoveTbl.setItems(partsToTransfer);
            modProductIDBCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            modProductNameBCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            modProductPriceBCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            modProductInvBCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        }
    }

    /**Cancel Button Control. This allows the user to Cancel Modifying the Product and return to the Main Menu.*/
    @FXML
    void onActionModPartCancel(ActionEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Changes not Saved");
        alert.setContentText("Would you like to continue?");
        Optional<ButtonType> choice = alert.showAndWait();

        if (choice.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        
    }

    /**Remove Part Button Control. This allows the user to remove an associated part from a Product.*/
    @FXML
    void onActionModPartRemove(ActionEvent event) {

        int selectedItem = addProdRemoveTbl.getSelectionModel().getSelectedIndex();
        
        if (selectedItem >= 0) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete selected item?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                addProdRemoveTbl.getItems().remove(selectedItem);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("No item was selected!");
            alert.showAndWait();
        }
    }

    /**Save Part Button Control. This allows the user to save any changes made during modifying a product and will return user to the Main Menu.*/
    @FXML
    void onActionModPartSave(ActionEvent event) throws IOException {

        try {
         int id = Integer.parseInt(modProductIDTxt.getText());
        String name = modProductNameTxt.getText();
        int stock = Integer.parseInt(modProductInvTxt.getText());
        double price = Double.parseDouble(modProductPriceTxt.getText());
        int max = Integer.parseInt(modProductMaxTxt.getText());
        int min = Integer.parseInt(modProductMinTxt.getText());
        associatedParts = addProdRemoveTbl.getItems();

        if ((min > max) || (max < stock) || (stock < min)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please check your input for the product. Min cannot be larger than max. Stock cannot be larger than max. Min cannot be larger than stock.");
            alert.showAndWait();
        } else {
            Product modifiedProduct = new Product(id, name, price, stock, min, max);

            modifiedProduct.setAssociatedParts(associatedParts);
            Inventory.updateProduct(getSelectedProductIndex(), modifiedProduct);
         
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Please confirm you want to modify the selected product!");
            Optional<ButtonType> choice = alert.showAndWait();
            
             if (choice.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
             }
        }
        } catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Error adding a product. Please correct it and try again!");
            alert.showAndWait();
        }
        
                
    }
    
    /**Search Part Button Control. This allows the user to search by Part ID or Part Name.  */
    @FXML
    void onActionSearchTxt(ActionEvent event) {

        String searchedPart = modProductSearchTxt.getText();
        ObservableList<Part> foundParts= Inventory.lookupPart(searchedPart);

        if (foundParts.size() == 0) {
            try{
            int id = Integer.parseInt(searchedPart);
            Part part = lookupPart(id);

            if (part != null) {
                foundParts.add(part);
            }else{
              
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

        addProdAddTbl.setItems(foundParts);
        
          
    }
    
     public void updateAddTbl() {
        addProdAddTbl.setItems(Inventory.getAllParts());
    }

    public void updateRemoveTbl() {
        addProdRemoveTbl.setItems(associatedParts);
    }
  
    
    /**
     * Initializes the controller class. This holds the getters for the Selected Product and Associated Parts. This also populates both Table Views. 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
       Product selectedProduct = getSelectedProduct();
       associatedParts.addAll(selectedProduct.getAllAssociatedParts());

        modProductIDTxt.setText(Integer.toString(selectedProduct.getId()));
        modProductIDTxt.setDisable(true);
        modProductNameTxt.setText(selectedProduct.getName());
        modProductInvTxt.setText(Integer.toString(selectedProduct.getStock()));
        modProductPriceTxt.setText(Double.toString(selectedProduct.getPrice()));
        modProductMaxTxt.setText(Integer.toString(selectedProduct.getMax()));
        modProductMinTxt.setText(Integer.toString(selectedProduct.getMin()));
        
   
        
        modProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductInvCol.setCellValueFactory(new PropertyValueFactory<>("inv"));
        modProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        updateAddTbl();
        
        modProductIDBCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductNameBCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProductInvBCol.setCellValueFactory(new PropertyValueFactory<>("inv"));
        modProductPriceBCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        updateRemoveTbl();
    }
}
    
   


