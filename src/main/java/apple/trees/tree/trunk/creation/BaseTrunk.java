package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.server.v1_15_R1.Quaternion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class BaseTrunk {

    private static final double DECAY_RATE = .05;
    private static final double BELL_CURVE_A = .14;
    private static final double BELL_CURVE_B = .5;
    private static final double BELL_CURVE_LEFT = 1 / (BELL_CURVE_A * Math.sqrt(2 * Math.PI));
    private static final double BRANCHING_CHANCE = .2;
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
        lastTreeSteps.add(createBaseStart(tree, new Vec3d(0, 1, 0), trunk_width, leanStart));

        TreeStep lastTreeStep;
        // loop until all the ends are finished
        treeStepLoop:
        for (int currentCompletedSteps = 0; currentCompletedSteps < maxCompletedSteps && !lastTreeSteps.isEmpty(); currentCompletedSteps++) {
            lastTreeStep = lastTreeSteps.remove(0);
            while (lastTreeStep == null) {
                if (lastTreeSteps.isEmpty())
                    break treeStepLoop;
                lastTreeStep = lastTreeSteps.remove(0);
            }
            if (lastTreeStep.y > trunk_height)
                break;
            TreeStep currentTreeStep;
            if (random.nextDouble() < BRANCHING_CHANCE) {
                System.out.println("Branched!");
                lastTreeSteps.addAll(getBranches(tree, lastTreeStep, 30, .5, 4));
            } else {
                currentTreeStep = getCurrentTreeStep(tree, lastTreeStep, leanMagnitude, leanLikelihood);
                lastTreeSteps.add(currentTreeStep);
            }
        }

        return tree;
    }

    /**
     * make a branch session instead of continuing the tree
     *
     * @param tree               the entire tree and all the steps within it
     * @param lastTreeStep       the tree step that was last created
     * @param branchAngle        (Degrees) the angle to branch off at
     * @param branchStealing     how much branches steal from the original
     * @param branchGroupingSize how many branches are typical in a group
     * @return all the last created steps for the branch session
     */
    private static Collection<TreeStep> getBranches(TreeArray tree, TreeStep lastTreeStep, double branchAngle, double branchStealing, int branchGroupingSize) {
        //todo use branch grouping size with normal distribution with min of 2
        int branchesToBuild = 2;

        ArrayList<TreeStep> branchSteps = new ArrayList<>();
        Vec3d lastDirection = lastTreeStep.direction;
        Vec3d lastSlopeOfSlope = lastTreeStep.slopeOfSlope;
        double lastWidth = lastTreeStep.width;

        // make the unit vector of lastDirection
        Vec3d unitLastDirection6 = new Vec3d();
        double magnitude = Math.sqrt(lastDirection.x * lastDirection.x + lastDirection.y * lastDirection.y + lastDirection.z * lastDirection.z);
        if (magnitude == 0)
            return branchSteps;
        unitLastDirection6.x = lastDirection.x / magnitude * 6;
        unitLastDirection6.y = lastDirection.y / magnitude * 6;
        unitLastDirection6.z = lastDirection.z / magnitude * 6;

        Vec3d newDirection;
        double x, y, z;

        newDirection = new Vec3d();
        newDirection.x = unitLastDirection6.x + .25;
        newDirection.y = unitLastDirection6.y;
        newDirection.z = unitLastDirection6.z + .25;

        x = lastTreeStep.x + newDirection.x;
        y = lastTreeStep.y + newDirection.y;
        z = lastTreeStep.z + newDirection.z;

        branchSteps.add(tree.put(x,y,z,newDirection,new Vec3d(0,0,0),lastWidth));

        newDirection = new Vec3d();
        newDirection.x = unitLastDirection6.x - .25;
        newDirection.y = unitLastDirection6.y;
        newDirection.z = unitLastDirection6.z - .25;

        x = lastTreeStep.x + newDirection.x;
        y = lastTreeStep.y + newDirection.y;
        z = lastTreeStep.z + newDirection.z;

        branchSteps.add(tree.put(x,y,z,newDirection,new Vec3d(0,0,0),lastWidth));

        return branchSteps;

    }

    /**
     * get the next tree step from what was given about the last tree step
     * add any steps to the tree
     *
     * @param tree           the entire tree and all the steps within it
     * @param lastTreeStep   the tree step that was last created
     * @param leanMagnitude  how much lean will happen (determines the intensity of curves)
     * @param leanLikelihood how likely a new lean will start (determines the squigiliness of the lean
     * @return the new main step in the tree
     */
    private static TreeStep getCurrentTreeStep(TreeArray tree, TreeStep lastTreeStep, double leanMagnitude, double leanLikelihood) {
        Vec3d lastDirection = lastTreeStep.direction;
        Vec3d lastSlopeOfSlope = lastTreeStep.slopeOfSlope;
        double lastWidth = lastTreeStep.width;

        
        // get the location of the newStep

        // get the immediate location
        double xFine = lastTreeStep.x + lastDirection.x;
        double yFine = lastTreeStep.y + lastDirection.y;
        double zFine = lastTreeStep.z + lastDirection.z;

        // keep going in that direction until you make it to a new square
        for (int i = 0; (int) xFine == lastTreeStep.x && (int) yFine == lastTreeStep.y && (int) zFine == lastTreeStep.z; i++) {
            if (i == 100)
                return null;
            xFine += lastTreeStep.x + lastDirection.x;
            yFine += lastTreeStep.y + lastDirection.y;
            zFine += lastTreeStep.z + lastDirection.z;
        }
        int xBroad = (int) xFine;
        int yBroad = (int) yFine;
        int zBroad = (int) zFine;

        // get the direction of the newStep
        double newDirectionX = lastDirection.x + lastSlopeOfSlope.x;
        double newDirectionY = lastDirection.y + lastSlopeOfSlope.y;
        double newDirectionZ = lastDirection.z + lastSlopeOfSlope.z;
        Vec3d newDirection = new Vec3d(newDirectionX, newDirectionY, newDirectionZ);

        // get slopeOfSlope of the newStep
        Vec3d newSlopeOfSlope = getRandomChangeSlopeOfSlope(lastSlopeOfSlope, leanMagnitude, leanLikelihood);

        // get the width of the newStep
        double newWidth = getRandomChangeWidth(lastWidth, DECAY_RATE);

        // make a list of the locations for the next full step
        ArrayList<Vec3d> locations = getTrailingSquares(lastDirection, new Vec3d(lastTreeStep.x, lastTreeStep.y, lastTreeStep.z));
        // put all the trailings in the tree
        for (Vec3d loc : locations) {
            tree.put(loc.x, loc.y, loc.z, newDirection, newSlopeOfSlope, newWidth);
        }
        tree.put(xFine, yFine, zFine, newDirection, newSlopeOfSlope, newWidth);

        return new TreeStep(xBroad, yBroad, zBroad, newDirection, newSlopeOfSlope, newWidth);
    }

    private static ArrayList<Vec3d> getTrailingSquares(Vec3d lastDirection, Vec3d lastTreeStepLocation) {
        ArrayList<Vec3d> locations = new ArrayList<>();

        // make the unit vector of lastDirection
        Vec3d unitLastDirection = new Vec3d();
        double magnitude = Math.sqrt(lastDirection.x * lastDirection.x + lastDirection.y * lastDirection.y + lastDirection.z * lastDirection.z);
        if (magnitude == 0)
            return locations;
        unitLastDirection.x = lastDirection.x / magnitude;
        unitLastDirection.y = lastDirection.y / magnitude;
        unitLastDirection.z = lastDirection.z / magnitude;
        double maxX = lastDirection.x + lastTreeStepLocation.x;
        double maxY = lastDirection.y + lastTreeStepLocation.y;
        double maxZ = lastDirection.z + lastTreeStepLocation.z;

        // keep going in that direction until all the inbetweens are filled
        for (double x = lastTreeStepLocation.x, y = lastTreeStepLocation.y, z = lastTreeStepLocation.z; true;
             x += unitLastDirection.x, y += unitLastDirection.y, z += unitLastDirection.z) {
            if (unitLastDirection.x < 0) {
                if (x < maxX)
                    break;
            } else if (x > maxX)
                break;
            if (unitLastDirection.y < 0) {
                if (y < maxY)
                    break;
            } else if (y > maxY)
                break;
            if (unitLastDirection.z < 0) {
                if (z < maxZ)
                    break;
            } else if (z > maxZ)
                break;

            //todo optimize
            boolean addMe = true;
            for (Vec3d location : locations) {
                if ((int) location.x == (int) x && (int) location.y == (int) y && (int) location.z == (int) z) {
                    addMe = false;
                    break;
                }
            }
            if (addMe) {
                System.out.println("x:" + (int) x + " ,y:" + (int) y + " ,z:" + (int) z + " added ");
                locations.add(new Vec3d(x, y, z));
            }
        }
        return locations;
    }


    /**
     * todo is this a good estimate?
     * good to have decay rate about 1/7.5 (bigger decay means bigger result
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
        double sum = r1 + r2 + r3;

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


    private static TreeStep createBaseStart(TreeArray tree, Vec3d direction, int trunk_width, Vec3d leanStart) {
        int centerX = tree.sizeX() / 2;
        int centerY = 0;
        int centerZ = tree.sizeZ() / 2;


        int trunk_width_half = trunk_width / 2;
        for (int xi = -trunk_width_half; xi < trunk_width_half; xi++) {
            for (int zi = -trunk_width_half; zi < trunk_width_half; zi++) {
                if (xi == 0 && zi == 0) {
                    int a = 3;
                }
                tree.put(centerX + xi + 0.5, centerY + 0.5, centerZ + zi + 0.5, leanStart, direction, trunk_width);
            }
        }
        return tree.get(centerX, centerY, centerZ);
    }

}
