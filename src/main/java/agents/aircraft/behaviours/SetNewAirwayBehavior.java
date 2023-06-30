package agents.aircraft.behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import model.ui_elements.Aircraft;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static util.Constants.PASSING;
import static jade.lang.acl.ACLMessage.PROPAGATE;

public class SetNewAirwayBehavior extends CyclicBehaviour {
    private final MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(PROPAGATE);
    private final Aircraft aircraft;
    private DFAgentDescription oldDescription;

    public SetNewAirwayBehavior(Aircraft aircraft, DFAgentDescription description) {
        this.aircraft = aircraft;
        this.oldDescription = description;
    }

    public static SetNewAirwayBehavior create(Aircraft aircraft, DFAgentDescription description) {
        return new SetNewAirwayBehavior(aircraft, description);
    }

    @Override
    public void action() {
        ACLMessage message = myAgent.receive(messageTemplate);

        if (message != null) {
            try {
                List<String> route = (List<String>) message.getContentObject();
                if (route.contains("Not_Found")) {
                    return; // This message is displayed if there is no route available for our aircraft
                }

                setNewAirway(route);
            } catch (UnreadableException | FIPAException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void setNewAirway(List<String> route) throws FIPAException {
        aircraft.airports = new LinkedList<>(route);
        aircraft.segments = new LinkedList<>(parseAirway(route));
        aircraft.setColor(0, 180, 0);
        myAgent.addBehaviour(TakeOffBehavior.create(aircraft));

        DFService.deregister(myAgent, oldDescription);

        DFAgentDescription description = new DFAgentDescription();
        description.setName(myAgent.getAID());

        for (String segment : aircraft.segments) {
            ServiceDescription serviceDescription = new ServiceDescription();
            serviceDescription.setType(PASSING);
            serviceDescription.setName(segment);
            description.addServices(serviceDescription);
        }

        DFService.register(myAgent, description);
        oldDescription = description;
    }

    private List<String> parseAirway(List<String> airway) {
        List<String> segments = new ArrayList<>();
        for (int i = 0; i < airway.size() - 1; ++i) {
            String airwayCurrentPart = airway.get(i).split("_")[1];
            String airwayFollowingPart = airway.get(i + 1).split("_")[1];
            segments.add(airwayCurrentPart + "-" + airwayFollowingPart);
        }
        return segments;
    }
}
