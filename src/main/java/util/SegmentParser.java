package util;

import org.javatuples.Pair;

public class SegmentParser {
    public static Pair<String, String> parse(String name) {
        String[] parsed = name.split("-");

        return new Pair<>(parsed[0], parsed[1]);
    }

    public static Pair<String, String> parseFullName(String name) {
        String[] airports = name.split("_");
        String[] parsed = airports[1].split("-");

        return new Pair<>(parsed[0], parsed[1]);
    }
}
