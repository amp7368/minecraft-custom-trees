package apple.trees;

import apple.trees.tree.trunk.data.TreeArray;
import org.bukkit.Location;
import org.bukkit.Material;

public class PlaceTree {
    public static void placeTree(TreeArray tree, Location location) {
        //todo
        for (int x = location.getBlockX(), xorig = 0; xorig < tree.sizeX(); x++, xorig++) {
            for (int y = location.getBlockY(), yorig = 0; yorig < tree.sizeY(); y++, yorig++) {
                for (int z = location.getBlockZ(), zorig = 0; zorig < tree.sizeZ(); z++, zorig++) {
                    if (tree.get(xorig, yorig, zorig) != null)
                        location.getWorld().getBlockAt(x, y, z).setType(Material.BIRCH_WOOD);
                }
            }
        }
    }
}
