package awgdl.lessE.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Landlord implements Serializable {
    private String firstName;
    private String lastName;
    private ArrayList<Tenant> tenants;
    private ArrayList<Expense> expenses;
    private double balance;

    public Landlord() {
        this.firstName = "User";
        this.lastName = "Default";
        this.tenants = new ArrayList<Tenant>();
        this.expenses = new ArrayList<Expense>();
        balance = 0;
    }

    public Landlord(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tenants = new ArrayList<Tenant>();
        this.expenses = new ArrayList<Expense>();
        balance = 0;
    }

    public Landlord(String firstName, String lastName, double balance) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.tenants = new ArrayList<Tenant>();
        this.expenses = new ArrayList<Expense>();
        this.balance = balance;
    }

    public String getFirstName() {
        return this.firstName;
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

    public ArrayList<Tenant> getTenants() {
        return this.tenants;
    }

    public void setTenants(ArrayList<Tenant> tenants) {
        this.tenants = tenants;
    }

    public ArrayList<Expense> getExpenses() {
        return this.expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    public double getBalance() {
        return this.balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void addTenant(Tenant tenant) {
        tenants.add(tenant);
    }

    public void removeTenant(int index) {
        tenants.remove(index);
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
        calculateBalance();
    }

    public void editExpense(int index, LocalDate date, String description, double amount) {
        Expense expense = this.expenses.get(index);

        expense.setDate(date);
        expense.setDescription(description);
        expense.setCost(amount);

        calculateBalance();
    }

    public void removeExpense(int index) {
        this.expenses.remove(index);
        calculateBalance();
    }

    private void calculateBalance() {
        double total = 0;
        for (AccountEntry expense : this.expenses) {
            total += expense.getCost();
        }
        setBalance(total);
    }

    @Override
    public String toString() {
        return "Tenants:\n" + tenants.size() +
                "\n\nExpenses:\n" + expenses.size() +
                "\n\nBalance:\n" + String.format("%.2f", balance);
    }
}
