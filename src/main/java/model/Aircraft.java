package model;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import simulation.GraphicsContext;
import simulation.IRenderableObject;
import simulation.SimulationObject;

import java.util.ArrayDeque;
import java.util.Queue;

import static org.lwjgl.nanovg.NanoVG.*;

public class Aircraft extends SimulationObject implements IRenderableObject {
    private static final NVGColor COlOR = GraphicsContext.colorFromRgb(11, 102, 52);

    private final float m_maxSpeed;
    private float speed;
    private NVGColor m_color;

    private float attitude;

    public Queue<String> intersections = new ArrayDeque<>();

    public Queue<String> segments = new ArrayDeque<>();

    private  boolean isRoadStable = true;

    // location data
    private AirwayElement airwayFragment;
    private float location;

    private AirwayIntersection previousIntersection;

    @Override
    public void update(float deltaTime) {
        float deltaLocation = speed * deltaTime;
        location = location + deltaLocation;

        if (location >= airwayFragment.getLength ()) {
            location = 0.0f;

            // leave and go to the next fragment
            airwayFragment.leave(this);
            airwayFragment = airwayFragment.getNextFragment();

            if (airwayFragment == null) {
                throw new RuntimeException(String.format("aircraft %s out of bounds", getName()));
            }

            airwayFragment.enter(this);
        }
    }


    @Override
    public void glRender(GraphicsContext context) { }

    @Override
    public void nvgRender(long nvg) {
        Vector2f pos = new Vector2f();

        if (airwayFragment instanceof AirwaySegment segment) {
            Vector2f dir = segment.getDirection();

            pos = segment.getStartIntersection().getPosition();
            pos = pos.add(dir.mul(location));
        } else if (airwayFragment instanceof AirwayIntersection intersection) {
            pos = intersection.getPosition();
        }

        nvgBeginPath(nvg);
        nvgCircle(nvg, pos.x, pos.y, 10.0f);
        nvgFillColor(nvg, m_color);
        nvgFill(nvg);
        nvgClosePath(nvg);

        // render label
        nvgFontSize(nvg, 16.0f);
        nvgFontFace(nvg, "font");
        nvgTextAlign(nvg, NVG_ALIGN_MIDDLE | NVG_ALIGN_BOTTOM);
        nvgText(nvg, pos.x + 12, pos.y - 15, String.format("%s", getName()));
        nvgText(nvg, pos.x + 12, pos.y, String.format("speed: %.2f", speed));
        nvgText(nvg, pos.x + 12, pos.y + 15, String.format("attitude: %.2f", attitude));
    }

    void setLocation(float location) {
        this.location = location;
    }

    public void setColor(int r, int g, int b) {
        m_color = GraphicsContext.colorFromRgb(r, g, b);
    }

    public AirwayElement getAirwayFragment() {
        return airwayFragment;
    }

    public float getLocation() {
        return location;
    }

    public float getAttitude() {
        return attitude;
    }

    public float getMaxSpeed() {
        return m_maxSpeed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = Math.min(Math.abs(speed), m_maxSpeed);
    }

    public boolean isRoadStable() {
        return isRoadStable;
    }

    public void setRoadStable(boolean roadStable) {
        isRoadStable = roadStable;
    }

    public AirwayIntersection getPreviousIntersection() {
        return previousIntersection;
    }

    public void setPreviousIntersection(AirwayIntersection previousIntersection) {
        this.previousIntersection = previousIntersection;
    }

    public boolean isTraversingSegment() {
        return airwayFragment.getName().contains("segment");
    }

    public Aircraft(String name, float maxSpeed, float initialAttitude, AirwayElement fragment) {
        super(name);

        attitude = initialAttitude;
        m_maxSpeed = Math.abs(maxSpeed);
        airwayFragment = fragment;
        location = 0.0f;
        m_color = Aircraft.COlOR;

        airwayFragment.enter(this);
    }

    public void setAttitude(float attitude) {
        this.attitude = attitude;
    }
}
