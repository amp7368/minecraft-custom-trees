package apple.trees.tree.trunk.creation.utils;

import com.sun.javafx.geom.Vec3d;

import java.util.Random;

public class RandomChange {

    // this is the normal curve that suits us best
    private double bellCurveA = .3;
    private double bellCurveB = .5;
    private double BELL_CURVE_LEFT;
    private double leanCoefficent;
    private double leanExponent;
    private Random random;

    public RandomChange(Random rand, double leanCoefficent, double leanExponent, double standardDeviation, double mean) {
        random = rand;
        bellCurveA = standardDeviation;
        bellCurveB = mean;
        BELL_CURVE_LEFT = 1 / (bellCurveA * Math.sqrt(2 * Math.PI));
        this.leanCoefficent = leanCoefficent;
        this.leanExponent = leanExponent;
    }

    /**
     * gets the random change in width (not the new width
     *
     * @param lastWidth what the width used to be
     * @param decayRate what the decay rate is
     * @return a number according to a bell curve with the height of about decay rate
     */
    public double getRandomChangeWidth(double lastWidth, double decayRate) {
        return decayRate * BELL_CURVE_LEFT * Math.pow(
                Math.E, -0.5 * (Math.pow(lastWidth - bellCurveB, 2)
                        / Math.pow(bellCurveA, 2)));
    }

    public Vec3d getRandomNextSlopeOfSlope(Vec3d lastSlopeOfSlope, double leanMagnitude, double leanLikelihood,
                                           Vec3d lastDirection, Vec3d firstDirection, double width) {
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

//        // modify slope based on lastDirection
//        double angle = angleBetween(lastDirection, firstDirection);
//        double shouldBeAngle = leanCoefficent * Math.pow(width, leanExponent);
//        if (angle < shouldBeAngle) {
//            // x and z need to be away from trunk
//            if (lastDirection.x > 0) {
//                // direction is going in the positive
//                // x needs to increase
//                if (addX < 0) {
//                    addX = -addX;
//                }
//            } else {
//                // direction is going in the negative
//                // x needs to decrease
//                if (addX > 0) {
//                    addX = -addX;
//                }
//            }
//            if (lastDirection.z > 0) {
//                // direction is going in the positive
//                // z needs to increase
//                if (addZ < 0) {
//                    addZ = -addZ;
//                }
//            } else {
//                // direction is going in the negative
//                // z needs to decrease
//                if (addZ > 0) {
//                    addZ = -addZ;
//                }
//            }
//        } else {
//            // x and z need to be towards trunk
//            if (lastDirection.x > 0) {
//                // direction is going in the positive
//                // x needs to decrease
//                if (addX > 0) {
//                    addX = -addX;
//                }
//            } else {
//                // direction is going in the negative
//                // x needs to increase
//                if (addX < 0) {
//                    addX = -addX;
//                }
//            }
//            if (lastDirection.z > 0) {
//                // direction is going in the positive
//                // z needs to decrease
//                if (addZ > 0) {
//                    addZ = -addZ;
//                }
//            } else {
//                // direction is going in the negative
//                // z needs to increase
//                if (addZ < 0) {
//                    addZ = -addZ;
//                }
//            }
//        }
        // add the changes to the old vector
        newSlopeOfSlope.x += addX;
        newSlopeOfSlope.y += addY;
        newSlopeOfSlope.z += addZ;


        return newSlopeOfSlope;
    }

    private double angleBetween(Vec3d lastDirection, Vec3d firstDirection) {
        return Math.acos(Math.toRadians(dot(lastDirection, firstDirection) /
                magnitude(lastDirection) / magnitude(firstDirection))) / Math.PI;
    }

    private double dot(Vec3d vec1, Vec3d vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y + vec1.z * vec2.z;
    }

    private double magnitude(Vec3d vec1) {
        return Math.sqrt(vec1.x * vec1.x + vec1.y * vec1.y + vec1.z * vec1.z);
    }

}
