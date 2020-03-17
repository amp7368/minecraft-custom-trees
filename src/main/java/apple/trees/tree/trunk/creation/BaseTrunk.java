package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.creation.utils.GetRotations;
import apple.trees.tree.trunk.creation.utils.RandomChange;
import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

public class BaseTrunk {
    private static final int MAX_COMPLETED_STEPS = 1000;
    private static final double BELL_CURVE_A = .15;
    private static final double BELL_CURVE_B = .1;
    private static final double BELL_CURVE_LEFT = 1 / (BELL_CURVE_A * Math.sqrt(2 * Math.PI));
    private final int branchAngle;
    private final double branchStealing;
    private int trunk_width;
    private int trunk_height;
    private float leanMagnitude;
    private float leanLikelihood;
    private float maxLean;
    private Vec3d leanStart;
    private double decayRate;
    private double branchingChance;
    private int branchesMean;
    private static Random random;
    private Vec3d startDirection;

    public BaseTrunk(int trunk_width, int trunk_height, float leanMagnitude, float leanLikelihood, float maxLean, Vec3d leanStart, double decayRate, double branchingChance, int branchesMean,int branchAngle) {
        this.startDirection = new Vec3d(0, 1, 0);
        this.trunk_width = trunk_width;
        this.trunk_height = trunk_height;
        this.leanMagnitude = leanMagnitude;
        this.leanLikelihood = leanLikelihood;
        this.maxLean = maxLean;
        this.leanStart = leanStart;
        this.decayRate = decayRate;
        this.branchingChance = branchingChance;
        this.branchesMean = branchesMean;
        this.branchAngle = branchAngle;
        this.branchStealing = .5;
    }

    public static void initialize(JavaPlugin pl) {
        random = new Random();
        RandomChange.initialize(random);
        GetRotations.initialize(random);
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
        BranchStep branchStep = new BranchStep(branchAngle, branchStealing, branchesMean, random);
        NormalStep normalStep = new NormalStep(leanMagnitude, leanLikelihood, decayRate);
        TreeStep lastTreeStep;
        // loop until all the ends are finished
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
                    || lastTreeStep.x < 0 || lastTreeStep.x > tree.sizeX()
                    || lastTreeStep.z < 0 || lastTreeStep.z > tree.sizeZ())
                continue;

            // do either a branch or a normal step
            TreeStep currentTreeStep;
            if (random.nextDouble() < getBranchingChance(lastTreeStep.width / trunk_width)) {
                lastTreeSteps.addAll(branchStep.getBranches(tree, lastTreeStep));
            } else {
                currentTreeStep = normalStep.getCurrentTreeStep(tree, lastTreeStep);
                lastTreeSteps.add(currentTreeStep);
            }
        }

        return tree;
    }

    private double getBranchingChance(double width) {
        return branchingChance * BELL_CURVE_LEFT * Math.pow(Math.E, -0.5 * (Math.pow(width - BELL_CURVE_B, 2)
                / Math.pow(BELL_CURVE_A, 2)));
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
                if (xi == 0 && zi == 0) {
                    int a = 3;
                }
                tree.put(centerX + xi + 0.5, centerY + 0.5, centerZ + zi + 0.5, leanStart, startDirection, trunk_width);
            }
        }
        return tree.get(centerX, centerY, centerZ);
    }

}
