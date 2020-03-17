package apple.trees.tree.refine;

import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;

public class ResolutionDecreaser {

    /**
     * creates a new tree that will have a lower resolution than the old tree
     *
     * @param tree  the old tree
     * @param ratio bigger than 1 (ie: treeOldSize = 3,ratio = 3,treeNewSize = 1)
     *              ratio is better if it is an int
     * @return the new tree
     */
    public static TreeArray pixelify(TreeArray tree, double ratio) {
        // create a new tree of the correct-ish size
        int sizeX = (int) (tree.sizeX() / ratio);
        int sizeY = (int) (tree.sizeY() / ratio);
        int sizeZ = (int) (tree.sizeZ() / ratio);
        TreeArray newTree = new TreeArray(sizeX, sizeY, sizeZ);
        int intRatio = (int) ratio;
        // loop through every square of the new tree
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    int xOld = (int) (x * ratio);
                    int yOld = (int) (y * ratio);
                    int zOld = (int) (z * ratio);

                    // if we should add it to the avg  todo change 0
                    if (tree.getAvgTrunkTrue(xOld, yOld, zOld, intRatio) > 0) {
                        Vec3d newDirection = tree.getAvgDirection(xOld, yOld, zOld, intRatio);
                        Vec3d newSlopeOfSlope = tree.getAvgSlopeOfSlope(xOld, yOld, zOld, intRatio);
                        double newWidth = tree.getAvgWidth(xOld, yOld, zOld, intRatio);
                        newTree.put(x, y, z, newDirection, newSlopeOfSlope, newWidth);
                    }
                }
            }
        }
        return newTree;
    }

}
