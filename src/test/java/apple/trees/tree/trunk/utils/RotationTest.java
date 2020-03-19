package apple.trees.tree.trunk.utils;

import apple.trees.tree.trunk.creation.utils.GetRotations;
import apple.trees.tree.trunk.creation.utils.VectorRotation;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class RotationTest {
    public static void rotationTest() {
        GetRotations.initialize(new Random());
        Vec3d baseVec = new Vec3d(0, 1, 0);
//        Collection<Vec3d> rotations = GetRotations.rotationFullFromDomain(30, 2, new ArrayList<>());
        ArrayList<Vec3d> rotations = new ArrayList<>();
        System.out.print("\nBase Vec: ");
        printVec(baseVec);
        System.out.println();
        rotations.add(new Vec3d(0, 0, 30));
        rotations.add(new Vec3d(0, 30, 0));
        rotations.add(new Vec3d(30, 0, 0));
        rotations.add(new Vec3d(0, 0, 0));
        for (Vec3d vec : rotations) {
            System.out.print("Rotate by: ");
            printVec(vec);
            System.out.print("Rotated Vec: ");
            //todo purposly reversed
            printVec(VectorRotation.rotate(vec.z, vec.y, vec.x, baseVec));
            System.out.println();
        }
    }

    private static void printVec(Vec3d vec) {
        System.out.println(String.format("x:%f,y:%f,z:%f", vec.x, vec.y, vec.z));
    }
}
