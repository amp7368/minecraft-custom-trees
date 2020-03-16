package apple.trees.tree.trunk.creation.utils;

import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

public class RotationPresets {
    protected static ArrayList<Vec3d> branches2FromDomain() {
        ArrayList<Vec3d> polars = new ArrayList<>();
        polars.add(new Vec3d(0, 0, 0));
        polars.add(new Vec3d(180, 180, 180));
        return polars;
    }
}
