package simulation;

import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.opengl.GL33.*;

public class GraphicsContext {
    private final long m_window;
    private final long m_nvg;
    private final IntBuffer m_bufferWidth;
    private final IntBuffer m_bufferHeight;

    public static NVGColor colorFromRgb (int r, int g, int b) {
        NVGColor color = NVGColor.create ();

        color.r (r / 255f);
        color.g (g / 255f);
        color.b (b / 255f);
        color.a (1.0f);

        return color;
    }

    public void loadFont (String name, String file) throws RuntimeException, IOException {
        byte [] fontBytes = null;

        try (InputStream stream = getClass ().getResourceAsStream (file)) {
            if (stream != null) {
                fontBytes = stream.readAllBytes ();
            }
        }

        if (fontBytes == null) {
            throw new RuntimeException (String.format ("failed to load font %s", file));
        }

        ByteBuffer fontBuffer = MemoryUtil.memCalloc (fontBytes.length + 1);
        fontBuffer.put (fontBytes);
        fontBuffer.put ((byte) 0);
        fontBuffer.flip ();

        int font = nvgCreateFontMem (m_nvg, name, fontBuffer, 0);
        if (font == -1) {
            throw new RuntimeException (String.format ("failed to add font %s%n", name));
        }
    }

    public GraphicsContext (long window) {
        m_window = window;

        // allocate buffers for window width and height
        m_bufferWidth = BufferUtils.createIntBuffer (1);
        m_bufferHeight = BufferUtils.createIntBuffer (1);

        m_nvg = nvgCreate (NVG_ANTIALIAS | NVG_STENCIL_STROKES);

        try {
            loadFont ("font", "/font.otf");
        } catch (Exception e) {
            e.printStackTrace ();
        }

        nvgFontFace (m_nvg, "font");
    }

    // private for this package
    void startFrame () {
        glfwGetWindowSize (m_window, m_bufferWidth, m_bufferHeight);
        glViewport (0, 0, m_bufferWidth.get (0), m_bufferHeight.get (0));

        // clear the viewport
        glClearColor (0.8f, 0.8f, 0.8f, 1.0f);
        glClear (GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    long nvgBegin () {
        nvgBeginFrame (m_nvg, m_bufferWidth.get (0), m_bufferHeight.get (0), 1);
        return m_nvg;
    }

    void nvgEnd () {
        nvgEndFrame (m_nvg);
    }

    void endFrame () {
        glfwSwapBuffers (m_window);
        glfwPollEvents ();
    }

    void terminate () {
        nvgDelete (m_nvg);
    }
}
