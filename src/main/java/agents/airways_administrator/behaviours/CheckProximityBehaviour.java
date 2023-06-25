
package agents.airways_administrator.behaviours;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.ui_elements.Aircraft;
import planner.AirwaysManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static jade.lang.acl.ACLMessage.INFORM;

public class CheckProximityBehaviour extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(INFORM);
    private final Map<AID, Aircraft> aircrafts = new HashMap<>();

    @Override
    public void action() {
        System.out.println("CheckProximityBehaviour");
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
            // AID of the message sender is the identifier of the Aircraft
            AID senderAID = message.getSender();
            Aircraft aircraft = aircrafts.get(senderAID);


            // The message content updates the current location of the aircraft
            if (aircraft != null) {
                aircraft.updatePositionFrom(message.getContent());
            } else {
                // Handle error - the aircraft was not found
                System.err.println("Aircraft not found: " + senderAID);
            }
            // check if this aircraft is too close to any other aircraft
            for (Aircraft other : aircrafts.values()) {

                if (!other.equals(aircraft) && other.isTooClose(aircraft)) {
                    // TODO: Handle the case when the two aircraft are too close
                    ACLMessage warning = new ACLMessage(INFORM);
                    warning.addReceiver(senderAID);
                    warning.setContent("Too close");
                    myAgent.send(warning);
                }
            }
        } else {
            block();
        }
    }

    public static CheckProximityBehaviour create() {
        return new CheckProximityBehaviour();
    }
    private CheckProximityBehaviour() {}
}

