package model.params;

import java.io.Serializable;

public class AircraftToAirportParams implements Serializable {
    private String previousAirport;
    private Float maxSpeed;

    public String getPreviousAirport() {
        return previousAirport;
    }

    public void setPreviousAirport(String previousAirport) {
        this.previousAirport = previousAirport;
    }

    public Float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
