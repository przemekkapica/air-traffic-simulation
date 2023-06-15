package agents.segment;

import agents.segment.behaviours.CheckAircraftConditionBehavious;
import jade.core.Agent;
import model.AirwaySegment;
import simulation.Simulation;


public class SegmentAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();

        final Object[] params = getArguments();
        if (params.length < 1) {
            System.out.println("Usage [segment name ]");
            doDelete();
        }
        String name = params[0].toString();
        AirwaySegment segment = (AirwaySegment) Simulation.getScene().getObject("segment_" + name);
        addBehaviour(CheckAircraftConditionBehavious.create(this, 1000, segment));
    }
}
