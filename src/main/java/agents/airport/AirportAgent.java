package agents.airport;

import util.Constants;
import agents.airport.behaviours.ChangePlaneDirectionBehaviour;
import agents.airport.behaviours.ReceivePlaneArrivalBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import model.AirwayIntersection;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.List;

public class AirportAgent extends Agent {
    private List<String>  outgoing = new ArrayList<>();

    @Override
    protected void setup() {
        super.setup();

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

        // register self as intersection
        DFAgentDescription dfDesc = new DFAgentDescription ();
        dfDesc.setName (getAID ());

        ServiceDescription sd = new ServiceDescription ();
        sd.setName (Constants.SERVICE_INTERSECTION);
        sd.setType (Constants.SERVICE_INTERSECTION);

        dfDesc.addServices (sd);

        try {
            DFService.register (this, dfDesc);
        } catch (FIPAException exception) {
            exception.printStackTrace ();
        }

        addBehaviour(ReceivePlaneArrivalBehaviour.create(intersection));
        addBehaviour(ChangePlaneDirectionBehaviour.create(intersection));
    }

    @Override
    protected void takeDown () {
        try {
            DFService.deregister (this);
        } catch (FIPAException fipaException) {
            fipaException.printStackTrace ();
        }

        super.takeDown ();
    }
}
