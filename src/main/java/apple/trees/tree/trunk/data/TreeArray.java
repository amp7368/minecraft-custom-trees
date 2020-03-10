package apple.trees.tree.trunk.data;

public class TreeArray {
    private TreeStep[][][] tree;

    public TreeArray(int xSize, int zSize, int ySize) {
        tree = new TreeStep[xSize][zSize][ySize];
    }
}
