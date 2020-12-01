package awgdl.lessE.models;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Bill extends AccountEntry implements Serializable {

    private LocalDate billingPeriodStart;
    private LocalDate billingPeriodEnd;
    private ArrayList<Charge> charges;

    public Bill(LocalDate readingDate) {
        super(readingDate);
        super.setEntryType("Bill");
    }

    public Bill(LocalDate readingDate, double cost) {
        super(readingDate, cost);
        super.setEntryType("Bill");
    }

    public Bill(LocalDate readingDate, double cost, String description) {
        super(readingDate, cost, description);
        super.setEntryType("Bill");
    }

    public Bill(LocalDate readingDate, double cost, String description, LocalDate billingPeriodStart, LocalDate billingPeriodEnd, ArrayList<Charge> charges) {
        super(readingDate, cost, description);
        super.setEntryType("Bill");

        this.billingPeriodStart = billingPeriodStart;
        this.billingPeriodEnd = billingPeriodEnd;
        this.charges = charges;
    }

    public LocalDate getBillingPeriodStart() {
        return billingPeriodStart;
    }

    public void setBillingPeriodStart(LocalDate billingPeriodStart) {
        this.billingPeriodStart = billingPeriodStart;
    }

    public LocalDate getBillingPeriodEnd() {
        return billingPeriodEnd;
    }

    public void setBillingPeriodEnd(LocalDate billingPeriodEnd) {
        this.billingPeriodEnd = billingPeriodEnd;
    }

    public ArrayList<Charge> getCharges() {
        return charges;
    }

    public void setCharges(ArrayList<Charge> charges) {
        this.charges = charges;
    }

    public void addCharge(String chargeName, double chargeCost) {
        this.charges.add(new Charge(chargeName, chargeCost));
    }

    public void deleteCharge(int index) {
        this.charges.remove(index);
    }

    public String toStringDetailed() {
        return super.getDescription() + "\n" +
                super.getDate() + "\n" +
                super.getCost() + "\n" +
                getBillingPeriodStart() + "\n" +
                getBillingPeriodEnd() + "\n" +
                getCharges().get(0).getCost() + "\n" +
                getCharges().get(0).getName() + "\n";
    }
}

