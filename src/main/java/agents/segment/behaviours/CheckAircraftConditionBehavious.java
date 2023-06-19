package agents.segment.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import model.AirwaySegment;

import static jade.lang.acl.ACLMessage.INFORM;

public class CheckAircraftConditionBehavious extends TickerBehaviour {
    private AirwaySegment segment;
    public CheckAircraftConditionBehavious(Agent a, long period, AirwaySegment simulationSegment) {
        super(a, period);
        segment = simulationSegment;
    }

    public static CheckAircraftConditionBehavious create(Agent agent, long period, AirwaySegment simulationSegment) {
        return new CheckAircraftConditionBehavious(agent, period, simulationSegment);
    }
    @Override
    protected void onTick() {

        if(segment.isBroken()) {
            System.out.println(segment.getName() + " broke down");
            ACLMessage message = new ACLMessage(INFORM);
            message.addReceiver(new AID("planner", AID.ISLOCALNAME));
            message.setContent(segment.getName() + "; got Broken");
            myAgent.send(message);
            myAgent.addBehaviour(ManageAircraftMalfunctionBehaviour.create(myAgent, getPeriod(), segment));
            myAgent.removeBehaviour(this);
        }
    }
}
