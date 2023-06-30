package agents.airways_administrator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import planner.AirwaysManager;

import java.util.Objects;

import static jade.lang.acl.ACLMessage.INFORM_IF;

public class HandlePlaneArrivalBehavior extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(INFORM_IF);

    private final AirwaysManager airwaysManager;

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
            String aircraftName = message.getContent();
            airwaysManager.notifyAircraftArrived(aircraftName);
        }
    }

    public static HandlePlaneArrivalBehavior create(AirwaysManager airwayPlan) {
        return new HandlePlaneArrivalBehavior(airwayPlan);
    }

    private HandlePlaneArrivalBehavior(AirwaysManager airwaysManager) {
        this.airwaysManager = airwaysManager;
    }
}