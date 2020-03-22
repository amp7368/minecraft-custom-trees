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
    public static TreeArray pixelify(TreeArray tree, double ratio, double ratioModifier) {
        // create a new tree of the correct-ish size
        int sizeX = tree.sizeX();
        int sizeY = tree.sizeY();
        int sizeZ = tree.sizeZ();
        TreeArray newTree = new TreeArray((int)(sizeX/ratio), (int)(sizeY/ratio),(int)(sizeZ/ratio));
        // loop through every square of the new tree
        for (double x = ratio; x < sizeX; x += ratio) {
            for (double y = ratio; y < sizeY; y += ratio) {
                for (double z = ratio; z < sizeZ; z += ratio) {

                    int xNew = (int) (x / ratio);
                    int yNew = (int) (y / ratio);
                    int zNew = (int) (z / ratio);

                    // if we should add it to the avg > (1/3 of the blocks)
                    if (tree.getAvgTrunkTrue(xNew, yNew, zNew, ratio * 2) > 1.0 / ratio * ratioModifier) {
                        Vec3d newDirection = tree.getAvgDirection(xNew, yNew, zNew, ratio * 2);
                        Vec3d newSlopeOfSlope = tree.getAvgSlopeOfSlope(xNew, yNew, zNew, ratio * 2);
                        double newWidth = tree.getAvgWidth(xNew, yNew, zNew, ratio * 2);
                        newTree.put(xNew, yNew, zNew, newDirection, newSlopeOfSlope, newWidth);
                    }
                }
            }
        }
        System.out.println("Reduced the size of the tree");
        return newTree;
    }

}
