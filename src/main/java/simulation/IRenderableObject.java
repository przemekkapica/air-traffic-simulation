package simulation;

public interface IRenderableObject {
    void glRender (GraphicsContext context);

    void nvgRender (long nvg);
}
