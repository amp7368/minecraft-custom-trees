package apple.trees.tree.trunk.creation.utils;

import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.Rotation;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class GetRotations {
    private static Random random;

    public static void initialize(JavaPlugin pl, Random rand) {
        random = rand;
    }

    public static void initialize(Random rand) {
        random = rand;
    }

    public static Collection<Vec3d> rotationFullFromDomain(double branchAngle, int branchesToBuild, ArrayList<Double> branchWeights) {
        ArrayList<Vec3d> rotations = rotationBaseFromDomain(branchesToBuild);
        reduceRotations(rotations, branchAngle);
        printify(rotations);
        return rotations;

    }

    private static void printify(ArrayList<Vec3d> rotations) {
        for (Vec3d vec : rotations) {
            printVec(vec);
        }
    }

    private static void printVec(Vec3d vec) {
        System.out.println(String.format("x:%f,y:%f,z:%f", vec.x, vec.y, vec.z));
    }

    private static void reduceRotations(ArrayList<Vec3d> rotations, double branchAngle) {
        double divisor = 360 / branchAngle;
        // make this 0 a random number
        for (Vec3d rotation : rotations) {
            rotation.x = (rotation.x) / divisor;
            rotation.y = (rotation.y ) / divisor;
            rotation.z = (rotation.z ) / divisor;
        }
    }

    private static ArrayList<Vec3d> rotationBaseFromDomain(int branchesToBuild) {
        ArrayList<Vec3d> rotations;
        if (branchesToBuild == 2) {
            rotations = RotationPresets.branches2FromDomain();
        } else if (branchesToBuild == 3) {
            rotations = RotationPresets.branches3FromDomain();
        } else {
            rotations = new ArrayList<>();

        }
        return rotations;
    }

}
