package apple.trees.tree.trunk.creation.utils;

import com.sun.javafx.geom.Vec3d;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class GetRotations {
    private static Random random;

    public static void initialize(JavaPlugin pl, Random rand) {
        random = rand;
    }

    public static Collection<Vec3d> rotationFullFromDomain(double branchAngle, int branchesToBuild) {
        ArrayList<Vec3d> rotations = new ArrayList<>();
        rotations.add(new Vec3d(random.nextDouble() * 30, random.nextDouble() * 30, random.nextDouble() * 30));

        rotations.add(new Vec3d(random.nextDouble() * -30, random.nextDouble() * -30, random.nextDouble() * -30));

        return rotations;

    }
}
