package model.ui_elements;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import simulation.GraphicsContext;
import simulation.IRenderableObject;

import static org.lwjgl.nanovg.NanoVG.*;

public class Airway extends UIElement implements IRenderableObject {
    private static final NVGColor COlOR_WORKING = GraphicsContext.colorFromRgb(67, 138, 236);
    private static final NVGColor COLOR_BROKEN = GraphicsContext.colorFromRgb(70, 70, 70);
    private final Airport startAirport;
    private final Airport endAirport;
    private final Vector2f startPosition;
    private final Vector2f endPosition;
    private final float length;
    private boolean broken;

    public Airway(String name, Airport start, Airport end) {
        super(name);

        startAirport = start;
        endAirport = end;

        startPosition = startAirport.getPosition();
        endPosition = endAirport.getPosition();

        length = startAirport.getPosition().distance(endAirport.getPosition());

        startAirport.addOutboundSegment(this);
        endAirport.addInboundSegment(this);

        broken = false;
    }

    @Override
    public float getLength() {
        return length;
    }

    @Override
    public UIElement getNextFragment() {
        return endAirport;
    }

    @Override
    public void glRender(GraphicsContext context) { }

    @Override
    public void nvgRender(long nvg) {
        nvgBeginPath(nvg);
        nvgMoveTo(nvg, startPosition.x, startPosition.y);
        nvgLineTo(nvg, endPosition.x, endPosition.y);

        if (broken) {
            nvgStrokeColor(nvg, COLOR_BROKEN);
        } else {
            nvgStrokeColor(nvg, COlOR_WORKING);
        }

        nvgStrokeWidth(nvg, 2.0f);
        nvgStroke(nvg);
        nvgClosePath(nvg);
    }

    Vector2f getDirection() {
        Vector2f pos = new Vector2f(endPosition);
        return pos.sub(startPosition).normalize();
    }

    public Airport getEndAirport() {
        return endAirport;
    }

    public Airport getStartAirport() {
        return startAirport;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean newStatus) {
        broken = newStatus;
    }
}
