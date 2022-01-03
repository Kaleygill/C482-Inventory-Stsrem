/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.MainMenuController.getSelectedPartIndex;
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

/**
 * FXML Controller class
 *
 * @author Koala
 */
public class ModifyPartInHouseController implements Initializable {

    Stage stage;
    Parent scene;
    
    private InHouse selectedInHousePart;
    private Outsourced selectedOutsourcedPart;
    private static int index;

    
    @FXML
    private TextField modPartIdInTxt;
    @FXML
    private TextField modPartNameInTxt;
    @FXML
    private TextField modPartInvInTxt;
    @FXML
    private TextField modPartPriceInTxt;
    @FXML
    private TextField modPartMaxInTxt;
    @FXML
    private TextField modPartMachineInTxt;
    @FXML
    private RadioButton inHouseBtn;
    @FXML
    private ToggleGroup modPartTG;
    @FXML
    private RadioButton outsourcedBtn;
    @FXML
    private TextField modPartMinInTxt;
    @FXML
    private Label machineIdLbl;

    
   
    

    /**Toggle Button Control, Outsourced. This sets the Label and prompt test to "Company Name" .*/
    @FXML
    void onActionOutMod(ActionEvent event) {
        machineIdLbl.setText("Company Name");
        modPartMachineInTxt.setPromptText("Company Name");
        
    }
    
    /**Toggle Button Control, Inhouse. This sets the Label and prompt test to "Machine ID" .*/
    @FXML
    void onActionInMod(ActionEvent event) {

        machineIdLbl.setText("Machine ID");
        modPartMachineInTxt.setPromptText("Machine ID");
    }

    /**Save Part Button Control. this allows the users to save changed made in the Modify Part window and return to the Main Menu.*/
    @FXML
    void onActionInModPartSave(ActionEvent event) throws IOException {
        try {
        int id = Integer.parseInt(modPartIdInTxt.getText());
        String name = modPartNameInTxt.getText();
        int stock = Integer.parseInt(modPartInvInTxt.getText());
        double price = Double.parseDouble(modPartPriceInTxt.getText());
        int max = Integer.parseInt(modPartMaxInTxt.getText());
        int min = Integer.parseInt(modPartMinInTxt.getText());
        index = getSelectedPartIndex();

        if ((min > max) || (max < stock) || (stock < min)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Please check your input for the product. Min cannot be larger than max. Stock cannot be larger than max. Min cannot be larger than stock.");
            alert.showAndWait();
        } else {
            if (inHouseBtn.isSelected()) {
                inHouseClicked();
                int machineId = Integer.parseInt(modPartMachineInTxt.getText());
                InHouse modifiedInHouse = new InHouse(id, name, price, stock, min, max, machineId);
                Inventory.updatePart(index, modifiedInHouse);
                
            } else {
                outsourcedClicked();
                String companyName = modPartMachineInTxt.getText();
                Outsourced modifiedOutsourced = new Outsourced(id, name, price, stock, min, max, companyName);
                Inventory.updatePart(index, modifiedOutsourced);
            }
            
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Please confirm you want to modify the selected part!");
            Optional<ButtonType> choice = alert.showAndWait();

            if (choice.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } 
        }catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Error adding a part. Please correct it and try again!");
            alert.showAndWait();
            }
        
    }
    
    /**In House Button Method. This ensures that when the button is clicked, it sets the text to Machine ID. */ 
     public void inHouseClicked() {
        machineIdLbl.setText("Machine ID");
    }

    /**Outsourced Button Method. This ensures that when the button is clicked, it sets the text to Company Name. */
    public void outsourcedClicked() {
        machineIdLbl.setText("Company Name");
    }

    /**Cancel Button Control. This allows the user to exit the Modify part window and return to the Main Menu without Saving.*/ 
    @FXML
    void onActionInModPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Changes not Saved");
        alert.setContentText("Would you like to continue?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        } 
    }
    
     
    /**Retrieve Part Method.This retrieves the selected Inhouse part.
     @param part
     */
    public void retrievePartIn(InHouse part) {
        selectedInHousePart = part;
        modPartIdInTxt.setText(Integer.toString(selectedInHousePart.getId()));
        modPartNameInTxt.setText(selectedInHousePart.getName());
        modPartInvInTxt.setText(Integer.toString(selectedInHousePart.getStock()));
        modPartPriceInTxt.setText(Double.toString(selectedInHousePart.getPrice()));
        modPartMaxInTxt.setText(Integer.toString(selectedInHousePart.getMax()));
        modPartMinInTxt.setText(Integer.toString(selectedInHousePart.getMin()));
        modPartMachineInTxt.setText(Integer.toString(selectedInHousePart.getMachineId()));
        machineIdLbl.setText("Machine ID");
        inHouseBtn.setSelected(true);
        modPartIdInTxt.setDisable(true);
            
    }
    
    /** Retrieve Part Method. This retrieves the selected Outsourced part.
      @param part*/
     public void retrievePartOut(Outsourced part) {
        selectedOutsourcedPart = part;
        modPartIdInTxt.setText(Integer.toString(selectedOutsourcedPart.getId()));
        modPartNameInTxt.setText(selectedOutsourcedPart.getName());
        modPartInvInTxt.setText(Integer.toString(selectedOutsourcedPart.getStock()));
        modPartPriceInTxt.setText(Double.toString(selectedOutsourcedPart.getPrice()));
        modPartMaxInTxt.setText(Integer.toString(selectedOutsourcedPart.getMax()));
        modPartMinInTxt.setText(Integer.toString(selectedOutsourcedPart.getMin()));
        modPartMachineInTxt.setText(selectedOutsourcedPart.getCompanyName());
        machineIdLbl.setText("Company Name");
        outsourcedBtn.setSelected(true);
        modPartIdInTxt.setDisable(true);
     }
     
     /**
     * Initializes the controller class. This sets the AutoGen ID.
     */ 
       @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         
        
         //sets ID TextField to "Auto Gen - Disabled" and disables its function
        modPartIdInTxt.setText("Auto Gen - Disabled");
        modPartIdInTxt.setDisable(true); 
    }
    
}
