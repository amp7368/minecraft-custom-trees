package apple.trees.commands;

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

public class PlaceVector implements CommandExecutor {
    private final JavaPlugin plugin;

    public PlaceVector(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getCommand("place_vector").setExecutor(this);
    }


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length != 1) {
            return false;
        }
        File file = new File(String.format("%s%s%s%s%s%s", plugin.getDataFolder(), File.separator, "treePresets", File.separator, "vectors", ".yml"));
        YamlConfiguration configOrig = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection configBase = configOrig.getConfigurationSection(args[0]);
        if (configBase == null) {
            Bukkit.getServer().broadcastMessage("oof");
            return false;
        }
        int i = 1;
        ConfigurationSection config = configBase.getConfigurationSection(args[0] + i);
        while (config != null) {

            Vec3d vector = new Vec3d();
            vector.x = config.getDouble("x");
            vector.y = config.getDouble("y");
            vector.z = config.getDouble("z");

            Location loc = Bukkit.getPlayer(commandSender.getName()).getLocation();
            placeVector(vector, loc);
            Bukkit.getServer().broadcastMessage("made a vector!");
            config = configBase.getConfigurationSection(String.format("%s%d", args[0], ++i));
        }
        return true;
    }

    private static void placeVector(Vec3d vector, Location location) {
        // the starting location where tree should be placed
        int startX = location.getBlockX();
        int startY = location.getBlockY();
        int startZ = location.getBlockZ();

        // the tree's dimensions
        double x = 10;
        double y = 0;
        double z = 10;
        World world = location.getWorld();
        for (int i = 0; i < 10; i++) {
            world.getBlockAt((int) x + startX, (int) y + startY, (int) z + startZ).setType(Material.DARK_OAK_WOOD);
            x += vector.x;
            y += vector.y;
            z += vector.z;

        }
    }
}
