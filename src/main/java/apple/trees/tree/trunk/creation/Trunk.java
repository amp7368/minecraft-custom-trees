package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.data.TreeArray;
import com.sun.javafx.geom.Vec3d;

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

    /**
     * creates a Trunk with default values
     */
    public Trunk() {
        trunk_width = 5;
        trunk_height = 100;
        leanMagnitude = (float) .1;
        leanLikelihood = 3;
        maxLean = 10;
        leanStart = new Vec3d(0, 3, 0);
        decayRate = .05;
        branchingChance = .08;
        branchesMean = 4;
    }

    /**
     * creates a random trunk with the specified options
     */
    public TreeArray makeTrunk() {
        BaseTrunk baseTrunk = new BaseTrunk(trunk_width, trunk_height, leanMagnitude, leanLikelihood, maxLean, leanStart, decayRate, branchingChance, branchesMean);
        return baseTrunk.createBaseTrunk();
    }

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
