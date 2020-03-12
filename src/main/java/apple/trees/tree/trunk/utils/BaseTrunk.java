package apple.trees.tree.trunk.utils;

import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
import java.util.Random;

public class BaseTrunk {

    private static final double DECAY_RATE = .05;
    private static final double BELL_CURVE_A = .14;
    private static final double BELL_CURVE_B = .5;
    private static final double BELL_CURVE_LEFT = 1 / (BELL_CURVE_A * Math.sqrt(2 * Math.PI));
    private static Random random;
    private static final int maxCompletedSteps = 1000;

    public static void initialize() {
        random = new Random();
    }

    /**
     * creates a random base trunk with the specified options
     *
     * @param trunk_width    the desired average width of the trunk
     * @param trunk_height   the absolute height of the trunk
     * @param leanLikelihood (between 0 and 1) (very low) the chance a change in slope of slope will occur in any given step
     * @param leanMagnitude  (between 0 and 1) the angle magnitude of lean shift if one occurs in any given step
     * @param maxLean        (between 0 and 1) the maximum amount a tree is allowed to lean
     * @param leanStart      (between 0 and 1) the starting lean of the trunk
     * @return the tree that was slightly filled in
     */
    public static TreeArray createBaseTrunk(TreeArray tree, int trunk_width, int trunk_height, float leanMagnitude, float leanLikelihood, float maxLean, Vec3d leanStart) {
        ArrayList<TreeStep> lastTreeSteps = new ArrayList<>();
        lastTreeSteps.add(createBaseStart(tree, trunk_width, leanStart));

        TreeStep lastTreeStep = lastTreeSteps.remove(0);
        for (int currentCompletedSteps = 0; currentCompletedSteps < maxCompletedSteps && lastTreeStep.y < trunk_height && !lastTreeSteps.isEmpty(); currentCompletedSteps++) {
            TreeStep currentTreeStep = getCurrentTreeStep(tree, lastTreeStep, leanMagnitude, leanLikelihood);
            lastTreeSteps.add(currentTreeStep);
        }


        return tree;
    }

    /**
     * get the next tree step from what was given about the last tree step
     *
     * @param tree           the entire tree and all the steps within it
     * @param lastTreeStep   the tree step that was last created
     * @param leanMagnitude  how much lean will happen (determines the intensity of curves)
     * @param leanLikelihood how likely a new lean will start (determines the squigiliness of the lean
     * @return the new main step in the tree
     */
    private static TreeStep getCurrentTreeStep(TreeArray tree, TreeStep lastTreeStep, double leanMagnitude, double leanLikelihood) {
        Vec3d lastDirection = tree.getAvgDirection(lastTreeStep.x, lastTreeStep.z, lastTreeStep.y);
        Vec3d lastSlopeOfSlope = tree.getAvgSlopeOfSlope(lastTreeStep.x, lastTreeStep.y, lastTreeStep.z);
        double lastWidth = tree.getAvgWidth(lastTreeStep.x, lastTreeStep.y, lastTreeStep.z);


        // get the location of the newStep

        // get the immediate location
        double xFine = lastTreeStep.x + lastDirection.x;
        double yFine = lastTreeStep.y + lastDirection.y;
        double zFine = lastTreeStep.z + lastDirection.z;

        // keep going in that direction until you make it to a new square
        while ((int) xFine == lastTreeStep.x && (int) yFine == lastTreeStep.y && (int) zFine == lastTreeStep.z) {
            xFine += lastTreeStep.x + lastDirection.x;
            yFine += lastTreeStep.y + lastDirection.y;
            zFine += lastTreeStep.z + lastDirection.z;
        }
        int xBroad = (int) xFine;
        int yBroad = (int) yFine;
        int zBroad = (int) zFine;

        // make a list of the locations for the next full step
        ArrayList<Vec3d> locations = getNearbySquares(xBroad, yBroad, zBroad, xFine, yFine, zFine);

        // get the direction of the newStep
        double newDirectionX = lastDirection.x + lastSlopeOfSlope.x;
        double newDirectionY = lastDirection.y + lastSlopeOfSlope.y;
        double newDirectionZ = lastDirection.z + lastSlopeOfSlope.z;
        Vec3d newDirection = new Vec3d(newDirectionX, newDirectionY, newDirectionZ);

        // get slopeOfSlope of the newStep
        Vec3d newSlopeOfSlope = getRandomChangeSlopeOfSlope(lastSlopeOfSlope, leanMagnitude, leanLikelihood);

        // get the width of the newStep
        double newWidth = getRandomChangeWidth(lastWidth, DECAY_RATE);

        for (Vec3d loc : locations) {
            tree.put((int) loc.x, (int) loc.y, (int) loc.z, newDirection, newSlopeOfSlope, newWidth);
        }

        return new TreeStep(xBroad, yBroad, zBroad, newDirection, newSlopeOfSlope, newWidth);
    }

    /**
     * gets all the squares nearby to the location sizeof (0, 1, 2, or 3) returned
     *
     * @param xBroad the x location
     * @param yBroad the y location
     * @param zBroad the z location
     * @param xFine  the exact x location
     * @param yFine  the exact y location
     * @param zFine  the exact z location
     * @return all the x,y,z's that are close to the xFine,yFine,zFine
     */
    private static ArrayList<Vec3d> getNearbySquares(int xBroad, int yBroad, int zBroad, double xFine, double yFine, double zFine) {
        ArrayList<Vec3d> locations = new ArrayList<>(2);

        // see if it is close to a boundary
        double xDecimal = xFine - xBroad;
        double yDecimal = xFine - yBroad;
        double zDecimal = xFine - zBroad;
        if (xDecimal < .25) {
            locations.add(new Vec3d(xBroad - 1, yBroad, zBroad));
        } else if (xDecimal > .75) {
            locations.add(new Vec3d(xBroad + 1, yBroad, zBroad));
        }
        if (yDecimal < .25) {
            locations.add(new Vec3d(xBroad, yBroad - 1, zBroad));
        } else if (yDecimal > .75) {
            locations.add(new Vec3d(xBroad, yBroad + 1, zBroad));
        }
        if (zDecimal < .25) {
            locations.add(new Vec3d(xBroad, yBroad - 1, zBroad));
        } else if (zDecimal > .75) {
            locations.add(new Vec3d(xBroad, yBroad + 1, zBroad));
        }
        return locations;
    }

    /**
     * todo is this a good estimate?
     * good to have decay rate about 7.5 (bigger decay means bigger result
     *
     * @param lastWidth what the width used to be
     * @param decayRate what the decay rate is
     * @return a number according to a bell curve with the height of about decay rate
     */
    public static double getRandomChangeWidth(double lastWidth, double decayRate) {
        //todo finish formula vvv
        return lastWidth * decayRate / 2 * BELL_CURVE_LEFT * Math.pow(
                Math.E, -0.5 * (Math.pow(random.nextDouble() - BELL_CURVE_B, 2)
                        / Math.pow(BELL_CURVE_A, 2)));
    }


    public static Vec3d getRandomChangeSlopeOfSlope(Vec3d lastSlopeOfSlope, double leanMagnitude, double leanLikelihood) {
        // todo use leanLikelihood
        // get the magnitude of change of slope
        double newLeanMagnitude = random.nextDouble() * leanMagnitude;

        // get the slopeOfSlope of the newStep
        Vec3d newSlopeOfSlope = new Vec3d(lastSlopeOfSlope);
        double r1 = random.nextDouble();
        double r2 = random.nextDouble();
        double r3 = random.nextDouble();
        newSlopeOfSlope.x += (r1 / newLeanMagnitude * 3);
        newSlopeOfSlope.y += (r2 / newLeanMagnitude * 3);
        newSlopeOfSlope.z += (r3 / newLeanMagnitude * 3);

        return newSlopeOfSlope;
    }

    private static TreeStep createBaseStart(TreeArray tree, int trunk_width, Vec3d leanStart) {
        int centerX = tree.sizeX() / 2;
        int centerZ = tree.sizeZ() / 2;
        int centerY = tree.sizeY() / 2;


        int trunk_width_half = trunk_width / 2;
        for (int xi = -(trunk_width - 1) / 2; xi < trunk_width_half; xi++) {
            for (int zi = -(trunk_width - 1) / 2; zi < trunk_width_half; zi++) {
                tree.put(centerX, centerZ, centerY, leanStart, new Vec3d(0, 0, 0), trunk_width);
            }
        }
        return tree.get(centerX, centerZ, centerY);
    }

}
