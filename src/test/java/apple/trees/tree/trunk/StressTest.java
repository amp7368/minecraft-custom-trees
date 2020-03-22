package apple.trees.tree.trunk;

import apple.trees.tree.refine.ResolutionDecreaser;
import apple.trees.tree.trunk.creation.BaseTrunk;
import apple.trees.tree.trunk.creation.BranchStep;
import apple.trees.tree.trunk.creation.Trunk;
import apple.trees.tree.trunk.data.VectorRotationTest;
import apple.trees.tree.trunk.utils.RotationTest;
import apple.trees.tree.trunk.utils.WidthifyTest;
import org.bukkit.Bukkit;

import java.util.Random;

public class StressTest {

    public static void main(String[] args) {
//        WidthifyTest.widthifyTest();
//        RotationTest.rotationTest();
//        BranchStep  bs = new BranchStep();
        (new Trunk()).makeTrunk();
        ResolutionDecreaser.pixelify(new Trunk().makeTrunk(),1,.9);


    }
}
