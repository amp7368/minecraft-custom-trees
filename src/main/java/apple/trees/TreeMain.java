package apple.trees;

import apple.trees.commands.PlaceTreeCommand;
import apple.trees.commands.PlaceVector;
import apple.trees.tree.trunk.creation.utils.GetRotations;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeMain extends JavaPlugin {
    @Override
    public void onEnable() {
        new PlaceTreeCommand(this);
        new PlaceVector(this);
    }
}
