import java.util.ArrayList;

public abstract class GameLoader {
    int width;
    int height;
    ArrayList<ArrayList<Boolean>> data;
    public GameLoader (int width, int height, ArrayList<ArrayList<Boolean>> data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public void load () {
        for (int row = 0; row < height; row++) {
            data.add(new ArrayList<Boolean> ());
            for (int col = 0; col < width; col++) {
                boolean unit = unitLoad(col, row);
                data.get(row).add(unit);
            }
        }
    }

    public boolean unitLoad (int width, int height) {
        return false;
    }
}
