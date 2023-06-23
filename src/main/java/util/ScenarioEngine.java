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
    private final ContainerController containerController;

    public ScenarioEngine() throws StaleProxyException {
        Runtime runtime = Runtime.instance();
        Profile profile = new ProfileImpl("localhost", 8888, null);
        containerController = runtime.createMainContainer(profile);
    }

    public void runScenario() throws FileNotFoundException, StaleProxyException {
        Scanner sc = new Scanner(new File("src/main/resources/scenarios/scenario.csv"));
        sc.useDelimiter(";");
        while(sc.hasNext())
        {
            String agentClass = sc.next();
            switch (agentClass) {
                case "segment" -> CreateSegment(sc.next());
                case "intersection" -> CreateIntersection(sc.next());
                case "aircraft" -> CreateAircraft(sc.next());
                case "airways_administrator" -> CreatePlanner(sc.next());
            }
        }
        sc.close();
    }

    private void CreateSegment(String agentParams) throws StaleProxyException {
        Object[] params = GetAgentParams(agentParams);
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.segment.SegmentAgent", params ).start();
    }

    private void CreateIntersection(String agentParams) throws StaleProxyException {
        Object[] params = GetAgentParams(agentParams);
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.intersection.IntersectionAgent", params ).start();
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
        return agentParams.split(",");
    }

}

