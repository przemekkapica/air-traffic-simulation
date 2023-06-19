package util;

import model.AirwaySegment;
import org.javatuples.Pair;
import planner.AirwaysManager;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Constants.INTERSECTION_PREFIX;

public class GraphDescriptor {
    public static Map<String, List<AirwaysManager.RouteDescription>> describeAirway(List<String> segments, List<String> costs) {
        Map<String, List<AirwaysManager.RouteDescription>> plan = new HashMap<>();
        int counter = 0;
        for (String s : segments) {

            Pair<String, String> parsed = SegmentParser.parse(s);

            if (!plan.containsKey(INTERSECTION_PREFIX + parsed.getValue0()))
                plan.put(INTERSECTION_PREFIX + parsed.getValue0(), new ArrayList<>() {});

            AirwaySegment segment =(AirwaySegment) Simulation.getScene().getObject("segment_" + s);

            AirwaysManager.RouteDescription description = new AirwaysManager.RouteDescription(
                    INTERSECTION_PREFIX + parsed.getValue1(),
                    Float.parseFloat(costs.get(counter)),
                    segment.getLength()
            );
            plan.get(INTERSECTION_PREFIX + parsed.getValue0()).add(description);
            counter++;
        }
        return plan;
    }
}
