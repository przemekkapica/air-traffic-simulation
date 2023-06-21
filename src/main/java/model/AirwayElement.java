package model;

import simulation.SimulationObject;

import java.util.HashSet;
import java.util.Set;

public abstract class AirwayElement extends SimulationObject {
    private final Set<Aircraft> aircrafts;

    public abstract float getLength();
    public abstract AirwayElement getNextFragment();

    protected AirwayElement(String name) {
        super(name);
        aircrafts = new HashSet<>();
    }

    void enter(Aircraft aircraft) {
        aircrafts.add(aircraft);
    }

    void leave(Aircraft aircraft) {
        if (aircrafts.contains (aircraft)) {
            aircrafts.remove(aircraft);
        } else {
            throw new RuntimeException(String.format("aircraft %s was not in %s%n", aircraft.getName(), getName()));
        }
    }
}
