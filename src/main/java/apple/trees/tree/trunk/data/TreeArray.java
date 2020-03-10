package apple.trees.tree.trunk.data;

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
     * @param x             the current x index
     * @param z             the current z index
     * @param y             the current y index
     * @param xDirection    the current slope in x
     * @param zDirection    the current slope in z
     * @param yDirection    the current slope in y
     * @param xSlopeOfSlope the current acceleration in x
     * @param zSlopeOfSlope the current acceleration in y
     * @param ySlopeOfSlope the current acceleration in z
     * @return the newly created step
     */
    public TreeStep put(int x, int z, int y, float xDirection, float zDirection, float yDirection, float xSlopeOfSlope, float zSlopeOfSlope, float ySlopeOfSlope) {
        TreeStep step = new TreeStep(x, z, y, xDirection, zDirection, yDirection, xSlopeOfSlope, ySlopeOfSlope, zSlopeOfSlope);
        tree[x][y][z] = step;
        return step;
    }

}
