package simulation;

import org.joml.Vector2f;

public interface IPositionedObject {
    void setPosition(Vector2f position);

    Vector2f getPosition();
}
