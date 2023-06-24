package util;

import model.ui_elements.Airport;
import model.ui_elements.Airway;
import model.ui_elements.Aircraft;
import org.javatuples.Pair;
import org.joml.Vector2f;
import model.ui_elements.AircraftsDetailsDisplay;
import simulation.Simulation;
import simulation.SimulationObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ScenarioManager {

    public static void runScenario() {

        final Airport[] airports = getAirports();

        final String[] segments = getSegments();

        for (String segment : segments) {
            Pair<String, String> parsed = SegmentParser.parse(segment);
            String from = parsed.getValue0(), to = parsed.getValue1();
            Simulation.getScene().addObject(new Airway(
                    String.format("segment_%s", segment),
                    airports[Integer.parseInt(from) - 1],
                    airports[Integer.parseInt(to) - 1]
            ));
        }

        for (Airport airport : airports) {
            Simulation.getScene().addObject(airport);
        }
        List<String> aircraftNames = new ArrayList<>(Arrays.asList("P1", "P2", "P3", "P4", "P5", "P6","P7", "P8", "P9", "P10", "P11"));

        AircraftsDetailsDisplay.createInstance(aircraftNames);
        AircraftsDetailsDisplay aircraftsDetailsDisplay = AircraftsDetailsDisplay.getInstance();
        Simulation.getScene().addObject(aircraftsDetailsDisplay);

        final Aircraft[] aircrafts = getAircrafts(airports, aircraftNames);

        Simulation.getScene().addObjects(aircrafts);
    }

    private static Aircraft[] getAircrafts(Airport[] airports, List<String> aircraftNames) {
        Aircraft aircraft1 = new Aircraft(aircraftNames.get(0), 440.0f, 0.0f, airports[0]);
        Aircraft aircraft2 = new Aircraft(aircraftNames.get(1), 550.0f, 0.0f, airports[1]);
        Aircraft aircraft3 = new Aircraft(aircraftNames.get(2), 320.0f, 0.0f, airports[2]);
        Aircraft aircraft4 = new Aircraft(aircraftNames.get(3), 400.0f, 0.0f, airports[3]);
        Aircraft aircraft5 = new Aircraft(aircraftNames.get(4), 330.0f, 0.0f, airports[4]);
        Aircraft aircraft6 = new Aircraft(aircraftNames.get(5), 320.0f, 0.0f, airports[5]);
        Aircraft aircraft7 = new Aircraft(aircraftNames.get(6), 300.0f, 0.0f, airports[6]);
        Aircraft aircraft8 = new Aircraft(aircraftNames.get(7), 480.0f, 0.0f, airports[7]);
        Aircraft aircraft9 = new Aircraft(aircraftNames.get(8), 420.0f, 0.0f, airports[8]);
        Aircraft aircraft10 = new Aircraft(aircraftNames.get(9), 380.0f, 0.0f, airports[9]);
        Aircraft aircraft11 = new Aircraft(aircraftNames.get(10), 500.0f, 0.0f, airports[10]);
//        return new Aircraft[]{aircraft1};
        return new Aircraft[]{
                aircraft1, aircraft2, aircraft3, aircraft4, aircraft5, aircraft6,
                aircraft7, aircraft8, aircraft9, aircraft10, aircraft11
        };
    }

    private static String[] getSegments() {
        return new String[]{
                "1-2", "2-3", "3-4", "1-6", "3-6", "3-7", "4-8", "5-1",
                "5-6", "6-7", "7-8", "7-9", "8-3", "9-5", "8-7", "7-3", "6-10",
                "9-11", "3-11", "1-11", "7-11", "9-2", "5-7", "8-5", "9-10", "4-9",
                "4-1", "11-1", "11-6", "10-9"
        };
    }

    private static Airport[] getAirports() {
        return new Airport[]{
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
    }
}
