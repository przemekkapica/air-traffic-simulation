package planner;

import java.util.*;

public class PlannerGraph {
    public static class PlannerGraphEdge {
        private final float m_distance;
        private float m_cost;
        private float m_load;
        private boolean m_enabled;
        private final PlannerGraphNode m_endNode;

        public float getDistance() {
            return m_distance;
        }

        public PlannerGraphNode getNode() {
            return m_endNode;
        }

        public float getCost() {
            return m_cost;
        }

        public float getLoad() {
            return m_load;
        }

        public boolean isEnabled() {
            return m_enabled;
        }

        public void setLoad(float load) {
            m_load = load;
        }

        public void setEnabled (boolean enabled) {
            m_enabled = enabled;
        }

        public PlannerGraphEdge (PlannerGraphNode node, float distance, float cost) {
            m_distance = distance;
            m_cost = cost;
            m_load = 0.0f;
            m_enabled = true;
            m_endNode = node;
        }
    }

    public static class PlannerGraphNode {
        private final String m_name;
        private final List<PlannerGraphEdge> m_neighbors = new ArrayList<> ();

        public PlannerGraphNode (String name) {
            m_name = name;
        }

        public String getName () {
            return m_name;
        }

        public void addNeighbor (PlannerGraphNode node, float distance, float cost) {
            m_neighbors.add (new PlannerGraphEdge (node, distance, cost));
        }

        public List<PlannerGraphEdge> getNeighbors () {
            return m_neighbors;
        }
    }

    private final Map<String, PlannerGraphNode> m_graph = new HashMap<> ();

    public void addNode(String name) {
        PlannerGraphNode node = new PlannerGraphNode (name);
        m_graph.put (name, node);

    }

    public PlannerGraphNode getNode (String name) {
        if (m_graph.containsKey (name)) {
            return m_graph.get (name);
        }
        return null;
    }

    public Set<String> getNodeNames () {
        return m_graph.keySet ();
    }
}
