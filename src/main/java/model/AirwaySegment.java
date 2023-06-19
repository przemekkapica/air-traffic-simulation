package model;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import simulation.GraphicsContext;
import simulation.IRenderableObject;

import static org.lwjgl.nanovg.NanoVG.*;

public class AirwaySegment extends AirwayFragment implements IRenderableObject {
    private static final NVGColor COlOR_WORKING = GraphicsContext.colorFromRgb(127, 195, 219);
    private static final NVGColor COLOR_BROKEN = GraphicsContext.colorFromRgb(70, 70, 70);

    private final AirwayIntersection m_startIntersection;
    private final AirwayIntersection m_endIntersection;

    private final Vector2f m_startPosition;
    private final Vector2f m_endPosition;

    private final float m_length;

    private boolean broken;

    Vector2f getDirection() {
        Vector2f pos = new Vector2f(m_endPosition);
        return pos.sub(m_startPosition).normalize();
    }

    @Override
    public float getLength() {
        return m_length;
    }

    @Override
    public AirwayFragment getNextFragment() {
        return m_endIntersection;
    }

    public AirwayIntersection getEndIntersection() {
        return m_endIntersection;
    }

    public AirwayIntersection getStartIntersection() {
        return m_startIntersection;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken(boolean newStatus) {
        broken = newStatus;
    }

    public AirwaySegment(String name, AirwayIntersection start, AirwayIntersection end) {
        super(name);

        m_startIntersection = start;
        m_endIntersection = end;

        m_startPosition = m_startIntersection.getPosition();
        m_endPosition = m_endIntersection.getPosition();

        m_length = m_startIntersection.getPosition().distance(m_endIntersection.getPosition());

        m_startIntersection.addOutboundSegment(this);
        m_endIntersection.addInboundSegment(this);


            broken = false;
    }

    @Override
    public void glRender(GraphicsContext context) {
    }

    @Override
    public void nvgRender(long nvg) {
        nvgBeginPath(nvg);
        nvgMoveTo(nvg, m_startPosition.x, m_startPosition.y);
        nvgLineTo(nvg, m_endPosition.x, m_endPosition.y);

        if (broken) {
            nvgStrokeColor(nvg, COLOR_BROKEN);
        } else {
            nvgStrokeColor(nvg, COlOR_WORKING);
        }

        nvgStrokeWidth(nvg, 5.0f);
        nvgStroke(nvg);
        nvgClosePath(nvg);
    }
}
