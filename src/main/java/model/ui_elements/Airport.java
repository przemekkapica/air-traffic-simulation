package model.ui_elements;

import org.joml.Vector2f;
import org.lwjgl.nanovg.NVGColor;
import simulation.GraphicsContext;
import simulation.IPositionedObject;
import simulation.IRenderableObject;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.nanovg.NanoVG.*;

public class Airport extends UIElement implements IPositionedObject, IRenderableObject {

    @Override
    public float getLength() {
        return 10.0f;
    }
    private static final NVGColor COlOR = GraphicsContext.colorFromRgb(67, 138, 236);

    private final Set<Airway> m_outbound;
    private final Set<Airway> inbound;

    private Airway nextSegment;
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
        nvgCircle(nvg, position.x, position.y, 7.0f);
        nvgFillColor(nvg, COlOR);
        nvgFill(nvg);
        nvgClosePath(nvg);

        // render label
        nvgFontSize(nvg, 16.0f);
        nvgFillColor(nvg,  GraphicsContext.colorFromRgb(0, 0, 0));
        nvgFontFace(nvg, "font");
        nvgTextAlign(nvg, NVG_ALIGN_MIDDLE | NVG_ALIGN_BOTTOM);
        nvgText(nvg, position.x + 15, position.y + 2, String.format("%s", getName()));
    }

    @Override
    public Airway getNextFragment() {
        return nextSegment;
    }

    void addOutboundSegment(Airway segment) {
        m_outbound.add(segment);

        if (nextSegment == null) {
            nextSegment = segment;
        }
    }

    void addInboundSegment(Airway segment) {
        inbound.add(segment);
    }

    public void setNextSegmentByName(String name) {
        for (Airway segment : m_outbound) {
            if (segment.getName().equals("segment_" + name)) {
                nextSegment = segment;
                return;
            }
        }
        System.out.println("ERROR You cannot go in that direction");
    }

    public Airport(String name, Vector2f position) {
        super(name);
        this.position = new Vector2f(position.x, position.y);

        m_outbound = new HashSet<>();
        inbound = new HashSet<>();
    }
}
