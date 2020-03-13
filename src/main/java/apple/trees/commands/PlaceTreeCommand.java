package apple.trees.commands;

import apple.trees.tree.refine.ResolutionDecreaser;
import apple.trees.tree.trunk.creation.Trunk;
import apple.trees.tree.trunk.data.TreeArray;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceTreeCommand implements CommandExecutor {
    public PlaceTreeCommand(JavaPlugin plugin) {
        plugin.getCommand("place_tree").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        TreeArray tree = Trunk.makeTrunkFromRaw(10, 100, 1, 3, 10, 1);
        tree = ResolutionDecreaser.pixelify(tree, 3);
        System.out.println("made the trunk");
        Location loc = Bukkit.getPlayer(commandSender.getName()).getLocation();
        placeTree(tree, loc);
        return false;
    }

    private static void placeTree(TreeArray tree, Location location) {
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
