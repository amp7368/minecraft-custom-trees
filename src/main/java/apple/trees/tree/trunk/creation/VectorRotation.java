package apple.trees.tree.trunk.creation;

import com.sun.javafx.geom.Matrix3f;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.Vec3f;

public class VectorRotation {

    public static Vec3d rotate(double xTheta, double yTheta, double zTheta, Vec3d vectorRaw) {
        // convert the Vec3d to a Vec3f to do dot products later
        Vec3f vector = new Vec3f((float) vectorRaw.x, (float) vectorRaw.y, (float) vectorRaw.z);

        // convert the thetas to radians
        xTheta = Math.toRadians(xTheta);
        yTheta = Math.toRadians(yTheta);
        zTheta = Math.toRadians(zTheta);

        Matrix3f matrix = new Matrix3f();

        //refresher m01 is row 0, col 1
        matrix.m00 = (float) (Math.cos(zTheta) * Math.cos(yTheta));
        matrix.m01 = (float) (Math.cos(zTheta) * Math.sin(yTheta) * Math.sin(xTheta) - Math.sin(zTheta) * Math.cos(xTheta));
        matrix.m02 = (float) (Math.cos(zTheta) * Math.sin(yTheta) * Math.cos(xTheta) + Math.sin(zTheta) * Math.sin(xTheta));
        matrix.m10 = (float) (Math.sin(zTheta) * Math.cos(yTheta));
        matrix.m11 = (float) (Math.sin(zTheta) * Math.sin(yTheta) * Math.sin(xTheta) + Math.cos(zTheta) * Math.cos(xTheta));
        matrix.m12 = (float) (Math.sin(zTheta) * Math.sin(yTheta) * Math.cos(xTheta) - Math.cos(zTheta) * Math.sin(xTheta));
        matrix.m20 = (float) (-Math.sin(yTheta));
        matrix.m21 = (float) (Math.cos(yTheta) * Math.sin(xTheta));
        matrix.m22 = (float) (Math.cos(yTheta) * Math.cos(xTheta));

        // dot product of row 1 with vector makes first entry
        Vec3f row1 = new Vec3f();
        matrix.getRow(0, row1);
        double firstEntry = row1.dot(vector);

        // dot product of row 2 with vector makes second entry
        Vec3f row2 = new Vec3f();
        matrix.getRow(1, row2);
        double secondEntry = row2.dot(vector);

        // dot product of row 1 with vector makes third entry
        Vec3f row3 = new Vec3f();
        matrix.getRow(0, row3);
        double thirdEntry = row3.dot(vector);

        // return the rotated vector
        return new Vec3d(firstEntry, secondEntry, thirdEntry);
    }
}
