package awgdl.lessE.models;

import java.time.LocalDate;

public class Income extends AccountEntry {
    public Income(LocalDate readingDate) {
        super(readingDate);
    }

    public Income(LocalDate readingDate, double cost) {
        super(readingDate, cost);
    }

    public Income(LocalDate readingDate, double cost, String description) {
        super(readingDate, cost, description);
    }
}
