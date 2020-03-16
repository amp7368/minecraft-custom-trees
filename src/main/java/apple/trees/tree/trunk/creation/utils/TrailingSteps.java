package apple.trees.tree.trunk.creation.utils;

import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

public class TrailingSteps {
    public static ArrayList<Vec3d> getTrailingSquares(Vec3d lastDirection, Vec3d lastTreeStepLocation) {
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

}
