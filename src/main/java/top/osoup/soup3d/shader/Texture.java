package top.osoup.soup3d.shader;

import java.awt.image.BufferedImage;

public class Texture implements IBaseColor {
    private final BufferedImage image;
    private final int hash;

    /**
     * 构造方法
     * @param image 图片
     */
    public Texture(BufferedImage image) {
        this.image = image;
        this.hash = 0;

        update();
    }

    /**
     * 更新
     */
    public void update() {}

    @Override
    public BufferedImage getPic() {
        return image;
    }
}
