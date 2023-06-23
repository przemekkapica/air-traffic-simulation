package simulation;

import org.lwjgl.BufferUtils;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
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
    private final long window;
    private final long nvg;
    private final IntBuffer bufferWidth;
    private final IntBuffer bufferHeight;
    private int bgImage;

    public static NVGColor colorFromRgb(int r, int g, int b) {
        NVGColor color = NVGColor.create();

        color.r(r / 255f);
        color.g(g / 255f);
        color.b(b / 255f);
        color.a(1.0f);

        return color;
    }
    public void loadFont(String name, String file) throws RuntimeException, IOException {
        byte[] fontBytes = null;

        try (InputStream stream = getClass().getResourceAsStream(file)) {
            if (stream != null) {
                fontBytes = stream.readAllBytes();
            }
        }

        if (fontBytes == null) {
            throw new RuntimeException(String.format("failed to load font %s", file));
        }

        ByteBuffer fontBuffer = MemoryUtil.memCalloc(fontBytes.length + 1);
        fontBuffer.put(fontBytes);
        fontBuffer.put((byte) 0);
        fontBuffer.flip();

        int font = nvgCreateFontMem(nvg, name, fontBuffer, 0);
        if (font == -1) {
            throw new RuntimeException(String.format("failed to add font %s%n", name));
        }
    }

    public void loadImage(String file) throws RuntimeException, IOException {
        byte[] imageBytes = null;

        try (InputStream stream = getClass().getResourceAsStream(file)) {
            if (stream != null) {
                imageBytes = stream.readAllBytes();
            }
        }

        if (imageBytes == null) {
            throw new RuntimeException(String.format("failed to load image %s", file));
        }

        ByteBuffer imageBuffer = MemoryUtil.memCalloc(imageBytes.length + 1);
        imageBuffer.put(imageBytes);
        imageBuffer.put((byte) 0);
        imageBuffer.flip();

        bgImage = nvgCreateImageMem(nvg, 0, imageBuffer);
        if (bgImage == -1) {
            throw new RuntimeException(String.format("failed to add image %s%n", file));
        }
    }

    public GraphicsContext(long window) {
        this.window = window;

        // allocate buffers for window width and height
        bufferWidth = BufferUtils.createIntBuffer(1);
        bufferHeight = BufferUtils.createIntBuffer(1);

        nvg = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);

        try {
            loadFont("font", "/font.otf");
            loadImage("/map.jpg");  // Load the background image
        } catch (Exception e) {
            e.printStackTrace();
        }

        nvgFontFace(nvg, "font");
    }

    // private for this package
    void startFrame() {
        glfwGetWindowSize(window, bufferWidth, bufferHeight);
        glViewport(0, 0, bufferWidth.get(0), bufferHeight.get(0));

        // clear the viewport
        glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        // Draw the background image
        try (NVGPaint paint = NVGPaint.calloc()) {
            nvgImagePattern(nvg, 0, 0, bufferWidth.get(0), bufferHeight.get(0), 0, bgImage, 1, paint);
            nvgBeginPath(nvg);
            nvgRect(nvg, 0, 0, bufferWidth.get(0), bufferHeight.get(0));
            nvgFillPaint(nvg, paint);
            nvgFill(nvg);
        }
    }

    long nvgBegin() {
        nvgBeginFrame(nvg, bufferWidth.get(0), bufferHeight.get(0), 1);
        return nvg;
    }

    void nvgEnd() {
        nvgEndFrame(nvg);
    }

    void endFrame() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    void terminate() {
        nvgDelete(nvg);
    }
}
