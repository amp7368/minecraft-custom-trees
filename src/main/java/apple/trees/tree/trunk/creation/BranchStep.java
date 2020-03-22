package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.creation.utils.GetRotations;
import apple.trees.tree.trunk.creation.utils.RandomChange;
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
    private double decayRate;
    private double branchWidthModifier;
    private RandomChange randomChange;

    public BranchStep(int branchAngle, double branchStealing, int branchesMean, Random random, double decayRate, double branchWidthModifier, RandomChange randomChange) {
        this.branchAngle = branchAngle;
        this.branchStealing = branchStealing;
        this.branchesMean = branchesMean;
        this.random = random;
        this.decayRate = decayRate;
        this.branchWidthModifier = branchWidthModifier;
        this.randomChange = randomChange;
    }


    /**
     * make a branch session instead of continuing the tree
     *
     * @param tree         the entire tree and all the steps within it
     * @param lastTreeStep the tree step that was last created
     * @return all the last created steps for the branch session
     */
    protected Collection<TreeStep> getBranches(TreeArray tree, TreeStep lastTreeStep, double startWidth) {
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
        double newWidth = lastWidth - randomChange.getRandomChangeWidth(lastWidth / startWidth, decayRate);

        // if this branch shouldn't exist, return a collection of no branches
        if (newWidth < Trunk.MIN_STEP_WIDTH_SIZE)
            return new ArrayList<>(1);

        // get an arrayList of weights of size branchesToBuild
        ArrayList<Double> branchWeights = new ArrayList<>(branchesToBuild);
        for (int i = 0; i < branchesToBuild; i++) {
            branchWeights.add(random.nextDouble());
        }
        double sum = 0;
        for (Double weight : branchWeights) {
            sum += weight;
        }
        for (int i = 0; i < branchesToBuild; i++) {
            branchWeights.set(i, branchWeights.get(i) / sum * branchesToBuild * newWidth);
        }

        newWidth = newWidth * branchWidthModifier;

        // get all the different rotations
        ArrayList<Vec3d> rotationAmounts = GetRotations.rotationFullFromDomain(branchAngle, branchWeights);
        double x, y, z;
        int rotationAmountsSize = rotationAmounts.size();
        // go through each rotation and make a branch for it
        for (int i = 0; i < rotationAmountsSize; i++) {
            Vec3d rotation = rotationAmounts.get(i);
            Vec3d newDirection;
            newDirection = VectorRotation.rotate(rotation.x, rotation.y, rotation.z, unitLastDirection6);

            x = lastTreeStep.x + newDirection.x;
            y = lastTreeStep.y + newDirection.y;
            z = lastTreeStep.z + newDirection.z;

            //todo maybe slope of (0,0,0) should be somefin else maybe shouldnt be newWidth
            branchSteps.add(tree.put(x, y, z, newDirection, new Vec3d(0, 0, 0), newWidth));

            // make a list of the locations for the next full step
            ArrayList<Vec3d> locations = TrailingSteps.getTrailingSquares(newDirection, lastStepLocation);
            // put all the trailings in the tree
            for (Vec3d loc : locations) {
                tree.put(loc.x, loc.y, loc.z, newDirection, lastSlopeOfSlope, newWidth);
            }
        }

        return branchSteps;

    }

}
