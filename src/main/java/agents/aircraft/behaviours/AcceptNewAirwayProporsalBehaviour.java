package agents.aircraft.behaviours;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Aircraft;
import model.params.AirwayParams;
import planner.AirwaysManager;

import java.io.IOException;
import java.util.Objects;

import static jade.lang.acl.ACLMessage.*;

public class AcceptNewAirwayProporsalBehaviour extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(PROPOSE);
    private final Aircraft aircraft;
    private String destination;
    private AirwaysManager.RoutePriority priority;


    public AcceptNewAirwayProporsalBehaviour(Aircraft aircraft, String destination, AirwaysManager.RoutePriority priority) {
        this.aircraft = aircraft;
        this.destination = destination;
        this.priority = priority;
    }

    public static AcceptNewAirwayProporsalBehaviour create(Aircraft aircraft, String destination, AirwaysManager.RoutePriority priority) {
        return new AcceptNewAirwayProporsalBehaviour(aircraft,  destination, priority);
    }


    @Override
    public void action() {

        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message))
        {

            ACLMessage disqualifyRoute = new ACLMessage(INFORM_IF);
            disqualifyRoute.addReceiver(new AID("planner", AID.ISLOCALNAME));
            disqualifyRoute.setContent(aircraft.getName());
            myAgent.send(disqualifyRoute);

            aircraft.setSpeed(0);
            aircraft.setColor(9,143,53);
            AirwayParams responseParams = new AirwayParams( aircraft.getMaxSpeed(), aircraft.getPreviousIntersection().getName(), destination, priority);
            ACLMessage response = new ACLMessage(CONFIRM);

            try {
                response.setContentObject(responseParams);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            response.addReceiver(message.getSender());
            myAgent.send(response);
            aircraft.setRoadStable(false);

        }
    }
}
