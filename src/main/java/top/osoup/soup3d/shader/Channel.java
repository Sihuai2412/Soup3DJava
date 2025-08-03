package top.osoup.soup3d.shader;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;

public class Channel {
    private final IBaseColor texture;
    private final int channelId;
    public BufferedImage channelImage;
    private int hash;

    /**
     * 构造方法
     * @param texture 纹理
     * @param channelId 通道ID
     */
    public Channel(IBaseColor texture, int channelId) {
        this.texture = texture;
        this.channelId = channelId;

        this.hash = 0;
        update();
    }

    /**
     * 获取通道图片
     * @return 通道图片
     */
    public BufferedImage getChannelImage() {
        BufferedImage img = texture.getPic();
        int width = img.getWidth();
        int height = img.getHeight();

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        Raster raster = img.getRaster();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int[] pixels = new int[4];
                raster.getPixel(x, y, pixels);
                int val = pixels[channelId];
                image.getRaster().setSample(x, y, 0, val);
            }
        }
        this.channelImage = image;
        return image;
    }

    /**
     * 更新
     */
    private void update() {
        getChannelImage();
    }
}
