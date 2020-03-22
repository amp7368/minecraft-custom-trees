package apple.trees.commands;

import apple.trees.YMLNavigate;
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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class PlaceTreeCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public PlaceTreeCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("place_tree").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        Trunk trunk;
        Material material;
        double ratioModifier;
        int ratio = 3;
        if (args.length == 1) {
            trunk = new Trunk(args[0], plugin);
            File file = new File(String.format("%s%s%s%s%s%s", plugin.getDataFolder(), File.separator, "treePresets", File.separator, args[0], ".yml"));
            if (!file.exists()) {
                material = Material.DARK_OAK_WOOD;
                ratioModifier = 0.5;
            } else {
                YamlConfiguration configOrig = YamlConfiguration.loadConfiguration(file);
                ConfigurationSection config = configOrig.getConfigurationSection("vars");
                assert config != null;
                material = Material.getMaterial(config.getString(YMLNavigate.MATERIAL));
                ratioModifier = config.getDouble(YMLNavigate.RATIO_MODIFIER);
                ratio = config.getInt(YMLNavigate.RATIO);
            }
        } else {
            // create a trunk with default values
            trunk = new Trunk();
            ratioModifier = .5;
            material = Material.DARK_OAK_WOOD;
        }
        // make a trunk given the default values in trunk
        TreeArray tree = trunk.makeTrunk();

        // make the leaves given a tree with a trunk in place

        // place the tree near the player todo should be at a given location later
        Location loc = Bukkit.getPlayer(commandSender.getName()).getLocation();
        placeTree(tree, loc, material, plugin);

        // reduce the resolution of the entire tree
        tree = ResolutionDecreaser.pixelify(tree, ratio, ratioModifier);
        Bukkit.getServer().broadcastMessage("made a treeBig!");
        loc.setX(loc.getX() - 50);
        placeTree(tree, loc, material, plugin);
        Bukkit.getServer().broadcastMessage("made a treeSmall!");
        return false;
    }

    public static void placeTree(TreeArray tree, Location location, Material material, JavaPlugin plugin) {
        // the starting location where tree should be placed
        int startX = location.getBlockX();
        int startY = location.getBlockY();
        int startZ = location.getBlockZ();

        // the tree's dimensions
        int sizeX = tree.sizeX();
        int sizeY = tree.sizeY();
        int sizeZ = tree.sizeZ();


        World world = location.getWorld();
        float i = 0;
        for (int x = startX, xorig = 0; xorig < sizeX; x++, xorig++, i += .1) {
            int finalXorig = xorig;
            int finalX = x;
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
                    () -> {
                        for (int y = startY, yorig = 0; yorig < sizeY; y++, yorig++) {
                            for (int z = startZ, zorig = 0; zorig < sizeZ; z++, zorig++) {
                                if (tree.get(finalXorig, yorig, zorig) != null) {
                                    world.getBlockAt(finalX, y, z).setType(material);
                                }
                            }
                        }
                    }
                    , (int) i);
        }
        System.out.println("Placed a tree");
    }


}
