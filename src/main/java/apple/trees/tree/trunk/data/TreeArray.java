package apple.trees.tree.trunk.data;

import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

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
        if (x < sizeX() && y < sizeY() && z < sizeZ() && 0 <= x && 0 <= y && 0 <= z)
            return tree[x][y][z];
        return null;
    }

    /**
     * places a treeStep where the last treeStep was
     *
     * @param block the old TreeStep
     */
    public void put(TreeStep block) {
        int broadX = (int) block.x;
        int broadY = (int) block.y;
        int broadZ = (int) block.z;
        if (broadX < sizeX() && broadY < sizeY() && broadZ < sizeZ() && broadX >= 0 && broadY >= 0 && broadZ >= 0) {
            TreeStep newStep = new TreeStep(block.x, block.y, block.z, block.direction, block.slopeOfSlope, block.width);
            tree[broadX][broadY][broadZ] = newStep;
        }
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
    public TreeStep put(double x, double y, double z, Vec3d direction, Vec3d slopeOfSlope, double width) {
        int broadX = (int) x;
        int broadY = (int) y;
        int broadZ = (int) z;
        if (x < sizeX() && y < sizeY() && z < sizeZ() && x >= 0 && y >= 0 && z >= 0) {
            if (tree[broadX][broadY][broadZ] == null) {
                TreeStep step = new TreeStep(x, y, z, direction, slopeOfSlope, width);
                tree[broadX][broadY][broadZ] = step;
                return step;
            } else {
                return tree[broadX][broadY][broadZ];
            }
        }
        return null;
    }

    public int sizeX() {
        return tree.length;
    }

    public int sizeY() {
        if (tree.length != 0)
            return tree[0].length;
        return 0;
    }

    public int sizeZ() {
        if (tree.length != 0 && tree[0].length != 0)
            return tree[0][0].length;
        return 0;
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
    public Vec3d getAvgDirection(double x, double z, double y, double distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        double avgX = 0;
        double avgY = 0;
        double avgZ = 0;
        int totalNotNull = 0;
        for (double i = -distance; i <= distance; i++) {
            for (double j = -distance; j <= distance; j++) {
                for (double k = -distance; k <= distance; k++) {
                    TreeStep treeStep;
                    treeStep = get((int) (x + i), (int) (y + j), (int) (z + k));
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
    public Vec3d getAvgSlopeOfSlope(double x, double y, double z, double distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        double avgX = 0;
        double avgY = 0;
        double avgZ = 0;
        int totalNotNull = 0;
        for (double i = -distance; i <= distance; i++) {
            for (double j = -distance; j <= distance; j++) {
                for (double k = -distance; k <= distance; k++) {
                    TreeStep treeStep;
                        treeStep = get((int) (x + i), (int) (y + j), (int) (z + k));
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
    public double getAvgWidth(double x, double y, double z, double distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        double avg = 0;
        int totalNotNull = 0;
        for (double i = -distance; i <= distance; i++) {
            for (double j = -distance; j <= distance; j++) {
                for (double k = -distance; k <= distance; k++) {
                    TreeStep treeStep;
                    treeStep = get((int) (x + i), (int) (y + j), (int) (z + k));
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


    public double getAvgTrunkTrue(double x, double y, double z, double distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        int trues = 0;
        int totals = 0;
        for (double i = -distance; i <= distance; i++) {
            for (double j = -distance; j <= distance; j++) {
                for (double k = -distance; k <= distance; k++) {
                    TreeStep treeStep;
                    treeStep = get((int) (x + i), (int) (y + j), (int) (z + k));
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

    public void print() {
        for (int x = 0; x < sizeX(); x++) {
            for (int y = 0; y < sizeY(); y++) {
                for (int z = 0; z < sizeZ(); z++) {
                    TreeStep block = get(x, y, z);
                    if (block != null) {
                        System.out.println(String.format("x:%d, y:%d, z:%d", x, y, z));
                    }
                }
            }
        }
    }
}
