package agents.atc.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.ui_elements.Airport;
import model.params.AircraftToAirportParams;
import org.javatuples.Pair;
import org.joml.Vector2f;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.List;

import static util.Constants.SIMULATION_WINDOW;
import static jade.lang.acl.ACLMessage.INFORM;

public class ReceivePlaneArrivalBehavior extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(INFORM);
    private final Airport airport;
    private final List<Pair<String, Long>> scheduledAircrafts = new ArrayList<>();

    public ReceivePlaneArrivalBehavior(Airport airport) {
        this.airport = airport;
    }

    public static ReceivePlaneArrivalBehavior create(Airport airport) {
        return new ReceivePlaneArrivalBehavior(airport);
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(messageTemplate);

        if (message != null) {
            try {
                System.out.println("[" + message.getSender() + "] is approaching me");

                AircraftToAirportParams info = (AircraftToAirportParams) message.getContentObject();

                Airport secondAirport = (Airport) Simulation.getScene().getObject(info.getPreviousAirport());

                Vector2f positionStart = airport.getPosition();
                Vector2f positionEnd = secondAirport.getPosition();

                float distance = positionStart.distance(positionEnd);

                float arrivalTime;
                long time = 1;
                float i = 1;
                for (; i != 0; i *= 0.99) {
                    arrivalTime = distance / (info.getMaxSpeed() * i);
                    time = System.currentTimeMillis() + (long) (arrivalTime * 1000);
                    if (checkForCollision(time))
                        break;
                }

                scheduledAircrafts.add(new Pair<>(message.getSender().getName(), time));

                sendMessage(message, info, i);

            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean checkForCollision(final long time) {
        return scheduledAircrafts.stream().filter(
                aircraft -> time - SIMULATION_WINDOW <= aircraft.getValue1() || aircraft.getValue1() >= time + SIMULATION_WINDOW
        ).toList().isEmpty();
    }

    private void sendMessage(ACLMessage message, AircraftToAirportParams info, float i) {
        ACLMessage response = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        response.addReceiver(message.getSender());
        response.setContent(Float.toString(info.getMaxSpeed() * i));
        myAgent.send(response);
    }
}
