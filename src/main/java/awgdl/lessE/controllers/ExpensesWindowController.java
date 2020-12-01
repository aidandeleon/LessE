package awgdl.lessE.controllers;

import awgdl.lessE.models.Expense;
import awgdl.lessE.models.Landlord;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;


public class ExpensesWindowController {
    private Expense expense;

    @FXML
    private TextField amountTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ToggleGroup incomeExpenseToggleGroup;

    @FXML
    private RadioButton expenseRadioButton;

    @FXML
    private RadioButton incomeRadioButton;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    public Expense getExpense() {
        return expense;
    }

    public void initDataAdd(MainMenuController mainMenuController, Landlord landlord) {
        confirmButton.setText("Add");
        confirmButton.setOnAction(e -> {
            updateExpenseEntry();
        });
    }

    public void initDataEdit(MainMenuController mainMenuController, Landlord landlord, Expense expense) {
        this.expense = expense;

        confirmButton.setText("Apply");
        confirmButton.setOnAction(e -> {
            updateExpenseEntry();
        });

        datePicker.setValue(expense.getDate());
        descriptionTextField.setText(expense.getDescription());

        double cost = expense.getCost();

        if (cost < 0) {
            expenseRadioButton.setSelected(true);
        } else {
            incomeRadioButton.setSelected(true);
        }

        amountTextField.setText(Double.toString(Math.abs(cost)));
    }

    @FXML
    public Expense createExpense() {
        RadioButton selectedRadioButton = (RadioButton) incomeExpenseToggleGroup.getSelectedToggle();
        String toggleGroupValue = selectedRadioButton.getText();

        String description = descriptionTextField.getText();
        double amount = Double.parseDouble(amountTextField.getText());
        LocalDate date = datePicker.getValue();

        if (toggleGroupValue.equals("Expense")) {
            amount *= -1; //expenses deduct from funds
        }

        return new Expense(date, amount, description);
    }

    private boolean isEmptyExpenseInputs() {
        String description = descriptionTextField.getText();
        String amount = amountTextField.getText();
        LocalDate date = datePicker.getValue();

        return description == null || description.equals("") ||
                amount == null || amount.equals("") ||
                date == null;
    }

    private boolean isNonFloat(String input) {
        return !input.matches("^\\d*\\.?\\d+$");
    }

    private boolean isInvalidExpenseInputs() {
        return isEmptyExpenseInputs() || isNonFloat(amountTextField.getText());
    }

    @FXML
    public void updateExpenseEntry() {
        if (isInvalidExpenseInputs()) {
            showInputErrors();

        } else {
            expense = createExpense();
            closeWindow();
        }
    }

    private void showInputErrors() {
        String errors = "";

        String description = descriptionTextField.getText();
        String cost = amountTextField.getText();
        LocalDate date = datePicker.getValue();


        if (description == null || description.equals("")) {
            errors += "Description: No description entered.\n";
        }

        if (cost == null || cost.equals("")) {
            errors += "Amount: No amount entered.\n";
        } else {
            if (isNonFloat(cost)) {
                errors += "Amount: Please enter a positive decimal value.\n";
            }
        }

        if (date == null) {
            errors += "Date: No date entered.\n";
        }

        if (!errors.equals("")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Expenses Input Error");
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
