package agents.intersection;

import util.Constants;
import agents.intersection.behaviours.ChangePlaneDirectionBehaviour;
import agents.intersection.behaviours.ReceivePlaneArrivalBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import model.AirwayIntersection;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.List;

public class IntersectionAgent extends Agent {
    final private List<String> outgoing = new ArrayList<>();

    @Override
    protected void setup() {
        super.setup();

        AirwayIntersection intersection = extractParams();

        DFAgentDescription dfDesc = new DFAgentDescription();
        dfDesc.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(Constants.SERVICE_INTERSECTION);
        serviceDescription.setType(Constants.SERVICE_INTERSECTION);

        dfDesc.addServices(serviceDescription);

        try {
            DFService.register(this, dfDesc);
        } catch (FIPAException exception) {
            exception.printStackTrace();
        }

        addBehaviors(intersection);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fipaException) {
            fipaException.printStackTrace();
        }

        super.takeDown();
    }

    private void addBehaviors(AirwayIntersection intersection) {
        addBehaviour(ReceivePlaneArrivalBehaviour.create(intersection));
        addBehaviour(ChangePlaneDirectionBehaviour.create(intersection));
    }

    private AirwayIntersection extractParams() {
        final Object[] params = getArguments();
        if (params.length < 2) {
            System.out.println("Usage [intersection name ], [intersections separated by comas]");
            doDelete();
        }
        String intersectionName = params[0].toString();
        AirwayIntersection intersection = (AirwayIntersection) Simulation.getScene().getObject(intersectionName);

        for (int i = 1; i < params.length; ++i) {
            outgoing.add(params[i].toString());
        }
        return intersection;
    }
}
