package awgdl.lessE.models;

import java.io.Serializable;
import java.time.LocalDate;

public class Payment extends AccountEntry implements Serializable {

    public Payment(LocalDate readingDate) {
        super(readingDate);
        super.setEntryType("Payment");
    }

    public Payment(LocalDate readingDate, double cost) {
        super(readingDate, cost);
        super.setEntryType("Payment");
    }

    public Payment(LocalDate readingDate, double cost, String description) {
        super(readingDate, cost, description);
        super.setEntryType("Payment");
    }

}
