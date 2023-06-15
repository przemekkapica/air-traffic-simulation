package agents.airways_administrator.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.params.AirwayParams;
import planner.CentralizedPlanner;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static jade.lang.acl.ACLMessage.CONFIRM;
import static jade.lang.acl.ACLMessage.PROPAGATE;

public class ProposeNewAirwayBehaviour extends CyclicBehaviour {

    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(CONFIRM);

    private CentralizedPlanner plan;

    public static ProposeNewAirwayBehaviour create(CentralizedPlanner airwayPlan) {
        return new ProposeNewAirwayBehaviour(airwayPlan);
    }

    private ProposeNewAirwayBehaviour(CentralizedPlanner airwayPlan) {
        plan = airwayPlan;
    }
    @Override
    public void action() {

        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message))
        {
            try {
                AirwayParams aircraftParams = (AirwayParams) message.getContentObject();
                List<String> newRoute = plan.findRoute(aircraftParams.getBeginning(), aircraftParams.getEnd(), aircraftParams.getMaxSpeed(), aircraftParams.getPriority());
                ACLMessage routeProposal = new ACLMessage(PROPAGATE);
                routeProposal.addReceiver(message.getSender());
                routeProposal.setContentObject((Serializable) newRoute);
                myAgent.send(routeProposal);
            } catch (UnreadableException | IOException e) {
                throw new RuntimeException(e);
            }


        }
    }
}
