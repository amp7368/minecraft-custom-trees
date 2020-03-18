package apple.trees.tree.trunk;

import apple.trees.tree.trunk.creation.BaseTrunk;
import apple.trees.tree.trunk.creation.Trunk;
import apple.trees.tree.trunk.data.VectorRotationTest;
import apple.trees.tree.trunk.utils.RotationTest;
import apple.trees.tree.trunk.utils.WidthifyTest;

import java.util.Random;

public class StressTest {

    private static final double BELL_CURVE_A = .3;
    private static final double BELL_CURVE_B = .5;
    private static final double BELL_CURVE_LEFT = 1 / (BELL_CURVE_A * Math.sqrt(2 * Math.PI));

    public static void main(String[] args) {
        BaseTrunk.initialize();
//        WidthifyTest.widthifyTest();
        (new Trunk()).makeTrunk();
    }
}
