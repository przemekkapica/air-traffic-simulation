package agents.airways_administrator;

import agents.airways_administrator.behaviours.*;
import util.Constants;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import planner.AirwaysManager;
import util.GraphDescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AirwaysAdministratorAgent extends Agent {
    private final List<String> segments = new ArrayList<>();
    private final List<String> costs = new ArrayList<>();

    @Override
    protected void setup() {
        super.setup();
        extractParams();
        registerAsPlanner();

        Map<String, List<AirwaysManager.RouteDescription>> graphDescription = GraphDescriptor.describeAirway(segments, costs);
        AirwaysManager planner = new AirwaysManager(graphDescription);

        addBehaviours(planner);
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

    private void registerAsPlanner() {
        DFAgentDescription dfDesc = new DFAgentDescription();
        dfDesc.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(Constants.SERVICE_PLANNER);
        serviceDescription.setType(Constants.SERVICE_PLANNER);

        dfDesc.addServices(serviceDescription);

        try {
            DFService.register(this, dfDesc);
        } catch (FIPAException exception) {
            exception.printStackTrace();
        }
    }

    private void addBehaviours(AirwaysManager planner) {
        addBehaviour(ProposeNewAirwayBehavior.create(planner));
        addBehaviour(AcceptAirwayBehavior.create(planner));
        addBehaviour(HandlePlaneArrivalBehavior.create(planner));
        addBehaviour(CheckProximityBehavior.create(planner));
    }

    private void extractParams() {
        Object[] params = getArguments();
        if (params.length < 2) {
            System.out.println("Usage: [planner name], [pairs of segments and costs separated by commas]");
            doDelete();
        }
        for (int i = 1; i < params.length; ++i) {
            if (i % 2 != 0)
                segments.add(params[i].toString());
            else
                costs.add(params[i].toString());
        }
    }
}

