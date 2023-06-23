package model;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import simulation.GraphicsContext;
import simulation.IRenderableObject;
import simulation.SimulationObject;
import util.Constants;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import static org.lwjgl.nanovg.NanoVG.*;

public class Aircraft extends SimulationObject implements IRenderableObject {
    private static final NVGColor COlOR = GraphicsContext.colorFromRgb(11, 102, 52);

    private final float maxSpeed;
    private float speed;
    private NVGColor color;

    private float attitude;
    public Vector2f pos = new Vector2f();

    public Queue<String> intersections = new ArrayDeque<>();

    public Queue<String> segments = new ArrayDeque<>();

    private  boolean isRoadStable = true;

    // location data
    private GraphicalElement airwayFragment;
    private float location;

    private Airport previousIntersection;

    @Override
    public void update(float deltaTime) {
        float deltaLocation = speed * deltaTime;
        location = location + deltaLocation;
        //if (speed < maxSpeed) speed = speed+10;

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
        if (airwayFragment instanceof Airway segment) {
            Vector2f dir = segment.getDirection();

            pos = segment.getStartIntersection().getPosition();
            pos = pos.add(dir.mul(location));
        } else if (airwayFragment instanceof Airport intersection) {
            pos = intersection.getPosition();
        }

        nvgBeginPath(nvg);
        nvgCircle(nvg, pos.x, pos.y, 10.0f);
        nvgFillColor(nvg, color);
        nvgFill(nvg);
        nvgClosePath(nvg);

        // render label
        nvgFillColor(nvg, GraphicsContext.colorFromRgb(0, 0, 0));
        nvgFontSize(nvg, 13.0f);
        nvgFontFace(nvg, "font");
        nvgTextAlign(nvg, NVG_ALIGN_MIDDLE | NVG_ALIGN_BOTTOM);
        nvgText(nvg, pos.x + 12, pos.y - 15, String.format("%s", getName()));
        nvgText(nvg, pos.x + 12, pos.y, String.format("speed: %.2f", speed));
        nvgText(nvg, pos.x + 12, pos.y + 15, String.format("attitude: %.2f", attitude));
    }

    public void setColor(int r, int g, int b) {
        color = GraphicsContext.colorFromRgb(r, g, b);
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setSpeed(float speed) {
        this.speed = Math.min(Math.abs(speed), maxSpeed);
    }

    public void accelerate() {
        while (speed < maxSpeed) {
            setSpeed(speed + 10);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    public void accelerate_decelerate() {
        while (speed < maxSpeed) {
            setSpeed(speed + 10);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        while (speed > 0) {
            setSpeed(speed - 10);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void decelerate() {

    }

    public void ascend_descend() {
        while (attitude < Constants.MAX_ATTITUDE) {
            setSpeed(attitude + 100);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        while (speed > 0) {
            setSpeed(speed - 100);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    public void descent() {

    }

    public boolean isRoadStable() {
        return isRoadStable;
    }

    public void setRoadStable(boolean roadStable) {
        isRoadStable = roadStable;
    }

    public Airport getPreviousIntersection() {
        return previousIntersection;
    }

    public void setPreviousIntersection(Airport previousIntersection) {
        this.previousIntersection = previousIntersection;
    }

    public boolean isTraversingSegment() {
        return airwayFragment.getName().contains("segment");
    }

    public Aircraft(String name, float maxSpeed, float initialAttitude, GraphicalElement fragment) {
        super(name);

        attitude = initialAttitude;
        this.maxSpeed = Math.abs(maxSpeed);
        airwayFragment = fragment;
        location = 0.0f;
        color = Aircraft.COlOR;

        airwayFragment.enter(this);
    }

    public void setAttitude(float attitude) {
        this.attitude = attitude;
    }
}
