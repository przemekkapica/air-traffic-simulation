package agents.airways_administrator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.params.AirwayRegistrationParams;
import planner.CentralizedPlanner;

import java.util.Objects;

import static jade.lang.acl.ACLMessage.ACCEPT_PROPOSAL;

public class AcceptAirwayBehaviour extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACCEPT_PROPOSAL);

    private CentralizedPlanner plan;

    public static AcceptAirwayBehaviour create(CentralizedPlanner airwayPlan) {
        return new AcceptAirwayBehaviour(airwayPlan);
    }

    private AcceptAirwayBehaviour(CentralizedPlanner airwayPlan) {
        plan = airwayPlan;
    }
    @Override
    public void action() {

        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message))
        {
            try {
                AirwayRegistrationParams aircraft = (AirwayRegistrationParams) message.getContentObject();
                plan.acceptRoute(aircraft.getRoute(), aircraft.getName(), aircraft.getMaxSpeed());
            } catch (UnreadableException e) {
                throw new RuntimeException(e);
            }


        }
    }
}