import java.util.ArrayList;
import java.util.Random;

public class GameLoaderRandom extends GameLoader {
    Random random;
    public GameLoaderRandom(int width, int height, ArrayList<ArrayList<Boolean>> data) {
        super(width, height, data);
        initializeRandom();
    }

    private void initializeRandom() {
        random = new Random();
    }

    public boolean unitLoad (int width, int height) {
        return random.nextBoolean();
    }
}
