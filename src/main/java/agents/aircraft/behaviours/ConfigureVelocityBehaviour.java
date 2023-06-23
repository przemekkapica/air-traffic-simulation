package agents.aircraft.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.ui_elements.Aircraft;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static util.Constants.FINAL_DESTINATION;
import static jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL;

public class ConfigureVelocityBehaviour extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACCEPT_PROPOSAL);
    private final Aircraft aircraft;
    private float speed = 0;

    public ConfigureVelocityBehaviour(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public static ConfigureVelocityBehaviour create(Aircraft aircraft) {
        return new ConfigureVelocityBehaviour(aircraft);
    }

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
            System.out.println("They know I'm coming. Time to wait now");

            float maxSpeed = Float.parseFloat(message.getContent());
            aircraft.setSpeed(speed+200);

//            while (speed < maxSpeed) {
//                aircraft.setSpeed(speed+10);
//                try {
//                    TimeUnit.MILLISECONDS.sleep(50);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }


            while (!aircraft.isTraversingSegment()) {System.out.print("");}
            while (aircraft.isTraversingSegment()) {System.out.print("");}

            ACLMessage response =  new ACLMessage(ACLMessage.AGREE);
            response.addReceiver(message.getSender());
            if (aircraft.segments.isEmpty())
                response.setContent(FINAL_DESTINATION);
            else
                response.setContent(aircraft.segments.remove());
            myAgent.send(response);

        }
    }
}
