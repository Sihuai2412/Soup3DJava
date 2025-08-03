package top.osoup.soup3d.shader;

import top.osoup.soup3d.utils.ShaderUtils;

import java.awt.image.BufferedImage;

public class FPL {
    public final IBaseColor baseColor;
    public final float emission;
    public int baseColorId;
    private int hash;

    public FPL(IBaseColor baseColor) {
        this(baseColor, 0);
    }

    public FPL(IBaseColor baseColor, float emission) {
        if (baseColor == null) {
            throw new IllegalArgumentException("baseColor cannot be null");
        }

        this.baseColor = baseColor;
        this.emission = emission;
        this.baseColorId = 0;

        update();
    }

    public void update() {
        BufferedImage img = baseColor.getPic();
        this.baseColorId = ShaderUtils.imgToTexture(img, 0, 0);
    }

    public IBaseColor getBaseColor() {
        return baseColor;
    }

    public float getEmission() {
        return emission;
    }

    public int getBaseColorId() {
        return baseColorId;
    }

    public int getHash() {
        return hash;
    }

    public void setHash(int hash) {
        this.hash = hash;
    }
}
