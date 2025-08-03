package top.osoup.soup3d.render;

import org.lwjgl.opengl.GL11;
import top.osoup.soup3d.shader.FPL;
import top.osoup.soup3d.utils.Vec3;
import top.osoup.soup3d.utils.Vec5;

import java.util.List;

public class Face {
    public final int mode;
    public final FPL surface;
    public final List<Vec5> vertex;
    public final Vec3 normal;

    /**
     * 构造方法
     * @param mode 渲染模式，填入Face.ShapeType中的常量或OpenGL常量
     * @see ShapeType
     * @param surface 纹理
     * @param vertex 顶点列表
     */
    public Face(int mode, FPL surface, List<Vec5> vertex) {
        this.mode = mode;
        this.surface = surface;
        this.vertex = vertex;

        if (vertex.size() >= 3) {
            Vec5 v0 = vertex.get(0);
            Vec5 v1 = vertex.get(1);
            Vec5 v2 = vertex.get(2);

            Vec3 u = new Vec3(v1.x - v0.x, v1.y - v0.y, v1.z - v0.z);
            Vec3 v = new Vec3(v2.x - v0.x, v2.y - v0.y, v2.z - v0.z);

            this.normal = new Vec3(
                    u.y * v.z - u.z * v.y,
                    u.z * v.x - u.x * v.z,
                    u.x * v.y - u.y * v.x
            );
        } else {
            this.normal = new Vec3(0, 0, 1);
        }
    }

    public static class ShapeType {
        public static final int LINE_B = GL11.GL_LINES;
        public static final int LINE_S = GL11.GL_LINE_STRIP;
        public static final int LINE_L = GL11.GL_LINE_LOOP;
        public static final int TRIANGLE_B = GL11.GL_TRIANGLES;
        public static final int TRIANGLE_S = GL11.GL_TRIANGLE_STRIP;
        public static final int TRIANGLE_L = GL11.GL_TRIANGLE_FAN;
        public static final int QUAD = GL11.GL_QUADS;
    }
}
