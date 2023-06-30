
package agents.aircraft.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.ui_elements.Aircraft;

import java.util.Objects;

import static jade.lang.acl.ACLMessage.INFORM;

public class AdjustAltitudeBehavior extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(INFORM);
    private final Aircraft aircraft;

    public AdjustAltitudeBehavior(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public static AdjustAltitudeBehavior create(Aircraft aircraft) {
        return new AdjustAltitudeBehavior(aircraft);
    }

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message) && message.getContent().equals("Too close")) {
            aircraft.adjustAltitude();

        }
    }
}
