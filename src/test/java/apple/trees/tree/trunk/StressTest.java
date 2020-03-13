package apple.trees.tree.trunk;

import apple.trees.tree.trunk.creation.Trunk;
import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeArrayStressTest;
import apple.trees.tree.trunk.utils.BaseTrunk;
import apple.trees.tree.trunk.utils.BaseTrunkTest;

public class StressTest {
    public static void main(String[] args) {
        BaseTrunk.initialize();
//        TreeArrayStressTest.treeArrayTest();
//        BaseTrunkTest.baseTrunkTest();
        TreeArray tree = Trunk.makeTrunkFromRaw(10, 100, 3, 3, 10, 1);
        treeIsNull(tree);
    }

    private static void treeIsNull(TreeArray tree) {
        for (int x = 0; x < tree.sizeX(); x++) {
            for (int y = 0; y < tree.sizeY(); y++) {
                for (int z = 0; z < tree.sizeZ(); z++) {
                    if (tree.get(x, y, z) != null) {
                        System.out.println("x:" + x + " ,y:" + y + " ,z:" + z + " is not null");
                    }
                }
            }
        }
    }
}
