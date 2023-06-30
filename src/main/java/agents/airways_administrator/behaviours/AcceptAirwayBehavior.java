package agents.airways_administrator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.params.AirwayRegistrationParams;
import planner.AirwaysManager;

import java.util.Objects;

import static jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL;

public class AcceptAirwayBehavior extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACCEPT_PROPOSAL);

    private final AirwaysManager airwaysManager;

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
            try {
                AirwayRegistrationParams aircraft = (AirwayRegistrationParams)message.getContentObject();

                airwaysManager.acceptRoute(aircraft.route(), aircraft.name());
            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static AcceptAirwayBehavior create(AirwaysManager manager) {
        return new AcceptAirwayBehavior(manager);
    }

    private AcceptAirwayBehavior(AirwaysManager manager) {
        airwaysManager = manager;
    }
}