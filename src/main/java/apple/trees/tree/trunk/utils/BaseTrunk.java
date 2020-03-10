package apple.trees.tree.trunk.utils;

import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;

import java.util.Vector;

public class BaseTrunk {
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
        int centerX = tree.sizeX() / 2;
        int centerZ = tree.sizeZ() / 2;
        int centerY = tree.sizeY() / 2;


        int trunk_width_half = trunk_width / 2;
        for (int xi = -(trunk_width - 1) / 2; xi < trunk_width_half; xi++) {
            for (int zi = -(trunk_width - 1) / 2; zi < trunk_width_half; zi++) {
                tree.put(centerX, centerZ, centerY, leanStart, new Vec3d(0, 0, 0));
            }
        }

        TreeStep lastTreeStep;
        return tree;
    }
}
