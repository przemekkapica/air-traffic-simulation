package simulation;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Simulation {
    private final static SimulationScene scene = new SimulationScene();

    public static SimulationScene getScene() {
        return scene;
    }

    private long window;
    private boolean isInitialized;

    public void initialize (int width, int height, String title) throws RuntimeException {
        if (isInitialized) {
            throw new RuntimeException ("simulation was already initialized");
        }

        if (!glfwInit ()) {
            throw new RuntimeException ("failed to initialize glfw");
        }

        // setup the window
        glfwDefaultWindowHints ();
        glfwWindowHint (GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint (GLFW_VISIBLE, GL_TRUE);

        glfwWindowHint (GLFW_SAMPLES, 4);

        window = glfwCreateWindow (width, height, title, NULL, NULL);

        isInitialized = true;
    }

    public void run () {
        if (!isInitialized) {
            throw new RuntimeException ("simulation not initialized");
        }

        // setup context in this thread
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities ();

        long lastFrame = System.currentTimeMillis();

        GraphicsContext context = new GraphicsContext(window);
        while (!glfwWindowShouldClose(window)) {
            // calculate delta time
            lastFrame = getLastFrame(lastFrame, context);
        }

        context.terminate();
        terminate();
    }

    private static long getLastFrame(long lastTick, GraphicsContext context) {
        long nvg;
        float deltaTime;
        long tick;
        long elapsed;
        tick = System.currentTimeMillis();
        elapsed = tick - lastTick;
        lastTick = tick;
        deltaTime = elapsed / 7000f;

        getScene().update(deltaTime);

        context.startFrame();
        getScene().glRender(context);

        nvg = context.nvgBegin();

        getScene().nvgRender(nvg);
        context.nvgEnd();

        // end frame
        context.endFrame();
        return lastTick;
    }

    private void terminate () {
        glfwDestroyWindow (window);
        glfwTerminate();
    }
}
