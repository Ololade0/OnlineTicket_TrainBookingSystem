package OnlineBookingSystem.OnlineBookingSystem.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;
@Embeddable

public class Fare {
    private Double adultPrices;
    private Double minorPrices;
    public Fare() {
    }


    public Fare(Double adultPrices, Double minorPrices) {
        this.adultPrices = adultPrices;
        this.minorPrices = minorPrices;
    }

    public Fare(Fare fare) {

    }


    public Double getAdultPrices() {
        return adultPrices;
    }

    public void setAdultPrices(Double adultPrices) {
        this.adultPrices = adultPrices;
    }

    public Double getMinorPrices() {
        return minorPrices;
    }

    public void setMinorPrices(Double minorPrices) {
        this.minorPrices = minorPrices;
    }
}



