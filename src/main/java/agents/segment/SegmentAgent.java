package agents.segment;

import agents.segment.behaviours.CheckAircraftConditionBehavior;
import jade.core.Agent;
import model.ui_elements.Airway;
import simulation.Simulation;


public class SegmentAgent extends Agent {

    @Override
    protected void setup() {
        super.setup();

        final Object[] params = getArguments();
        if(params.length < 1) {
            System.out.println("Usage [segment name ]");
            doDelete();
        }
        String name = params[0].toString();
        Airway segment =(Airway) Simulation.getScene().getObject("segment_" + name);
        addBehaviour(CheckAircraftConditionBehavior.create(this, 1000, segment));
    }
}
