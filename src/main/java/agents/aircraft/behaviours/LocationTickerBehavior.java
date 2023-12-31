
package agents.aircraft.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import model.ui_elements.Aircraft;

import static jade.lang.acl.ACLMessage.INFORM;

public class LocationTickerBehavior extends TickerBehaviour {
    private final Aircraft aircraft;

    public LocationTickerBehavior(Agent agent, Aircraft aircraft, long period) {
        super(agent, period);
        this.aircraft = aircraft;
    }

    public static LocationTickerBehavior create(Agent agent, Aircraft aircraft, long period) {
        return new LocationTickerBehavior(agent, aircraft, period);
    }

    @Override
    protected void onTick() {
        ACLMessage locationMessage = new ACLMessage(INFORM);
        locationMessage.setContent(aircraft.position.toString()); // sending position as a string
        locationMessage.addReceiver(new AID("airways_administrator", AID.ISLOCALNAME));
        myAgent.send(locationMessage);
    }
}
