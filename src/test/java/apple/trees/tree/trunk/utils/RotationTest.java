package apple.trees.tree.trunk.utils;

import apple.trees.TreeMain;
import apple.trees.tree.trunk.creation.utils.GetRotations;
import apple.trees.tree.trunk.creation.utils.RotationPresets;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RotationTest {
    public static void rotationTest() {
        GetRotations.initialize(new Random());
        RotationPresets.initialize(new Random());

        Collection<Vec3d> rotations = GetRotations.rotationFullFromDomain(30, 2, new ArrayList<>());
        for (Vec3d vec : rotations) {
            printVec(vec);
        }
    }

    private static void printVec(Vec3d vec) {
        System.out.println(String.format("x:%f,y:%f,z:%f", vec.x, vec.y, vec.z));
    }
}
