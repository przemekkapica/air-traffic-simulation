import jade.wrapper.StaleProxyException;
import simulation.Simulation;
import util.ScenarioManager;
import util.ScenarioEngine;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        loadSimulation();
        startAgentPlatformThread();
        startSimulation();
    }

    private static void loadSimulation() {
        ScenarioManager.runScenario();
    }

    private static void startAgentPlatformThread() {
        Thread agentPlatformThread = new Thread(Main::runJadeThread);
        agentPlatformThread.start();
    }

    private static void runJadeThread() {
        try {
            ScenarioEngine scenarioEngine = new ScenarioEngine();
            scenarioEngine.runScenario();
        } catch (FileNotFoundException | StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startSimulation() {
        Simulation simulation = new Simulation();
        simulation.initialize(1280, 800, "Air Traffic Simulation");
        simulation.run();
    }
}
