package top.osoup.soup3d.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import top.osoup.soup3d.Soup3D;
import top.osoup.soup3d.utils.math.Vec5;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Model {
    public final Face[] faces;
    public static Set<Model> stableShapes = new HashSet<>();
    public final int listId;

    public float x;
    public float y;
    public float z;

    public float yaw;
    public float pitch;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return listId == model.listId && Float.compare(x, model.x) == 0 && Float.compare(y, model.y) == 0 && Float.compare(z, model.z) == 0 && Float.compare(yaw, model.yaw) == 0 && Float.compare(pitch, model.pitch) == 0 && Float.compare(roll, model.roll) == 0 && Float.compare(width, model.width) == 0 && Float.compare(height, model.height) == 0 && Float.compare(length, model.length) == 0 && Objects.deepEquals(faces, model.faces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(faces), listId, x, y, z, yaw, pitch, roll, width, height, length);
    }

    public float roll;

    public float width;
    public float height;
    public float length;

    /**
     * 构造方法
     * @param x 模型坐标
     * @param y 模型坐标
     * @param z 模型坐标
     * @param faces 面
     */
    public Model(float x, float y, float z, Face... faces) {
        this.x = x;
        this.y = y;
        this.z = z;

        this.yaw = 0;
        this.pitch = 0;
        this.roll = 0;

        this.width = 1;
        this.height = 1;
        this.length = 1;

        this.faces = faces;

        this.listId = GL11.glGenLists(1);
        _generate_display_list();
    }

    /**
     * 向渲染队列添加模型
     */
    public void paint() {
        Soup3D.renderQueue.add(this);
    }

    /**
     * 内部方法
     */
    private void _generate_display_list() {
        GL11.glNewList(listId, GL11.GL_COMPILE);
        for (Face face : faces) {
            int textureId = face.surface.baseColorId;
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1, 1, 1, 1);
            if (textureId > 0) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
                GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
            }

            if (face.surface.emission != 0.0) {
                float emission = (float) Math.max(0.0, Math.min(1.0, face.surface.emission));
                GL11.glMaterialfv(GL11.GL_FRONT, GL11.GL_EMISSION, new float[]{emission, emission, emission, 1.0f});
            }

            GL11.glBegin(face.mode);
            GL11.glNormal3f(face.normal.x, face.normal.y, face.normal.z);
            for (Vec5 v : face.vertex) {
                if (textureId > 0) {
                    GL13.glMultiTexCoord2f(GL13.GL_TEXTURE0, v.u, v.v);
                }
                GL11.glVertex3f(v.x, v.y, v.z);
            }
            GL11.glEnd();

            if (textureId > 0) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
            }

            if (face.surface.emission != 0) {
                GL11.glMaterialfv(GL11.GL_FRONT, GL11.GL_EMISSION, new float[]{0, 0, 0, 1});
            }
        }
        GL11.glEndList();
    }

    /**
     * 显示模型
     */
    public void show() {
        stableShapes.add(this);
    }

    /**
     * 隐藏模型
     */
    public void hide() {
        stableShapes.remove(this);
    }

    /**
     * 设置模型坐标
     * @param x 模型坐标
     * @param y 模型坐标
     * @param z 模型坐标
     */
    public void goTo(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * 设置模型旋转
     * @param yaw 模型旋转
     * @param pitch 模型旋转
     * @param roll 模型旋转
     */
    public void turn(float yaw, float pitch, float roll) {
        this.yaw = yaw;
        this.pitch = pitch;
        this.roll = roll;
    }

    /**
     * 设置模型大小
     * @param width 模型大小
     * @param height 模型大小
     * @param length 模型大小
     */
    public void size(float width, float height, float length) {
        this.width = width;
        this.height = height;
        this.length = length;
    }
}
