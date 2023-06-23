package util;

import model.Airport;
import model.Airway;
import model.Aircraft;
import org.javatuples.Pair;
import org.joml.Vector2f;
import simulation.Simulation;

public class ScenarioManager {

    public static void RunScenario1() {

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
                "9-11", "3-11", "1-11", "7-11", "9-2", "5-7", "8-5", "9-10"
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

        Aircraft aircraft = new Aircraft("A_1", 300.0f, 0.0f, intersections[0]);
        aircraft.setSpeed(0.0f);
        aircraft.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft);

        Aircraft aircraft2 = new Aircraft("A_2", 550.0f, 0.0f, intersections[1]);
        aircraft2.setSpeed(0.0f);
        aircraft2.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft2);

        Aircraft aircraft3 = new Aircraft("A_3", 320.0f, 0.0f, intersections[2]);
        aircraft3.setSpeed(0.0f);
        aircraft3.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft3);

        Aircraft aircraft4 = new Aircraft("A_4", 400.0f, 0.0f, intersections[3]);
        aircraft4.setSpeed(0.0f);
        aircraft4.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft4);

        Aircraft aircraft5 = new Aircraft("A_5", 330.0f, 0.0f, intersections[4]);
        aircraft5.setSpeed(0.0f);
        aircraft5.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft5);

        Aircraft aircraft6 = new Aircraft("A_6", 320.0f, 0.0f, intersections[5]);
        aircraft6.setSpeed(0.0f);
        aircraft6.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft6);

        Aircraft aircraft7 = new Aircraft("A_7", 300.0f, 0.0f, intersections[6]);
        aircraft7.setSpeed(0.0f);
        aircraft7.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft7);

        Aircraft aircraft8 = new Aircraft("A_8", 300.0f, 0.0f, intersections[7]);
        aircraft8.setSpeed(0.0f);
        aircraft8.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft8);
    }

    public static void RunScenario2() {

        final Airport[] intersections = {
                new Airport("intersection_1", new Vector2f(600, 200)),
                new Airport("intersection_2", new Vector2f(600, 400)),
                new Airport("intersection_3", new Vector2f(250, 400)),
                new Airport("intersection_4", new Vector2f(300, 500)),
                new Airport("intersection_5", new Vector2f(600, 750)),
                new Airport("intersection_6", new Vector2f(900, 500)),
                new Airport("intersection_7", new Vector2f(950, 400)),


        };
        final String[] segments = { "2-1", "1-2", "3-2", "4-2", "5-2", "6-2", "7-2", "2-3", "2-4", "2-5", "2-6", "2-7"};

        for (String segment : segments) {
            Pair<String, String> parsed = SegmentParser.parse(segment);
            String from = parsed.getValue0(), to = parsed.getValue1();
            Simulation.getScene().addObject(new Airway(
                    String.format("segment_%s", segment),
                    intersections[ Integer.parseInt(from) - 1],
                    intersections[Integer.parseInt(to) - 1]
            ));
        }

        for(Airport intersection : intersections) {
            Simulation.getScene().addObject(intersection);
        }

        Aircraft aircraft = new Aircraft("aircraft_1", 100.0f, 100.0f, intersections[2]);
        aircraft.setSpeed(0.0f);
        aircraft.setColor(44, 16, 130);

        Aircraft aircraft2 = new Aircraft("aircraft_2", 100.0f,100.0f, intersections[3]);
        aircraft2.setSpeed(0.0f);
        aircraft2.setColor(44, 16, 130);

        Aircraft aircraft3 = new Aircraft("aircraft_3", 100.0f, 100.0f, intersections[4]);
        aircraft3.setSpeed(0.0f);
        aircraft3.setColor(44, 16, 130);

        Aircraft aircraft4 = new Aircraft("aircraft_4", 100.0f, 100.0f, intersections[5]);
        aircraft4.setSpeed(0.0f);
        aircraft4.setColor(44, 16, 130);

        Aircraft aircraft5 = new Aircraft("aircraft_5", 100.0f, 100.0f, intersections[6]);
        aircraft5.setSpeed(0.0f);
        aircraft5.setColor(44, 16, 130);

        Simulation.getScene().addObject(aircraft);
        Simulation.getScene().addObject(aircraft2);
        Simulation.getScene().addObject(aircraft3);
        Simulation.getScene().addObject(aircraft4);
        Simulation.getScene().addObject(aircraft5);
    }

    public static void RunScenario3() {
        final Airport[] intersections = {
                new Airport("intersection_1", new Vector2f(100, 360)),
                new Airport("intersection_2", new Vector2f(300, 380)),
                new Airport("intersection_3", new Vector2f(500, 400)),
                new Airport("intersection_4", new Vector2f(600, 200)),
                new Airport("intersection_5", new Vector2f(500, 600)),
                new Airport("intersection_6", new Vector2f(700, 600)),
                new Airport("intersection_7", new Vector2f(700, 400)),
                new Airport("intersection_8", new Vector2f(900, 420)),
        };
        final String[] segments = { "2-1", "1-2", "3-2", "2-3", "3-4", "4-3", "3-7", "4-7", "7-4", "3-5", "5-3", "5-6", "6-5", "6-7", "7-6", "7-8", "8-7"};

        for (String segment : segments) {
            Pair<String, String> parsed = SegmentParser.parse(segment);
            String from = parsed.getValue0(), to = parsed.getValue1();
            Simulation.getScene().addObject(new Airway(
                    String.format("segment_%s", segment),
                    intersections[ Integer.parseInt(from) - 1],
                    intersections[Integer.parseInt(to) - 1]
            ));
        }

        for (Airport intersection : intersections) {
            Simulation.getScene().addObject(intersection);
        }

        Aircraft aircraft = new Aircraft("aircraft_1", 100.0f, 100.0f, intersections[0]);
        aircraft.setSpeed(0.0f);
        aircraft.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft);
    }

    public static void RunScenario4() {
        final Airport[] intersections = {
                new Airport("intersection_1", new Vector2f(100, 400)),
                new Airport("intersection_2", new Vector2f(300, 600)),
                new Airport("intersection_3", new Vector2f(300, 400)),
                new Airport("intersection_4", new Vector2f(600, 200)),
                new Airport("intersection_5", new Vector2f(900, 400)),
                new Airport("intersection_6", new Vector2f(600, 600)),
        };
        final String[] segments = { "3-1", "1-3", "3-2", "2-3", "3-4", "4-5", "3-5", "5-6", "3-6", "6-5"};

        for (String segment : segments) {
            Pair<String, String> parsed = SegmentParser.parse(segment);
            String from = parsed.getValue0(), to = parsed.getValue1();
            Simulation.getScene().addObject(new Airway(
                    String.format("segment_%s", segment),
                    intersections[ Integer.parseInt(from) - 1],
                    intersections[Integer.parseInt(to) - 1]
            ));
        }

        for (Airport intersection : intersections) {
            Simulation.getScene().addObject(intersection);
        }

        Aircraft aircraft = new Aircraft("aircraft_1", 100.0f, 100.0f, intersections[0]);
        aircraft.setSpeed(0.0f);
        aircraft.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft);

        Aircraft aircraft2 = new Aircraft("aircraft_2", 100.0f, 100.0f, intersections[1]);
        aircraft2.setSpeed(0.0f);
        aircraft2.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft2);
    }

    public static void RunScenario0() {
        final Airport[] intersections = {
                new Airport("intersection_1", new Vector2f(200, 100)),
                new Airport("intersection_2", new Vector2f(400, 100)),
                new Airport("intersection_3", new Vector2f(600, 100)),
                new Airport("intersection_4", new Vector2f(800, 100)),
                new Airport("intersection_5", new Vector2f(1000, 100)),
                new Airport("intersection_6", new Vector2f(200, 300)),
                new Airport("intersection_7", new Vector2f(400, 300)),
                new Airport("intersection_8", new Vector2f(600, 300)),
                new Airport("intersection_9", new Vector2f(800, 300)),
                new Airport("intersection_10", new Vector2f(1000, 300)),
                new Airport("intersection_11", new Vector2f(200, 500)),
                new Airport("intersection_12", new Vector2f(400, 500)),
                new Airport("intersection_13", new Vector2f(600, 500)),
                new Airport("intersection_14", new Vector2f(800, 500)),
                new Airport("intersection_15", new Vector2f(1000, 500)),
                new Airport("intersection_16", new Vector2f(200, 700)),
                new Airport("intersection_17", new Vector2f(400, 700)),
                new Airport("intersection_18", new Vector2f(600, 700)),
                new Airport("intersection_19", new Vector2f(800, 700)),
                new Airport("intersection_20", new Vector2f(1000, 700)),

        };
        final String[] segments = {
                "1-2", "1-6", "1-7",
                "2-1", "2-3", "2-6", "2-7", "2-8",
                "3-2", "3-4", "3-7", "3-8", "3-9",
                "4-3", "4-5", "4-8", "4-9", "4-10",
                "5-4", "5-9", "5-10",
                "6-1", "6-2", "6-7", "6-11", "6-12",
                "7-1", "7-2", "7-3", "7-6", "7-8", "7-11", "7-12", "7-13",
                "8-2", "8-3", "8-4", "8-7", "8-9", "8-12", "8-13", "8-14",
                "9-3", "9-4", "9-5", "9-8", "9-10", "9-13", "9-14", "9-15",
                "10-4", "10-5", "10-9", "10-14", "10-15",
                "11-6", "11-7", "11-12", "11-16", "11-17",
                "12-6", "12-7", "12-8", "12-11", "12-13", "12-15", "12-16", "12-17",
                "13-7", "13-8", "13-9", "13-12", "13-14", "13-17", "13-18", "13-19",
                "14-8", "14-9", "14-10", "14-13", "14-15", "14-18", "14-19", "14-20",
                "15-9", "15-10", "15-14", "15-19", "15-20",
                "16-11", "16-12", "16-17",
                "17-11", "17-12", "17-13", "17-16", "17-18",
                "18-12", "18-13", "18-14", "18-17", "18-19",
                "19-13", "19-14", "19-15", "19-18", "19-20",
                "20-14", "20-15", "20-19"
        };

        for (String segment : segments) {
            Pair<String, String> parsed = SegmentParser.parse(segment);
            String from = parsed.getValue0(), to = parsed.getValue1();
            Simulation.getScene().addObject(new Airway(
                    String.format("segment_%s", segment),
                    intersections[ Integer.parseInt(from) - 1],
                    intersections[Integer.parseInt(to) - 1]
            ));
        }

        for (Airport intersection : intersections) {
            Simulation.getScene().addObject(intersection);
        }

        Aircraft aircraft = new Aircraft("aircraft_1", 100.0f, 100.0f, intersections[0]);
        aircraft.setSpeed(0.0f);
        aircraft.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft);

        Aircraft aircraft2 = new Aircraft("aircraft_2", 100.0f, 100.0f, intersections[1]);
        aircraft2.setSpeed(0.0f);
        aircraft2.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft2);

        Aircraft aircraft3 = new Aircraft("aircraft_3", 100.0f, 100.0f, intersections[2]);
        aircraft3.setSpeed(0.0f);
        aircraft3.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft3);

        Aircraft aircraft4 = new Aircraft("aircraft_4", 100.0f, 100.0f, intersections[3]);
        aircraft4.setSpeed(0.0f);
        aircraft4.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft4);

        Aircraft aircraft5 = new Aircraft("aircraft_5", 100.0f, 100.0f, intersections[4]);
        aircraft5.setSpeed(0.0f);
        aircraft5.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft5);

        Aircraft aircraft6 = new Aircraft("aircraft_6", 100.0f, 100.0f, intersections[15]);
        aircraft6.setSpeed(0.0f);
        aircraft6.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft6);

        Aircraft aircraft7 = new Aircraft("aircraft_7", 100.0f, 100.0f, intersections[16]);
        aircraft7.setSpeed(0.0f);
        aircraft7.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft7);

        Aircraft aircraft8 = new Aircraft("aircraft_8", 100.0f, 100.0f, intersections[17]);
        aircraft8.setSpeed(0.0f);
        aircraft8.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft8);

        Aircraft aircraft9 = new Aircraft("aircraft_9", 100.0f, 100.0f, intersections[18]);
        aircraft9.setSpeed(0.0f);
        aircraft9.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft9);

        Aircraft aircraft10 = new Aircraft("aircraft_10", 100.0f, 100.0f, intersections[19]);
        aircraft10.setSpeed(0.0f);
        aircraft10.setColor(44, 16, 130);
        Simulation.getScene().addObject(aircraft10);
    }
}
