package agents.atc;

import util.Constants;
import agents.atc.behaviours.ChangePlaneDirectionBehaviour;
import agents.atc.behaviours.ReceivePlaneArrivalBehaviour;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import model.ui_elements.Airport;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.List;

public class ATCAgent extends Agent {
    final private List<String> outgoing = new ArrayList<>();

    @Override
    protected void setup() {
        super.setup();

        Airport airport = extractParams();

        DFAgentDescription dfDesc = new DFAgentDescription();
        dfDesc.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setName(Constants.SERVICE_AIRPORT);
        serviceDescription.setType(Constants.SERVICE_AIRPORT);

        dfDesc.addServices(serviceDescription);

        try {
            DFService.register(this, dfDesc);
        } catch (FIPAException exception) {
            exception.printStackTrace();
        }

        addBehaviors(airport);
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

    private void addBehaviors(Airport airport) {
        addBehaviour(ReceivePlaneArrivalBehaviour.create(airport));
        addBehaviour(ChangePlaneDirectionBehaviour.create(airport));
    }

    private Airport extractParams() {
        final Object[] params = getArguments();
        if (params.length < 2) {
            System.out.println("Usage [airport name ], [airports separated by comas]");
            doDelete();
        }
        String airportName = params[0].toString();
        Airport airport = (Airport) Simulation.getScene().getObject(airportName);

        for (int i = 1; i < params.length; ++i) {
            outgoing.add(params[i].toString());
        }
        return airport;
    }
}
