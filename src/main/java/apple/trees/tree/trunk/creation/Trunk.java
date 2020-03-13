package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.utils.BaseTrunk;
import com.sun.javafx.geom.Vec3d;

public class Trunk {

    /**
     * creates a random trunk with the specified options
     *
     * @param trunk_width    the desired average width of the trunk
     * @param trunk_height   the absolute height of the trunk
     * @param leanLikelihood (between 0 and 1) (very low) the chance a change in slope of slope will occur in any given step
     * @param leanMagnitude  (between 0 and 1) the angle magnitude of lean shift if one occurs in any given step
     * @param maxLean        (between 0 and 1) the maximum amount a tree is allowed to lean
     * @param leanStart      (between 0 and 1) the starting lean of the trunk
     */
    private static TreeArray makeTrunk(int trunk_width, int trunk_height, float leanMagnitude, float leanLikelihood, float maxLean, Vec3d leanStart) {
        TreeArray tree = new TreeArray(100, 100, 100);
        BaseTrunk.createBaseTrunk(tree, trunk_width, trunk_height, leanMagnitude, leanLikelihood, maxLean, leanStart);
        return tree;
    }

    /**
     * creates a random trunk with the specified options
     *
     * @param trunk_width    the desired average width of the trunk
     * @param trunk_height   the absolute height of the trunk
     * @param leanMagnitude  the angle magnitude of lean shift if one occurs in any given step
     * @param leanLikelihood the chance a change in slope of slope will occur in any given step
     * @param maxLean        the maximum amount a tree is allowed to lean
     * @param leanStart      the starting lean of the trunk
     * @return a randomly created trunk or null if the options are bad
     */
    public static TreeArray makeTrunkFromRaw(int trunk_width, int trunk_height, int leanMagnitude, int leanLikelihood, int maxLean, int leanStart) {
        //todo
        return makeTrunk(trunk_width, trunk_height, leanMagnitude, leanLikelihood, maxLean, new Vec3d(leanStart, leanStart, leanStart));
    }
}
