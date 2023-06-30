package agents.aircraft.behaviours;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.ui_elements.Aircraft;
import model.params.AirwayParams;

import java.io.IOException;
import java.util.Objects;

import static jade.lang.acl.ACLMessage.*;

public class AcceptNewAirwayProposalBehavior extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(PROPOSE);
    private final Aircraft aircraft;
    private final String destination;

    public AcceptNewAirwayProposalBehavior(Aircraft aircraft, String destination) {
        this.aircraft = aircraft;
        this.destination = destination;
    }

    public static AcceptNewAirwayProposalBehavior create(Aircraft aircraft, String destination) {
        return new AcceptNewAirwayProposalBehavior(aircraft, destination);
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(messageTemplate);

        if (message != null) {
            sendMessage();

            aircraft.setSpeed(0);
            aircraft.setColor(9, 143, 53);

            AirwayParams responseParams = new AirwayParams(
                    aircraft.getMaxSpeed(),
                    aircraft.getPreviousAirport().getName(),
                    destination
            );

            ACLMessage response = new ACLMessage(CONFIRM);

            try {
                response.setContentObject(responseParams);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            response.addReceiver(message.getSender());
            myAgent.send(response);
            aircraft.setRoadStable(false);
        }
    }

    private void sendMessage() {
        ACLMessage disqualifyRoute = new ACLMessage(INFORM_IF);
        disqualifyRoute.addReceiver(new AID("airways_administrator", AID.ISLOCALNAME));
        disqualifyRoute.setContent(aircraft.getName());
        myAgent.send(disqualifyRoute);
    }
}

