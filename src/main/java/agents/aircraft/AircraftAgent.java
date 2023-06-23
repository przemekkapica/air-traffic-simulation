package agents.aircraft;

import agents.aircraft.behaviours.*;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import model.ui_elements.Aircraft;
import planner.AirwaysManager;
import simulation.Simulation;

import static util.Constants.PASSING;

public class AircraftAgent extends Agent {
    private String finalDestination;

    @Override
    protected void setup() {
        super.setup();

        final Object[] params = getArguments();
        if (params.length < 4) {
            System.out.println("Usage [aircraft name], [priority], [starting point], [pair <segment name, airport name>]");
            doDelete();
        }
        String aircraftName = params[0].toString();
        String priorityName = params[1].toString();

        Aircraft aircraft = (Aircraft)Simulation.getScene().getObject(aircraftName);
        //aircraft.pos.x

        AirwaysManager.AirwayPriority priority = getPriority(priorityName);

        setSegmentsAndDestination(params, aircraft);

        final DFAgentDescription description = new DFAgentDescription();
        description.setName(getAID());

        addServicesForSegments(aircraft, description);

        addBehaviors(aircraft, priority, description);
    }

    private static AirwaysManager.AirwayPriority getPriority(String priorityName) {
        return switch (priorityName) {
            case "DISTANCE" -> AirwaysManager.AirwayPriority.DISTANCE;
            case "COST" -> AirwaysManager.AirwayPriority.COST;
            case "LOAD" -> AirwaysManager.AirwayPriority.LOAD;
            default -> AirwaysManager.AirwayPriority.DEFAULT;
        };
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

    private void addBehaviors(Aircraft aircraft, AirwaysManager.AirwayPriority priority, DFAgentDescription description) {
        addBehaviour(ForwardArrivalInfoToAirportBehavior.create(aircraft));
        addBehaviour(ConfigureVelocityBehaviour.create(aircraft));
        addBehaviour(TakeOffBehaviour.create(aircraft));
        addBehaviour(AcceptNewAirwayProposalBehaviour.create(aircraft, finalDestination, priority));
        addBehaviour(SetNewAirwayBehaviour.create(aircraft, description));
    }
}
