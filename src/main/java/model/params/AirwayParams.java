package model.params;

import planner.CentralizedPlanner;

import java.io.Serializable;

public class AirwayParams implements Serializable {
    private final float maxSpeed;
    private final String beginning;
    private final String end;

    private final CentralizedPlanner.RoutePriority priority;

    public AirwayParams(float maxSpeed, String beginning, String end, CentralizedPlanner.RoutePriority priority) {
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

    public CentralizedPlanner.RoutePriority getPriority() {
        return priority;
    }
}
