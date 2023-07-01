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
        File file = new File("src/main/resources/scenarios/scenario.csv");
        try (Scanner sc = new Scanner(file)) {
            sc.useDelimiter(";");
            while (sc.hasNextLine()) {
                String data = sc.nextLine();
                processScenarioData(data);
            }
        }
    }

    private void processScenarioData(String data) throws StaleProxyException {
        int start = data.indexOf(";") + 1;
        int stop = data.indexOf(";", data.indexOf(";") + 1);
        String params = data.substring(start, stop);

        if (data.contains("airport")) {
            createAgent("agents.atc.ATCAgent", params);
        } else if (data.contains("aircraft")) {
            createAgent("agents.aircraft.AircraftAgent", params);
        } else if (data.contains("administrator")) {
            createAgent("agents.airways_administrator.AirwaysAdministratorAgent", params);
        }
    }

    private void createAgent(String agentClass, String agentParams) throws StaleProxyException {
        Object[] params = getAgentParams(agentParams);
        String agentName = Arrays.stream(params).findFirst().get().toString();
        System.out.println(agentName);
        containerController.createNewAgent(agentName, agentClass, params).start();
    }

    private Object[] getAgentParams(String agentParams) {
        return agentParams.split(",");
    }
}