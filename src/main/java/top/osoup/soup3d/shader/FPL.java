package top.osoup.soup3d.shader;

import top.osoup.soup3d.utils.ShaderUtils;

import java.awt.image.BufferedImage;

public class FPL {
    public final IBaseColor baseColor;
    public final float emission;
    public int baseColorId;
    private int hash;

    /**
     * 构造方法
     * @param baseColor 纹理
     */
    public FPL(IBaseColor baseColor) {
        this(baseColor, 0);
    }

    /**
     * 构造方法
     * @param baseColor 纹理
     * @param emission 发光度
     */
    public FPL(IBaseColor baseColor, float emission) {
        if (baseColor == null) {
            throw new IllegalArgumentException("baseColor cannot be null");
        }

        this.baseColor = baseColor;
        this.emission = emission;
        this.baseColorId = 0;

        update();
    }

    /**
     * 更新
     */
    private void update() {
        BufferedImage img = baseColor.getPic();
        this.baseColorId = ShaderUtils.imgToTexture(img, 0, 0);
    }
}
