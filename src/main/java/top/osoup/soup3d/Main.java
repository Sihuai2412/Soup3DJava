package top.osoup.soup3d;

import top.osoup.soup3d.render.Face;
import top.osoup.soup3d.render.Model;
import top.osoup.soup3d.shader.FPL;
import top.osoup.soup3d.shader.MixChannel;
import top.osoup.soup3d.utils.SoupColor;
import top.osoup.soup3d.utils.Vec5;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Soup3D soup3D = new Soup3D(1920, 1080, 45, new SoupColor(0.5f, 0.75f, 1f), 1024);  // 实例化Soup3D
        FPL red = new FPL(new MixChannel(1, 1, 1, 0, 0));      // 创建材质
        FPL green = new FPL(new MixChannel(1, 1, 0, 1, 0));
        FPL blue = new FPL(new MixChannel(1, 1, 0, 0, 1));
        List<Face> faces = new ArrayList<>();

        Vec5[] vertices = {  // 定义定点
                new Vec5(-0.5f, -0.5f, -0.5f, 0, 0), // 0
                new Vec5( 0.5f, -0.5f, -0.5f, 0, 0), // 1
                new Vec5( 0.5f,  0.5f, -0.5f, 0, 0), // 2
                new Vec5(-0.5f,  0.5f, -0.5f, 0, 0), // 3
                new Vec5(-0.5f, -0.5f,  0.5f, 0, 0), // 4
                new Vec5( 0.5f, -0.5f,  0.5f, 0, 0), // 5
                new Vec5( 0.5f,  0.5f,  0.5f, 0, 0), // 6
                new Vec5(-0.5f,  0.5f,  0.5f, 0, 0)  // 7
        };

        FPL[] colors = {red, green, blue};

        faces.add(new Face(Face.ShapeType.QUAD, colors[0], List.of(
                vertices[4], vertices[5], vertices[6], vertices[7]
        )));  // 定义面

        faces.add(new Face(Face.ShapeType.QUAD, colors[1], List.of(
                vertices[0], vertices[1], vertices[2], vertices[3]
        )));

        faces.add(new Face(Face.ShapeType.QUAD, colors[2], List.of(
                vertices[0], vertices[3], vertices[7], vertices[4]
        )));

        faces.add(new Face(Face.ShapeType.QUAD, colors[0], List.of(
                vertices[1], vertices[5], vertices[6], vertices[2]
        )));

        faces.add(new Face(Face.ShapeType.QUAD, colors[1], List.of(
                vertices[3], vertices[2], vertices[6], vertices[7]
        )));

        faces.add(new Face(Face.ShapeType.QUAD, colors[2], List.of(
                vertices[0], vertices[1], vertices[5], vertices[4]
        )));

        Model cube = new Model(0, 0, 0, faces.toArray(new Face[0]));   // 定义模型
        cube.show(); // 显示
        soup3D.setUpdateFunction((s) -> cube.turn(cube.yaw + 0.1f, cube.pitch + 0.1f, cube.roll));  // 设置更新方法，每次更新都会被调用
        soup3D.run();  // 启动，必须放在最后
    }
}