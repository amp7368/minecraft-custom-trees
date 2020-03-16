package apple.trees.commands;

import apple.trees.tree.refine.ResolutionDecreaser;
import apple.trees.tree.trunk.creation.Trunk;
import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceTreeCommand implements CommandExecutor {
    public PlaceTreeCommand(JavaPlugin plugin) {
        plugin.getCommand("place_tree").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        TreeArray tree = Trunk.makeTrunk(10, 100, (float) .3, 3, 10, new Vec3d(0, 3, 0));
//        tree = ResolutionDecreaser.pixelify(tree, 3);
        Location loc = Bukkit.getPlayer(commandSender.getName()).getLocation();
        placeTree(tree, loc);
        Bukkit.getServer().broadcastMessage("made a tree!");
        return false;

    }

    private static void placeTree(TreeArray tree, Location location) {
        //todo optimize
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
