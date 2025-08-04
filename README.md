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

        Vec5[] vertices = {  // 定义顶点
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
        soup3D.putUpdateFunction("cube".hashCode(), (s) -> cube.turn(cube.yaw + 0.1f, cube.pitch + 0.1f, cube.roll));  // 添加更新方法，每次更新都会被调用，需要填入id以便移除，建议使用字符串hash
        soup3D.run();  // 启动，必须放在最后
    }
}
```

这段代码运行后，您可以看到一个不断旋转的立方体在窗口中
