package agents.airways_administrator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import planner.CentralizedPlanner;

import java.util.Objects;

import static jade.lang.acl.ACLMessage.INFORM_IF;

public class HandlePlaneArrivalBehaviour extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(INFORM_IF);

    private CentralizedPlanner plan;

    public static HandlePlaneArrivalBehaviour create(CentralizedPlanner airwayPlan) {
        return new HandlePlaneArrivalBehaviour(airwayPlan);
    }

    private HandlePlaneArrivalBehaviour(CentralizedPlanner airwayPlan) {
        plan = airwayPlan;
    }
    @Override
    public void action() {

        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message))
        {
            String aircraftName =  message.getContent();
            plan.notifyAircraftArrived(aircraftName);
        }
    }
}