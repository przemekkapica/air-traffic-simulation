package model.ui_elements;

import simulation.GraphicsContext;
import simulation.IRenderableObject;
import simulation.SimulationObject;
import util.GraphicsUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVG.nvgText;

public class AircraftsDetailsDisplay extends SimulationObject implements IRenderableObject {
    private static AircraftsDetailsDisplay instance = null;
    public static class AircraftDetails {
        public AircraftDetails(float speed, float attitude, AirTrafficElement airTrafficElement) {
            this.speed = speed;
            this.attitude = attitude;
            this.airTrafficElement = airTrafficElement;
        }
        public float speed;
        public float attitude;
        public AirTrafficElement airTrafficElement;
    }
    private final HashMap<String, AircraftDetails> aircraftDetailsMap = new HashMap<>();

    public AircraftsDetailsDisplay(List<String> aircraftNames) {
        super("aircraftsDetailsDisplay");
        for (var name : aircraftNames) {
            aircraftDetailsMap.put(name, new AircraftDetails(0, 0, null));
        }
    }
    @Override
    public void glRender(GraphicsContext context) { }

    @Override
    public void nvgRender(long nvg) {
        nvgFontFace(nvg, "font");
        nvgTextAlign(nvg, NVG_ALIGN_MIDDLE | NVG_ALIGN_BOTTOM);

//        _renderTable(nvg);
        _renderLabels(nvg);
        _renderAircraftsDetails(nvg);
    }

    public static void createInstance(List<String> aircraftNames) {
        instance = new AircraftsDetailsDisplay(aircraftNames);
    }

    public static AircraftsDetailsDisplay getInstance() {
        if (instance == null) {
            instance = new AircraftsDetailsDisplay(new ArrayList<>());
        }
        return instance;
    }

    public void update(String name, float speed, float attitude, AirTrafficElement airTrafficElement) {
        aircraftDetailsMap.put(name, new AircraftDetails(speed, attitude, airTrafficElement));
    }

    private void _renderAircraftsDetails(long nvg) {
        nvgFontSize(nvg, 16.0f);
        nvgFillColor(nvg,  GraphicsUtil.colorFromRgb(0, 0, 0));
        int i = 1;
        for (Map.Entry<String, AircraftDetails> entry : aircraftDetailsMap.entrySet()) {
            String name = entry.getKey();
            float speed = entry.getValue().speed;
            float attitude = entry.getValue().attitude;
            AirTrafficElement fragment = entry.getValue().airTrafficElement;

            nvgText(nvg, 30, 460 + i*30 , name);
            nvgText(nvg, 80, 460 + i*30 , String.valueOf(speed));
            nvgText(nvg, 140, 460 + i*30, String.valueOf(attitude));
            nvgText(nvg, 200, 460 + i*30, fragment.toString());
            i++;
        }
    }

    private static void _renderLabels(long nvg) {
        nvgFontSize(nvg, 13.0f);
        nvgFillColor(nvg,  GraphicsUtil.colorFromRgb(66, 138, 236));
        nvgText(nvg, 30, 460, "Plane");
        nvgText(nvg, 80, 460, "Speed");
        nvgText(nvg, 140, 460, "Attitude");
        nvgText(nvg, 200, 460, "Location");
    }

    private static void _renderTable(long nvg) {
        nvgBeginPath(nvg);
        nvgStrokeColor(nvg, GraphicsUtil.colorFromRgb(0, 0, 0));
        nvgStrokeWidth(nvg, 1.0f);

        nvgMoveTo(nvg, 20, 400);
        nvgLineTo(nvg, 230,400);
        nvgStroke(nvg);
        nvgClosePath(nvg);
        nvgBeginPath(nvg);
        nvgMoveTo(nvg, 20, 430);
        nvgLineTo(nvg, 230,430);
        nvgStroke(nvg);
        nvgClosePath(nvg);
        nvgBeginPath(nvg);
        nvgMoveTo(nvg, 20, 430);
        nvgLineTo(nvg, 20,680);
        nvgStroke(nvg);
        nvgClosePath(nvg);
        nvgBeginPath(nvg);
        nvgMoveTo(nvg, 230, 430);
        nvgLineTo(nvg, 230,680);
        nvgStroke(nvg);
    }
}
