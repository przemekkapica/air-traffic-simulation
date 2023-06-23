package planner;

import org.javatuples.Pair;

import java.util.*;
import java.util.concurrent.Semaphore;

public class AirwaysManager {
    public enum AirwayPriority {
        DEFAULT, DISTANCE, COST, LOAD
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
                    begin.addNeighbor(end, route.length, route.baseCost);
                }
            }
        }
    }

    private void setBroken (String from, String to, boolean broken) {
        try {
            mutex.acquire ();
            PlannerGraph.PlannerGraphNode node = plannerGraph.getNode(from);

            if (node != null) {
                for (PlannerGraph.PlannerGraphEdge edge : node.getNeighbors ()) {
                    if (edge.getNode().getName ().equals (to)) {
                        edge.setEnabled (!broken);
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } finally {
            mutex.release ();
        }
    }

    public void notifyRouteBroken (String from, String to) {
        setBroken (from, to, true);
    }

    public void notifyRouteRepaired (String from, String to) {
        setBroken (from, to, false);
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

    public void acceptRoute(List<String> route, String aircraftName, float maxSpeed) {
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

    private float getDistance(PlannerGraph.PlannerGraphEdge edge, AirwayPriority priority) {
        float weight = 0.0f;

        switch (priority) {
            case COST -> weight = edge.getCost ();
            case DISTANCE -> weight = edge.getDistance ();
            case LOAD -> weight = edge.getLoad ();
            case DEFAULT -> weight = (edge.getDistance () + edge.getLoad () + edge.getCost ()) / 3.0f;
        }

        return weight;
    }

    public List<String> findRoute(String from, String to, float maxSpeed, AirwayPriority priority) {
        // dijkstra algoritm
        List<String> route = null;

        try {
            mutex.acquire();
            Map<String, Float> distance = new HashMap<> ();
            Map<String, String> prevNode = new HashMap<> ();

            final float INF = 100000000.0f;

            for (String node : plannerGraph.getNodeNames ()) {
                if (node.equals (from)) {
                    distance.put (node, 0.0f);
                } else {
                    distance.put (node, INF);
                }
            }

            PriorityQueue<Pair<String, Float>> queue = new PriorityQueue<> (Comparator.comparing (Pair::getValue1));
            queue.add (new Pair<> (from, 0.0f));

            while (!queue.isEmpty ()) {
                Pair<String, Float> pair = queue.poll ();
                String currName = pair.getValue0 ();
                PlannerGraph.PlannerGraphNode u = plannerGraph.getNode (currName);
                float uWeight = distance.get (currName);

                for (PlannerGraph.PlannerGraphEdge edge : u.getNeighbors ()) {
                    if (!edge.isEnabled ()) {
                        continue; // edge is broken, ignore it
                    }

                    PlannerGraph.PlannerGraphNode v = edge.getNode();
                    float vWeight = distance.get (v.getName());
                    float dist = getDistance (edge, priority);

                    if (vWeight > uWeight + dist) {
                        distance.put (v.getName (), uWeight + dist);
                        prevNode.put (v.getName (), u.getName ());

                        queue.add (new Pair<> (v.getName (), uWeight + dist));
                    }
                }
            }

            // backtrack
            List<String> finalRoute = new ArrayList<> ();
            String curr = to;

            while (curr != null && !curr.equals (from)) {
                finalRoute.add (curr);
                curr = prevNode.get (curr);

                if (curr == null) {
                    finalRoute = null;
                }
            }

            route = finalRoute;
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } finally {
            mutex.release ();
        }
        if (route != null)
        {
            route.add(from);
            Collections.reverse(route);
            return route;
        }
        else
        {
            List<String> routeNotFound = new ArrayList<>();
            routeNotFound.add("Not_Found");
            return  routeNotFound;
        }
    }
}
