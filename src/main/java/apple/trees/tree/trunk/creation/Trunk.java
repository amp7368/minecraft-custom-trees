package apple.trees.tree.trunk.creation;

import apple.trees.YMLNavigate;
import apple.trees.tree.trunk.creation.utils.Widthify;
import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Trunk {

    private int trunk_width;
    private int trunk_height;
    private float leanMagnitude;
    private float leanLikelihood;
    private float maxLean;
    private Vec3d leanStart;
    private double decayRate;
    private double branchingChance;
    private int branchesMean;
    private int branchAngle;

    /**
     * creates a Trunk with default values
     * todo make a yml with different default values
     */
    public Trunk() {
        setDefaultValues();
    }

    /**
     * gets the presets in a yml file
     *
     * @param treeType the type of tree we're making and the name of the file to open
     * @param plugin   the plugin so we can get the plugin folder
     */
    public Trunk(String treeType, JavaPlugin plugin) {
        File file = new File(String.format("%s%s%s%s%s%s", plugin.getDataFolder(), File.separator, "treePresets", File.separator, treeType, ".yml"));
        if (!file.exists()) {
            setDefaultValues();
            System.out.println(String.format("there is no tree that exists with name: %s", treeType));
            return;
        }

        YamlConfiguration configOrig = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection config = configOrig.getConfigurationSection("vars");

        assert config != null;
        trunk_width = config.getInt(YMLNavigate.TRUNK_WIDTH);
        trunk_height = config.getInt(YMLNavigate.TRUNK_HEIGHT);
        leanLikelihood = (float) config.getDouble(YMLNavigate.LEAN_LIKELIHOOD);
        maxLean = (float) config.getDouble(YMLNavigate.MAX_LEAN);
        double leanStartX = config.getDouble(YMLNavigate.LEAN_START_X);
        double leanStartY = config.getDouble(YMLNavigate.LEAN_START_Y);
        double leanStartZ = config.getDouble(YMLNavigate.LEAN_START_Z);
        leanStart = new Vec3d(leanStartX, leanStartY, leanStartZ);

        decayRate = config.getDouble(YMLNavigate.DECAY_RATE);
        branchingChance = config.getDouble(YMLNavigate.BRANCHING_CHANCE);
        branchesMean = config.getInt(YMLNavigate.BRANCHES_MEAN);
        branchAngle = config.getInt(YMLNavigate.BRANCH_ANGLE);
    }

    /**
     * sets default values for this tree
     */
    private void setDefaultValues() {
        trunk_width = 5;
        trunk_height = 100;
        leanMagnitude = (float) .1;
        leanLikelihood = 3;
        maxLean = 10;
        leanStart = new Vec3d(0, 3, 0);
        decayRate = .1;
        branchingChance = .4;
        branchesMean = 4;
        branchAngle = 20;
    }

    /**
     * creates a random trunk with the specified options
     */
    public TreeArray makeTrunk() {
        BaseTrunk baseTrunk = new BaseTrunk(trunk_width, trunk_height, leanMagnitude, leanLikelihood, maxLean, leanStart, decayRate, branchingChance, branchesMean, branchAngle);
        return Widthify.addWidth(baseTrunk.createBaseTrunk());

    }

    //todo do input validation
    public Trunk setTrunk_width(int trunk_width) {
        this.trunk_width = trunk_width;
        return this;
    }

    public Trunk setTrunk_height(int trunk_height) {
        this.trunk_height = trunk_height;
        return this;
    }

    public Trunk setLeanMagnitude(float leanMagnitude) {
        this.leanMagnitude = leanMagnitude;
        return this;
    }

    public Trunk setLeanLikelihood(float leanLikelihood) {
        this.leanLikelihood = leanLikelihood;
        return this;
    }

    public Trunk setMaxLean(float maxLean) {
        this.maxLean = maxLean;
        return this;
    }

    public Trunk setLeanStart(Vec3d leanStart) {
        this.leanStart = leanStart;
        return this;
    }

    public Trunk setDecayRate(double decayRate) {
        this.decayRate = decayRate;
        return this;
    }

    public Trunk setBranchingChance(double branchingChance) {
        this.branchingChance = branchingChance;
        return this;
    }

    public Trunk setBranchesMean(int branchesMean) {
        this.branchesMean = branchesMean;
        return this;
    }
}
