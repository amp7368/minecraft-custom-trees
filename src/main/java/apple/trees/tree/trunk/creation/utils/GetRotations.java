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

    public static void initialize(Random rand) {
        random = rand;
    }

    /**
     * get all the rotations that need to be made to make different angled branches
     *
     * @param branchAngle     the angle at which all the branches should try to follow
     * @param branchesToBuild the number of branches that need to be made
     * @param branchWeights   an array of the branches' weights so that we know how greatly they impact each other
     * @return a collection of rotations to be made in the x, y, and z.
     */
    public static Collection<Vec3d> rotationFullFromDomain(double branchAngle, int branchesToBuild, ArrayList<Double> branchWeights) {
        // get the raw branch angles in a 'double sphere'
        ArrayList<Vec3d> rotations = branchesFromDomain(branchesToBuild);

        // reduce the rotations into a cone
        reduceRotations(rotations, branchAngle);
        return rotations;

    }

    /**
     * reduces the rotations to a cone rather than a 'double' sphere
     *
     * @param rotations   the collection of rotations
     * @param branchAngle the branch angle we would like to follow
     */
    private static void reduceRotations(ArrayList<Vec3d> rotations, double branchAngle) {
        if (branchAngle == 0) {
            // oof.. just set it to 30 i guess
            branchAngle = 30;
        }

        // reduce the rotations to -branchAngle to branchAngle
        double divisor = 360 / branchAngle;
        for (Vec3d rotation : rotations) {
            rotation.x = (rotation.x) / divisor;
            rotation.y = (rotation.y) / divisor;
            rotation.z = (rotation.z) / divisor;
        }
    }

    /**
     * creates a set of branches given how many branches need to be made
     *
     * @param numOfBranches how many branches need to be made
     * @return the set of perfect rotations to be made given all branches were the same weight
     */
    private static ArrayList<Vec3d> branchesFromDomain(int numOfBranches) {
        // the polar branches rotations to be filled with actual rotations
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
            // print an error message i suppose and just return no rotations? this should never happen
            System.err.println("somefin went wrong in making the increments");
            for (Double i : arr) {
                System.err.print(i + " ");
            }
            System.err.println();
            return polars;
        }

        // for each component, add the correct component to each branch

        // set all the x's
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).x = arr.get(i);
        }

        // set all the y's
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).y = arr.get(i);
        }

        // set all the z's
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).z = arr.get(i);
        }

        return polars;
    }

}
