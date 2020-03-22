package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.creation.utils.RandomChange;
import apple.trees.tree.trunk.creation.utils.TrailingSteps;
import apple.trees.tree.trunk.creation.utils.VectorRotation;
import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class BaseTrunk {
    private static final int maxCompletedSteps = 1000;
    private double branchingChanceSD = .2;
    private double branchingChanceMean = .4;
    private double BELL_CURVE_LEFT;
    private final int branchAngle;
    private final double branchStealing;
    private double branchMaxWidth;
    private int trunk_width;
    private int trunk_height;
    private float leanMagnitude;
    private float leanLikelihood;
    private float maxLean;
    private Vec3d leanStart;
    private double decayRate;
    private double branchingChance;
    private int branchesMean;
    private double branchWidthModifier;
    private ArrayList<Vec3d> firstAngles;
    private Random random;
    private Vec3d startDirection;
    private RandomChange randomChange;
    private double leanCoefficent;
    private double leanExponent;

    public BaseTrunk(int trunk_width, int trunk_height, float leanMagnitude, float leanLikelihood, float maxLean,
                     Vec3d leanStart, double decayRate, double branchingChance, int branchesMean, int branchAngle,
                     double branchMaxWidth, double branchingChanceSD, double branchingChanceMean, double branchWidthModifier,
                     ArrayList<Vec3d> firstAngles, double leanCoefficent, double leanExponent,
                     RandomChange randomChange, Random random) {
        this.startDirection = new Vec3d(0, 1, 0);
        this.trunk_width = trunk_width;
        this.trunk_height = trunk_height;
        this.leanMagnitude = leanMagnitude;
        this.leanLikelihood = leanLikelihood;
        this.maxLean = maxLean;
        this.leanStart = leanStart;
        this.decayRate = decayRate;
        this.branchMaxWidth = branchMaxWidth;
        this.branchingChance = branchingChance;
        this.branchesMean = branchesMean;
        this.branchAngle = branchAngle;
        this.randomChange = randomChange;
        this.branchingChanceSD = branchingChanceSD;
        this.branchingChanceMean = branchingChanceMean;
        this.branchWidthModifier = branchWidthModifier;
        this.firstAngles = firstAngles;
        this.leanCoefficent = leanCoefficent;
        this.leanExponent = leanExponent;
        this.random = random;
        BELL_CURVE_LEFT = 1 / (branchingChanceSD * Math.sqrt(2 * Math.PI));
        this.branchStealing = .5;
        //todo ^^^
    }

    /**
     * creates a random base trunk with the specified options
     *
     * @return the tree that was slightly filled in
     */
    public TreeArray createBaseTrunk() {
        TreeArray tree = new TreeArray(100, 100, 100);

        ArrayList<TreeStep> lastTreeSteps = new ArrayList<>();
        lastTreeSteps.add(createBaseStart(tree));
        BranchStep branchStep = new BranchStep(branchAngle, branchStealing, branchesMean, random, decayRate, branchWidthModifier, randomChange);
        NormalStep normalStep = new NormalStep(leanMagnitude, leanLikelihood, decayRate, startDirection);
        TreeStep lastTreeStep;
        // loop until all the ends are finished
        double sizeX = tree.sizeX();
        double sizeZ = tree.sizeZ();
        boolean isFirstBranch = true;
        if (firstAngles == null || firstAngles.isEmpty()) {
            isFirstBranch = false;
        }
        treeStepLoop:
        for (int currentCompletedSteps = 0; currentCompletedSteps < maxCompletedSteps && !lastTreeSteps.isEmpty(); currentCompletedSteps++) {
            // get the last step
            lastTreeStep = lastTreeSteps.remove(0);

            // if the step is bad, go to the next one
            while (lastTreeStep == null) {
                if (lastTreeSteps.isEmpty())
                    break treeStepLoop;
                lastTreeStep = lastTreeSteps.remove(0);
            }

            // if we went outside of where we should, stop this branch
            if (lastTreeStep.y > trunk_height || lastTreeStep.y < 0
                    || lastTreeStep.x < 0 || lastTreeStep.x > sizeX
                    || lastTreeStep.z < 0 || lastTreeStep.z > sizeZ)
                continue;

            // do either a branch or a normal step
            TreeStep currentTreeStep;
            if (lastTreeStep.width < branchMaxWidth && random.nextDouble() < getBranchingChance(lastTreeStep.width / trunk_width)) {
                if (isFirstBranch) {
                    lastTreeSteps.addAll(getFirstBranches(tree, lastTreeStep, firstAngles, trunk_width));
                    isFirstBranch = false;
                }
                lastTreeSteps.addAll(branchStep.getBranches(tree, lastTreeStep, trunk_width));
            } else {
                currentTreeStep = normalStep.getCurrentTreeStep(tree, lastTreeStep, randomChange, trunk_width);
                lastTreeSteps.add(currentTreeStep);
            }
        }

        return tree;
    }

    private Collection<TreeStep> getFirstBranches(TreeArray tree, TreeStep lastTreeStep, ArrayList<Vec3d> rotationAmounts, double startWidth) {
        //todo use branch grouping size with normal distribution with min of 2
        int branchesToBuild = firstAngles.size();

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

        double newWidth = lastWidth - randomChange.getRandomChangeWidth(lastWidth / startWidth, decayRate);
        newWidth *= branchWidthModifier;
        // if this branch shouldn't exist, return a collection of no branches
        if (newWidth < Trunk.MIN_STEP_WIDTH_SIZE)
            return new ArrayList<>(1);

        Vec3d lastStepLocation = new Vec3d(lastTreeStep.x, lastTreeStep.y, lastTreeStep.z);

        // get all the different rotations
        double x, y, z;
        // go through each rotation and make a branch for it
        for (Vec3d rotation : rotationAmounts) {
            Vec3d newDirection;
            newDirection = VectorRotation.rotate(rotation.x, rotation.y, rotation.z, unitLastDirection6);

            x = lastTreeStep.x + newDirection.x;
            y = lastTreeStep.y + newDirection.y;
            z = lastTreeStep.z + newDirection.z;

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

    private double getBranchingChance(double width) {
        return branchingChance * BELL_CURVE_LEFT * Math.pow(Math.E, -0.5 * (Math.pow(width - branchingChanceMean, 2))
                / Math.pow(branchingChanceSD, 2));
    }

    /**
     * create the base of the tree (won't be needed later)
     *
     * @param tree the tree we're adding to
     * @return the base of the tre
     */
    private TreeStep createBaseStart(TreeArray tree) {
        int centerX = tree.sizeX() / 2;
        int centerY = 0;
        int centerZ = tree.sizeZ() / 2;


        int trunk_width_half = trunk_width / 2;
        for (int xi = -trunk_width_half; xi < trunk_width_half; xi++) {
            for (int zi = -trunk_width_half; zi < trunk_width_half; zi++) {
                tree.put(centerX + xi + 0.5, centerY + 0.5, centerZ + zi + 0.5, leanStart, startDirection, trunk_width);
            }
        }
        return tree.get(centerX, centerY, centerZ);
    }

}
