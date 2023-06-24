package model.ui_elements;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import simulation.GraphicsContext;
import simulation.IRenderableObject;
import simulation.SimulationObject;
import util.Constants;
import util.GraphicsUtil;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import static org.lwjgl.nanovg.NanoVG.*;

public class Aircraft extends SimulationObject implements IRenderableObject {
    private static final NVGColor COlOR = GraphicsUtil.colorFromRgb(11, 102, 52);
    private final float maxSpeed;
    private float speed;
    private NVGColor color;

    private float attitude;
    public Vector2f position = new Vector2f();

    public Queue<String> airports = new ArrayDeque<>();

    public Queue<String> segments = new ArrayDeque<>();

    private  boolean isRoadStable = true;

    // location data
    private AirTrafficElement airwayFragment;
    private float location;

    private Airport previousAirport;

    private final AircraftsDetailsDisplay detailsDisplay;

    public Aircraft(String name, float maxSpeed, float initialAttitude, AirTrafficElement fragment) {
        super(name);

        attitude = initialAttitude;
        this.maxSpeed = Math.abs(maxSpeed);
        speed = 0.0f;
        airwayFragment = fragment;
        location = 0.0f;
        color = Aircraft.COlOR;

        airwayFragment.enter(this);

        detailsDisplay = AircraftsDetailsDisplay.getInstance();
    }

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
        detailsDisplay.update(getName(), speed, attitude, airwayFragment);
    }


    @Override
    public void glRender(GraphicsContext context) { }

    @Override
    public void nvgRender(long nvg)  {
        if (airwayFragment instanceof Airway segment) {
            Vector2f dir = segment.getDirection();

            position = segment.getStartAirport().getPosition();
            position = position.add(dir.mul(location));
        } else if (airwayFragment instanceof Airport airport) {
            position = airport.getPosition();
        }

        _renderIcon(nvg);

        _renderLabel(nvg);
    }

    private void _renderIcon(long nvg) {
        final GraphicsUtil graphicsUtil = new GraphicsUtil();

        int aircraftIcon = -1;
        try {
             aircraftIcon = graphicsUtil.loadImage("/images/aircraft.png", nvg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // render aircraft icon
        try (NVGPaint paint = NVGPaint.calloc()) {
            nvgImagePattern(nvg, position.x - 8, position.y - 16, 30, 30, 0, aircraftIcon, 1, paint);
            nvgBeginPath(nvg);
            nvgRect(nvg, position.x - 8, position.y - 16, 30, 30);
            nvgFillPaint(nvg, paint);
            nvgFill(nvg);
        } catch (Exception e) {
            nvgCircle(nvg, position.x, position.y, 5.0f);
            nvgBeginPath(nvg);
            nvgFillColor(nvg, color);
            nvgFill(nvg);
            nvgClosePath(nvg);
        }
    }

    private void _renderLabel(long nvg) {
        nvgFillColor(nvg, GraphicsUtil.colorFromRgb(0, 0, 0));
        nvgFontSize(nvg, 13.0f);
        nvgFontFace(nvg, "font");
        nvgTextAlign(nvg, NVG_ALIGN_MIDDLE | NVG_ALIGN_BOTTOM);
        nvgText(nvg, position.x + 14, position.y, String.format("%s", getName()));
//        nvgText(nvg, position.x + 14, position.y, String.format("speed: %.2f", speed));
//        nvgText(nvg, position.x + 14, position.y + 15, String.format("attitude: %.2f", attitude));
    }

    public void setColor(int r, int g, int b) {
        color = GraphicsUtil.colorFromRgb(r, g, b);
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
        while (speed > 0) {
            setSpeed(speed - 10);
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
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

    public Airport getPreviousAirport() {
        return previousAirport;
    }

    public void setPreviousAirport(Airport previousAirport) {
        this.previousAirport = previousAirport;
    }

    public boolean isTraversingSegment() {
        return airwayFragment.getName().contains("segment");
    }

    public void setAttitude(float attitude) {
        this.attitude = attitude;
    }
}
