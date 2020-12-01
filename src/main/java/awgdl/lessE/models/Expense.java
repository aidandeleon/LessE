package awgdl.lessE.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Expense extends AccountEntry implements Serializable {

    public Expense(LocalDate readingDate) {
        super(readingDate);
        super.setEntryType("Expense");
    }

    public Expense(LocalDate readingDate, double cost) {
        super(readingDate, cost);
        super.setEntryType("Expense");
    }

    public Expense(LocalDate readingDate, double cost, String description) {
        super(readingDate, cost, description);
        super.setEntryType("Expense");
    }

}
