package top.osoup.soup3d.utils;

import top.osoup.soup3d.Soup3D;

@FunctionalInterface
public interface UpdateFunction {
    void update(Soup3D soup3D);
}
