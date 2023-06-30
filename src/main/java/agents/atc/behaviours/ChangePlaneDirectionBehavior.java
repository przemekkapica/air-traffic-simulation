package agents.atc.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.ui_elements.Airport;

import java.util.Objects;

import static util.Constants.FINAL_DESTINATION;
import static jade.lang.acl.ACLMessage.AGREE;

public class ChangePlaneDirectionBehavior extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(AGREE);
    private final Airport airport;

    public ChangePlaneDirectionBehavior(Airport airport) {
        this.airport = airport;
    }

    public static ChangePlaneDirectionBehavior create(Airport airport) {
        return new ChangePlaneDirectionBehavior(airport);
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(messageTemplate);

        if (message != null) {
            ACLMessage response = new ACLMessage(ACLMessage.CONFIRM);
            String segment = message.getContent();

            checkIfFinalDestinationAndSetResponse(response, segment);
            sendMessage(message, response);
        }
    }

    private void checkIfFinalDestinationAndSetResponse(ACLMessage response, String segment) {
        if (segment.equals(FINAL_DESTINATION)) {
            System.out.println("[" + myAgent.getLocalName() + "] This aircraft will be stopping here");
            response.setContent(FINAL_DESTINATION);
        } else {
            airport.setNextSegmentByName(segment);
            System.out.println("[" + myAgent.getLocalName() + "] Switching to " + segment);
            response.setContent("Not final station");
        }
    }

    private void sendMessage(ACLMessage message, ACLMessage response) {
        response.addReceiver(message.getSender());
        myAgent.send(response);
    }
}
