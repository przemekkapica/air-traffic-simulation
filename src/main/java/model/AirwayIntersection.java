package model;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import simulation.GraphicsContext;
import simulation.IPositionedObject;
import simulation.IRenderableObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.lwjgl.nanovg.NanoVG.*;

public class AirwayIntersection extends AirwayFragment implements IPositionedObject, IRenderableObject {

    @Override
    public float getLength() {
        return 10.0f;
    }
    private static final NVGColor COlOR = GraphicsContext.colorFromRgb(19, 112, 145);

    private final Set<AirwaySegment> m_outbound;
    private final Set<AirwaySegment> m_inbound;

    private AirwaySegment nextSegment;
    private final Vector2f position;

    @Override
    public Vector2f getPosition() {
        return new Vector2f(position.x, position.y);
    }

    @Override
    public void glRender(GraphicsContext context) {
    }

    @Override
    public void nvgRender(long nvg) {
        nvgBeginPath(nvg);
        nvgCircle(nvg, position.x, position.y, 10.0f);
        nvgFillColor(nvg, COlOR);
        nvgFill(nvg);
        nvgClosePath(nvg);

        // render label
        nvgFontSize(nvg, 16.0f);
        nvgFontFace(nvg, "font");
        nvgTextAlign(nvg, NVG_ALIGN_MIDDLE | NVG_ALIGN_BOTTOM);
        nvgText(nvg, position.x, position.y - 30, String.format("%s", getName()));
        nvgText(nvg, position.x, position.y - 15, String.format("to: %s", getNextFragment().getNextFragment().getName()));
    }

    @Override
    public AirwaySegment getNextFragment() {
        return nextSegment;
    }

    void addOutboundSegment(AirwaySegment segment) {
        m_outbound.add(segment);

        if (nextSegment == null) {
            nextSegment = segment;
        }
    }

    void addInboundSegment(AirwaySegment segment) {
        m_inbound.add(segment);
    }

    public void setNextSegmentByName(String name) {
        for (AirwaySegment segment : m_outbound) {
            if (segment.getName().equals("segment_" + name)) {
                nextSegment = segment;
                return;
            }
        }
        System.out.println("ERROR You cannot go in that direction");
    }

    public AirwayIntersection(String name, Vector2f position) {
        super(name);
        this.position = new Vector2f(position.x, position.y);

        m_outbound = new HashSet<>();
        m_inbound = new HashSet<>();
    }
}
