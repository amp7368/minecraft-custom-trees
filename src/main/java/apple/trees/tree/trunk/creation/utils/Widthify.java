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
                        addWidthStep(newTree, block);
                    }
                }
            }
        }


        return newTree;
    }

    private static void addWidthStep(TreeArray newTree, TreeStep block) {
        Vec3d direction = block.direction;
        double widthDiv2 = block.width / 2;
        // (v0,v1,v2) = normal vector
        // (x0,y0,z0) = plane passes through this point (circle is around this point)
        // v0 (x-x0) + v1 (y-y0) + v2 (z-z0)
        // expands to
        // v0(x) - v0(x0) + v1(y) - v1(y0) + v2(z) - v2(z0)
        // - v0(x0) - v1(y0) - v2(z0) + v0(x) + v1(y) + v2(z)
        // v0(x) + v1(y) + v2(z) = v0(x0) + v1(y0) + v2(z0)
        double rightSide = direction.x * block.x + direction.y * block.y + direction.z * block.z;
        ArrayList<Vec3d> list = new ArrayList<>();

        //todo optimize
        for (double x = -widthDiv2 + block.x; x < widthDiv2 + block.x; x++) {
            // v1(y) + v2(z) = v0(x0) + v1(y0) + v2(z0) - v0(x)
            double xvRightSide = rightSide - direction.x * x;
            for (double y = -widthDiv2 + block.y; y < widthDiv2 + block.y; y++) {
                // v2(z) = v0(x0) + v1(y0) + v2(z0) - v0(x) -v1(y)
                double yvRightSide = xvRightSide - direction.y * y;

                // solve for z
                double z = yvRightSide / direction.y;

                if (-widthDiv2 + block.z < z && z < widthDiv2 + block.z) {
                    list.add(new Vec3d(x, y, z));
                    System.out.println(String.format("x:%f,y:%f,z:%f", x, y, z));
                }

            }
        }

    }
}
