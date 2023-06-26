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
        while(sc.hasNextLine())  {
            String data = sc.nextLine();

            int start = data.indexOf(";") + 1;
            int stop = data.indexOf(";", data.indexOf(";") + 1);
            String params = data.substring(start, stop);

            if (data.contains("segment")) {
                createSegment(params);
            } else if (data.contains("airport")) {
                createAirport(params);
            } else if (data.contains("aircraft")) {
                createAircraft(params);
            } else if (data.contains("administrator")) {
                createAdministrator(params);
            }
        }
        sc.close();
        //containerController.createNewAgent("Gui Guy", "jade.tools.rma.rma", null).start();
    }

    private void createSegment(String agentParams) throws StaleProxyException {
        Object[] params = getAgentParams(agentParams);
        System.out.println(Arrays.stream(params).findFirst().get().toString());
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.segment.SegmentAgent", params).start();
    }

    private void createAirport(String agentParams) throws StaleProxyException {
        Object[] params = getAgentParams(agentParams);
        System.out.println(Arrays.stream(params).findFirst().get().toString());
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.atc.ATCAgent", params).start();
    }
    private void createAircraft(String agentParams) throws StaleProxyException {
        Object[] params = getAgentParams(agentParams);
        System.out.println(Arrays.stream(params).findFirst().get().toString());
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.aircraft.AircraftAgent", params).start();
    }
    private void createAdministrator(String agentParams) throws StaleProxyException {
        Object[] params = getAgentParams(agentParams);
        containerController.createNewAgent(Arrays.stream(params).findFirst().get().toString(), "agents.airways_administrator.AirwaysAdministratorAgent", params).start();
    }

    private Object[] getAgentParams(String agentParams) {
        return agentParams.split(",");
    }

}

