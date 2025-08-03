package top.osoup.soup3d.utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class ShaderUtils {
    public static int imgToTexture(BufferedImage img, int textureId, int textureUnit) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0 + textureUnit);
        img = asRGBA(img);
        int width = img.getWidth();
        int height = img.getHeight();

        if (textureId == 0) {
            textureId = GL11.glGenTextures();
        }

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bufferedImageToRGBA(img));
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

        return textureId;
    }

    public static BufferedImage asRGBA(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_4BYTE_ABGR) {
            return image;
        }

        BufferedImage rgbaImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_4BYTE_ABGR
        );

        Graphics2D g = rgbaImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return rgbaImage;
    }

    public static ByteBuffer bufferedImageToRGBA(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage rgbaImg = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        rgbaImg.getGraphics().drawImage(img, 0, 0, null);

        byte[] pixels = new byte[width * height * 4];
        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = rgbaImg.getRGB(x, y);

                pixels[index++] = (byte)((argb >> 16) & 0xFF);  // R
                pixels[index++] = (byte)((argb >> 8) & 0xFF);   // G
                pixels[index++] = (byte)(argb & 0xFF);          // B
                pixels[index++] = (byte)((argb >> 24) & 0xFF);  // A
            }
        }

        ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length);
        buffer.put(pixels);
        buffer.flip();
        return buffer;
    }

}
