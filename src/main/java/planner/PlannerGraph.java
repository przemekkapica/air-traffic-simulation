package planner;

import java.util.*;

public class PlannerGraph {
    public static class PlannerGraphEdge {
        private final float distance;
        private final float cost;
        private float load;
        private boolean isEnabled;
        private final PlannerGraphNode endNode;

        public PlannerGraphEdge (PlannerGraphNode node, float distance, float cost) {
            this.distance = distance;
            this.cost = cost;
            load = 0.0f;
            isEnabled = true;
            endNode = node;
        }

        public float getDistance() {
            return distance;
        }

        public PlannerGraphNode getNode() {
            return endNode;
        }

        public float getCost() {
            return cost;
        }

        public float getLoad() {
            return load;
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        public void setLoad(float load) {
            this.load = load;
        }

        public void setEnabled (boolean enabled) {
            isEnabled = enabled;
        }


    }

    public static class PlannerGraphNode {
        private final String name;
        private final List<PlannerGraphEdge> neighbors = new ArrayList<> ();

        public PlannerGraphNode (String name) {
            this.name = name;
        }

        public String getName () {
            return name;
        }

        public void addNeighbor (PlannerGraphNode node, float distance, float cost) {
            neighbors.add (new PlannerGraphEdge (node, distance, cost));
        }

        public List<PlannerGraphEdge> getNeighbors () {
            return neighbors;
        }
    }

    private final Map<String, PlannerGraphNode> graph = new HashMap<> ();

    public void addNode(String name) {
        PlannerGraphNode node = new PlannerGraphNode (name);
        graph.put (name, node);

    }

    public PlannerGraphNode getNode (String name) {
        if (graph.containsKey (name)) {
            return graph.get (name);
        }
        return null;
    }

    public Set<String> getNodeNames () {
        return graph.keySet ();
    }
}
