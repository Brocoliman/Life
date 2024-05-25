import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class GameSliderUI extends BasicSliderUI {

    private Color trackColor;

    public GameSliderUI(JSlider slider, Color trackColor) {
        super(slider);
        this.trackColor = trackColor;
        slider.setOpaque(false); // Make the slider background transparent
    }

    @Override
    public void paintTrack(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Rectangle trackBounds = trackRect;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(trackColor);

        // Draw a rounded rectangle for the track
        int trackHeight = 8; // Customize the track height
        int trackY = (trackBounds.height - trackHeight) / 2;
        g2d.fillRoundRect(trackBounds.x, trackBounds.y + trackY, trackBounds.width, trackHeight, trackHeight, trackHeight);
    }

    @Override
    public void paintThumb(Graphics g) {
        // Customize the thumb appearance if needed
        super.paintThumb(g);
    }
}