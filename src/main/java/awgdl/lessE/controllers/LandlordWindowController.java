package awgdl.lessE.controllers;

import awgdl.lessE.models.Landlord;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LandlordWindowController {
    private Landlord landlord;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    public void initData() {
        confirmButton.setText("Save");
        confirmButton.setOnAction(e -> { addLandlord(); });
    }

    @FXML
    public void initData(Landlord landlord) {
        this.landlord = landlord;

        firstNameTextField.setText(landlord.getFirstName());
        lastNameTextField.setText(landlord.getLastName());

        confirmButton.setText("Apply");
        confirmButton.setOnAction(e -> { updateLandlord(); });
    }

    public Landlord getLandlord() {
        return landlord;
    }

    public void setLandlord(Landlord landlord) {
        this.landlord = landlord;
    }

    @FXML
    public Landlord createLandlord() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();

        return new Landlord(firstName, lastName);
    }

    @FXML
    public void addLandlord() {
        if (isEmptyLandlordInputs()) {
            showLandlordInputErrors();

        } else {
            landlord = createLandlord();
            closeWindow();
        }
    }

    @FXML
    public void updateLandlord() {
        if (isEmptyLandlordInputs()) {
            showLandlordInputErrors();

        } else {
            Landlord updatedLandlord = createLandlord();

            landlord.setFirstName(updatedLandlord.getFirstName());
            landlord.setLastName(updatedLandlord.getLastName());

            closeWindow();
        }
    }

    private boolean isEmptyLandlordInputs() {
        return firstNameTextField.getText().equals("") || lastNameTextField.getText().equals("");
    }

    private void showLandlordInputErrors() {
        String errors = "";

        if (firstNameTextField.getText().equals("")) {
            errors += "First Name: No name entered.\n";
        }

        if (lastNameTextField.getText().equals("")) {
            errors += "Last Name: No name entered.\n";
        }

        if (!errors.equals("")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Landlord Input Error");
            errorAlert.setContentText(errors);
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
