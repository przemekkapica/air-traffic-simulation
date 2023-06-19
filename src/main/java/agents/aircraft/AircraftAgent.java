package agents.aircraft;

import agents.aircraft.behaviours.*;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import model.Aircraft;
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
            System.out.println("Usage [aircraft name], [priority], [starting station], [pair <segment name, intersection name>]");
            doDelete();
        }
        String aircraftName = params[0].toString();
        Aircraft aircraft =(Aircraft) Simulation.getScene().getObject(aircraftName);
        String priorityName = params[1].toString();

        AirwaysManager.RoutePriority priority;
        switch (priorityName) {
            case "DISTANCE" -> priority = AirwaysManager.RoutePriority.DISTANCE;
            case "COST" -> priority = AirwaysManager.RoutePriority.COST;
            case "LOAD" -> priority = AirwaysManager.RoutePriority.LOAD;
            default -> priority = AirwaysManager.RoutePriority.DEFAULT;
        }

        for (int i = 2; i < params.length; ++i) {
            if (i % 2 == 0) {
                aircraft.intersections.add(params[i].toString());
                finalDestination = params[i].toString();
            }
            else
                aircraft.segments.add(params[i].toString());
        }

        final DFAgentDescription description = new DFAgentDescription();
        description.setName(getAID());

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


        addBehaviour(ForwardArrivalInfoToIntersectionBehaviour.create(aircraft));
        addBehaviour(ConfigureVelocityBehaviour.create(aircraft));
        addBehaviour(TakeOffBehaviour.create(aircraft));
        addBehaviour(AcceptNewAirwayProporsalBehaviour.create(aircraft, finalDestination, priority));
        addBehaviour(SetNewAirwayBehaviour.create(aircraft, description));





    }
}
