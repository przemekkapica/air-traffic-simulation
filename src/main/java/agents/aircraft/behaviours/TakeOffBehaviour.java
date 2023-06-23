package agents.aircraft.behaviours;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import model.Airport;
import model.Aircraft;
import model.params.AirwayRegistrationParams;
import model.params.AircraftToIntersectionParams;
import simulation.Simulation;

import java.io.IOException;

import static jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL;
import static simulation.Simulation.getScene;

public class TakeOffBehaviour extends OneShotBehaviour {

    private final Aircraft aircraft;

    public TakeOffBehaviour(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public static TakeOffBehaviour create(Aircraft aircraft) {
        return new TakeOffBehaviour(aircraft);
    }

    @Override
    public void action() {
        try {
            sendMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        aircraft.setRoadStable(true);
        aircraft.accelerate();
        //aircraft.ascend();

        Airport intersection = (Airport) Simulation.getScene().getObject(aircraft.intersections.remove());
        aircraft.setPreviousIntersection(intersection);
        intersection.setNextSegmentByName(aircraft.segments.remove());

        System.out.println("Sending message to next intersection:" + aircraft.intersections.peek());

        final ACLMessage proposal = new ACLMessage(ACLMessage.INFORM);
        AircraftToIntersectionParams messageContent = new AircraftToIntersectionParams();
        messageContent.setMaxSpeed(aircraft.getMaxSpeed());
        messageContent.setPreviousIntersection(aircraft.getPreviousIntersection().getName());
        aircraft.setPreviousIntersection((Airport) getScene().getObject(aircraft.intersections.peek()));

        proposal.addReceiver(new AID(aircraft.intersections.remove(), AID.ISLOCALNAME));
        try {
            proposal.setContentObject(messageContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        myAgent.send(proposal);

    }

    private void sendMessage() throws IOException {
        ACLMessage message = new ACLMessage(ACCEPT_PROPOSAL);
        message.addReceiver(new AID("airways_administrator", AID.ISLOCALNAME));
        AirwayRegistrationParams plannerParams = new AirwayRegistrationParams(aircraft.intersections.stream().toList(), aircraft.getName(), aircraft.getMaxSpeed());
        message.setContentObject(plannerParams);
        myAgent.send(message);
    }
}
