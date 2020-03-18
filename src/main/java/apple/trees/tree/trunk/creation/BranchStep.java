package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.creation.utils.GetRotations;
import apple.trees.tree.trunk.creation.utils.TrailingSteps;
import apple.trees.tree.trunk.creation.utils.VectorRotation;
import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class BranchStep {
    private Random random;
    private int branchAngle;
    private double branchStealing;
    private int branchesMean;

    protected BranchStep(int branchAngle, double branchStealing, int branchesMean, Random random) {
        this.branchAngle = branchAngle;
        this.branchStealing = branchStealing;
        this.branchesMean = branchesMean;
        this.random = random;
    }


    /**
     * make a branch session instead of continuing the tree
     *
     * @param tree         the entire tree and all the steps within it
     * @param lastTreeStep the tree step that was last created
     * @return all the last created steps for the branch session
     */
    protected Collection<TreeStep> getBranches(TreeArray tree, TreeStep lastTreeStep) {
        System.out.println("branched!");
        //todo use branch grouping size with normal distribution with min of 2
        int branchesToBuild = branchesMean;

        ArrayList<TreeStep> branchSteps = new ArrayList<>();
        Vec3d lastDirection = lastTreeStep.direction;
        Vec3d lastSlopeOfSlope = lastTreeStep.slopeOfSlope;
        double lastWidth = lastTreeStep.width;

        // make the unit vector of lastDirection
        Vec3d unitLastDirection6 = new Vec3d();
        double magnitude = Math.sqrt(lastDirection.x * lastDirection.x + lastDirection.y * lastDirection.y + lastDirection.z * lastDirection.z);
        if (magnitude == 0)
            return branchSteps;
        unitLastDirection6.x = lastDirection.x / magnitude * 6; // todo maybe change the 6 to somefin else? idk it worked so..
        unitLastDirection6.y = lastDirection.y / magnitude * 6;
        unitLastDirection6.z = lastDirection.z / magnitude * 6;

        Vec3d lastStepLocation = new Vec3d(lastTreeStep.x, lastTreeStep.y, lastTreeStep.z);

        // get all the different rotations //todo change arrayList to an arraylist of weights
        Collection<Vec3d> rotationAmounts = GetRotations.rotationFullFromDomain(branchAngle, branchesToBuild, new ArrayList<>());
        double x, y, z;
        // go through each rotation and make a branch for it
        for (Vec3d rotation : rotationAmounts) {
            Vec3d newDirection;
            newDirection = VectorRotation.rotate(rotation.x, rotation.y, rotation.z, unitLastDirection6);

            x = lastTreeStep.x + newDirection.x;
            y = lastTreeStep.y + newDirection.y;
            z = lastTreeStep.z + newDirection.z;

            //todo maybe slope of (0,0,0) should be somefin else
            // todo last width should be changed to the new width
            branchSteps.add(tree.put(x, y, z, newDirection, new Vec3d(0, 0, 0), lastWidth));

            // make a list of the locations for the next full step
            ArrayList<Vec3d> locations = TrailingSteps.getTrailingSquares(newDirection, lastStepLocation);
            // put all the trailings in the tree
            for (Vec3d loc : locations) {
                tree.put(loc.x, loc.y, loc.z, newDirection, lastSlopeOfSlope, lastWidth);
            }
        }

        return branchSteps;

    }

}
