package agents.aircraft.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import model.ui_elements.Airport;
import model.ui_elements.Aircraft;
import model.params.AirwayRegistrationParams;
import model.params.AircraftToAirportParams;
import simulation.Simulation;

import java.io.IOException;

import static jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL;
import static simulation.Simulation.getScene;

public class TakeOffBehavior extends OneShotBehaviour {
    private final Aircraft aircraft;

    public TakeOffBehavior(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public static TakeOffBehavior create(Aircraft aircraft) {
        return new TakeOffBehavior(aircraft);
    }

    @Override
    public void action() {
        try {
            sendMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        aircraft.setRoadStable(true);
        aircraft.takeoff();

        Airport airport = (Airport) Simulation.getScene().getObject(aircraft.airports.remove());
        aircraft.setPreviousAirport(airport);
        airport.setNextSegmentByName(aircraft.segments.remove());

        System.out.println("Sending message to next airport: " + aircraft.airports.peek());

        ACLMessage proposal = createProposalMessage();

        try {
            proposal.setContentObject(createMessageContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        myAgent.send(proposal);
    }

    private void sendMessage() throws IOException {
        ACLMessage message = new ACLMessage(ACCEPT_PROPOSAL);
        message.addReceiver(new AID("airways_administrator", AID.ISLOCALNAME));
        AirwayRegistrationParams plannerParams = new AirwayRegistrationParams(aircraft.airports.stream().toList(), aircraft.getName(), aircraft.getMaxSpeed());
        message.setContentObject(plannerParams);
        myAgent.send(message);
    }

    private ACLMessage createProposalMessage() {
        ACLMessage proposal = new ACLMessage(ACLMessage.INFORM);
        proposal.addReceiver(new AID(aircraft.airports.remove(), AID.ISLOCALNAME));
        return proposal;
    }

    private AircraftToAirportParams createMessageContent() {
        AircraftToAirportParams messageContent = new AircraftToAirportParams();
        messageContent.setMaxSpeed(aircraft.getMaxSpeed());
        messageContent.setPreviousAirport(aircraft.getPreviousAirport().getName());
        aircraft.setPreviousAirport((Airport) getScene().getObject(aircraft.airports.peek()));
        return messageContent;
    }
}

