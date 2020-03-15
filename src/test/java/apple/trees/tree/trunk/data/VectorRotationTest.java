package apple.trees.tree.trunk.data;

import apple.trees.tree.trunk.creation.VectorRotation;
import com.sun.javafx.geom.Vec3d;

public class VectorRotationTest {
    public static void testVectorRotation() {
        Vec3d oldVec =  new Vec3d(0, 1, 0);
        Vec3d newVec = VectorRotation.rotate(30, 30, 30,oldVec);
        printVectors(oldVec,newVec);
    }

    private static void printVectors(Vec3d oldVec, Vec3d newVec) {
        System.out.println(String.format("(%f,%f,%f) --> (%f,%f,%f)",
                oldVec.x, oldVec.y, oldVec.z, newVec.x, newVec.y, newVec.z));
    }
}
