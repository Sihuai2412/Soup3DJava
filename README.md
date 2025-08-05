这是一个基于`OpenGL`和`LWJGL`开发的3D引擎，易于新手学习，可  
用于3D游戏开发、数据可视化、3D图形的绘制等开发。

[Python版本仓库](https://github.com/OpenSoup/soup3D)

## 安装
[![](https://jitpack.io/v/Sihuai2412/Soup3DJava.svg)](https://jitpack.io/#Sihuai2412/Soup3DJava)


Maven:  
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependency>
    <groupId>com.github.Sihuai2412</groupId>
    <artifactId>Soup3DJava</artifactId>
    <version>[这里填入版本号如v1.0.1]</version>
</dependency>
```

Gradle:
```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation 'com.github.Sihuai2412:Soup3DJava:[这里填入版本号如v1.0.1]'
}
```
## 小试牛刀

安装完成后，您可以试试这段代码：

```java
package top.osoup.soup3d;

import top.osoup.soup3d.shader.Texture;
import top.osoup.soup3d.utils.color.SoupColor;

import top.osoup.soup3d.render.Face;
import top.osoup.soup3d.render.Model;
import top.osoup.soup3d.shader.FPL;
import top.osoup.soup3d.shader.MixChannel;
import top.osoup.soup3d.utils.math.Vec5;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        Soup3D soup3D = new Soup3D(1920, 1080, 45, new SoupColor(0.5f, 0.75f, 1f), 1024);  // 实例化Soup3D
        List<Face> faces = new ArrayList<>();

        Vec5[] vertices = {  // 定义顶点
                // 前面
                new Vec5(-0.5f, -0.5f,  0.5f, 0, 0),  // 0
                new Vec5( 0.5f, -0.5f,  0.5f, 1, 0),  // 1
                new Vec5( 0.5f,  0.5f,  0.5f, 1, 1),  // 2
                new Vec5(-0.5f,  0.5f,  0.5f, 0, 1),  // 3

                // 背面
                new Vec5( 0.5f, -0.5f, -0.5f, 0, 0),  // 4
                new Vec5(-0.5f, -0.5f, -0.5f, 1, 0),  // 5
                new Vec5(-0.5f,  0.5f, -0.5f, 1, 1),  // 6
                new Vec5( 0.5f,  0.5f, -0.5f, 0, 1),  // 7

                // 左面
                new Vec5(-0.5f, -0.5f, -0.5f, 0, 0),  // 8
                new Vec5(-0.5f, -0.5f,  0.5f, 1, 0),  // 9
                new Vec5(-0.5f,  0.5f,  0.5f, 1, 1),  // 10
                new Vec5(-0.5f,  0.5f, -0.5f, 0, 1),  // 11

                // 右面
                new Vec5( 0.5f, -0.5f,  0.5f, 0, 0),  // 12
                new Vec5( 0.5f, -0.5f, -0.5f, 1, 0),  // 13
                new Vec5( 0.5f,  0.5f, -0.5f, 1, 1),  // 14
                new Vec5( 0.5f,  0.5f,  0.5f, 0, 1),  // 15

                // 顶面
                new Vec5(-0.5f,  0.5f,  0.5f, 0, 0),  // 16
                new Vec5( 0.5f,  0.5f,  0.5f, 1, 0),  // 17
                new Vec5( 0.5f,  0.5f, -0.5f, 1, 1),  // 18
                new Vec5(-0.5f,  0.5f, -0.5f, 0, 1),  // 19

                // 底面
                new Vec5(-0.5f, -0.5f, -0.5f, 0, 0),  // 20
                new Vec5( 0.5f, -0.5f, -0.5f, 1, 0),  // 21
                new Vec5( 0.5f, -0.5f,  0.5f, 1, 1),  // 22
                new Vec5(-0.5f, -0.5f,  0.5f, 0, 1)   // 23
        };

        FPL pic = new FPL(new Texture(ImageIO.read(new File("src/main/resources/osoup.png"))));  // 你的图片

        faces.add(new Face(Face.ShapeType.QUAD, pic, List.of(vertices[0], vertices[1], vertices[2], vertices[3])));   // 前
        faces.add(new Face(Face.ShapeType.QUAD, pic, List.of(vertices[4], vertices[5], vertices[6], vertices[7])));   // 后
        faces.add(new Face(Face.ShapeType.QUAD, pic, List.of(vertices[8], vertices[9], vertices[10], vertices[11]))); // 左
        faces.add(new Face(Face.ShapeType.QUAD, pic, List.of(vertices[12], vertices[13], vertices[14], vertices[15]))); // 右
        faces.add(new Face(Face.ShapeType.QUAD, pic, List.of(vertices[16], vertices[17], vertices[18], vertices[19]))); // 上
        faces.add(new Face(Face.ShapeType.QUAD, pic, List.of(vertices[20], vertices[21], vertices[22], vertices[23]))); // 下


        Model cube = new Model(0, 0, 0, faces.toArray(new Face[0]));   // 定义模型
        cube.show(); // 显示
        soup3D.putUpdateFunction("cube".hashCode(), (s) -> cube.turn(cube.yaw + 0.1f, cube.pitch + 0.1f, cube.roll));  // 添加更新方法，每次更新都会被调用，需要填入id以便移除，建议使用字符串hash
        soup3D.run();  // 启动，必须放在最后
    }
}
```

这段代码运行后，您可以看到一个不断旋转的立方体在窗口中
