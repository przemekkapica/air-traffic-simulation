package util;

import model.Airport;
import model.Airway;
import model.Aircraft;
import org.javatuples.Pair;
import org.joml.Vector2f;
import simulation.Simulation;

public class ScenarioManager {

    public static void runScenario() {

        final Airport[] intersections = {
                new Airport(":New Delhi", new Vector2f(872.42f, 416.3f)),
                new Airport(":Tokyo", new Vector2f(1079.76f, 377.48f)),
                new Airport(":Seoul", new Vector2f(1076.14f, 301.46f)),
                new Airport(":Istanbul", new Vector2f(717.76f, 359.38f)),
                new Airport(":New York", new Vector2f(383.72f, 340.28f)),
                new Airport(":Mexico City", new Vector2f(289.6f, 434.4f)),
                new Airport(":Rio de Janeiro", new Vector2f(452.5f, 579.2f)),
                new Airport(":Lagos", new Vector2f(626.26f, 471.6f)),
                new Airport(":London", new Vector2f(615.4f, 304.08f)),
                new Airport(":Cairo", new Vector2f(709.52f, 409.06f)),
                new Airport(":Moscow", new Vector2f(752.96f, 287.98f))
        };

        final String[] segments = {
                "1-2", "2-3", "3-4", "1-6", "3-6", "3-7", "4-8", "5-1",
                "5-6", "6-7", "7-8", "7-9", "8-3", "9-5", "8-7", "7-3", "6-10",
                "9-11", "3-11", "1-11", "7-11", "9-2", "5-7", "8-5", "9-10", "4-9", "4-1"
        };


        for (String segment : segments) {
            Pair<String, String> parsed = SegmentParser.parse(segment);
            String from = parsed.getValue0(), to = parsed.getValue1();
            Simulation.getScene().addObject(new Airway(
                    String.format("segment_%s", segment),
                    intersections[Integer.parseInt(from) - 1],
                    intersections[Integer.parseInt(to) - 1]
            ));
        }

        for (Airport intersection : intersections) {
            Simulation.getScene().addObject(intersection);
        }

        Aircraft aircraft1 = new Aircraft("A_1", 300.0f, 0.0f, intersections[0]);
        aircraft1.setSpeed(0.0f);
        aircraft1.setColor(44, 16, 130);

        Aircraft aircraft2 = new Aircraft("A_2", 550.0f, 0.0f, intersections[1]);
        aircraft2.setSpeed(0.0f);
        aircraft2.setColor(44, 16, 130);

        Aircraft aircraft3 = new Aircraft("A_3", 320.0f, 0.0f, intersections[2]);
        aircraft3.setSpeed(0.0f);
        aircraft3.setColor(44, 16, 130);

        Aircraft aircraft4 = new Aircraft("A_4", 400.0f, 0.0f, intersections[3]);
        aircraft4.setSpeed(0.0f);
        aircraft4.setColor(44, 16, 130);

        Aircraft aircraft5 = new Aircraft("A_5", 330.0f, 0.0f, intersections[4]);
        aircraft5.setSpeed(0.0f);
        aircraft5.setColor(44, 16, 130);

        Aircraft aircraft6 = new Aircraft("A_6", 320.0f, 0.0f, intersections[5]);
        aircraft6.setSpeed(0.0f);
        aircraft6.setColor(44, 16, 130);

        Aircraft aircraft7 = new Aircraft("A_7", 300.0f, 0.0f, intersections[6]);
        aircraft7.setSpeed(0.0f);
        aircraft7.setColor(44, 16, 130);

        Aircraft aircraft8 = new Aircraft("A_8", 300.0f, 0.0f, intersections[7]);
        aircraft8.setSpeed(0.0f);
        aircraft8.setColor(44, 16, 130);

        final Aircraft[] aircrafts = {aircraft1, aircraft2, aircraft3, aircraft4, aircraft5, aircraft6, aircraft7, aircraft8};

        Simulation.getScene().addObjects(aircrafts);
    }
}
