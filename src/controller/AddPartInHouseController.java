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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
        
/**
 *
 * @author Koala
 */
public class AddPartInHouseController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    Part newPartId;
    
    
    @FXML
    private TextField addPartIDInTxt;

    @FXML
    private TextField addPartNameInTxt;

    @FXML
    private TextField addPartInvInTxt;

    @FXML
    private TextField addPartPriceInTxt;

    @FXML
    private TextField addPartMaxInTxt;

    @FXML
    private TextField addPartMachineInTxt;

    @FXML
    private RadioButton addPartInBtn;

    @FXML
    private RadioButton addPartOutBtn;

    @FXML
    private TextField addPartMinInTxt;
    
    @FXML
    private Label AddPartLbl;
    
    @FXML
    private ToggleGroup addPartTG;
    
    private int idGen;
    
    
    /**Cancel Button Control. This allows the user to exit the Add Part window and return to the Main Menu*/
        @FXML
    void onActionInAddPartCancel(ActionEvent event) throws IOException {
        
       Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Changes not Saved");
        alert.setContentText("Would you like to continue to Main Screen?");
        
        Optional<ButtonType> choice = alert.showAndWait();
        if (choice.get() == ButtonType.OK) {
       stage = (Stage)((Button)event.getSource()).getScene().getWindow();
       scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
       stage.setScene(new Scene(scene));
       stage.show();
        }
    }

    /**Save Button Control. This allows the user to Save changed made is the App Part Window.*/
    @FXML
    void onActionInAddPartSave(ActionEvent event) throws IOException {

       
         try {
            String name = addPartNameInTxt.getText();
            int stock = Integer.parseInt(addPartInvInTxt.getText());
            double price = Double.parseDouble(addPartPriceInTxt.getText());
            int max = Integer.parseInt(addPartMaxInTxt.getText());
            int min = Integer.parseInt(addPartMinInTxt.getText());

            if ((min > max) || (max < stock) || (stock < min)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please check your input for the part. Min cannot be larger than max. Stock cannot be larger than max. Min cannot be larger than stock.");
                alert.showAndWait();
            } else {
                if (addPartInBtn.isSelected()) {
                    int machineId = Integer.parseInt(addPartMachineInTxt.getText());
                    InHouse newInHouse = new InHouse(idGen, name, price, stock, min, max, machineId);

                    Inventory.addPart(newInHouse);
                } else {
                    String companyName = addPartMachineInTxt.getText();
                    Outsourced newOutsourced = new Outsourced(idGen, name, price, stock, min, max, companyName);

                    Inventory.addPart(newOutsourced);
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Please confirm that you want to add the part to Inventory!");
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
            alert.setContentText("Error adding a part. Please correct it and try again!");
            alert.showAndWait();
        }

    }
    
    /**Inhouse Button Control. This sets the text and Prompt Text when Inhouse button is clicked.*/
    @FXML
    private void onActionInHouse(ActionEvent event) {
        AddPartLbl.setText("Machine ID");
        addPartMachineInTxt.setPromptText("Machine ID");
        
    }

    /**Outsourced Button Control. This sets the text and Prompt Text when Outsourced button is clicked.*/
    @FXML
    private void onActionOutsourced(ActionEvent event) {
        AddPartLbl.setText("Company Name");
        addPartMachineInTxt.setPromptText("Company Name");
    }


    /**Initialize Method. This set the Auto Gen ID. */
    @Override 
    public void initialize(URL url, ResourceBundle rb) {
       
        addPartIDInTxt.setText("Auto Gen - Disabled");
        addPartIDInTxt.setDisable(true);  
        
         idGen = Inventory.handlePartIdCount();
         addPartIDInTxt.setText(Integer.toString(idGen));
    }   

    
    
}