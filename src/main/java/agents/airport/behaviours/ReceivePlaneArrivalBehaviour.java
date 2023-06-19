package agents.airport.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.AirwayIntersection;
import model.params.AircraftToIntersectionParams;
import org.javatuples.Pair;
import org.joml.Vector2f;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static util.Constants.SIMULATION_WINOW;
import static jade.lang.acl.ACLMessage.INFORM;

public class ReceivePlaneArrivalBehaviour extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(INFORM);
    private final AirwayIntersection intersection;

    private final List<Pair<String, Long>> scheduledAircrafts = new ArrayList<>();



    public ReceivePlaneArrivalBehaviour(AirwayIntersection intersection) {
        this.intersection = intersection;
    }

    public static ReceivePlaneArrivalBehaviour create(AirwayIntersection intersection) {
        return new ReceivePlaneArrivalBehaviour(intersection);
    }

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message)) {
            try {
                System.out.println("[" + message.getSender() + "] is approaching me");
                final AircraftToIntersectionParams info =(AircraftToIntersectionParams) message.getContentObject();

                AirwayIntersection secondIntersection =(AirwayIntersection) Simulation.getScene().getObject(info.getPreviousIntersection());

                Vector2f positionStart = intersection.getPosition();
                Vector2f positionEnd = secondIntersection.getPosition();

                final float distance = positionStart.distance(positionEnd);

                float arrivalTime;
                long time = 1;
                float i = 1;
                for (; i != 0; i *= 0.99) {
                    arrivalTime = distance /(info.getMaxSpeed() * i);
                    time = System.currentTimeMillis() +(long)(arrivalTime * 1000);
                    if (checkForCollision(time))
                        break;
                }
                scheduledAircrafts.add(new Pair<>(message.getSender().getName(), time));
                final ACLMessage response = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                response.addReceiver(message.getSender());
                response.setContent(Float.toString(info.getMaxSpeed() * i));
                myAgent.send(response);

            } catch(UnreadableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean checkForCollision(final long time) {
        return scheduledAircrafts.stream().filter(aircraft -> time - SIMULATION_WINOW <= aircraft.getValue1() || aircraft.getValue1() >= time + SIMULATION_WINOW).toList().isEmpty();
    }

}
