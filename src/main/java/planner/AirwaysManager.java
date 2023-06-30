package planner;

import java.util.*;
import java.util.concurrent.Semaphore;

public class AirwaysManager {
    public List<String> findRoute(String beginning, String end) {
        return List.of(beginning, end);
    }

    public static class RouteDescription {
        public String endAirportName;
        public float baseCost;
        public float length;

        public RouteDescription(String endAirportName, float baseCost, float length) {
            this.endAirportName = endAirportName;
            this.baseCost = baseCost;
            this.length = length;
        }
    }

    private final PlannerGraph plannerGraph = new PlannerGraph ();
    private final Map<String, List<String>> aircrafts = new HashMap<> ();
    private final Semaphore mutex = new Semaphore(1);

    public AirwaysManager(Map<String, List<RouteDescription>> graphDescription) {
        // initialize the graph
        for (String node : graphDescription.keySet ()) {
            plannerGraph.addNode (node);
        }

        for (Map.Entry<String, List<RouteDescription>> entry : graphDescription.entrySet ()) {
            PlannerGraph.PlannerGraphNode begin = plannerGraph.getNode(entry.getKey ());
            for (RouteDescription route : entry.getValue ()) {
                PlannerGraph.PlannerGraphNode end = plannerGraph.getNode(route.endAirportName);
                if (end != null) {
                    begin.addNeighbor(end, route.length);
                }
            }
        }
    }

    public void notifyAircraftArrived (String aircraftName) {
        try {
            mutex.acquire ();
            List<String> route = aircrafts.get (aircraftName);

            if (route != null) {
                for (int i = 0; i < route.size () - 1; ++i) {
                    String first = route.get (i);
                    String second = route.get (i + 1);

                    PlannerGraph.PlannerGraphNode node = plannerGraph.getNode(first);
                    for (PlannerGraph.PlannerGraphEdge edge : node.getNeighbors ()) {
                        if (edge.getNode ().getName ().equals (second)) {
                            float load = Math.max (0.0f, edge.getLoad () - 100.0f);
                            edge.setLoad (load);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } finally {
            mutex.release ();
        }

        aircrafts.remove (aircraftName);
    }

    public void acceptRoute(List<String> route, String aircraftName) {
        try {
            mutex.acquire ();
            for (int i = 0; i < route.size () - 1; ++i) {
                String first = route.get (i);
                String second = route.get (i + 1);

                PlannerGraph.PlannerGraphNode node = plannerGraph.getNode (first);
                for (PlannerGraph.PlannerGraphEdge edge : node.getNeighbors ()) {
                    if (edge.getNode ().getName ().equals (second)) {
                        float load = edge.getLoad () + 100.0f;
                        edge.setLoad (load);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } finally {
            mutex.release ();
        }

        aircrafts.put (aircraftName, route);
    }
}
