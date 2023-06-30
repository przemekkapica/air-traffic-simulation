package util;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.nvgCreateImageMem;

public class GraphicsUtil {
    public GraphicsUtil() {}

    public int loadImage(String file, long nvg) throws RuntimeException, IOException {
        byte[] imageBytes = null;

        try (InputStream stream = getClass().getResourceAsStream(file)) {
            if (stream != null) {
                imageBytes = stream.readAllBytes();
            }
        }

        if (imageBytes == null) {
            throw new RuntimeException(String.format("Failed to load image %s", file));
        }

        ByteBuffer imageBuffer = MemoryUtil.memCalloc(imageBytes.length + 1);
        imageBuffer.put(imageBytes);
        imageBuffer.put((byte) 0);
        imageBuffer.flip();

        final int image = nvgCreateImageMem(nvg, 0, imageBuffer);

        if (image == -1) {
            throw new RuntimeException(String.format("Failed to add image %s", file));
        }
        return image;
    }

    public static NVGColor colorFromRgb(int r, int g, int b) {
        NVGColor color = NVGColor.create();

        color.r(r / 255f);
        color.g(g / 255f);
        color.b(b / 255f);
        color.a(1.0f);

        return color;
    }
}