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
        public AircraftDetails(float speed, float attitude) {
            this.speed = speed;
            this.attitude = attitude;
        }
        public float speed;
        public float attitude;
    }
    private final HashMap<String, AircraftDetails> aircraftDetailsMap = new HashMap<>();

    public AircraftsDetailsDisplay(List<String> aircraftNames) {
        super("aircraftsDetailsDisplay");
        for (var name : aircraftNames) {
            aircraftDetailsMap.put(name, new AircraftDetails(0, 0));
        }
    }
    @Override
    public void glRender(GraphicsContext context) { }

    @Override
    public void nvgRender(long nvg) {
        nvgFontSize(nvg, 16.0f);
        nvgFillColor(nvg,  GraphicsUtil.colorFromRgb(0, 0, 0));
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

    public void update(String name, float speed, float attitude) {
        aircraftDetailsMap.put(name, new AircraftDetails(speed, attitude));
    }

    private void _renderAircraftsDetails(long nvg) {
        int i = 1;
        for (Map.Entry<String, AircraftDetails> entry : aircraftDetailsMap.entrySet()) {
            String name = entry.getKey();
            float speed = entry.getValue().speed;
            float attitude = entry.getValue().attitude;

            nvgText(nvg, 30, 420 + i*30 , name);
            nvgText(nvg, 90, 420 + i*30 , String.valueOf(speed));
            nvgText(nvg, 150, 420 + i*30, String.valueOf(attitude));
            i++;
        }
    }

    private static void _renderLabels(long nvg) {
        nvgText(nvg, 30, 420, "Plane");
        nvgText(nvg, 90, 420, "Speed");
        nvgText(nvg, 150, 420, "Attitude");
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
