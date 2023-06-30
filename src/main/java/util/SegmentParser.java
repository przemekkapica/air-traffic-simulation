package util;

import org.javatuples.Pair;

public class SegmentParser {
    final static String segmentCharacter = "-";
    public static Pair<String, String> parse(String name) {
        String[] parsed = name.split(segmentCharacter);

        return new Pair<>(parsed[0], parsed[1]);
    }
}
