package awgdl.lessE.controllers;

import awgdl.lessE.models.AccountEntry;
import awgdl.lessE.models.Expense;
import awgdl.lessE.models.Landlord;
import awgdl.lessE.models.Tenant;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;


public class MainMenuController {
    static final int TENANT_WINDOW_WIDTH = 220;
    static final int TENANT_WINDOW_HEIGHT = 313;

    static final int EXPENSES_WINDOW_WIDTH = 210;
    static final int EXPENSES_WINDOW_HEIGHT = 270;

    static final int LANDLORD_WINDOW_WIDTH = 221;
    static final int LANDLORD_WINDOW_HEIGHT = 185;

    private Landlord landlord;
    private File profileFile;

    @FXML
    private ListView<HBox> tenantListView;

    @FXML
    private ListView<HBox> expensesListView;

    @FXML
    private AnchorPane billingAnchorPane;

    @FXML
    private Text landlordGreetingText;

    @FXML
    private Text landlordInfoText;

    public MainMenuController() {
        landlord = new Landlord();
    }

    public void initData() {
        updateProfilePanel();
    }

    private void updateProfilePanel() {
        landlordGreetingText.setText("Welcome, " + landlord.getFirstName() + "!");
        landlordInfoText.setText(landlord.toString());
    }

    public void updateTenantListView() {
        tenantListView.getItems().clear();
        for (Tenant tenant : landlord.getTenants()) {
            addToTenantList(tenant);
        }
    }


    //TENANT
    public void addToTenantList(Tenant newTenant) {
        HBox tenantBox = new HBox();
        tenantBox.setPadding(new Insets(10));
        Text tenantInfo = new Text(newTenant.toString());

        tenantBox.setOnMouseClicked(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/TenantDetails.fxml"));
                Pane content = fxmlLoader.load();
                billingAnchorPane.getChildren().setAll(content);
                TenantWindowController tenantWindowController = fxmlLoader.getController();

                tenantWindowController.initData(this, newTenant);

                if (newTenant.getBills().size() != 0) {
                    tenantWindowController.updateBillListView();
                }

                if (newTenant.getPayments().size() != 0) {
                    tenantWindowController.updatePaymentListView();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        tenantBox.getChildren().addAll(tenantInfo);
        tenantListView.getItems().add(tenantBox);
    }

    @FXML
    public void removeFromTenantList() {
        if (!tenantListView.getSelectionModel().isEmpty()) {
            int selectedTenant = tenantListView.getSelectionModel().getSelectedIndex();

            tenantListView.getItems().remove(selectedTenant);
            landlord.removeTenant(selectedTenant);

            billingAnchorPane.getChildren().clear();
            updateProfilePanel();
        }
    }

    @FXML
    public void showAddTenantWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/TenantWindow.fxml"));
            Pane content = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("New Tenant");
            stage.setScene(new Scene(content, TENANT_WINDOW_WIDTH, TENANT_WINDOW_HEIGHT));
            stage.setResizable(false);

            TenantWindowController tenantWindowController = fxmlLoader.getController();
            tenantWindowController.initDataAdd(this);

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            Tenant newTenant = tenantWindowController.getTenant();
            if (newTenant != null) {
                landlord.addTenant(newTenant);
                addToTenantList(newTenant);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editTenant() {
        if (!tenantListView.getSelectionModel().isEmpty()) {
            int selectedIndex = tenantListView.getSelectionModel().getSelectedIndex();
            Tenant selectedTenant = landlord.getTenants().get(selectedIndex);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/TenantWindow.fxml"));
                Parent content = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Edit Tenant");
                stage.setScene(new Scene(content, TENANT_WINDOW_WIDTH, TENANT_WINDOW_HEIGHT));
                stage.setResizable(false);

                TenantWindowController tenantWindowController = loader.getController();
                tenantWindowController.initDataEdit(this, selectedTenant);

                stage.showAndWait();
                updateTenantListView();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //EXPENSES
    @FXML
    public void showAddExpenseWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ExpenseWindow.fxml"));
            Pane content = fxmlLoader.load();
            ExpensesWindowController expensesWindowController = fxmlLoader.getController();

            expensesWindowController.initDataAdd(this, landlord);

            Stage stage = new Stage();
            stage.setTitle("New Expense");
            stage.setScene(new Scene(content, EXPENSES_WINDOW_WIDTH, EXPENSES_WINDOW_HEIGHT));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if(expensesWindowController.getExpense() != null) {
                landlord.addExpense(expensesWindowController.getExpense());
                addToExpenseList(expensesWindowController.getExpense());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showEditExpenseWindow() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/ExpenseWindow.fxml"));
            Pane content = fxmlLoader.load();
            ExpensesWindowController expensesWindowController = fxmlLoader.getController();

            if (!expensesListView.getSelectionModel().isEmpty()) {
                int selectedIndex = expensesListView.getSelectionModel().getSelectedIndex();
                Expense currentExpense = landlord.getExpenses().get(selectedIndex);

                expensesWindowController.initDataEdit(this, landlord, currentExpense);

                Stage stage = new Stage();
                stage.setTitle("Edit Expense");
                stage.setScene(new Scene(content, EXPENSES_WINDOW_WIDTH, EXPENSES_WINDOW_HEIGHT));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                Expense updatedExpense = expensesWindowController.getExpense();

                if (!currentExpense.equals(updatedExpense)) {
                    landlord.editExpense(selectedIndex, updatedExpense.getDate(), updatedExpense.getDescription(), updatedExpense.getCost());
                    updateExpenseListView();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateExpenseListView() {
        expensesListView.getItems().clear();
        for (AccountEntry expense : landlord.getExpenses()) {
            addToExpenseList(expense);
        }
    }

    public void addToExpenseList(AccountEntry entry) {
        Text expenseDetailText = new Text(entry.toStringDetail());
        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        double expenseCost = entry.getCost();
        Text expenseCostText = new Text(entry.toStringCost());
        expenseCostText.setFill(expenseCost < 0 ? Color.RED : Color.DARKGREEN);

        HBox expenseHBox = new HBox(expenseDetailText, spacer, expenseCostText);
        expenseHBox.setPadding(new Insets(10));
        expensesListView.getItems().add(expenseHBox);
        updateProfilePanel();
    }

    public void removeFromExpenseList() {
        if (!expensesListView.getSelectionModel().isEmpty()) {
            int expenseClickedIndex = expensesListView.getSelectionModel().getSelectedIndex();
            expensesListView.getItems().remove(expenseClickedIndex);
            landlord.removeExpense(expenseClickedIndex);
            updateProfilePanel();
        }
    }


    //LANDLORD/PROFILE
    @FXML
    public void showAddLandlordWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LandlordWindow.fxml"));
            Pane content = loader.load();
            LandlordWindowController landlordWindowController = loader.getController();
            landlordWindowController.initData();

            Stage stage = new Stage();
            stage.setTitle("New Landlord Profile");
            stage.setScene(new Scene(content, LANDLORD_WINDOW_WIDTH, LANDLORD_WINDOW_HEIGHT));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if(landlordWindowController.getLandlord() != null) {
                landlord = landlordWindowController.getLandlord();
                updateProfilePanel();
                updateTenantListView();
                updateExpenseListView();
                billingAnchorPane.getChildren().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showEditLandlordWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LandlordWindow.fxml"));
            Pane content = loader.load();
            LandlordWindowController landlordWindowController = loader.getController();
            landlordWindowController.initData(landlord);

            Stage stage = new Stage();
            stage.setTitle("Edit Landlord Profile");
            stage.setScene(new Scene(content, LANDLORD_WINDOW_WIDTH, LANDLORD_WINDOW_HEIGHT));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if(landlordWindowController.getLandlord() != null) {
                landlord = landlordWindowController.getLandlord();
                updateProfilePanel();
                updateTenantListView();
                updateExpenseListView();
                billingAnchorPane.getChildren().clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveProfile() {
        File savedFile = saveFilePath();
        if (savedFile != null) {
            try {
                FileOutputStream fileOut = new FileOutputStream(savedFile);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(landlord);
                out.close();
                fileOut.close();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
    }

    private File saveFilePath() {
        File filepath = null;
        String initFileName;
        FileChooser fileChooser = new FileChooser();

        if (profileFile == null) {
            initFileName = "lessE-profile";
        } else {
            initFileName = profileFile.getName().replaceFirst("[.][^.]+$", "");
        }
        fileChooser.setInitialFileName(initFileName);

        Stage stage = (Stage) landlordInfoText.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            filepath = new File(selectedFile.getAbsolutePath() + ".ser");
            profileFile = selectedFile;
        }

        return filepath;
    }

    @FXML
    public void loadProfile() {
        Landlord profile;
        File loadedFile = loadFilePath();

        if (loadedFile != null) {
            try {
                FileInputStream fileIn = new FileInputStream(loadedFile);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                profile = (Landlord) in.readObject();

                landlord = profile;

                updateProfilePanel();
                updateTenantListView();
                updateExpenseListView();
                billingAnchorPane.getChildren().clear();

                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found");
                c.printStackTrace();
            }
        }
    }

    private File loadFilePath() {
        File filepath = null;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serializable (*.ser)", "*.ser"));
        Stage stage = (Stage) landlordInfoText.getScene().getWindow();
        File savedFile = fileChooser.showOpenDialog(stage);

        if (savedFile != null) {
            filepath = savedFile;
            profileFile = savedFile;
        }

        return filepath;
    }

    @FXML
    public void closeWindow() {
        Stage stage = (Stage) landlordInfoText.getScene().getWindow();
        stage.close();
    }
}
