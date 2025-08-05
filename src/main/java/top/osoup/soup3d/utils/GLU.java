package top.osoup.soup3d.utils;

import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

public class GLU {
    private static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);

    public static void gluLookAt(
            float eyex, float eyey, float eyez,
            float centerx, float centery, float centerz,
            float upx, float upy, float upz) {

        float[] forward = new float[3];
        float[] up = new float[3];
        float[] side = new float[3];

        forward[0] = centerx - eyex;
        forward[1] = centery - eyey;
        forward[2] = centerz - eyez;

        normalize(forward);

        up[0] = upx;
        up[1] = upy;
        up[2] = upz;

        cross(forward, up, side);
        normalize(side);
        cross(side, forward, up); // recompute up

        float[] m = new float[16];
        identity(m);

        m[0] = side[0];
        m[4] = side[1];
        m[8] = side[2];

        m[1] = up[0];
        m[5] = up[1];
        m[9] = up[2];

        m[2] = -forward[0];
        m[6] = -forward[1];
        m[10] = -forward[2];

        matrix.clear();
        matrix.put(m);
        matrix.flip();

        GL11.glMultMatrixf(matrix);
        GL11.glTranslatef(-eyex, -eyey, -eyez);
    }

    private static void normalize(float[] v) {
        float r = (float)Math.sqrt(v[0]*v[0] + v[1]*v[1] + v[2]*v[2]);
        if (r == 0.0f) return;
        v[0] /= r;
        v[1] /= r;
        v[2] /= r;
    }

    private static void cross(float[] a, float[] b, float[] result) {
        result[0] = a[1]*b[2] - a[2]*b[1];
        result[1] = a[2]*b[0] - a[0]*b[2];
        result[2] = a[0]*b[1] - a[1]*b[0];
    }

    private static void identity(float[] m) {
        for (int i = 0; i < 16; i++) m[i] = 0.0f;
        m[0] = m[5] = m[10] = m[15] = 1.0f;
    }
}
