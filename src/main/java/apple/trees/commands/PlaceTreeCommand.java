package apple.trees.commands;

import apple.trees.tree.refine.ResolutionDecreaser;
import apple.trees.tree.trunk.creation.Trunk;
import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class PlaceTreeCommand implements CommandExecutor {
    private static final double DECAY_RATE = .05;
    private static final double BRANCHING_CHANCE = .06;
    private static final int BRANCHES_MEAN = 4;
    private final JavaPlugin plugin;

    public PlaceTreeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("place_tree").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Trunk trunk;
        if (args.length == 1) {
            trunk = new Trunk(args[0], plugin);
        } else {
            // create a trunk with default values
            trunk = new Trunk();
        }
        // make a trunk given the default values in trunk
        TreeArray tree = trunk.makeTrunk();

        // make the leaves given a tree with a trunk in place

        // reduce the resolution of the entire tree
//        tree = ResolutionDecreaser.pixelify(tree, 3);

        // place the tree near the player todo should be at a given location later
        Location loc = Bukkit.getPlayer(commandSender.getName()).getLocation();
        placeTree(tree, loc);
        Bukkit.getServer().broadcastMessage("made a tree!");
        return false;
    }

    private static void placeTree(TreeArray tree, Location location) {
        // the starting location where tree should be placed
        int startX = location.getBlockX();
        int startY = location.getBlockY();
        int startZ = location.getBlockZ();

        // the tree's dimensions
        int sizeX = tree.sizeX();
        int sizeY = tree.sizeY();
        int sizeZ = tree.sizeZ();

        World world = location.getWorld();
        for (int x = startX, xorig = 0; xorig < sizeX; x++, xorig++) {
            for (int y = startY, yorig = 0; yorig < sizeY; y++, yorig++) {
                for (int z = startZ, zorig = 0; zorig < sizeZ; z++, zorig++) {
                    if (tree.get(xorig, yorig, zorig) != null)
                        world.getBlockAt(x, y, z).setType(Material.BIRCH_WOOD);
                }
            }
        }
    }


}
