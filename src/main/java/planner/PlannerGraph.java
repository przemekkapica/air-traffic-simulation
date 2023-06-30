package planner;

import java.util.*;

public class PlannerGraph {
    public static class PlannerGraphEdge {
        private float load;
        private final PlannerGraphNode endNode;

        public PlannerGraphEdge (PlannerGraphNode node, float distance) {
            load = 0.0f;
            boolean isEnabled = true;
            endNode = node;
        }

        public PlannerGraphNode getNode() {
            return endNode;
        }

        public float getLoad() {
            return load;
        }

        public void setLoad(float load) {
            this.load = load;
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

        public void addNeighbor(PlannerGraphNode node, float distance) {
            neighbors.add (new PlannerGraphEdge (node, distance));
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
}
