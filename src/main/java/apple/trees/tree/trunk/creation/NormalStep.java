package apple.trees.tree.trunk.creation;

import apple.trees.tree.trunk.creation.utils.RandomChange;
import apple.trees.tree.trunk.creation.utils.TrailingSteps;
import apple.trees.tree.trunk.data.TreeArray;
import apple.trees.tree.trunk.data.TreeStep;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

public class NormalStep {
    private double leanMagnitude;
    private double leanLikelihood;
    private double decayRate;

    protected NormalStep(double leanMagnitude, double leanLikelihood, double decayRate) {
        this.leanMagnitude = leanMagnitude;
        this.leanLikelihood = leanLikelihood;
        this.decayRate = decayRate;
    }

    /**
     * get the next tree step from what was given about the last tree step
     * add any steps to the tree
     *
     * @param tree         the entire tree and all the steps within it
     * @param lastTreeStep the tree step that was last created
     * @return the new main step in the tree
     */
    protected TreeStep getCurrentTreeStep(TreeArray tree, TreeStep lastTreeStep) {
        Vec3d lastDirection = lastTreeStep.direction;
        Vec3d lastSlopeOfSlope = lastTreeStep.slopeOfSlope;
        double lastWidth = lastTreeStep.width;

        // get the width of the newStep
        double newWidth = lastWidth - RandomChange.getRandomChangeWidth(lastWidth, decayRate);
        // todo magic value
        if (newWidth < Trunk.MIN_STEP_SIZE)
            return null;

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
        Vec3d newSlopeOfSlope = RandomChange.getRandomChangeSlopeOfSlope(lastSlopeOfSlope, leanMagnitude, leanLikelihood);


        // make a list of the locations for the next full step
        ArrayList<Vec3d> locations = TrailingSteps.getTrailingSquares(lastDirection, new Vec3d(lastTreeStep.x, lastTreeStep.y, lastTreeStep.z));
        // put all the trailings in the tree
        for (Vec3d loc : locations) {
            tree.put(loc.x, loc.y, loc.z, newDirection, newSlopeOfSlope, newWidth);
        }
        tree.put(xFine, yFine, zFine, newDirection, newSlopeOfSlope, newWidth);

        return tree.get(xBroad, yBroad, zBroad);
    }
}
