package apple.trees.tree.trunk.utils;

import apple.trees.tree.trunk.creation.utils.Widthify;
import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;

public class WidthifyTest {

    public static void widthifyTest() {
        System.out.println("testing widthify");
        TreeArray tree = new TreeArray(100, 100, 100);
        tree.put(50, 50, 50, new Vec3d(0, 0, 1), new Vec3d(0, 0, 0), 5);
        Widthify.addWidth(tree);
    }
}
