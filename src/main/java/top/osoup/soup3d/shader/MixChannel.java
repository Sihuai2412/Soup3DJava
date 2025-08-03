package top.osoup.soup3d.shader;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MixChannel implements IBaseColor {
    private final int width;
    private final int height;
    private final Object R;
    private final Object G;
    private final Object B;
    private final Object A;

    private BufferedImage img;

    private int hash;

    /**
     * 构造方法
     * @param width 宽
     * @param height 高
     * @param R 红色值，实际类型为float或Channel，如果为float则在0-1之间
     * @param G 绿色值，实际类型为float或Channel，如果为float则在0-1之间
     * @param B 蓝色值，实际类型为float或Channel，如果为float则在0-1之间
     */
    public MixChannel(int width, int height,
                      Object R, Object G, Object B) {
        this(width, height, R, G, B, 1);
    }

    /**
     * 构造方法
     * @param width 宽
     * @param height 高
     * @param R 红色值，实际类型为float或Channel，如果为float则在0-1之间
     * @param G 绿色值，实际类型为float或Channel，如果为float则在0-1之间
     * @param B 蓝色值，实际类型为float或Channel，如果为float则在0-1之间
     * @param A 透明度，实际类型为float或Channel，如果为float则在0-1之间
     */
    public MixChannel(int width, int height,
                      Object R, Object G, Object B, Object A) {

        if (!(R instanceof Number || R instanceof Channel))
            throw new IllegalArgumentException("R should be Number or Channel");
        if (!(G instanceof Number || G instanceof Channel))
            throw new IllegalArgumentException("G should be Number or Channel");
        if (!(B instanceof Number || B instanceof Channel))
            throw new IllegalArgumentException("B should be Number or Channel");
        if (!(A instanceof Number || A instanceof Channel))
            throw new IllegalArgumentException("A should be Number or Channel");

        this.width = width;
        this.height = height;
        this.R = R;
        this.G = G;
        this.B = B;
        this.A = A;

        update();
    }

    /**
     * 更新
     */
    private void update() {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = result.createGraphics();

        Map<String, BufferedImage> bands = new HashMap<>();
        bands.put("R", getChannelImage(R));
        bands.put("G", getChannelImage(G));
        bands.put("B", getChannelImage(B));
        bands.put("A", getChannelImage(A));

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = getGrayValue(bands.get("R"), x, y);
                int gVal = getGrayValue(bands.get("G"), x, y);
                int bVal = getGrayValue(bands.get("B"), x, y);
                int a = getGrayValue(bands.get("A"), x, y);
                int argb = ((a & 0xFF) << 24) |
                        ((r & 0xFF) << 16) |
                        ((gVal & 0xFF) << 8) |
                        (bVal & 0xFF);
                result.setRGB(x, y, argb);
            }
        }
        g.dispose();

        this.img = result;
    }

    /**
     * 根据不同类型的输入源生成灰度BufferedImage。
     * 输出图像始终为指定尺寸(width × height)且类型为BufferedImage.TYPE_BYTE_GRAY。
     *
     * @param source 输入源，支持以下类型：
     *               <ul>
     *                 <li>Number类型：生成纯色灰度图，数值转换为0-255的灰度值</li>
     *                 <li>Channel类型：获取通道图像并缩放到目标尺寸</li>
     *                 <li>其他类型：生成全白(255)灰度图</li>
     *               </ul>
     * @return 生成的灰度图像，尺寸为width × height
     */
    private BufferedImage getChannelImage(Object source) {
        if (source instanceof Number) {
            int val = Math.min(255, Math.max(0, (int)(((Number)source).floatValue() * 255f)));
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            int gray = (val << 16) | (val << 8) | val;
            for (int y=0; y<height; y++) {
                for (int x=0; x<width; x++) {
                    img.getRaster().setSample(x, y, 0, val);
                }
            }
            return img;
        } else if (source instanceof Channel) {
            BufferedImage srcImg = ((Channel) source).getChannelImage();
            if (srcImg.getWidth() != width || srcImg.getHeight() != height) {
                BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
                AffineTransform at = AffineTransform.getScaleInstance(
                        (double)width / srcImg.getWidth(), (double)height / srcImg.getHeight());
                AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
                scaleOp.filter(srcImg, resized);
                return resized;
            }
            return srcImg;
        } else {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            for (int y=0; y<height; y++) {
                for (int x=0; x<width; x++) {
                    img.getRaster().setSample(x, y, 0, 255);
                }
            }
            return img;
        }
    }

    /**
     * 内部方法
     */
    private int getGrayValue(BufferedImage img, int x, int y) {
        if (img == null) return 128;
        return img.getRaster().getSample(x, y, 0);
    }

    @Override
    public BufferedImage getPic() {
        return img;
    }
}
