package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.creation.utils.GetRotations;
import apple.trees.tree.trunk.creation.utils.RandomChange;
import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.block.data.type.Bell;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

public class BaseTrunk {
    private static final int MAX_COMPLETED_STEPS = 1000;
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
    private Random random;
    private Vec3d startDirection;
    private RandomChange randomChange;

    public BaseTrunk(int trunk_width, int trunk_height, float leanMagnitude, float leanLikelihood, float maxLean,
                     Vec3d leanStart, double decayRate, double branchingChance, int branchesMean, int branchAngle,
                     double branchMaxWidth, double branchingChanceSD, double branchingChanceMean, RandomChange randomChange, Random random) {
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
        BranchStep branchStep = new BranchStep(branchAngle, branchStealing, branchesMean, random, decayRate, randomChange);
        NormalStep normalStep = new NormalStep(leanMagnitude, leanLikelihood, decayRate);
        TreeStep lastTreeStep;
        // loop until all the ends are finished
        double sizeX = tree.sizeX();
        double sizeZ = tree.sizeZ();
        treeStepLoop:
        for (int currentCompletedSteps = 0; currentCompletedSteps < MAX_COMPLETED_STEPS && !lastTreeSteps.isEmpty(); currentCompletedSteps++) {
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
            if (lastTreeStep.width<branchMaxWidth && random.nextDouble() < getBranchingChance(lastTreeStep.width / trunk_width)) {
                lastTreeSteps.addAll(branchStep.getBranches(tree, lastTreeStep));
            } else {
                currentTreeStep = normalStep.getCurrentTreeStep(tree, lastTreeStep, randomChange);
                lastTreeSteps.add(currentTreeStep);
            }
        }

        return tree;
    }

    private double getBranchingChance(double width) {
        System.out.println(width);
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
