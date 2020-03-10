package apple.trees.tree.trunk.data;

import com.sun.javafx.geom.Vec3d;

public class TreeArray {
    private TreeStep[][][] tree;

    /**
     * creates an empty array of a tree
     *
     * @param xSize the max size of x expected
     * @param zSize the max size of z expected
     * @param ySize the max size of y expected
     */
    public TreeArray(int xSize, int zSize, int ySize) {
        tree = new TreeStep[xSize][zSize][ySize];
    }

    /**
     * get the step at loc x, z, y
     *
     * @param x the x loc
     * @param z the y loc
     * @param y the z loc
     * @return the step at that location
     */
    public TreeStep get(int x, int z, int y) {
        return tree[x][z][y];
    }

    /**
     * places a treeStep at that loc
     *
     * @param x            the current x index
     * @param z            the current z index
     * @param y            the current y index
     * @param direction    the current slope
     * @param slopeOfSlope the current acceleration
     * @return the newly created step
     */
    public TreeStep put(int x, int z, int y, Vec3d direction, Vec3d slopeOfSlope) {
        TreeStep step = new TreeStep(x, z, y, direction, slopeOfSlope);
        tree[x][y][z] = step;
        return step;
    }

    public int sizeX() {
        return tree.length;
    }

    public int sizeY() {
        return tree[0].length;
    }

    public int sizeZ() {
        return tree[1].length;
    }
}
