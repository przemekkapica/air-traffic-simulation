package util;

import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.nvgCreateImageMem;
import static org.lwjgl.nanovg.NanoVGGL3.*;

public class GraphicsUtil {
    public GraphicsUtil() {}

    public int loadImage(String file, final long nvg) throws RuntimeException, IOException {
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

        final int image = nvgCreateImageMem(nvg, 0, imageBuffer);

        if (image == -1) {
            throw new RuntimeException(String.format("failed to add image %s%n", file));
        }
        return image;
    }
}
