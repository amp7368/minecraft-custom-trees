package apple.trees.tree.trunk.creation.utils;

import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;

public class TrailingSteps {

    /**
     * get the trailing squares that should be filled because they were skipped
     *
     * @param lastDirection        the direction the lastStep was heading
     * @param lastTreeStepLocation the location the lastStep was
     * @return a list of treeSteps that should be added with the same values of lastDirection and width
     */
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

            // if(lastDirection is going negative){
            //   see if we passed the component we're filling the inbetweens for
            // }else {
            //   see if we passed the component we're filling the inbetweens for
            // }
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

            // if this location has already been added, don't add it
            int broadX = (int) x;
            int broadY = (int) y;
            int broadZ = (int) z;
            boolean addMe = true;
            for (Vec3d location : locations) {
                if ((int) location.x == broadX && (int) location.y == broadY && (int) location.z == broadZ) {
                    addMe = false;
                    break;
                }
            }
            if (addMe) {
                locations.add(new Vec3d(x, y, z));
            }
        }
        return locations;
    }

}
