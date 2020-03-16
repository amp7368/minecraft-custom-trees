package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class Branch {
    private static Random random;

    public static void initialize(JavaPlugin pl, Random rand) {
        Branch.random = rand;
    }

    /**
     * make a branch session instead of continuing the tree
     *
     * @param tree               the entire tree and all the steps within it
     * @param lastTreeStep       the tree step that was last created
     * @param branchAngle        (Degrees) the angle to branch off at
     * @param branchStealing     how much branches steal from the original
     * @param branchGroupingSize how many branches are typical in a group
     * @return all the last created steps for the branch session
     */
    protected static Collection<TreeStep> getBranches(TreeArray tree, TreeStep lastTreeStep, double branchAngle, double branchStealing, int branchGroupingSize) {
        //todo use branch grouping size with normal distribution with min of 2
        int branchesToBuild = 2;

        ArrayList<TreeStep> branchSteps = new ArrayList<>();
        Vec3d lastDirection = lastTreeStep.direction;
        Vec3d lastSlopeOfSlope = lastTreeStep.slopeOfSlope;
        double lastWidth = lastTreeStep.width;

        // make the unit vector of lastDirection
        Vec3d unitLastDirection6 = new Vec3d();
        double magnitude = Math.sqrt(lastDirection.x * lastDirection.x + lastDirection.y * lastDirection.y + lastDirection.z * lastDirection.z);
        if (magnitude == 0)
            return branchSteps;
        unitLastDirection6.x = lastDirection.x / magnitude * 6;
        unitLastDirection6.y = lastDirection.y / magnitude * 6;
        unitLastDirection6.z = lastDirection.z / magnitude * 6;


        // make branchAngle be yTheta
        double xTheta = random.nextDouble() * 360;
        // todo make this a normal distribution curve
        double yTheta = branchAngle;
        double zTheta = random.nextDouble() * 360;

        Vec3d newDirection;
        double x, y, z;
        newDirection = VectorRotation.rotate(xTheta, yTheta, zTheta, unitLastDirection6);

        x = lastTreeStep.x + newDirection.x;
        y = lastTreeStep.y + newDirection.y;
        z = lastTreeStep.z + newDirection.z;

        branchSteps.add(tree.put(x, y, z, newDirection, new Vec3d(0, 0, 0), lastWidth));
        branchSteps.add(tree.put(x, y, z, lastDirection, lastSlopeOfSlope, lastWidth));

        // make a list of the locations for the next full step
        ArrayList<Vec3d> locations = TrailingSteps.getTrailingSquares(lastDirection, new Vec3d(lastTreeStep.x, lastTreeStep.y, lastTreeStep.z));
        // put all the trailings in the tree
        for (Vec3d loc : locations) {
            tree.put(loc.x, loc.y, loc.z, newDirection, lastSlopeOfSlope, lastWidth);
        }
        // make a list of the locations for the next full step
        locations = TrailingSteps.getTrailingSquares(newDirection, new Vec3d(lastTreeStep.x, lastTreeStep.y, lastTreeStep.z));
        // put all the trailings in the tree
        for (Vec3d loc : locations) {
            tree.put(loc.x, loc.y, loc.z, newDirection, lastSlopeOfSlope, lastWidth);
        }

        return branchSteps;

    }

}
