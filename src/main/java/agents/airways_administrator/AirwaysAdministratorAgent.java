package agents.airways_administrator;

import util.Constants;
import agents.airways_administrator.behaviours.AcceptAirwayBehaviour;
import agents.airways_administrator.behaviours.HandleMalfunctionBehaviour;
import agents.airways_administrator.behaviours.HandlePlaneArrivalBehaviour;
import agents.airways_administrator.behaviours.ProposeNewAirwayBehaviour;
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
    final private List<String> segments = new ArrayList<>();
    final private List<String> costs = new ArrayList<>();

    @Override
    protected void setup() {
        super.setup();

        extractParams();

        // register self as planner
        DFAgentDescription dfDesc = new DFAgentDescription();
        dfDesc.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription ();
        serviceDescription.setName(Constants.SERVICE_PLANNER);
        serviceDescription.setType(Constants.SERVICE_PLANNER);

        dfDesc.addServices(serviceDescription);

        try {
            DFService.register (this, dfDesc);
        } catch (FIPAException exception) {
            exception.printStackTrace ();
        }

        Map<String, List<AirwaysManager.RouteDescription>> graphDescription = GraphDescriptor.describeAirway(segments, costs);
        AirwaysManager planner = new AirwaysManager(graphDescription);

        addBehaviors(planner);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister (this);
        } catch (FIPAException fipaException) {
            fipaException.printStackTrace ();
        }

        super.takeDown ();
    }

    private void addBehaviors(AirwaysManager planner) {
        addBehaviour(HandleMalfunctionBehaviour.create(planner));
        addBehaviour(ProposeNewAirwayBehaviour.create(planner));
        addBehaviour(AcceptAirwayBehaviour.create(planner));
        addBehaviour(HandlePlaneArrivalBehaviour.create(planner));
    }

    private void extractParams() {
        final Object[] params = getArguments();
        if (params.length < 2) {
            System.out.println("Usage [planner name ], [pairs of segments and costs separated by comas]");
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
