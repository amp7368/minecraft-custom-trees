package apple.trees.tree.trunk;

import apple.trees.tree.trunk.creation.BaseTrunk;
import apple.trees.tree.trunk.data.VectorRotationTest;

public class StressTest {
    public static void main(String[] args) {
        BaseTrunk.initialize();
//        TreeArrayStressTest.treeArrayTest();
//        BaseTrunkTest.baseTrunkTest();
//        TreeArray tree = Trunk.makeTrunk(10, 100, 3, 3, 10, new Vec3d(0, 1, 0));
//        tree = ResolutionDecreaser.pixelify(tree, 3);
        VectorRotationTest.testVectorRotation();
        int a =3;
    }
}
