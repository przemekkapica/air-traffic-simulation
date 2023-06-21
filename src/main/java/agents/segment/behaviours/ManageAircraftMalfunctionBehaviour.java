package agents.segment.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import model.AirwaySegment;

import static jade.lang.acl.ACLMessage.INFORM;

public class ManageAircraftMalfunctionBehaviour extends TickerBehaviour {
    private AirwaySegment segment;

    @Override
    protected void onTick() {
        if(!segment.isBroken()) {
            sendMessage();

            myAgent.addBehaviour(CheckAircraftConditionBehavior.create(myAgent, getPeriod(), segment));
            myAgent.removeBehaviour(this);
        }
    }

    public ManageAircraftMalfunctionBehaviour(Agent a, long period, AirwaySegment simulationSegment) {
        super(a, period);
        segment = simulationSegment;
    }

    public static ManageAircraftMalfunctionBehaviour create(Agent agent, long period, AirwaySegment simulationSegment) {
        return new ManageAircraftMalfunctionBehaviour(agent, period, simulationSegment);
    }

    private void sendMessage() {
        System.out.println(segment.getName() + " was repaired");
        ACLMessage message = new ACLMessage(INFORM);
        message.addReceiver(new AID("planner", AID.ISLOCALNAME));
        message.setContent(segment.getName()  + "; got Repaired");
        myAgent.send(message);
    }
}