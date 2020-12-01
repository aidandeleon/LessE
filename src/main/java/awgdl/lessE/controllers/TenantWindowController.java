package awgdl.lessE.controllers;

import awgdl.lessE.models.AccountEntry;
import awgdl.lessE.models.Bill;
import awgdl.lessE.models.Payment;
import awgdl.lessE.models.Tenant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class TenantWindowController {
    static final int BILL_WINDOW_WIDTH = 520;
    static final int BILL_WINDOW_HEIGHT = 410;

    static final int PAYMENT_WINDOW_WIDTH = 234;
    static final int PAYMENT_WINDOW_HEIGHT = 235;

    private MainMenuController mainMenuController;

    private Tenant tenant;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private TextField currencyTextField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ListView<HBox> billListView;

    @FXML
    private ListView<HBox> paymentListView;

    public void initDataAdd(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;

        confirmButton.setText("Add");
        confirmButton.setOnAction(e -> {
            addTenant();
        });
    }

    public void initData(MainMenuController mainMenuController, Tenant tenant) {
        this.mainMenuController = mainMenuController;
        this.tenant = tenant;
    }

    public void initDataEdit(MainMenuController mainMenuController, Tenant tenant) {
        this.mainMenuController = mainMenuController;
        this.tenant = tenant;

        confirmButton.setText("Apply");
        confirmButton.setOnAction(e -> {
            editTenant();
        });

        firstNameTextField.setText(tenant.getFirstName());
        lastNameTextField.setText(tenant.getLastName());
        addressTextField.setText(tenant.getAddress());
        currencyTextField.setText(tenant.getCurrency());
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public void updateBillListView() {
        ArrayList<Bill> bills = this.tenant.getBills();
        billListView.getItems().clear();
        for (Bill bill : bills) {
            HBox newBillHBox = createEntryHBox(bill);
            billListView.getItems().add(newBillHBox);
        }
    }

    public void updatePaymentListView() {
        ArrayList<Payment> payments = this.tenant.getPayments();
        paymentListView.getItems().clear();
        for (Payment payment : payments) {
            HBox newBillHBox = createEntryHBox(payment);
            paymentListView.getItems().add(newBillHBox);
        }
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public Tenant createTenant() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String currency = currencyTextField.getText();

        return new Tenant(firstName, lastName, address, currency);
    }

    @FXML
    public void addTenant() {
        if (isEmptyTenantInputs()) {
            showTenantInputErrors();

        } else {
            Tenant newTenant = createTenant();
            setTenant(newTenant);

            closeWindow();
        }
    }

    private void showTenantInputErrors() {
        String errors = "";

        if (firstNameTextField.getText().equals("")) {
            errors += "First Name: No first name entered.\n";
        }

        if (lastNameTextField.getText().equals("")) {
            errors += "Last Name: No last name entered.\n";
        }

        if (addressTextField.getText().equals("")) {
            errors += "Address: No address entered.\n";
        }

        if (currencyTextField.getText().equals("")) {
            errors += "Currency: No currency entered.\n";
        }

        if (!errors.equals("")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Tenant Input Error");
            errorAlert.setContentText(errors);
            errorAlert.showAndWait();
        }
    }

    @FXML
    public void editTenant() {
        if (isEmptyTenantInputs()) {
            showTenantInputErrors();

        } else {
            Tenant newTenant = createTenant();

            tenant.setFirstName(newTenant.getFirstName());
            tenant.setLastName(newTenant.getLastName());
            tenant.setAddress(newTenant.getAddress());
            tenant.setCurrency(newTenant.getCurrency());

            closeWindow();
        }
    }

    private boolean isEmptyTenantInputs() {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String address = addressTextField.getText();
        String currency = currencyTextField.getText();

        return firstName.equals("") ||
                lastName.equals("") ||
                address.equals("")  ||
                currency.equals("") ;
    }


    public void showAddBillWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BillWindow.fxml"));
            Parent content = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Bill Entry");
            stage.setScene(new Scene(content, BILL_WINDOW_WIDTH, BILL_WINDOW_HEIGHT));
            stage.setResizable(false);

            BillingWindowController billingWindowController = loader.getController();
            billingWindowController.initDataAdd(tenant, true);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (billingWindowController.getEntry() != null) {
                Bill newBill = (Bill) billingWindowController.getEntry();
                tenant.addBillEntry(newBill);

                HBox newBillHBox = createEntryHBox(newBill);
                billListView.getItems().add(newBillHBox);
                mainMenuController.updateTenantListView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showBillEditWindow() {
        if (!billListView.getSelectionModel().isEmpty()) {
            int billClickedIndex = billListView.getSelectionModel().getSelectedIndex();
            Bill currentEntry = tenant.getBills().get(billClickedIndex);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BillWindow.fxml"));
                Parent content = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Edit Bill Entry");
                stage.setScene(new Scene(content, BILL_WINDOW_WIDTH, BILL_WINDOW_HEIGHT));
                stage.setResizable(false);

                BillingWindowController billingWindowController = loader.getController();
                billingWindowController.initDataBillEdit(tenant, currentEntry);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                Bill updatedEntry = (Bill) billingWindowController.getEntry();

                if (!currentEntry.equals(updatedEntry)) {
                    tenant.updateBill(billClickedIndex,
                            updatedEntry.getDate(),
                            updatedEntry.getBillingPeriodStart(),
                            updatedEntry.getBillingPeriodEnd(),
                            updatedEntry.getCost(),
                            updatedEntry.getDescription(),
                            updatedEntry.getCharges());
                }
                updateBillListView();
                mainMenuController.updateTenantListView();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void deleteBill() {
        if (!billListView.getSelectionModel().isEmpty()) {
            int selectedBill = billListView.getSelectionModel().getSelectedIndex();
            billListView.getItems().remove(selectedBill);
            tenant.removeBill(selectedBill);
            mainMenuController.updateTenantListView();
        }
    }

    public void showAddPaymentWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PaymentWindow.fxml"));
            Parent content = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Add Payment Entry");
            stage.setScene(new Scene(content, PAYMENT_WINDOW_WIDTH, PAYMENT_WINDOW_HEIGHT));
            stage.setResizable(false);

            BillingWindowController billingWindowController = loader.getController();
            billingWindowController.initDataAdd(tenant, false);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (billingWindowController.getEntry() != null) {
                Payment newEntry = (Payment) billingWindowController.getEntry();
                tenant.addPaymentEntry(newEntry);

                HBox newPaymentHBox = createEntryHBox(newEntry);

                paymentListView.getItems().add(newPaymentHBox);
                mainMenuController.updateTenantListView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showPaymentEditWindow() {
        if (!paymentListView.getSelectionModel().isEmpty()) {
            int selectedPayment = paymentListView.getSelectionModel().getSelectedIndex();
            Payment currentEntry = tenant.getPayments().get(selectedPayment);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PaymentWindow.fxml"));
                Parent content = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Edit Payment Entry");
                stage.setScene(new Scene(content, PAYMENT_WINDOW_WIDTH, PAYMENT_WINDOW_HEIGHT));
                stage.setResizable(false);

                BillingWindowController billingWindowController = loader.getController();
                billingWindowController.initDataPaymentEdit(tenant, currentEntry);

                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                Payment updatedEntry = (Payment) billingWindowController.getEntry();

                if (!currentEntry.equals(updatedEntry)) {
                    tenant.updatePayment(selectedPayment,
                            updatedEntry.getDate(),
                            updatedEntry.getCost(),
                            updatedEntry.getDescription());
                }
                updatePaymentListView();
                mainMenuController.updateTenantListView();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void deletePayment() {
        if (!paymentListView.getSelectionModel().isEmpty()) {
            int selectedPayment = paymentListView.getSelectionModel().getSelectedIndex();
            paymentListView.getItems().remove(selectedPayment);
            tenant.removePayment(selectedPayment);
            mainMenuController.updateTenantListView();
        }
    }

    private HBox createEntryHBox(AccountEntry entry) {
        Text textDetails = new Text(entry.toStringDetail());
        Text textCost = new Text(entry.toStringCost());

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox hbox = new HBox(textDetails, spacer, textCost);
        hbox.setPadding(new Insets(5));

        return hbox;
    }
}
