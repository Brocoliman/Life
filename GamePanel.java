import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Variables
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // Game Objects
    Timer timer;
    JSlider simSpeedSlider;

    // Final variables
    static final int WIDTH = 900; // pixel width of window
    static final int HEIGHT = 1000; // pixel height of window
    static final int DELAY = 25; // overall frame rate
    static final boolean PAUSE_WHEN_EDIT = false; // whether to pause when user edits

    // Settable variables (display related)
    int playRate = 4; // simulation frame rate (not overall frame rate)
    int blockSize = 20; // pixel size of blocks (can be zoomed in or out)
    int gapSize = 1; // gap for editing purpose
    int worldCenterX = 0; // where (0, 0) is on the pixel display
    int worldCenterY = 100;
    int simFrame = 0;

    // Settable variables (discrete-game related)
    boolean paused = false; // pause simulation for edits
    int worldWidth = 45; // width in number of blocks (how far the simulation goes)
    int worldHeight = 45; // height in number of blocks (how far the simulation goes)
    int simGeneration = 0;

    // GUI Colors
    Color bkgColor = Color.white;
    Color guiColor = Color.black;
    Color textColor = Color.black;
    Color blockColor = Color.black;

    // Game data
    ArrayList<ArrayList<Boolean>> data = new ArrayList<>();

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Construction
    ///////////////////////////////////////////////////////////////////////////////////////////////

    GamePanel() {
        // Window settings
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); // minimum size for the component to display correctly
        this.setBackground(bkgColor);
        this.setFocusable(true);

        // Add KUI
        this.addKeyListener(new GameKeyAdapter());

        // Declare Timer
        timer = new Timer(DELAY, this);

        // Add Sim Speed Slider
        simSpeedSlider = new JSlider(1, 10, 10);
        simSpeedSlider.setPreferredSize(new Dimension(200, 75));
        simSpeedSlider.setPaintTicks(true);
        simSpeedSlider.setMinorTickSpacing(20);
        simSpeedSlider.setPaintTrack(true);
        simSpeedSlider.setMajorTickSpacing(25);
        simSpeedSlider.setPaintLabels(true);
        //simSpeedSlider.setUI(new GameSliderUI(simSpeedSlider, Color.GREEN));
        simSpeedSlider.addChangeListener(e -> playRate = 40 / simSpeedSlider.getValue());
        this.add(simSpeedSlider);
        System.out.println(simSpeedSlider.getValue());

        loadGame();
        startGame();
    }

    public void loadGame() {
        GameLoader loader = new GameLoaderRandom(worldWidth, worldHeight, data);
        loader.load();
    }

    public void startGame() {
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Utility Function
    ///////////////////////////////////////////////////////////////////////////////////////////////

    // Print out data
    public void printData() {
        for (ArrayList<Boolean> row: data) {
            for (boolean item: row) {
                System.out.print(item?1:0);
            }
            System.out.println();
        }
    }

    // Increment arraylist value
    public void increment (ArrayList<ArrayList<Integer>> list, int col, int row) {
        ArrayList<Integer> inner = list.get(row);
        inner.set(col, inner.get(col)+1);
        list.set(row, inner);
    }

    // Display all the blocks
    public void redraw(Graphics g, boolean gap) {
        int drawSize = blockSize - (gap ? gapSize:0);
        g.setColor(blockColor);
        for (int row = 0; row < worldHeight; row++) {
            for (int col = 0; col < worldWidth; col++) {
                if (data.get(row).get(col)){
                    g.fillRect(worldCenterX + col * blockSize, worldCenterY + row * blockSize, drawSize, drawSize); // horizon
                }
            }
        }
    }

    // Simulate one step based on rules
    public void calculateStep() {
        // Optimal construction: store number of neighbors on a grid
        ArrayList<ArrayList<Integer>> neighborGrid = new ArrayList<> ();
        for (int row = -1; row < worldHeight+1; row++) {
            neighborGrid.add(new ArrayList<>());
            for (int col = -1; col < worldWidth+1; col++) {
                neighborGrid.get(row+1).add(0);
            }
        }
        // Populate neighbor grid
        for (int row = 0; row < worldHeight; row++) {
            for (int col = 0; col < worldWidth; col++) {
                if (data.get(row).get(col)) { // if exists, fill neighbors
                    int col_ = col+1;
                    int row_ = row+1;
                    // Diagonal
                    increment(neighborGrid, col_+1, row_+1);
                    increment(neighborGrid, col_+1, row_-1);
                    increment(neighborGrid, col_-1, row_+1);
                    increment(neighborGrid, col_-1, row_-1);
                    // Adjacent
                    increment(neighborGrid, col_+1, row_);
                    increment(neighborGrid, col_-1, row_);
                    increment(neighborGrid, col_, row_+1);
                    increment(neighborGrid, col_, row_-1);
                }
            }
        }
        // Calculate new cells based on # neighbors
        for (int row = 0; row < worldHeight; row++) {
            for (int col = 0; col < worldWidth; col++) {
                int neighbors = neighborGrid.get(row+1).get(col+1);
                if (data.get(row).get(col)) { // if currently alive
                    if (neighbors != 2 && neighbors != 3) {
                        data.get(row).set(col, false);
                    }
                } else { // if currently dead
                    if (neighbors == 3) {
                        data.get(row).set(col, true);
                    }
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // Draw Function (main body)
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public void draw(Graphics g) {
        // Create block display
        redraw(g, paused);

        // Information board
        g.setColor(textColor);
        g.drawString("Generation: " + simGeneration, 50, 50);

        // Simulate if not paused
        if (!paused) {
            if (simFrame % playRate == 0) {
                calculateStep(); // next step per generation
                simGeneration ++;
                System.out.println(".............");
            }
            simFrame ++;
            System.out.println("..");
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // UI Interaction
    ///////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

}
