package agents.segment.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import model.AirwaySegment;

import static jade.lang.acl.ACLMessage.INFORM;

public class CheckAircraftConditionBehavior extends TickerBehaviour {
    private AirwaySegment segment;

    @Override
    protected void onTick() {
        if (segment.isBroken()) {
            System.out.println(segment.getName() + " broke down");
            sendMessage();
            myAgent.addBehaviour(ManageAircraftMalfunctionBehaviour.create(myAgent, getPeriod(), segment));
            myAgent.removeBehaviour(this);
        }
    }

    public CheckAircraftConditionBehavior(Agent agent, long period, AirwaySegment simulationSegment) {
        super(agent, period);
        segment = simulationSegment;
    }

    public static CheckAircraftConditionBehavior create(Agent agent, long period, AirwaySegment simulationSegment) {
        return new CheckAircraftConditionBehavior(agent, period, simulationSegment);
    }

    private void sendMessage() {
        ACLMessage message = new ACLMessage(INFORM);
        message.addReceiver(new AID("airways_administrator", AID.ISLOCALNAME));
        message.setContent(segment.getName() + "; got Broken");
        myAgent.send(message);
    }
}
