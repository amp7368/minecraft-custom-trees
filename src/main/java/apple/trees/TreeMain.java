package apple.trees;

import apple.trees.commands.PlaceTreeCommand;
import apple.trees.tree.trunk.creation.BaseTrunk;
import org.bukkit.plugin.java.JavaPlugin;

public class TreeMain extends JavaPlugin {
    @Override
    public void onEnable() {
        BaseTrunk.initialize();
        new PlaceTreeCommand(this);
    }
}
