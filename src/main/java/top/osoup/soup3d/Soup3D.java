package top.osoup.soup3d;
import org.lwjgl.Version;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import top.osoup.soup3d.render.Model;
import top.osoup.soup3d.utils.SoupColor;
import top.osoup.soup3d.utils.UpdateFunction;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.system.MemoryUtil.NULL;

public final class Soup3D {
    private final float fov;
    private final float far;
    private Logger logger;
    private GLFWErrorCallback errorCallback;
    private long window;
    private Map<Long, UpdateFunction> updateFunctions = new HashMap<>();
    private boolean shouldClose = false;

    public static List<Model> renderQueue = new ArrayList<>();

    public Soup3D(int width, int height, float fov, SoupColor bgColor, float far) {
        this.fov = fov;
        this.far = far;
        this.logger = LogManager.getLogger("Soup3D");
        this.errorCallback = GLFWErrorCallback.createPrint(System.err);
        logger.info("LWJGL Version: {}", Version.getVersion());
        GLFW.glfwSetErrorCallback(errorCallback);
        if (!GLFW.glfwInit()) {
            logger.error("Unable to initialize GLFW", new IllegalStateException());
        }

        long window = GLFW.glfwCreateWindow(width, height, "soup3D", NULL, NULL);
        if (window == NULL) {
            GLFW.glfwTerminate();
            logger.error("Failed to create the GLFW window", new RuntimeException());
        }
        this.window = window;

        GLFW.glfwMakeContextCurrent(window);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        this.setIcon("src/main/resources/osoup.png");
        this.setBackgroundColor(bgColor.r, bgColor.g, bgColor.b);

        int[] widthArr = new int[1];
        int[] heightArr = new int[1];
        GLFW.glfwGetWindowSize(window, widthArr, heightArr);
        int w = widthArr[0], h = heightArr[0];

        onChangeWindow(w, h);

        GLFW.glfwSetWindowSizeCallback(window, (wd, _width, _height) -> {
            onChangeWindow(_width, _height);
        });
    }

    private void onChangeWindow(int _width, int _height) {
        GL11.glViewport(0, 0, _width, _height);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        float aspect_ratio = (float) _width / _height;
        gluPerspective(fov, aspect_ratio, 0.1f, far);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0f, 0.0f, -5.0f);
    }

    public static void gluPerspective(float fovY, float aspect, float zNear, float zFar) {
        float fH = (float) Math.tan(Math.toRadians(fovY) / 2) * zNear;
        float fW = fH * aspect;
        GL11.glFrustum(-fW, fW, -fH, fH, zNear, zFar);
    }

    public void run() {
        while (!shouldClose) {
            if (GLFW.glfwWindowShouldClose(window)) {
                shouldClose = true;
                break;
            }
            this.update();
            if (this.updateFunctions != null && !this.updateFunctions.isEmpty()) {
                updateFunctions.values().forEach(u -> {
                    u.update(this);
                });
            }
        }

        GLFW.glfwSetWindowShouldClose(window, true);
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        errorCallback.free();
    }


    private void update() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        for (Model shape : Model.stableShapes) {
            shape.paint();
        }

        for (Model model : renderQueue) {
            float x = model.x;
            float y = model.y;
            float z = model.z;

            float yaw = model.yaw;
            float pitch = model.pitch;
            float roll = model.roll;

            float width = model.width;
            float height = model.height;
            float length = model.length;

            GL11.glPushMatrix();

            GL11.glTranslatef(x, y, z);
            GL11.glRotatef(roll, 1, 0, 0);
            GL11.glRotatef(pitch, 0, 0, 1);
            GL11.glRotatef(yaw, 0, 1, 0);
            GL11.glScalef(width, height, length);

            GL11.glCallList(model.listId);

            GL11.glPopMatrix();
        }

        renderQueue = new ArrayList<>();

        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }

    public void resize(int width, int height) {
        GLFW.glfwSetWindowSize(window, width, height);
    }

    public void setTitle(String title) {
        GLFW.glfwSetWindowTitle(window, title);
    }

    public void setIcon(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            ByteBuffer iconPixels = STBImage.stbi_load(path , w, h, comp, 4);
            if (iconPixels == null) {
                logger.error("Failed to load icon: {}", STBImage.stbi_failure_reason());
                return;
            }

            GLFWImage icon = GLFWImage.mallocStack(stack);
            icon.set(w.get(0), h.get(0), iconPixels);

            GLFWImage.Buffer icons = GLFWImage.mallocStack(1, stack);
            icons.put(0, icon);

            GLFW.glfwSetWindowIcon(window, icons);

            STBImage.stbi_image_free(iconPixels);
        }
    }

    public void setBackgroundColor(float r, float g, float b) {
        GL11.glClearColor(r, g, b, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    /////////////////////////////////////////////////

    public void close() {
        this.shouldClose = true;
    }

    public long getWindow() {
        return window;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public Map<Long, UpdateFunction> getUpdateFunctions() {
        return updateFunctions;
    }

    public void putUpdateFunction(long id, UpdateFunction updateFunction) {
        this.updateFunctions.put(id, updateFunction);
    }

    public void removeUpdateFunction(long id) {
        this.updateFunctions.remove(id);
    }
}
