package agents.aircraft.behaviours;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.ui_elements.Airport;
import model.ui_elements.Aircraft;
import model.params.AircraftToAirportParams;
import simulation.Simulation;

import java.io.IOException;
import java.util.Objects;

import static util.Constants.FINAL_DESTINATION;
import static jade.lang.acl.ACLMessage.CONFIRM;
import static jade.lang.acl.ACLMessage.INFORM_IF;

public class ForwardArrivalInfoToAirportBehavior extends CyclicBehaviour {
    private final Aircraft aircraft;
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(CONFIRM);

    public ForwardArrivalInfoToAirportBehavior(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public static ForwardArrivalInfoToAirportBehavior create(Aircraft aircraft) {
        return new ForwardArrivalInfoToAirportBehavior(aircraft);
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(messageTemplate);

        if (message != null && aircraft.isRoadStable()) {
            if (message.getContent().equals(FINAL_DESTINATION)) {
                handleFinalDestinationMessage();
            } else {
                sendNextAirportMessage();
            }
        }
    }

    private void handleFinalDestinationMessage() {
        aircraft.setSpeed(0);
        aircraft.setAltitude(0);
        aircraft.setColor(9, 143, 53);

        ACLMessage disqualifyRoute = new ACLMessage(INFORM_IF);
        disqualifyRoute.addReceiver(new AID("airways_administrator", AID.ISLOCALNAME));
        disqualifyRoute.setContent(aircraft.getName());
        myAgent.send(disqualifyRoute);
        myAgent.doDelete();
    }

    private void sendNextAirportMessage() {
        System.out.println("Sending message to next airport: " + aircraft.airports.peek());
        ACLMessage proposal = new ACLMessage(ACLMessage.INFORM);
        AircraftToAirportParams messageContent = createAircraftToAirportParams();

        Airport last = (Airport) Simulation.getScene().getObject(aircraft.airports.peek());
        aircraft.setPreviousAirport(last);
        proposal.addReceiver(new AID(aircraft.airports.remove(), AID.ISLOCALNAME));

        try {
            proposal.setContentObject(messageContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        myAgent.send(proposal);
    }

    private AircraftToAirportParams createAircraftToAirportParams() {
        AircraftToAirportParams messageContent = new AircraftToAirportParams();
        messageContent.setMaxSpeed(aircraft.getMaxSpeed());
        messageContent.setPreviousAirport(aircraft.getPreviousAirport().getName());
        return messageContent;
    }
}
