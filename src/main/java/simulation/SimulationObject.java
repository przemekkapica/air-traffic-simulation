package simulation;

public abstract class SimulationObject {
    private final String name;

    protected SimulationObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void update(float deltaTime) { }
}
