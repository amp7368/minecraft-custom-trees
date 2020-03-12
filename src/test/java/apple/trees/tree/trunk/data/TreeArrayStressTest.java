package apple.trees.tree.trunk.data;

import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;

import java.util.Random;

public class TreeArrayStressTest {
    public static void treeArrayTest() {
        long start = System.currentTimeMillis();
        int x = 100;
        int y = 100;
        int z = 100;
        TreeArray array = new TreeArray(x, z, y);
        Random random = new Random();
        System.out.println("Made the giant array");
        for (int i = 0; i < x; i++) {
            System.out.println("x of " + i + " out of " + x);
            for (int j = 0; j < z; j++) {
                for (int k = 0; k < y; k++) {
                    array.put(i, j, k, new Vec3d(random.nextFloat(), random.nextFloat(), random.nextFloat()), new Vec3d(random.nextFloat(), random.nextFloat(), random.nextFloat()), 3);
                }
            }
        }
        long finish = System.currentTimeMillis();
        long time = finish - start;
        System.out.println("It took " + time + " milliseconds to create " + x * y * z + " TreeSteps.");

    }
}
