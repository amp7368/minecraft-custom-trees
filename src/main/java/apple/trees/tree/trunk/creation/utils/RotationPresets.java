package apple.trees.tree.trunk.creation.utils;

import com.sun.javafx.geom.Vec3d;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

public class RotationPresets {
    private static Random random;

    public static void initialize(JavaPlugin pl, Random rand) {
        random = rand;
    }

    protected static ArrayList<Vec3d> branches2FromDomain() {
        ArrayList<Vec3d> polars = new ArrayList<>();
        double xr = random.nextDouble();
        double yr = random.nextDouble();
        double zr = random.nextDouble();
        double x1, x2, y1, y2, z1, z2;
        if (xr < .5) {
            x1 = 0;
            x2 = 360;
        } else {
            x1 = 360;
            x2 = 0;
        }
        if (yr < .5) {
            y1 = 0;
            y2 = 360;
        } else {
            y1 = 360;
            y2 = 0;
        }
        if (zr < .5) {
            z1 = 0;
            z2 = 360;
        } else {
            z1 = 360;
            z2 = 0;
        }
        polars.add(new Vec3d(x1, y1, z1));
        polars.add(new Vec3d(x2, y2, z2));
        return polars;
    }

    protected static ArrayList<Vec3d> branches3FromDomain() {
        ArrayList<Vec3d> polars = new ArrayList<>();
        polars.add(new Vec3d(0, 0, 0));
        polars.add(new Vec3d(360, 360, 360));
        return polars;
    }
}
