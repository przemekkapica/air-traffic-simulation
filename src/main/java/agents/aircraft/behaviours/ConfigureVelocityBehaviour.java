package agents.aircraft.behaviours;

import agents.aircraft.AircraftAgent;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.ui_elements.Aircraft;

import java.util.Objects;

import static util.Constants.FINAL_DESTINATION;
import static jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL;

public class ConfigureVelocityBehaviour extends TickerBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACCEPT_PROPOSAL);
    private final Aircraft aircraft;

    public ConfigureVelocityBehaviour(Agent agent, Aircraft aircraft, long period) {
        super(agent, period);
        this.aircraft = aircraft;
    }

    public static Behaviour create(AircraftAgent agent, Aircraft aircraft, int period) {
        return new ConfigureVelocityBehaviour(agent, aircraft, period);
    }


//    @Override
//    public void action() {
//
//    }

    @Override
    protected void onTick() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
//            float speed = Float.parseFloat(message.getContent());
//          aircraft.setSpeed(Float.parseFloat(speed));
//            aircraft.accelerate(speed);

            while (!aircraft.isTraversingSegment()) {
                aircraft.alternate();
            }
            while (aircraft.isTraversingSegment()) {aircraft.alternate();}

            ACLMessage response = new ACLMessage(ACLMessage.AGREE);
            response.addReceiver(message.getSender());
            if (aircraft.segments.isEmpty()) {
                response.setContent(FINAL_DESTINATION);
            } else {
                response.setContent(aircraft.segments.remove());
            }
            myAgent.send(response);
        }
    }
}
