package model;

import simulation.SimulationObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public abstract class AirwayFragment extends SimulationObject {
    private final Set<Aircraft> m_aircrafts;

    public abstract float getLength();
    public abstract AirwayFragment getNextFragment();

    protected AirwayFragment(String name) {
        super(name);
        m_aircrafts = new HashSet<>();
    }

    void enter(Aircraft aircraft) {
        m_aircrafts.add(aircraft);
    }

    void leave(Aircraft aircraft) {
        if (m_aircrafts.contains (aircraft)) {
            m_aircrafts.remove(aircraft);
        } else {
            throw new RuntimeException(String.format("aircraft %s was not in %s%n", aircraft.getName(), getName()));
        }
    }
}
