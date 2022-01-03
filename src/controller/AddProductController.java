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
public class AddProductController implements Initializable {

    Stage stage;
    Parent scene;
    
    Product product;
    
    @FXML
    private TextField addProductIDTxt;

    @FXML
    private TextField addProductNameTxt;

    @FXML
    private TextField addProductInvTxt;

    @FXML
    private TextField addProductPriceTxt;

    @FXML
    private TextField addProductMaxTxt;

    @FXML
    private TextField addProductMinTxt;
    
    @FXML
    private TableColumn<Part, Integer> addProductIDCol;

    @FXML
    private TableColumn<Part, String> addProductNameCol;

    @FXML
    private TableColumn<Part, Integer> addProductInvCol;

    @FXML
    private TableColumn<Part, Double> addProductPriceCol;

    @FXML
    private TableView<Part> addProdAddTbl;

    @FXML
    private TableColumn<Part, Integer> removeProductIDCol;

    @FXML
    private TableColumn<Part, String> removeProductNameCol;

    @FXML
    private TableColumn<Part, Integer> removeProductInvCol;

    @FXML
    private TableColumn<Part, Double> removeProductPriceCol;

    @FXML
    private TableView<Part> addProdRemoveTbl;

    @FXML
    private TextField addProductSearchTxt;
    
    private int idGen;
    private Part selectedPart;
    private ObservableList<Part> partsToTransfer = FXCollections.observableArrayList();

    /**Add Product Button Control. This allows the user to ass a Part to a Product*/
    @FXML
    void onActionAddProdAdd(ActionEvent event) {
        
        selectedPart = addProdAddTbl.getSelectionModel().getSelectedItem();
        partsToTransfer.add(selectedPart);
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please select part to add.");
            alert.showAndWait();
        } else {
            addProdRemoveTbl.setItems(partsToTransfer);
            removeProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            removeProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            removeProductInvCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            removeProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        }

    }

    /**Cancel Product Button Control. This allows the user to Exit from the Modify Product Window and return to the Main Menu.*/ 
    @FXML
    void onActionAddProdCancel(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Changes not Saved");
        alert.setContentText("Would you like to continue to Main Screen?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        } 
       
    }

    /**Remove Product Button Control. This allows the user to remove a part from a Product*/
    @FXML
    void onActionAddProdRemove(ActionEvent event) {
        
        int selectedItem = addProdRemoveTbl.getSelectionModel().getSelectedIndex();

        if (selectedItem >= 0) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to delete selected item?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                addProdRemoveTbl.getItems().remove(selectedItem);
            }
        } else {
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setContentText("No item was selected!");
            alert1.showAndWait();
        }

    }

    /**Save Product Button Control. This allows the user to save changes and return to the Main Menu*/
    @FXML
    void onActionAddProdSave(ActionEvent event) throws IOException {
        
        
          try {
            String name = addProductNameTxt.getText();
            int stock = Integer.parseInt(addProductInvTxt.getText());
            double price = Double.parseDouble(addProductPriceTxt.getText());
            int max = Integer.parseInt(addProductMaxTxt.getText());
            int min = Integer.parseInt(addProductMinTxt.getText());

            if ((min > max) || (max < stock) || (stock < min)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please check your input for the product. Min cannot be larger than max. Stock cannot be larger than max. Min cannot be larger than stock.");
                alert.showAndWait();
            } else {

                Product product = new Product(idGen, name, price, stock, min, max);
                product.setAssociatedParts(addProdRemoveTbl.getItems());
                Inventory.addProduct(product);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Please confirm that you want to add the Product to Inventory!");
                Optional<ButtonType> choice = alert.showAndWait();

                if (choice.get() == ButtonType.OK) {
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                }
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Error adding a product. Please correct it and try again!");
            alert.showAndWait();
        }
        
    
    }
        
    /**Search Product Button Control. This allows the user to search for a part by ID or Name*/
    @FXML
    void onActionSearchProd(ActionEvent event) {
        
         String searchedPart = addProductSearchTxt.getText();
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
         addProdAddTbl.setItems(foundParts);
        
    }
    

    /**Initialize Method. This sets the AutoGen ID and TableView Contents*/
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        product = new Product();
        
        addProductIDTxt.setText("Auto Gen - Disabled");
        addProductIDTxt.setDisable(true);
        
         idGen = Inventory.handleProductIdCount();
        addProductIDTxt.setText(Integer.toString(idGen));
        
        addProdAddTbl.setItems(Inventory.getAllParts());
        addProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
    }    
    
}
