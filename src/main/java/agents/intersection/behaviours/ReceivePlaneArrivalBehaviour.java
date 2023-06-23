package agents.intersection.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Airport;
import model.params.AircraftToIntersectionParams;
import org.javatuples.Pair;
import org.joml.Vector2f;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static util.Constants.SIMULATION_WINDOW;
import static jade.lang.acl.ACLMessage.INFORM;

public class ReceivePlaneArrivalBehaviour extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(INFORM);

    private final Airport intersection;

    private final List<Pair<String, Long>> scheduledAircrafts = new ArrayList<>();

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
            try {
                System.out.println("[" + message.getSender() + "] is approaching me");

                final AircraftToIntersectionParams info =(AircraftToIntersectionParams) message.getContentObject();

                Airport secondIntersection =(Airport) Simulation.getScene().getObject(info.getPreviousIntersection());

                Vector2f positionStart = intersection.getPosition();
                Vector2f positionEnd = secondIntersection.getPosition();

                final float distance = positionStart.distance(positionEnd);

                float arrivalTime;
                long time = 1;
                float i = 1;
                for (; i != 0; i *= 0.99) {
                    arrivalTime = distance /(info.getMaxSpeed() * i);
                    time = System.currentTimeMillis() + (long)(arrivalTime * 1000);
                    if (checkForCollision(time))
                        break;
                }

                scheduledAircrafts.add(new Pair<>(message.getSender().getName(), time));

                sendMessage(message, info, i);

            } catch(UnreadableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ReceivePlaneArrivalBehaviour(Airport intersection) {
        this.intersection = intersection;
    }

    public static ReceivePlaneArrivalBehaviour create(Airport intersection) {
        return new ReceivePlaneArrivalBehaviour(intersection);
    }

    private boolean checkForCollision(final long time) {
        return scheduledAircrafts.stream().filter(
                aircraft -> time - SIMULATION_WINDOW <= aircraft.getValue1() || aircraft.getValue1() >= time + SIMULATION_WINDOW
        ).toList().isEmpty();
    }

    private void sendMessage(ACLMessage message, AircraftToIntersectionParams info, float i) {
        final ACLMessage response = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        response.addReceiver(message.getSender());
        response.setContent(Float.toString(info.getMaxSpeed() * i));
        myAgent.send(response);
    }

}
