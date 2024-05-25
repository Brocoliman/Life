import java.util.ArrayList;

public class GameLoaderBySave extends GameLoader {
    String filename;
    public GameLoaderBySave(int width, int height, ArrayList<ArrayList<Boolean>> data, String filename) {
        super(width, height, data);
        this.filename = filename;
    }
}
