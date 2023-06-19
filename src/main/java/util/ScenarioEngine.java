package util;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class ScenarioEngine {
    private Runtime runtime;
    private Profile profile;
    private ContainerController containerController;

    public ScenarioEngine() throws StaleProxyException {
        runtime = Runtime.instance();
        profile = new ProfileImpl("localhost", 8888, null);
        containerController = runtime.createMainContainer(profile);


    }


    public void runScenario(int scenario_id) throws FileNotFoundException, StaleProxyException {


        Scanner sc = new Scanner(new File("src/main/resources/scenario" + scenario_id +".csv"));
        sc.useDelimiter(";");
        while(sc.hasNext())
        {
            String agentClass = sc.next();
            if (agentClass.equals("segment")) {
                CreateSegment(sc.next());
            }
            else if (agentClass.equals("intersection")) {
                CreateIntersection(sc.next());
            }
            else if (agentClass.equals("aircraft")) {
                CreateAircraft(sc.next());
            }
            else if (agentClass.equals("planner")) {
                CreatePlanner(sc.next());
            }

        }
        sc.close();
        //containerController.createNewAgent("Gui Guy", "jade.tools.rma.rma", null).start();

    }

    private void CreateSegment(String agentParams) throws StaleProxyException {
        Object[] params = GetAgentParams(agentParams);
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.segment.SegmentAgent", params ).start();

    }

    private void CreateIntersection(String agentParams) throws StaleProxyException {
        Object[] params = GetAgentParams(agentParams);
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.airport.AirportAgent", params ).start();
    }
    private void CreateAircraft(String agentParams) throws StaleProxyException {
        Object[] params = GetAgentParams(agentParams);
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.aircraft.AircraftAgent", params ).start();
    }
    private void CreatePlanner(String agentParams) throws StaleProxyException {
        Object[] params = GetAgentParams(agentParams);
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.airways_administrator.AirwaysAdministratorAgent", params ).start();
    }

    private Object[] GetAgentParams(String agentParams) {
        return agentParams.split("\\,");
    }

}

