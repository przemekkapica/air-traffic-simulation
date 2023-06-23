package agents.intersection.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.Airport;

import java.util.Objects;

import static util.Constants.FINAL_DESTINATION;
import static jade.lang.acl.ACLMessage.AGREE;

public class ChangePlaneDirectionBehaviour extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(AGREE);

    private final Airport intersection;

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
            final ACLMessage response = new ACLMessage(ACLMessage.CONFIRM);
            final String segment = message.getContent();

            checkIfFinalDestinationAndSetResponse(response, segment);

            sendMessage(message, response);
        }
    }

    public ChangePlaneDirectionBehaviour(Airport intersection) {
        this.intersection = intersection;
    }

    public static ChangePlaneDirectionBehaviour create(Airport intersection) {
        return new ChangePlaneDirectionBehaviour(intersection);
    }

    private void checkIfFinalDestinationAndSetResponse(ACLMessage response, String segment) {
        if (segment.equals(FINAL_DESTINATION)) {
            System.out.println("[" + myAgent.getLocalName() + "] This aircraft will be stopping here ");
            response.setContent(FINAL_DESTINATION);
        }
        else  {
            intersection.setNextSegmentByName(segment);
            System.out.println("[" + myAgent.getLocalName() + "] Switching to " + segment);
            response.setContent("Not final station");
        }
    }

    private void sendMessage(ACLMessage message, ACLMessage response) {
        response.addReceiver(message.getSender());
        myAgent.send(response);
    }
}
