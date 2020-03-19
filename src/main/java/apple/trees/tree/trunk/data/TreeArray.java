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
        return tree[x][y][z];
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
            TreeStep newStep = new TreeStep(broadX, broadY, broadZ, block.direction, block.slopeOfSlope, block.width);
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
            if (x != broadX && y != broadY && z != broadZ) {
                ArrayList<Vec3d> locations = getNearbySquares(broadX, broadY, broadZ, x, y, z);
                for (Vec3d loc : locations) {
                    tree[broadX][broadY][broadZ] = new TreeStep((int) loc.x, (int) loc.y, (int) loc.z, direction, slopeOfSlope, width);
                }
            }
            if (tree[broadX][broadY][broadZ] == null) {
                TreeStep step = new TreeStep(broadX, broadY, broadZ, direction, slopeOfSlope, width);
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
    public Vec3d getAvgDirection(int x, int z, int y, int distance) {
        if (distance == -1) {
            distance = 3;
        }
        distance = distance / 2;
        double avgX = 0;
        double avgY = 0;
        double avgZ = 0;
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
        double avgX = 0;
        double avgY = 0;
        double avgZ = 0;
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
        double avg = 0;
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

    /**
     * gets all the squares nearby to the location sizeof (0, 1, 2, or 3) returned
     *
     * @param xBroad the x location
     * @param yBroad the y location
     * @param zBroad the z location
     * @param xFine  the exact x location
     * @param yFine  the exact y location
     * @param zFine  the exact z location
     * @return all the x,y,z's that are close to the xFine,yFine,zFine
     */
    private static ArrayList<Vec3d> getNearbySquares(int xBroad, int yBroad, int zBroad, double xFine, double yFine, double zFine) {
        ArrayList<Vec3d> locations = new ArrayList<>(2);

        // see if it is close to a boundary
        double xDecimal = xFine - xBroad;
        double yDecimal = yFine - yBroad;
        double zDecimal = zFine - zBroad;
        if (xDecimal < .25) {
            locations.add(new Vec3d(xBroad - 1, yBroad, zBroad));
        } else if (xDecimal > .75) {
            locations.add(new Vec3d(xBroad + 1, yBroad, zBroad));
        }
        if (yDecimal < .25) {
            locations.add(new Vec3d(xBroad, yBroad - 1, zBroad));
        } else if (yDecimal > .75) {
            locations.add(new Vec3d(xBroad, yBroad + 1, zBroad));
        }
        if (zDecimal < .25) {
            locations.add(new Vec3d(xBroad, yBroad - 1, zBroad));
        } else if (zDecimal > .75) {
            locations.add(new Vec3d(xBroad, yBroad + 1, zBroad));
        }
        return locations;
    }


}
