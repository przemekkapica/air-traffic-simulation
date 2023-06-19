package agents.segment.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import model.AirwaySegment;

import static jade.lang.acl.ACLMessage.INFORM;

public class ManageAircraftMalfunctionBehaviour extends TickerBehaviour {
    private AirwaySegment segment;

    public ManageAircraftMalfunctionBehaviour(Agent a, long period, AirwaySegment simulationSegment) {
        super(a, period);
        segment = simulationSegment;
    }

    public static ManageAircraftMalfunctionBehaviour create(Agent agent, long period, AirwaySegment simulationSegment) {
        return new ManageAircraftMalfunctionBehaviour(agent, period, simulationSegment);
    }
    @Override
    protected void onTick() {

        if(!segment.isBroken()) {
            System.out.println(segment.getName() + " was repaired");
            ACLMessage message = new ACLMessage(INFORM);
            message.addReceiver(new AID("planner", AID.ISLOCALNAME));
            message.setContent(segment.getName()  + "; got Repaired");
            myAgent.send(message);

            myAgent.addBehaviour(CheckAircraftConditionBehavious.create(myAgent, getPeriod(), segment));
            myAgent.removeBehaviour(this);
        }
    }
}