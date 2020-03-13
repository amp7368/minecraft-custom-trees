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
    public TreeStep get(int x, int y, int z) {
        return tree[x][y][z];
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
    public TreeStep put(int x, int z, int y, Vec3d direction, Vec3d slopeOfSlope, double width) {
        if (x < sizeX() && y < sizeY() && z < sizeZ() && x > 0 && y > 0 && z > 0) {
            TreeStep step = new TreeStep(x, z, y, direction, slopeOfSlope, width);
            tree[x][y][z] = step;
            return step;
        }
        return null;
    }

    public int sizeX() {
        return tree.length;
    }

    public int sizeY() {
        //todo
        return tree[0].length;
    }

    public int sizeZ() {
        //todo
        return tree[0][0].length;
    }

    /**
     * gets the average direction of the tree around that area
     *
     * @param x        the x to get the avg at
     * @param z        the y to get the avg at
     * @param y        the z to get the avg at
     * @param distance how far to look around that xyz (-1 for default 3)
     * @return the avg direction
     */
    public Vec3d getAvgDirection(int x, int z, int y, int distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        int avgX = 0;
        int avgY = 0;
        int avgZ = 0;
        int totalNotNull = 0;
        for (int i = -distance; i <= distance; i++) {
            for (int j = -distance; j <= distance; j++) {
                for (int k = -distance; k <= distance; k++) {
                    TreeStep treeStep;
                    try {
                        treeStep = tree[x + i][y + j][z + k];
                    } catch (IndexOutOfBoundsException ignored) {
                        continue;
                    }
                    if (treeStep == null)
                        continue;
                    Vec3d vec = treeStep.direction;
                    avgX += vec.x;
                    avgY += vec.y;
                    avgZ += vec.z;
                    totalNotNull++;

                }
            }
        }
        if (totalNotNull != 0) {
            avgX /= totalNotNull;
            avgY /= totalNotNull;
            avgZ /= totalNotNull;
        }
        return new Vec3d(avgX, avgY, avgZ);
    }

    /**
     * gets the average slopeOfSlope of the tree around that location
     *
     * @param x        the x to get the avg at
     * @param z        the y to get the avg at
     * @param y        the z to get the avg at
     * @param distance how far to look around that xyz (-1 for default 3)
     * @return the avg slopeOfSlope
     */
    public Vec3d getAvgSlopeOfSlope(int x, int y, int z, int distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        int avgX = 0;
        int avgY = 0;
        int avgZ = 0;
        int totalNotNull = 0;
        for (int i = -distance; i <= distance; i++) {
            for (int j = -distance; j <= distance; j++) {
                for (int k = -distance; k <= distance; k++) {
                    TreeStep treeStep;
                    try {
                        treeStep = tree[x + i][y + j][z + k];

                    } catch (IndexOutOfBoundsException ignored) {
                        continue;
                    }
                    if (treeStep == null)
                        continue;
                    totalNotNull++;
                    Vec3d vec = treeStep.slopeOfSlope;
                    avgX += vec.x;
                    avgY += vec.y;
                    avgZ += vec.z;
                }
            }
        }
        if (totalNotNull != 0) {
            avgX /= totalNotNull;
            avgY /= totalNotNull;
            avgZ /= totalNotNull;
        }
        return new Vec3d(avgX, avgY, avgZ);
    }

    /**
     * gets the average width of the tree around that location
     *
     * @param x        the x to get the avg at
     * @param z        the y to get the avg at
     * @param y        the z to get the avg at
     * @param distance how far to look around that xyz (-1 for default 3)
     * @return the avg width
     */
    public double getAvgWidth(int x, int y, int z, int distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        int avg = 0;
        int totalNotNull = 0;
        for (int i = -distance; i <= distance; i++) {
            for (int j = -distance; j <= distance; j++) {
                for (int k = -distance; k <= distance; k++) {
                    TreeStep treeStep;
                    try {
                        treeStep = tree[x + i][y + j][z + k];

                    } catch (IndexOutOfBoundsException ignored) {
                        continue;
                    }
                    if (treeStep == null)
                        continue;
                    avg += treeStep.width;
                    totalNotNull++;

                }
            }
        }
        if (totalNotNull != 0) {
            avg /= totalNotNull;
        }
        return avg;
    }


    public double getAvgTrunkTrue(int x, int y, int z, int distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        int trues = 0;
        int totals = 0;
        for (int i = -distance; i <= distance; i++) {
            for (int j = -distance; j <= distance; j++) {
                for (int k = -distance; k <= distance; k++) {
                    TreeStep treeStep;
                    try {
                        treeStep = tree[x + i][y + j][z + k];
                    } catch (IndexOutOfBoundsException ignored) {
                        continue;
                    }
                    if (treeStep != null) {
                        trues++;
                    }
                    totals++;
                }
            }
        }
        if (totals == 0) {
            return 0;
        }
        return ((double) trues) / totals;
    }
}
