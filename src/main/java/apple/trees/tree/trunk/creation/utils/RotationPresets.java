package apple.trees.tree.trunk.creation.utils;

import com.sun.javafx.geom.Vec3d;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RotationPresets {
    private static Random random;

    public static void initialize(JavaPlugin pl, Random rand) {
        random = rand;
    }

    public static void initialize(Random rand) {
        random = rand;
    }

    protected static ArrayList<Vec3d> branches2FromDomain() {
        ArrayList<Vec3d> polars = new ArrayList<>();
        double xr = random.nextDouble();
        double yr = random.nextDouble();
        double zr = random.nextDouble();
        double x1, x2, y1, y2, z1, z2;
        if (xr < .5) {
            x1 = -360;
            x2 = 360;
        } else {
            x1 = 360;
            x2 = -360;
        }
        if (yr < .5) {
            y1 = -360;
            y2 = 360;
        } else {
            y1 = 360;
            y2 = -360;
        }
        if (zr < .5) {
            z1 = -360;
            z2 = 360;
        } else {
            z1 = 360;
            z2 = -360;
        }
        polars.add(new Vec3d(x1, y1, z1));
        polars.add(new Vec3d(x2, y2, z2));
        return polars;
    }

    protected static ArrayList<Vec3d> branches3FromDomain() {
        ArrayList<Vec3d> polars = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            polars.add(new Vec3d());
        }

        //fill arr with the different vals that the components could be
        ArrayList<Double> arr = new ArrayList<>();
        arr.add((double) -360);
        arr.add((double) 0);
        arr.add((double) 360);

        // for each component, add the correct component to each branch
        for (int i = 0; i < 3; i++) {
            Collections.shuffle(arr);
            polars.get(i).x = arr.get(i);
        }
        for (int i = 0; i < 3; i++) {
            Collections.shuffle(arr);
            polars.get(i).y = arr.get(i);
        }
        for (int i = 0; i < 3; i++) {
            Collections.shuffle(arr);
            polars.get(i).z = arr.get(i);
        }

        return polars;
    }

    protected static ArrayList<Vec3d> branches4FromDomain() {
        ArrayList<Vec3d> polars = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            polars.add(new Vec3d());
        }

        //fill arr with the different vals that the components could be
        ArrayList<Double> arr = new ArrayList<>();
        arr.add((double) -360);
        arr.add((double) -180);
        arr.add((double) 180);
        arr.add((double) 360);

        int size = arr.size();

        // for each component, add the correct component to each branch
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).x = arr.get(i);
        }
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).y = arr.get(i);
        }
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).z = arr.get(i);
        }

        return polars;
    }

    protected static ArrayList<Vec3d> branchesFromDomain(int numOfBranches) {
        ArrayList<Vec3d> polars = new ArrayList<>();
        for (int i = 0; i < numOfBranches; i++) {
            polars.add(new Vec3d());
        }

        //fill arr with the different vals that the components could be
        ArrayList<Double> arr = new ArrayList<>();
        double increment = 720.0 / (numOfBranches - 1);
        for (double i = -360; i <= 360; i += increment) {
            arr.add(i);
        }
        int size = polars.size();
        if (size != arr.size()) {
            System.err.println("somefin went wrong in making the increments");
            for (Double i : arr) {
                System.err.print(i + " ");
            }
            System.err.println();
        }
        // for each component, add the correct component to each branch
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).x = arr.get(i);
        }
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).y = arr.get(i);
        }
        for (int i = 0; i < size; i++) {
            Collections.shuffle(arr);
            polars.get(i).z = arr.get(i);
        }

        return polars;
    }
}
