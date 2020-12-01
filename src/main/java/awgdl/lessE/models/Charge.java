package awgdl.lessE.models;

import java.io.Serializable;

public class Charge implements Serializable {
    private String name;
    private double cost;

    public Charge(String name, double cost) {
        this.name = name;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
