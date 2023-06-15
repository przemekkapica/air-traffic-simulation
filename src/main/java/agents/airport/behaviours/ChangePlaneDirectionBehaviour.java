package agents.airport.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.AirwayIntersection;

import java.util.Objects;

import static util.Constants.FINAL_STATION;
import static jade.lang.acl.ACLMessage.AGREE;

public class ChangePlaneDirectionBehaviour extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(AGREE);

    private final AirwayIntersection intersection;

    public ChangePlaneDirectionBehaviour(AirwayIntersection intersection) {
        this.intersection = intersection;
    }

    public static ChangePlaneDirectionBehaviour create(AirwayIntersection intersection) {
        return new ChangePlaneDirectionBehaviour(intersection);
    }

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
            final ACLMessage response = new ACLMessage(ACLMessage.CONFIRM);
            final String segment = message.getContent();

            if (segment.equals(FINAL_STATION))
            {
                System.out.println("[" + myAgent.getLocalName() + "] This aircraft will be stopping here ");
                response.setContent(FINAL_STATION);
            }
            else
            {
                intersection.setNextSegmentByName(segment);
                System.out.println("[" + myAgent.getLocalName() + "] Switching to " + segment);
                response.setContent("Not final station");
            }
            response.addReceiver(message.getSender());
            myAgent.send(response);


        }
    }
}
