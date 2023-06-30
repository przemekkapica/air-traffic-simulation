package util;

import model.ui_elements.Airway;
import org.javatuples.Pair;
import planner.AirwaysManager;
import simulation.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static util.Constants.AIRPORT_PREFIX;

public class GraphDescriptor {
    public static Map<String, List<AirwaysManager.RouteDescription>> describeAirway(List<String> segments, List<String> costs) {
        Map<String, List<AirwaysManager.RouteDescription>> plan = new HashMap<>();

        int counter = 0;
        for (String segment : segments) {
            Pair<String, String> parsed = SegmentParser.parse(segment);
            String sourceAirport = AIRPORT_PREFIX + parsed.getValue0();
            String destinationAirport = AIRPORT_PREFIX + parsed.getValue1();

            plan.putIfAbsent(sourceAirport, new ArrayList<>());

            Airway segmentObject = (Airway) Simulation.getScene().getObject("segment_" + segment);

            AirwaysManager.RouteDescription description = new AirwaysManager.RouteDescription(
                    destinationAirport,
                    Float.parseFloat(costs.get(counter)),
                    segmentObject.getLength()
            );
            plan.get(sourceAirport).add(description);

            counter++;
        }

        return plan;
    }
}