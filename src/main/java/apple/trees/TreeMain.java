package apple.trees;

import apple.trees.tree.refine.ResolutionDecreaser;
import apple.trees.tree.trunk.creation.Trunk;
import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.utils.BaseTrunk;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeMain extends JavaPlugin {
    @Override
    public void onEnable() {
        BaseTrunk.initialize();

        TreeArray tree = Trunk.makeTrunkFromRaw(10, 100, 1, 3, 10, 1);
        tree = ResolutionDecreaser.pixelify(tree, 3);
        System.out.println("made the trunk");
        PlaceTree.placeTree(tree, new Location(Bukkit.getWorld("world"), 0, 100, 0));

    }
}
