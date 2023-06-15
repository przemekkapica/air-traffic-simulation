package simulation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationScene {
    private final Map<String, SimulationObject> m_objects;

    SimulationScene () {
        m_objects = new LinkedHashMap<>();
    }

    Collection<SimulationObject> getAllObjects () {
        return m_objects.values ();
    }

    public void addObject (SimulationObject object) {
        String objectName = object.getName ();
        if (m_objects.containsKey (objectName)) {
            throw new RuntimeException (String.format ("object with name %s already exits", objectName));
        }

        m_objects.put (objectName, object);
    }

    public SimulationObject getObject (String name) {
        if (m_objects.containsKey (name)) {
            return m_objects.get (name);
        }

        return null;
    }

    void update (float deltaTime) {
        for (SimulationObject object : m_objects.values ()) {
            object.update (deltaTime);
        }
    }

    void glRender (GraphicsContext context) {
        for (SimulationObject object : m_objects.values ()) {
            if (object instanceof IRenderableObject) {
                ((IRenderableObject) object).glRender (context);
            }
        }
    }

    void nvgRender (long nvg) {
        for (SimulationObject object : m_objects.values ()) {
            if (object instanceof IRenderableObject) {
                ((IRenderableObject) object).nvgRender (nvg);
            }
        }
    }
}
