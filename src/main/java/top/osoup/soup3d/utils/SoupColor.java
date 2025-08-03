package top.osoup.soup3d.utils;

import java.util.Objects;

public class SoupColor {
    public static final SoupColor BLACK = new SoupColor(0, 0, 0);

    public float r, g, b;

    public SoupColor(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoupColor soupColor = (SoupColor) o;
        return Float.compare(r, soupColor.r) == 0 && Float.compare(g, soupColor.g) == 0 && Float.compare(b, soupColor.b) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b);
    }

    @Override
    public String toString() {
        return "SoupColor{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                '}';
    }
}
