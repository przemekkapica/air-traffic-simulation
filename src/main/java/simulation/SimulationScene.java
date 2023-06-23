package simulation;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimulationScene {
    private final Map<String, SimulationObject> simulationObjects;

    SimulationScene() {
        simulationObjects = new LinkedHashMap<>();
    }

    Collection<SimulationObject> getAllObjects() {
        return simulationObjects.values();
    }

    public void addObject(SimulationObject object) {
        String objectName = object.getName();
        if (simulationObjects.containsKey (objectName)) {
            throw new RuntimeException(String.format("%s with that name already exists", objectName));
        }

        simulationObjects.put(objectName, object);
    }

    public void addObjects(SimulationObject[] objects) {
        for (var object : objects) {
            String objectName = object.getName();
            if (simulationObjects.containsKey (objectName)) {
                throw new RuntimeException(String.format("%s with that name already exists", objectName));
            }

            simulationObjects.put(objectName, object);
        }
    }

    public SimulationObject getObject(String name) {
        if (simulationObjects.containsKey (name)) {
            return simulationObjects.get(name);
        }

        return null;
    }

    void update(float deltaTime) {
        for (SimulationObject object : simulationObjects.values ()) {
            object.update(deltaTime);
        }
    }

    void glRender(GraphicsContext context) {
        for (SimulationObject object : simulationObjects.values ()) {
            if (object instanceof IRenderableObject) {
                ((IRenderableObject)object).glRender (context);
            }
        }
    }

    void nvgRender(long nvg) {
        for (SimulationObject object : simulationObjects.values ()) {
            if (object instanceof IRenderableObject) {
                ((IRenderableObject)object).nvgRender (nvg);
            }
        }
    }
}
