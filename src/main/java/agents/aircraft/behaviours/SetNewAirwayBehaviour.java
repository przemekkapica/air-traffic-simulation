package agents.aircraft.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.Aircraft;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static util.Constants.PASSING;
import static jade.lang.acl.ACLMessage.PROPAGATE;


public class SetNewAirwayBehaviour extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(PROPAGATE);
    Aircraft aircraft;
    DFAgentDescription oldDescription;

    @Override
    public void action() {
        final ACLMessage message = myAgent.receive(messageTemplate);

        if (Objects.nonNull(message))
        {
            try {
                List<String> route = (List<String>) message.getContentObject();
                if (route.contains("Not_Found")) {
                    return; //this message is displayed if there is no route available for our aircraft
                }

                aircraft.intersections = new LinkedList<>(route);
                aircraft.segments = new LinkedList<>(parseAirway(route));
                aircraft.setColor(0,180,0);
                myAgent.addBehaviour(TakeOffBehaviour.create(aircraft));

                DFService.deregister(myAgent, oldDescription);

                final DFAgentDescription description = new DFAgentDescription();
                description.setName(myAgent.getAID());

                for (String segment : aircraft.segments) {
                    final ServiceDescription serviceDescription = new ServiceDescription();
                    serviceDescription.setType(PASSING);
                    serviceDescription.setName(segment);
                    description.addServices(serviceDescription);

                }
                DFService.register(myAgent, description);
                oldDescription = description;
            } catch (UnreadableException | FIPAException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static SetNewAirwayBehaviour create(Aircraft aircraft, DFAgentDescription description) {
        return new SetNewAirwayBehaviour(aircraft, description);
    }

    public SetNewAirwayBehaviour(Aircraft aircraft, DFAgentDescription description) {
        this.aircraft = aircraft;
        oldDescription = description;
    }

    private List<String> parseAirway(List<String> airway) {
        List<String> segments = new ArrayList<>();
        for (int i = 0; i < airway.size() - 1 ; ++i) {
            var airwayCurrentPart = airway.get(i).split("_")[1];
            var airwayFollowingPart = airway.get(i + 1).split("_")[1];

            segments.add(airwayCurrentPart + "-" + airwayFollowingPart);
        }
        return segments;
    }
}