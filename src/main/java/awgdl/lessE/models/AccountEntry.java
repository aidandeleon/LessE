package awgdl.lessE.models;

import java.io.Serializable;
import java.time.LocalDate;

public class AccountEntry implements Serializable {
    private LocalDate date;
    private double cost;
    private String description;
    private String entryType;

    public AccountEntry(LocalDate date) {
        this.date = date;
        cost = 0.0;
        description = "";
        entryType = "AccountEntry";
    }

    public AccountEntry(LocalDate date, double cost) {
        this.date = date;
        this.cost = cost;
        this.description = "";
        entryType = "AccountEntry";
    }

    public AccountEntry(LocalDate date, double cost, String description) {
        this.date = date;
        this.cost = cost;
        this.description = description;
        entryType = "AccountEntry";
    }

    public AccountEntry(LocalDate date, double cost, String description, String entryType) {
        this.date = date;
        this.cost = cost;
        this.description = description;
        this.entryType = entryType;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    @Override
    public String toString() {
        return  date + "\t" + cost + "\n" + description;
    }

    public String toStringDetail() {
        return getDate() + "\n" + getDescription();
    }

    public String toStringCost() {
        return Double.toString(getCost());
    }

}
