package simulation;

import model.AirwaySegment;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;

import java.nio.DoubleBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Simulation {
    private static SimulationScene m_scene = new SimulationScene();

    public static void restartScene() {
        m_scene = new SimulationScene();
    }

    public static SimulationScene getScene() {
        return m_scene;
    }

    private long m_window;
    private boolean m_initialized;

    private void onMouseClick(long window, int button, int action, int mods) {
        DoubleBuffer bufferPosX = BufferUtils.createDoubleBuffer(1);
        DoubleBuffer bufferPosY = BufferUtils.createDoubleBuffer(1);

        // get cursor pos
        glfwGetCursorPos(window, bufferPosX, bufferPosY);

        double mouseX = bufferPosX.get(0);
        double mouseY = bufferPosY.get(0);

        boolean isShiftClick =(mods & GLFW_MOD_SHIFT) == GLFW_MOD_SHIFT;
        boolean isAltClick =(mods & GLFW_MOD_ALT) == GLFW_MOD_ALT;


        // lmb shift click deletes a segment
        Vector2f p0 = new Vector2f((float) mouseX,(float) mouseY);

        if (button == GLFW_MOUSE_BUTTON_LEFT && (isShiftClick || isAltClick)) {
            for (SimulationObject object : m_scene.getAllObjects ()) {
                if (object instanceof AirwaySegment segment) {
                    Vector2f p1 = segment.getStartIntersection().getPosition();
                    Vector2f p2 = segment.getEndIntersection().getPosition();

                    float numerator = Math.abs(((p2.x - p1.x) *(p1.y - p0.y)) -((p2.y - p1.y) *(p1.x - p0.x)));
                    float lengthSquared =((p2.x - p1.x) *(p2.x - p1.x)) +((p2.y - p1.y) *(p2.y - p1.y));
                    float denominator =(float) Math.sqrt(lengthSquared);
                    float distFromLine = numerator / denominator;

                    if (distFromLine < 7) {
                        if (isShiftClick) {
                            System.out.printf("break segment %s%n", segment.getName());
                            segment.setBroken(true);
                        } else {
                            System.out.printf("repair segment %s%n", segment.getName ());
                            segment.setBroken (false);
                        }

                        break;
                    }
                }
            }
        }
    }

    private void onKeyEvent (long window, int key, int scancode, int action, int mods) {
        // todo: key handling
    }

    public void initialize (int width, int height, String title) throws RuntimeException {
        if (m_initialized) {
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

        m_window = glfwCreateWindow (width, height, title, NULL, NULL);

        // initialize input callbacks
        glfwSetMouseButtonCallback (m_window, this::onMouseClick);
        glfwSetKeyCallback (m_window, this::onKeyEvent);

        m_initialized = true;
    }

    public void run () {
        if (!m_initialized) {
            throw new RuntimeException ("simulation not initialized");
        }

        // setup context in this thread
        glfwMakeContextCurrent (m_window);
        glfwSwapInterval (1);
        glfwShowWindow (m_window);

        GL.createCapabilities ();

        long lastTick = System.currentTimeMillis();
        long tick;
        long elapsed;
        float deltaTime;

        // handle for nanovg
        long nvg;

        GraphicsContext context = new GraphicsContext (m_window);
        while (!glfwWindowShouldClose(m_window)) {
            // calculate delta time
            tick = System.currentTimeMillis();
            elapsed = tick - lastTick;
            lastTick = tick;
            deltaTime = elapsed / 1000f;

            getScene().update(deltaTime);

            context.startFrame();
            getScene().glRender(context);

            nvg = context.nvgBegin ();
            getScene().nvgRender(nvg);
            context.nvgEnd();

            // end frame
            context.endFrame();
        }

        context.terminate();
        terminate();
    }

    private void terminate () {
        glfwDestroyWindow (m_window);
        glfwTerminate();
    }
}
