package top.osoup.soup3d.utils;

import top.osoup.soup3d.Soup3D;

@FunctionalInterface
public interface UpdateFunction {
    /**
     * 更新方法，每次更新时调用
     * @param soup3D Soup3D实例
     */
    void update(Soup3D soup3D);
}
