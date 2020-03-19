package apple.trees.tree.trunk.utils;

import apple.trees.tree.trunk.creation.utils.Widthify;
import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;

public class WidthifyTest {

    public static void widthifyTest() {
        System.out.println("testing widthify");
        TreeArray tree = new TreeArray(100, 100, 100);
        tree.put(50, 50, 50, new Vec3d(0, 3, 3), new Vec3d(0, 0, 0), 5);
        Widthify.addWidth(tree);
        for (int i = 0; i < 100; i++) {
            for (int y = 0; y < 100; y++) {
                for (int z = 0; z < 100; z++) {
                    if (tree.get(i, y, z) != null)
                        System.out.println(String.format("x:%d, y:%d, z:%d",i,y,z));
                }
            }
        }
    }
}
