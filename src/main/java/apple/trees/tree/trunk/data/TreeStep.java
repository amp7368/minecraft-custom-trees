package apple.trees.tree.trunk.data;

import com.sun.javafx.geom.Vec3d;

public class TreeStep {
    public int x;
    public int y;
    public int z;
    public Vec3d direction;
    public Vec3d slopeOfSlope;
    public double width;

    /**
     * simple contructor for simple data storage
     *
     * @param x            the current x index
     * @param z            the current z index
     * @param y            the current y index
     * @param direction    the current slope
     * @param slopeOfSlope the current acceleration
     */
    public TreeStep(int x, int z, int y, Vec3d direction, Vec3d slopeOfSlope, double width) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.direction = direction;
        this.slopeOfSlope = slopeOfSlope;
        this.width = width;
    }
}
