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

    private float altitude;
    public Vector2f position = new Vector2f();

    public Queue<String> airports = new ArrayDeque<>();

    public Queue<String> segments = new ArrayDeque<>();

    private  boolean isRoadStable = true;

    // location data
    private AirTrafficElement airwayFragment;
    private float location;

    private Airport previousAirport;

    private final AircraftsDetailsDisplay detailsDisplay;

    public Aircraft(String name, float maxSpeed, float initialAltitude, AirTrafficElement fragment) {
        super(name);

        altitude = initialAltitude;
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
        detailsDisplay.update(getName(), speed, altitude, airwayFragment);
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

        renderIcon(nvg);
        renderLabel(nvg);
    }

    private void renderIcon(long nvg) {
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

    private void renderLabel(long nvg) {
        nvgFillColor(nvg, GraphicsUtil.colorFromRgb(0, 0, 0));
        nvgFontSize(nvg, 13.0f);
        nvgFontFace(nvg, "font");
        nvgTextAlign(nvg, NVG_ALIGN_MIDDLE | NVG_ALIGN_BOTTOM);
        nvgText(nvg, position.x + 14, position.y, String.format("%s", getName()));
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

    public void accelerate(float maxSpeed) {
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
        while (altitude < Constants.MAX_ALTITUDE) {
            setSpeed(altitude + 100);
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

    public void setAltitude(float altitude) {
        this.altitude = altitude;
    }

    public void updatePositionFrom(String positionString) {
        String[] split = positionString.split(",");
        if (split.length != 2) {
            throw new IllegalArgumentException("Expected two coordinates, got: " + positionString);
        }
        try {
            // Update the pos attribute, which is a Vector2f object
            this.position.x = Float.parseFloat(split[0]);
            this.position.y = Float.parseFloat(split[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinate format in: " + positionString, e);
        }
    }


    public boolean isTooClose(Aircraft other) {
        // Calculate the Euclidean distance between this aircraft and the other aircraft
        double distance = Math.sqrt(Math.pow(this.position.x - other.position.x, 2) + Math.pow(this.position.y - other.position.y, 2));
        // Compare the distance with the minimum allowed distance
        return distance < Constants.MINIMUM_DISTANCE;
    }

    public void adjustAltitude() {
        // Assume that an "altitude" attribute exists and it's a float.
        // "ALTITUDE_CHANGE" is the amount by which to change the altitude.
        this.altitude += Constants.ALTITUDE_CHANGE;
    }
}
