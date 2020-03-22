package apple.trees.tree.trunk.creation;

import apple.trees.YMLNavigate;
import apple.trees.tree.trunk.creation.utils.GetRotations;
import apple.trees.tree.trunk.creation.utils.RandomChange;
import apple.trees.tree.trunk.creation.utils.Widthify;
import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Trunk {

    public static double MIN_STEP_WIDTH_SIZE;
    private int trunk_width;
    private int trunk_height; // maybe remove
    private float leanMagnitude;
    private float leanLikelihood; // remove
    private float maxLean; // remove
    private Vec3d leanStart;
    private int branchesMean;
    private int branchAngle;
    private double branchMaxWidth;
    private double branchWidthModifier;
    private ArrayList<Vec3d> firstAngles;
    private RandomChange randomChange;
    private Random random = new Random();
    private double branchingChance;
    private double branchingChanceMean;
    private double branchingChanceStandardDeviation;
    private double widthDecayRate;
    private double widthDecayMean;
    private double widthDecayStandardDeviation;
    private double leanCoefficent; // remove
    private double leanExponent; // remove
    private double ratio; // make this not a field but a local variable

    /**
     * creates a Trunk with default values
     */
    public Trunk() {
        setDefaultValues();
        GetRotations.initialize(random);
    }

    /**
     * gets the presets in a yml file
     *
     * @param treeType the type of tree we're making and the name of the file to open
     * @param plugin   the plugin so we can get the plugin folder
     */
    public Trunk(String treeType, JavaPlugin plugin) {
        GetRotations.initialize(random);
        File file = new File(String.format("%s%s%s%s%s%s", plugin.getDataFolder(), File.separator, "treePresets", File.separator, treeType, ".yml"));
        if (!file.exists()) {
            setDefaultValues();
            System.out.println(String.format("there is no tree that exists with name: %s", treeType));
            return;
        }

        YamlConfiguration configOrig = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection config = configOrig.getConfigurationSection("vars");

        assert config != null;
        MIN_STEP_WIDTH_SIZE = config.getDouble(YMLNavigate.MIN_STEP_WIDTH);
        trunk_width = config.getInt(YMLNavigate.TRUNK_WIDTH);
        trunk_height = config.getInt(YMLNavigate.TRUNK_HEIGHT);
        leanLikelihood = (float) config.getDouble(YMLNavigate.LEAN_LIKELIHOOD);
        leanMagnitude = (float) config.getDouble(YMLNavigate.LEAN_MAGNITUDE);
        maxLean = (float) config.getDouble(YMLNavigate.MAX_LEAN);
        double leanStartX = config.getDouble(YMLNavigate.LEAN_START_X);
        double leanStartY = config.getDouble(YMLNavigate.LEAN_START_Y);
        double leanStartZ = config.getDouble(YMLNavigate.LEAN_START_Z);

        branchesMean = config.getInt(YMLNavigate.BRANCHES_MEAN);
        branchAngle = config.getInt(YMLNavigate.BRANCH_ANGLE);
        branchMaxWidth = config.getDouble(YMLNavigate.BRANCH_MAX_WIDTH);
        branchWidthModifier = config.getDouble(YMLNavigate.BRANCH_WIDTH_MODIFIER);

        ratio = config.getInt(YMLNavigate.RATIO);
        config = configOrig.getConfigurationSection(YMLNavigate.FORMULAS);
        assert config != null;
        branchingChance = config.getDouble(YMLNavigate.BRANCHING_CHANCE);
        branchingChanceMean = config.getDouble(YMLNavigate.BRANCHING_CHANCE_MEAN);
        branchingChanceStandardDeviation = config.getDouble(YMLNavigate.BRANCHING_CHANCE_SD);
        widthDecayRate = config.getDouble(YMLNavigate.DECAY_RATE);
        widthDecayMean = config.getDouble(YMLNavigate.WIDTH_DECAY_MEAN);
        widthDecayStandardDeviation = config.getDouble(YMLNavigate.WIDTH_DECAY_SD);
        leanCoefficent = config.getDouble(YMLNavigate.LEAN_COEFFICENT);
        leanExponent = config.getDouble(YMLNavigate.LEAN_EXPONENT);
        randomChange = new RandomChange(random, leanCoefficent, leanExponent, widthDecayStandardDeviation, widthDecayMean);


        ArrayList<Integer> angleXs = new ArrayList<>();
        ArrayList<Integer> angleYs = new ArrayList<>();
        ArrayList<Integer> angleZs = new ArrayList<>();
        ConfigurationSection configAngles = configOrig.getConfigurationSection(YMLNavigate.FIRST_ANGLES);
        assert configAngles != null;
        for (int i = 1; ; i++) {
            config = configAngles.getConfigurationSection(YMLNavigate.BRANCH + i);
            if (config == null) {
                break;
            }
            angleXs.add(config.getInt(YMLNavigate.ANGLE_X));
            angleYs.add(config.getInt(YMLNavigate.ANGLE_Y));
            angleZs.add(config.getInt(YMLNavigate.ANGLE_Z));
        }

        firstAngles = new ArrayList<>(angleXs.size());
        int size = angleXs.size();
        for (int i = 0; i < size; i++)
            firstAngles.add(new Vec3d(angleXs.get(i), angleYs.get(i), angleZs.get(i)));

        ConfigurationSection configTesting = configOrig.getConfigurationSection("testing");
        widthDecayRate /= configTesting.getDouble("widthDecayRateDiv");
        double r =  configTesting.getDouble("leanStartDiv");
        leanStart = new Vec3d(leanStartX / r, leanStartY / r, leanStartZ / r);
        leanMagnitude*=configTesting.getDouble("leanMagnitudeMul");
        branchWidthModifier /= configTesting.getDouble("branchWidthModifierDiv");
        MIN_STEP_WIDTH_SIZE /= configTesting.getDouble("minStepWidthDiv");
    }

    /**
     * sets default values for this tree
     */
    private void setDefaultValues() {
        trunk_width = 4;
        trunk_height = 100;
        leanMagnitude = (float) .15;
        leanLikelihood = 3;
        maxLean = 10;
        leanStart = new Vec3d(0, 3, 0);
        widthDecayRate = .4;
        branchingChance = .5;
        branchesMean = 3;
        branchAngle = 25;
        branchMaxWidth = 4;
        branchingChanceMean = 0.5;
        branchingChanceStandardDeviation = 0.5;
        branchWidthModifier = .9;
        widthDecayMean = 0.35;
        widthDecayStandardDeviation = 0.3;
        leanCoefficent = 2;
        leanExponent = 2;
        randomChange = new RandomChange(random, leanCoefficent, leanExponent, widthDecayStandardDeviation, widthDecayMean);
        ratio = 1;
        MIN_STEP_WIDTH_SIZE = 0.25;
    }

    /**
     * creates a random trunk with the specified options
     */
    public TreeArray makeTrunk() {
        BaseTrunk baseTrunk = new BaseTrunk(trunk_width, trunk_height, leanMagnitude, leanLikelihood, maxLean, leanStart,
                widthDecayRate, branchingChance, branchesMean, branchAngle, branchMaxWidth, branchingChanceStandardDeviation,
                branchingChanceMean, branchWidthModifier, firstAngles, leanCoefficent, leanExponent, randomChange, random);
        System.out.println("Made a base trunk");
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

    public Trunk setWidthDecayRate(double widthDecayRate) {
        this.widthDecayRate = widthDecayRate;
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
