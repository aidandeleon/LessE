package awgdl.lessE.controllers;

import awgdl.lessE.models.*;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;

public class BillingWindowController {
    private Tenant tenant;
    private AccountEntry entry;

    @FXML
    private DatePicker readingDateDatePicker;

    @FXML
    private DatePicker billPeriodStartDatePicker;

    @FXML
    private DatePicker billPeriodEndDatePicker;

    @FXML
    private TextField totalAmountTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ListView<VBox> chargesListView;

    @FXML
    private TextField chargeNameTextField;

    @FXML
    private TextField chargeCostTextField;

    @FXML
    private Button pdfGeneratorButton;

    public void initDataAdd(Tenant tenant, boolean isBill) {
        this.tenant = tenant;

        if (isBill) {
            confirmButton.setText("Add Bill");
            confirmButton.setOnAction(e -> { addNewBill(); });

            pdfGeneratorButton.setVisible(false);
            pdfGeneratorButton.setDisable(true);

        } else {
            confirmButton.setText("Add");
            confirmButton.setOnAction(e -> { addNewPayment(); });
        }
    }

    public void initDataPaymentEdit(Tenant tenant, Payment payment) {
        this.tenant = tenant;
        this.entry = payment;

        confirmButton.setText("Apply");
        confirmButton.setOnAction(e -> { addNewPayment(); });

        readingDateDatePicker.setValue(entry.getDate());
        totalAmountTextField.setText(Double.toString(entry.getCost()));
        descriptionTextField.setText(entry.getDescription());
    }

    public void initDataBillEdit(Tenant tenant, Bill bill) {
        this.tenant = tenant;
        this.entry = bill;

        confirmButton.setText("Apply");
        confirmButton.setOnAction(e -> { addNewBill(); });

        readingDateDatePicker.setValue(entry.getDate());
        totalAmountTextField.setText(Double.toString(entry.getCost()));
        descriptionTextField.setText(entry.getDescription());

        billPeriodStartDatePicker.setValue(bill.getBillingPeriodStart());
        billPeriodEndDatePicker.setValue(bill.getBillingPeriodEnd());

        for (Charge charge : bill.getCharges()) {
            Text chargeNameText = new Text(charge.getName());
            Text chargeCostText = new Text(Double.toString(charge.getCost()));
            chargesListView.getItems().add(new VBox(chargeNameText, chargeCostText));
        }
    }

    public AccountEntry getEntry() {
        return entry;
    }

    public void setEntry(AccountEntry entry) {
        this.entry = entry;
    }

    @FXML
    public void addNewBill() {
        addNewBillingEntry(true);
    }

    @FXML
    public void addNewPayment() {
        addNewBillingEntry(false);
    }

    private void addNewBillingEntry(boolean isBill) {
        clearInputFields(isBill);

        if (isInvalidEntry(isBill)) {
            showBillingInputError(isBill);

        } else {
            if (isBill) {
                setEntry(createBill());
            } else {
                setEntry(createPayment());
            }

            closeWindow();
        }
    }

    private void clearInputFields(boolean isBill) {
        readingDateDatePicker.setPromptText("");
        readingDateDatePicker.setStyle("");
        totalAmountTextField.setPromptText("");
        totalAmountTextField.setStyle("");

        if (isBill) {
            billPeriodStartDatePicker.setPromptText("");
            billPeriodEndDatePicker.setPromptText("");
        }
    }

    @FXML
    public void addChargeToList() {
        String chargeName = chargeNameTextField.getText();
        String chargeCost = chargeCostTextField.getText();

        if (!isEmptyChargeInputs() && !isNonFloat(chargeCost)) {
            Text chargeNameText = new Text(chargeName);
            Text chargeCostText = new Text(chargeCost);

            chargesListView.getItems().add(new VBox(chargeNameText, chargeCostText));
            clearChargeInputs();

        } else {
            String errors = "";

            if (chargeName.equals("")) {
                errors += "Charge Name: No name entered.\n";
            }

            if (chargeCost.equals("")) {
                errors += "Charge Cost: No name entered.\n";

            } else {
                if (isNonFloat(chargeCost)) {
                    errors += "Charge Cost: Please enter a positive decimal value.\n";
                }
            }

            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Charge Input Error");
            errorAlert.setContentText(errors);
            errorAlert.showAndWait();
        }
    }

    

    @FXML
    public void removeChargeFromList() {
        if (chargesListView.getSelectionModel() != null) {
            int selectedIndex = chargesListView.getSelectionModel().getSelectedIndex();
            chargesListView.getItems().remove(selectedIndex);
        }
    }

    private void clearChargeInputs() {
        chargeNameTextField.setText("");
        chargeCostTextField.setText("");
    }

    private boolean isEmptyChargeInputs() {
        return chargeNameTextField.getText().equals("") || chargeCostTextField.getText().equals("");
    }

    @FXML
    public void calculateChargeTotal() {
        double total = 0;

        for (VBox chargeBox : chargesListView.getItems()) {
            Text chargeCostText = (Text) chargeBox.getChildren().get(1);
            double chargeCost = Double.parseDouble(chargeCostText.getText());

            total += chargeCost;
        }

        totalAmountTextField.setText(Double.toString(total));
    }

    private Bill createBill() {
        LocalDate readingDate = readingDateDatePicker.getValue();

        LocalDate billPeriodStart = billPeriodStartDatePicker.getValue();
        LocalDate billPeriodEnd = billPeriodEndDatePicker.getValue();

        double totalAmount = Double.parseDouble(totalAmountTextField.getText());
        String description= descriptionTextField.getText();

        ArrayList<Charge> charges = new ArrayList<>();

        for (VBox chargeBox : chargesListView.getItems()) {

            Text chargeNameText = (Text) chargeBox.getChildren().get(0);
            Text chargeCostText = (Text) chargeBox.getChildren().get(1);

            charges.add(new Charge(chargeNameText.getText(), Double.parseDouble(chargeCostText.getText())));
        }

        return new Bill(readingDate, totalAmount, description, billPeriodStart, billPeriodEnd, charges);
    }

    private Payment createPayment() {
        LocalDate date = readingDateDatePicker.getValue();
        String amount = totalAmountTextField.getText();
        String description= descriptionTextField.getText();

        double amountDouble = Double.parseDouble(amount);

        return new Payment(date, amountDouble, description);
    }

    private void showBillingInputError(boolean isBill) {
        String errors = "";

        if (readingDateDatePicker.getValue() == null) {
            errors += "Reading Date: No date entered.\n";
        }

        if (totalAmountTextField.getText() == null || totalAmountTextField.getText().equals("")) {
            errors += "Total Amount: No amount entered\n";

        }

        if (totalAmountTextField.getText() == null || totalAmountTextField.getText().equals("")) {
            errors += "Total Amount: No total entered.\n";
        } else {
            if (isNonFloat(totalAmountTextField.getText())) {
                totalAmountTextField.clear();
                errors += "Total Amount: Please enter a positive decimal value.\n";
            }
        }

        if (isBill) {
            if (billPeriodStartDatePicker.getValue() != null && billPeriodEndDatePicker.getValue() != null) {
                if (billPeriodStartDatePicker.getValue().compareTo(billPeriodEndDatePicker.getValue()) > 0) { //start date must be earlier than end date
                    billPeriodStartDatePicker.setValue(null);
                    billPeriodEndDatePicker.setValue(null);
                    errors += "Bill Period Dates: Start Date must be earlier than End Date.\n";
                }

            } else {
                if (billPeriodStartDatePicker.getValue() == null) {
                    errors += "Start Date: No date entered.\n";
                }

                if (billPeriodEndDatePicker.getValue() == null) {
                    errors += "End Date: No date entered.\n";
                }
            }
        }

        if (!errors.equals("")) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText("Billing Input Error");
            errorAlert.setContentText(errors);
            errorAlert.showAndWait();
        }
    }

    private boolean isInvalidEntry(boolean isBill) {
        LocalDate readingDate = readingDateDatePicker.getValue();
        String amount = totalAmountTextField.getText();

        if (isBill) {
            LocalDate billPeriodStart = billPeriodStartDatePicker.getValue();
            LocalDate billPeriodEnd = billPeriodEndDatePicker.getValue();
            return readingDate == null
                    || billPeriodStart == null
                    || billPeriodEnd == null
                    || billPeriodStart.compareTo(billPeriodEnd) > 0 //invalid if start date is more recent
                    || isNonFloat(amount);
        }

        return readingDate == null || isNonFloat(amount);
    }

    private boolean isNonFloat(String input) {
        return !input.matches("^\\d*\\.?\\d+$");
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void showPdfGeneratorWindow() throws FileNotFoundException {
        Bill bill = (Bill) this.entry;

        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        File fileDestination = fileChooser.showSaveDialog(stage);

        if (fileDestination != null) {
            PdfWriter writer = new PdfWriter(fileDestination.getAbsolutePath() + ".pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph(tenant.getFullName()).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph(tenant.getAddress()).setTextAlignment(TextAlignment.CENTER));
            document.add(new Paragraph("\n"));

            document.add(new Paragraph(bill.getDescription() + "\n"));
            document.add(new Paragraph("Billing Period:\t\t"
                    + bill.getBillingPeriodStart().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
                    + "\t\tto\t\t" + bill.getBillingPeriodEnd().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)) + "\n"));
            document.add(new Paragraph("Reading Date:\t\t" + bill.getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))));
            document.add(new Paragraph("Current Charges:"));

            float [] pointColumnWidths = { 300F, 150F };
            Table table = new Table(pointColumnWidths);
            table.setMarginLeft(20);

            for (Charge charge : bill.getCharges()) {
                Paragraph nameParagraph = new Paragraph(charge.getName());
                Paragraph costParagraph = new Paragraph(tenant.getCurrency() + " \t" + String.format("%.2f", charge.getCost()));

                table.addCell(new Cell().add(nameParagraph));
                table.addCell(new Cell().add(costParagraph));
            }
            document.add(table);

            Paragraph totalParagraph = new Paragraph("Total Amount Due:\t\t" + tenant.getCurrency() + "\t" + String.format("%.2f", bill.getCost()));
            totalParagraph.setBold();
            document.add(totalParagraph);

            document.close();

            closeWindow();
        }
    }
}
