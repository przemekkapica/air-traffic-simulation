package agents.aircraft.behaviours;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import model.AirwayIntersection;
import model.Aircraft;
import model.params.AircraftToIntersectionParams;
import simulation.Simulation;

import java.io.IOException;
import java.util.Objects;

import static util.Constants.FINAL_STATION;
import static jade.lang.acl.ACLMessage.CONFIRM;
import static jade.lang.acl.ACLMessage.INFORM_IF;

public class ForwardArrivalInfoToIntersectionBehaviour extends CyclicBehaviour {
    private final Aircraft aircraft;
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(CONFIRM);

    public ForwardArrivalInfoToIntersectionBehaviour(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    public static ForwardArrivalInfoToIntersectionBehaviour create(Aircraft aircraft) {
        return new ForwardArrivalInfoToIntersectionBehaviour(aircraft);
     }

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message) && aircraft.isRoadStable()) {

            if (message.getContent().equals(FINAL_STATION))
            {
                aircraft.setSpeed(0);
                aircraft.setColor(9,143,53);
                ACLMessage disqualifyRoute = new ACLMessage(INFORM_IF);
                disqualifyRoute.addReceiver(new AID("planner", AID.ISLOCALNAME));
                disqualifyRoute.setContent(aircraft.getName());
                myAgent.send(disqualifyRoute);
                myAgent.doDelete();
            }
            else
            {
                System.out.println("sending message to next intersection:" + aircraft.intersections.peek());
                final ACLMessage proposal = new ACLMessage(ACLMessage.INFORM);
                AircraftToIntersectionParams messageContent = new AircraftToIntersectionParams();
                messageContent.setMaxSpeed(aircraft.getMaxSpeed());
                messageContent.setPreviousIntersection(aircraft.getPreviousIntersection().getName());

                AirwayIntersection last = (AirwayIntersection) Simulation.getScene().getObject(aircraft.intersections.peek());
                aircraft.setPreviousIntersection(last);
                proposal.addReceiver(new AID(aircraft.intersections.remove(), AID.ISLOCALNAME));

                try {
                    proposal.setContentObject(messageContent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                myAgent.send(proposal);
            }
        }
    }
}
