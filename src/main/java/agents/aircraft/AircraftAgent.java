package agents.aircraft;

import agents.aircraft.behaviours.*;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import model.ui_elements.Aircraft;
import simulation.Simulation;

import static util.Constants.PASSING;

public class AircraftAgent extends Agent {
    private String finalDestination;

    @Override
    protected void setup() {
        super.setup();

        final Object[] params = getArguments();
        if (params.length < 4) {
            System.out.println("Usage [aircraft name], [starting point], [pair <segment name, airport name>]");
            doDelete();
        }
        String aircraftName = params[0].toString();

        Aircraft aircraft = (Aircraft)Simulation.getScene().getObject(aircraftName);
        //aircraft.pos.x

        setSegmentsAndDestination(params, aircraft);

        final DFAgentDescription description = new DFAgentDescription();
        description.setName(getAID());

        addServicesForSegments(aircraft, description);

        addBehaviors(aircraft, description);
    }

    private void addServicesForSegments(Aircraft aircraft, DFAgentDescription description) {
        for (String segment : aircraft.segments) {
            final ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setType(PASSING);
            serviceDescription.setName(segment);
            description.addServices(serviceDescription);
        }
        try {
            DFService.register(this, description);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    private void setSegmentsAndDestination(Object[] params, Aircraft aircraft) {
        for (int i = 2; i < params.length; ++i) {
            if (i % 2 == 0) {
                aircraft.airports.add(params[i].toString());
                finalDestination = params[i].toString();
            }
            else
                aircraft.segments.add(params[i].toString());
        }
    }

    private void addBehaviors(Aircraft aircraft, DFAgentDescription description) {
        addBehaviour(ForwardArrivalInfoToAirportBehavior.create(aircraft));
        addBehaviour(ConfigureVelocityBehavior.create(this, aircraft, 1000));
        addBehaviour(TakeOffBehavior.create(aircraft));
        addBehaviour(AcceptNewAirwayProposalBehavior.create(aircraft, finalDestination));
        addBehaviour(SetNewAirwayBehavior.create(aircraft, description));
        addBehaviour(LocationTickerBehavior.create(this, aircraft, 1000));
        addBehaviour(AdjustAltitudeBehavior.create(aircraft));
    }
}
