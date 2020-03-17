package apple.trees.tree.trunk.creation.utils;

import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.Rotation;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        return rotations;

    }

    private static void reduceRotations(ArrayList<Vec3d> rotations, double branchAngle) {
        double divisor = 360 / branchAngle;
        // make this 0 a random number
        for (Vec3d rotation : rotations) {
            rotation.x = (rotation.x) / divisor;
            rotation.y = (rotation.y) / divisor;
            rotation.z = (rotation.z) / divisor;
        }
    }

    private static ArrayList<Vec3d> rotationBaseFromDomain(int branchesToBuild) {
        ArrayList<Vec3d> rotations = branchesFromDomain(branchesToBuild);
        return rotations;
    }
    protected static ArrayList<Vec3d> branchesFromDomain(int numOfBranches) {
        ArrayList<Vec3d> polars = new ArrayList<>();
        for (int i = 0; i < numOfBranches; i++) {
            polars.add(new Vec3d());
        }

        //fill arr with the different vals that the components could be
        ArrayList<Double> arr = new ArrayList<>();
        double increment = 720.0 / (numOfBranches - 1);
        for (double i = -360; i <= 360; i += increment) {
            arr.add(i);
        }
        int size = polars.size();
        if (size != arr.size()) {
            System.err.println("somefin went wrong in making the increments");
            for (Double i : arr) {
                System.err.print(i + " ");
            }
            System.err.println();
        }
        // for each component, add the correct component to each branch
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).x = arr.get(i);
        }
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).y = arr.get(i);
        }
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).z = arr.get(i);
        }

        return polars;
    }

}
