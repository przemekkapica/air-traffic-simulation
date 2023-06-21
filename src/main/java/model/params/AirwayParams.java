package model.params;

import planner.AirwaysManager;

import java.io.Serializable;

public class AirwayParams implements Serializable {
    private final float maxSpeed;
    private final String beginning;
    private final String end;

    private final AirwaysManager.AirwayPriority priority;

    public AirwayParams(float maxSpeed, String beginning, String end, AirwaysManager.AirwayPriority priority) {
        this.maxSpeed = maxSpeed;
        this.beginning = beginning;
        this.end = end;
        this.priority = priority;
    }


    public float getMaxSpeed() {
        return maxSpeed;
    }

    public String getBeginning() {
        return beginning;
    }

    public String getEnd() {
        return end;
    }

    public AirwaysManager.AirwayPriority getPriority() {
        return priority;
    }
}
