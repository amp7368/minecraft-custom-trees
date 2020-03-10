package apple.trees.tree.trunk.data;

public class TreeStep {
    public int x;
    public int y;
    public int z;
    public double xDirection;
    public double yDirection;
    public double zDirection;
    public double xSlopeOfSlope;
    public double ySlopeOfSlope;
    public double zSlopeOfSlope;

    /**
     * simple contructor for simple data storage
     *
     * @param x             the current x index
     * @param z             the current z index
     * @param y             the current y index
     * @param xDirection    the current slope in x
     * @param zDirection    the current slope in z
     * @param yDirection    the current slope in y
     * @param xSlopeOfSlope the current acceleration in x
     * @param zSlopeOfSlope the current acceleration in y
     * @param ySlopeOfSlope the current acceleration in z
     */
    public TreeStep(int x, int z, int y, double xDirection, double zDirection, double yDirection, double xSlopeOfSlope, double zSlopeOfSlope, double ySlopeOfSlope) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xDirection = xDirection;
        this.yDirection = yDirection;
        this.zDirection = zDirection;
        this.xSlopeOfSlope = xSlopeOfSlope;
        this.ySlopeOfSlope = ySlopeOfSlope;
        this.zSlopeOfSlope = zSlopeOfSlope;
    }
}
