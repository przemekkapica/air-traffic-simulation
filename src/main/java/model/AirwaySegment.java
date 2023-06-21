package model;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import simulation.GraphicsContext;
import simulation.IRenderableObject;

import static org.lwjgl.nanovg.NanoVG.*;

public class AirwaySegment extends AirwayElement implements IRenderableObject {
    private static final NVGColor COlOR_WORKING = GraphicsContext.colorFromRgb(127, 195, 219);
    private static final NVGColor COLOR_BROKEN = GraphicsContext.colorFromRgb(70, 70, 70);
    private final AirwayIntersection startIntersection;
    private final AirwayIntersection endIntersection;
    private final Vector2f startPosition;
    private final Vector2f endPosition;
    private final float length;
    private boolean broken;

    public AirwaySegment(String name, AirwayIntersection start, AirwayIntersection end) {
        super(name);

        startIntersection = start;
        endIntersection = end;

        startPosition = startIntersection.getPosition();
        endPosition = endIntersection.getPosition();

        length = startIntersection.getPosition().distance(endIntersection.getPosition());

        startIntersection.addOutboundSegment(this);
        endIntersection.addInboundSegment(this);

        broken = false;
    }

    @Override
    public float getLength() {
        return length;
    }

    @Override
    public AirwayElement getNextFragment() {
        return endIntersection;
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

        nvgStrokeWidth(nvg, 5.0f);
        nvgStroke(nvg);
        nvgClosePath(nvg);
    }

    Vector2f getDirection() {
        Vector2f pos = new Vector2f(endPosition);
        return pos.sub(startPosition).normalize();
    }

    public AirwayIntersection getEndIntersection() {
        return endIntersection;
    }

    public AirwayIntersection getStartIntersection() {
        return startIntersection;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean newStatus) {
        broken = newStatus;
    }
}
