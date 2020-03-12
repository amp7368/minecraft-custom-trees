package apple.trees.tree.trunk.utils;

import com.sun.javafx.geom.Vec3d;

public class BaseTrunkTest {
    public static void baseTrunkTest() {
        BaseTrunk.initialize();
        for (int i = 0; i < 10; i++)
            printVec(BaseTrunk.getRandomChangeSlopeOfSlope(new Vec3d(1, 0, 0), 1, .5));
        for (int i = 0; i < 10; i++)
            System.out.println("newTrunkWidth from lastWidth:100,decayRate:5 " + BaseTrunk.getRandomChangeWidth(100, 5));
    }

    private static void printVec(Vec3d vec) {
        System.out.println("x: " + vec.x + "y: " + vec.y + "z: ");
    }
}
