package apple.trees.tree.trunk.creation.utils;

import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

public class Widthify {
    public static TreeArray addWidth(TreeArray tree) {
        // get the size of the tree
        int sizeX = tree.sizeX();
        int sizeY = tree.sizeY();
        int sizeZ = tree.sizeZ();

        // create a new tree with the same size
        TreeArray newTree = new TreeArray(sizeX, sizeY, sizeZ);


        // for each element in the TreeArray
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
                for (int z = 0; z < sizeZ; z++) {
                    TreeStep block = tree.get(x, y, z);
                    if (block != null) {
                        if (block.width > 1) {
                            addWidthStep(newTree, block);
                        }
                        newTree.put(block);
                    }
                }
            }
        }


        return newTree;
    }

    private static void addWidthStep(TreeArray newTree, TreeStep block) {
        double widthDiv2 = block.width / 2;
        // (v0,v1,v2) = normal vector
        // (x0,y0,z0) = plane passes through this point (circle is around this point)
        // v0 (x-x0) + v1 (y-y0) + v2 (z-z0)
        // expands to
        // v0(x) - v0(x0) + v1(y) - v1(y0) + v2(z) - v2(z0)
        // - v0(x0) - v1(y0) - v2(z0) + v0(x) + v1(y) + v2(z)
        // v0(x) + v1(y) + v2(z) = v0(x0) + v1(y0) + v2(z0)

        double v0 = block.direction.x;
        double v1 = block.direction.y;
        double v2 = block.direction.z;
        int x0 = (int) block.x;
        int y0 = (int) block.y;
        int z0 = (int) block.z;
        ArrayList<Vec3d> list = new ArrayList<>();

        double widthDiv2_1 = widthDiv2 - 1;
        for (int x = (int) Math.floor(-widthDiv2); x <= widthDiv2_1+.5; x++) {
            for (int y = (int) Math.floor(-widthDiv2); y <= widthDiv2_1+.5; y++) {
                if (v2 <= .2) {
                    // try all the Z's
                    for (int z = (int) Math.floor(-widthDiv2); z <= widthDiv2_1+.5; z++) {
                        final double highPoint = (x + 1) * v0 + (y + 1) * v1;
                        final double lowPoint = x * v0 + y * v1;
                        if (lowPoint < 0) {
                            if (highPoint >= 0) {
                                list.add(new Vec3d(x + x0, y + y0, z + z0));
                            } else {
                                // none of the Z's will work
                                break;
                            }
                        } else if (lowPoint == 0) {
                            list.add(new Vec3d(x + x0, y + y0, z + z0));
                        } else if (highPoint <= 0) {
                            list.add(new Vec3d(x + x0, y + y0, z + z0));
                        } else {
                            // none of the Z's will work
                            break;
                        }
                    }
                } else {
                    double lowZ = (-x * v0 - y * v1) / v2;
                    double highZ = (-(x + 1) * v0 - (y + 1) * v1) / v2;

                    // convert lowZ to the lowest magnitude it could be
                    boolean wasNegative = lowZ < 0;
                    lowZ = Math.min(Math.abs(lowZ), widthDiv2);
                    if (wasNegative)
                        lowZ = -lowZ;

                    // convert highZ to the lowest magnitude it could be
                    wasNegative = highZ < 0;
                    highZ = Math.min(Math.abs(highZ), widthDiv2);
                    if (wasNegative) {
                        highZ = -highZ;
                        highZ -= .2;
                        lowZ += .2;
                    } else {
                        highZ += .2;
                        lowZ -= .2;
                    }
                    if (lowZ < highZ) {
                        // go from lowZ to highZ
                        for (int z = (int) Math.floor(lowZ); z <= highZ; z++) {
                            // add all these z's
                            list.add(new Vec3d(x + x0, y + y0, z + z0));
                        }
                    } else {
                        // go from highZ to lowZ
                        for (int z = (int) Math.floor(highZ); z <= lowZ; z++) {
                            // add all these z's
                            list.add(new Vec3d(x + x0, y + y0, z + z0));
                        }
                    }
                }
            }
        }

        // add the stuff in the list
        for (Vec3d point : list) {
            newTree.put(point.x, point.y, point.z, block.direction, block.slopeOfSlope, block.width);
        }
    }
}
