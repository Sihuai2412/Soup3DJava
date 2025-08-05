package top.osoup.soup3d.render;

import org.lwjgl.opengl.GL11;
import top.osoup.soup3d.utils.GLU;

public class Camera {
    public float x = 0, y = 0, z = 0;
    public float yaw = 0, pitch = 0, roll = 0;
    private static final Camera CAMERA = new Camera();

    private Camera() {}

    public static Camera getInstance() {
        return CAMERA;
    }

    public void goTo(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        update();
    }

    public void turn(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
        update();
    }

    public void update() {
        float centerX = 0, centerY = 0, centerZ = -1;
        float upX = 0, upY = 1, upZ = 0;

        float[] roll1 = rotated(centerX, centerY, 0, 0, roll);
        centerX = roll1[0];
        centerY = roll1[1];
        float[] roll2 = rotated(upX, upY, 0, 0, roll);
        upX = roll2[0];
        upY = roll2[1];

        float[] pitch1 = rotated(centerY, centerZ, 0, 0, pitch);
        centerY = pitch1[0];
        centerZ = pitch1[1];
        float[] pitch2 = rotated(upY, upZ, 0, 0, pitch);
        upY = pitch2[0];
        upZ = pitch2[1];

        float[] yaw1 = rotated(centerX, centerZ, 0, 0, yaw);
        centerX = yaw1[0];
        centerZ = yaw1[1];
        float[] yaw2 = rotated(upX, upZ, 0, 0, yaw);
        upX = yaw2[0];
        upZ = yaw2[1];

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GLU.gluLookAt(
                x, y, z,
                centerX + x, centerY + y, centerZ + z,
                upX, upY, upZ
        );
    }

    private float[] rotated(float Xa, float Ya, float Xb, float Yb, float degree) {
        double rad = Math.toRadians(degree);
        float outx = (float)((Xa - Xb) * Math.cos(rad) - (Ya - Yb) * Math.sin(rad) + Xb);
        float outy = (float)((Xa - Xb) * Math.sin(rad) + (Ya - Yb) * Math.cos(rad) + Yb);
        return new float[]{outx, outy};
    }
}