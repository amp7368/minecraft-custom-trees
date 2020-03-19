package apple.trees.tree.trunk.creation.utils;

import apple.trees.YMLNavigate;
import com.sun.javafx.geom.Vec3d;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Random;

public class RandomChange {

    // this is the normal curve that suits us best
    private static double bellCurveA = .3;
    private static double bellCurveB = .5;
    private static double BELL_CURVE_LEFT;
    private static Random random;

    public RandomChange(Random rand, double standardDeviation, double mean) {
        random = rand;
        bellCurveA = standardDeviation;
        bellCurveB = mean;
        BELL_CURVE_LEFT = 1 / (bellCurveA * Math.sqrt(2 * Math.PI));
    }

    /**
     * todo is this a good estimate?
     * good to have decay rate about 1/7.5 (bigger decay means bigger result
     *
     * @param lastWidth what the width used to be
     * @param decayRate what the decay rate is
     * @return a number according to a bell curve with the height of about decay rate
     */
    public double getRandomChangeWidth(double lastWidth, double decayRate) {
        return lastWidth * decayRate * BELL_CURVE_LEFT * Math.pow(
                Math.E, -0.5 * (Math.pow(random.nextDouble() - bellCurveB, 2)
                        / Math.pow(bellCurveA, 2)));
    }

    public Vec3d getRandomChangeSlopeOfSlope(Vec3d lastSlopeOfSlope, double leanMagnitude, double leanLikelihood) {
        // todo use leanLikelihood
        // get the magnitude of change of slope
        double newLeanMagnitude = random.nextDouble() * leanMagnitude;

        // get the slopeOfSlope of the newStep
        Vec3d newSlopeOfSlope = new Vec3d(lastSlopeOfSlope);
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        double r3 = random.nextDouble();
        double sum = r1 + r2 + r3;

        // 3 because there are 3 r's
        double addX = (r1 / sum * 3 * newLeanMagnitude);
        double addY = (r2 / sum * 3 * newLeanMagnitude);
        double addZ = (r3 / sum * 3 * newLeanMagnitude);

        //negitify some of the numbers
        if (random.nextDouble() < .5)
            addX = -addX;
        if (random.nextDouble() < .5)
            addY = -addY;
        if (random.nextDouble() < .5)
            addZ = -addZ;

        // add the changes to the old vector
        newSlopeOfSlope.x += addX;
        newSlopeOfSlope.y += addY;
        newSlopeOfSlope.z += addZ;


        return newSlopeOfSlope;
    }

}
