package top.osoup.soup3d.utils.helper;

import top.osoup.soup3d.render.Face;
import top.osoup.soup3d.shader.FPL;
import top.osoup.soup3d.utils.math.Vec5;

import java.util.ArrayList;
import java.util.List;

public class ShapeBuilder {
    public static Face[] circle(float cx, float cy, float cz, float radius, int segments, FPL surface) {
        List<Face> faces = new ArrayList<>();
        double angleStep = 2 * Math.PI / segments;
        for (int i = 0; i < segments; i++) {
            double angle1 = i * angleStep;
            double angle2 = (i + 1) * angleStep;

            float x2 = cx + radius * (float)Math.cos(angle1);
            float y2 = cy + radius * (float)Math.sin(angle1);

            float x3 = cx + radius * (float)Math.cos(angle2);
            float y3 = cy + radius * (float)Math.sin(angle2);

            float u0 = 0.5f;
            float v0 = 0.5f;

            float u1 = 0.5f + 0.5f * (float)Math.cos(angle1);
            float v1 = 0.5f + 0.5f * (float)Math.sin(angle1);

            float u2 = 0.5f + 0.5f * (float)Math.cos(angle2);
            float v2 = 0.5f + 0.5f * (float)Math.sin(angle2);

            faces.add(new Face(Face.ShapeType.TRIANGLE_B, surface, List.of(
                    new Vec5(cx, cy, cz, u0, v0),
                    new Vec5(x2, y2, cz, u1, v1),
                    new Vec5(x3, y3, cz, u2, v2)
            )));
        }
        return faces.toArray(new Face[0]);
    }


    public static Face[] sphere(float cx, float cy, float cz, float radius, int latitudeSegments, int longitudeSegments, FPL surface) {
        List<Face> faces = new ArrayList<>();
        for (int lat = 0; lat < latitudeSegments; lat++) {
            double theta1 = Math.PI * lat / latitudeSegments;
            double theta2 = Math.PI * (lat + 1) / latitudeSegments;

            float y1 = cy + radius * (float) Math.cos(theta1);
            float y2 = cy + radius * (float) Math.cos(theta2);

            float r1 = radius * (float) Math.sin(theta1);
            float r2 = radius * (float) Math.sin(theta2);

            for (int lon = 0; lon < longitudeSegments; lon++) {
                double phi1 = 2 * Math.PI * lon / longitudeSegments;
                double phi2 = 2 * Math.PI * (lon + 1) / longitudeSegments;

                float x1 = cx + r1 * (float) Math.cos(phi1);
                float z1 = cz + r1 * (float) Math.sin(phi1);

                float x2 = cx + r2 * (float) Math.cos(phi1);
                float z2 = cz + r2 * (float) Math.sin(phi1);

                float x3 = cx + r2 * (float) Math.cos(phi2);
                float z3 = cz + r2 * (float) Math.sin(phi2);

                float x4 = cx + r1 * (float) Math.cos(phi2);
                float z4 = cz + r1 * (float) Math.sin(phi2);

                float u1 = (float)(phi1 / (2 * Math.PI));
                float u2 = (float)(phi2 / (2 * Math.PI));
                float v1 = (float)(theta1 / Math.PI);
                float v2 = (float)(theta2 / Math.PI);

                faces.add(new Face(Face.ShapeType.TRIANGLE_B, surface, List.of(
                        new Vec5(x1, y1, z1, u1, v1),
                        new Vec5(x2, y2, z2, u1, v2),
                        new Vec5(x3, y2, z3, u2, v2)
                )));
                faces.add(new Face(Face.ShapeType.TRIANGLE_B, surface, List.of(
                        new Vec5(x1, y1, z1, u1, v1),
                        new Vec5(x3, y2, z3, u2, v2),
                        new Vec5(x4, y1, z4, u2, v1)
                )));
            }
        }
        return faces.toArray(new Face[0]);
    }
}
