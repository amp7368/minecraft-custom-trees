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
        TreeStep step = new TreeStep(x, z, y, direction, slopeOfSlope, width);
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

    public Vec3d getAvgDirection(int x, int z, int y) {
        int avgX = 0;
        int avgY = 0;
        int avgZ = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    Vec3d vec = tree[x + i][y + j][z + k].direction;
                    avgX += vec.x;
                    avgY += vec.y;
                    avgZ += vec.z;
                }
            }
        }
        return new Vec3d(avgX, avgY, avgZ);
    }

    public Vec3d getAvgSlopeOfSlope(int x, int y, int z) {
        int avgX = 0;
        int avgY = 0;
        int avgZ = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    Vec3d vec = tree[x + i][y + j][z + k].direction;
                    avgX += vec.x;
                    avgY += vec.y;
                    avgZ += vec.z;
                }
            }
        }
        return new Vec3d(avgX, avgY, avgZ);
    }

    public double getAvgWidth(int x, int y, int z) {
        int avg = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    avg += tree[x + i][y + j][z + k].width;
                }
            }
        }
        return avg;
    }
}
