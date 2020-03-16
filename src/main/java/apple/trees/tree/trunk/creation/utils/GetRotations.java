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

    public static Collection<Vec3d> rotationFullFromDomain(double branchAngle, int branchesToBuild, ArrayList<Double> branchWeights) {
        ArrayList<Vec3d> rotations = rotationBaseFromDomain(branchesToBuild);
        reduceRotations(rotations, branchAngle);
        return rotations;

    }

    private static void reduceRotations(ArrayList<Vec3d> rotations, double branchAngle) {
        double divisor = 360 / branchAngle/2;
        double negate = branchAngle;
        // make this 0 a random number
        Vec3d base = rotations.get(0);
        for (Vec3d rotation : rotations) {
            rotation.x = (rotation.x - base.x) / divisor-negate;
            rotation.y = (rotation.y - base.y) / divisor-negate;
            rotation.z = (rotation.z - base.z) / divisor-negate;
        }
    }

    private static ArrayList<Vec3d> rotationBaseFromDomain(int branchesToBuild) {
        ArrayList<Vec3d> rotations;
        if (branchesToBuild == 2) {
            rotations = RotationPresets.branches2FromDomain();
        } else {
            rotations = new ArrayList<>();
        }
        return rotations;
    }

}
