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

    private static final double DECAY_RATE = .05;
    private static final double BRANCHING_CHANCE = .2;
    private static final int maxCompletedSteps = 1000;
    private static Random random;

    public static void initialize(JavaPlugin pl) {
        random = new Random();
        BranchStep.initialize(pl, random);
        RandomChange.initialize(pl, random);
        GetRotations.initialize(pl, random);
    }

    /**
     * creates a random base trunk with the specified options
     *
     * @param trunk_width    the desired average width of the trunk
     * @param trunk_height   the absolute height of the trunk
     * @param leanLikelihood (between 0 and 1) (very low) the chance a change in slope of slope will occur in any given step
     * @param leanMagnitude  (between 0 and 1) the angle magnitude of lean shift if one occurs in any given step
     * @param maxLean        (between 0 and 1) the maximum amount a tree is allowed to lean
     * @param leanStart      (between 0 and 1) the starting lean of the trunk
     * @return the tree that was slightly filled in
     */
    public static TreeArray createBaseTrunk(TreeArray tree, int trunk_width, int trunk_height, float leanMagnitude, float leanLikelihood, float maxLean, Vec3d leanStart) {
        ArrayList<TreeStep> lastTreeSteps = new ArrayList<>();
        lastTreeSteps.add(createBaseStart(tree, new Vec3d(0, 1, 0), trunk_width, leanStart));

        TreeStep lastTreeStep;
        // loop until all the ends are finished
        treeStepLoop:
        for (int currentCompletedSteps = 0; currentCompletedSteps < maxCompletedSteps && !lastTreeSteps.isEmpty(); currentCompletedSteps++) {
            lastTreeStep = lastTreeSteps.remove(0);
            while (lastTreeStep == null) {
                if (lastTreeSteps.isEmpty())
                    break treeStepLoop;
                lastTreeStep = lastTreeSteps.remove(0);
            }
            if (lastTreeStep.y > trunk_height || lastTreeStep.y < 0
                    || lastTreeStep.x < 0 || lastTreeStep.x > tree.sizeX()
                    || lastTreeStep.z < 0 || lastTreeStep.z > tree.sizeZ())
                continue;
            TreeStep currentTreeStep;
            if (random.nextDouble() < BRANCHING_CHANCE) {
                System.out.println("Branched!");
                lastTreeSteps.addAll(BranchStep.getBranches(tree, lastTreeStep, 30, .5, 4, 2));
            } else {
                currentTreeStep = NormalStep.getCurrentTreeStep(tree, lastTreeStep, leanMagnitude, leanLikelihood, DECAY_RATE);
                lastTreeSteps.add(currentTreeStep);
            }
        }

        return tree;
    }


    private static TreeStep createBaseStart(TreeArray tree, Vec3d direction, int trunk_width, Vec3d leanStart) {
        int centerX = tree.sizeX() / 2;
        int centerY = 0;
        int centerZ = tree.sizeZ() / 2;


        int trunk_width_half = trunk_width / 2;
        for (int xi = -trunk_width_half; xi < trunk_width_half; xi++) {
            for (int zi = -trunk_width_half; zi < trunk_width_half; zi++) {
                if (xi == 0 && zi == 0) {
                    int a = 3;
                }
                tree.put(centerX + xi + 0.5, centerY + 0.5, centerZ + zi + 0.5, leanStart, direction, trunk_width);
            }
        }
        return tree.get(centerX, centerY, centerZ);
    }

}
