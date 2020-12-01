package awgdl.lessE.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Tenant implements Serializable {
    private String firstName;
    private String lastName;
    private String address;
    private String currency;
    private double balance;

    private ArrayList<Bill> bills;
    private ArrayList<Payment> payments;

    public Tenant(String firstName, String lastName, String address, String currency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.currency = currency;
        this.balance = 0;
        this.bills = new ArrayList<>();
        this.payments = new ArrayList<>();
    }

    public Tenant(String firstName, String lastName, String address, String currency, Double balance, ArrayList<Bill> bills, ArrayList<Payment> payments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.currency = currency;
        this.balance = balance;
        this.bills = bills;
        this.payments = payments;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public ArrayList<Bill> getBills() {
        return bills;
    }

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addBillEntry(Bill newBilling) {
        bills.add(newBilling);
        setBalance(calculateBalance());
    }

    public void addPaymentEntry(Payment newPayment) {
        payments.add(newPayment);
        setBalance(calculateBalance());
    }

    public void updateBill(int index,
                           LocalDate readingDate,
                           LocalDate billPeriodStart,
                           LocalDate billPeriodEnd,
                           double cost,
                           String description,
                           ArrayList<Charge> charges) {
        Bill bill = bills.get(index);

        bill.setDate(readingDate);
        bill.setBillingPeriodStart(billPeriodStart);
        bill.setBillingPeriodEnd(billPeriodEnd);
        bill.setCost(cost);
        bill.setDescription(description);
        bill.setCharges(charges);

        setBalance(calculateBalance());
    }

    public void updatePayment(int index, LocalDate readingDate, double cost, String description) {
        Payment payment = payments.get(index);
        payment.setDate(readingDate);
        payment.setCost(cost);
        payment.setDescription(description);
        setBalance(calculateBalance());
    }

    public void removeBill(int index) {
        bills.remove(index);
        setBalance(calculateBalance());
    }

    public void removePayment(int index) {
        payments.remove(index);
        setBalance(calculateBalance());
    }

    private double calculateBalance() {
        double total = 0;

        for (Bill bill: this.bills) {
            total += bill.getCost();
        }

        for (Payment payment: this.payments) {
            total -= payment.getCost(); //payments deduct amount owed
        }

        return total;
    }

    @Override
    public String toString() {
        return "Name:\t\t" + this.getFullName()
                + "\nAddress:\t" + address
                + "\nBalance:\t" + currency + " " + String.format("%.2f", balance);
    }
}
