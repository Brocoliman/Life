import javax.swing.*;

public class GameFrame extends JFrame {
    public static GamePanel game;
    GameFrame(){
        game = new GamePanel();
        this.add(game);

        this.setTitle("Life");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); // fit JFrame around every component
        this.setVisible(true);
        this.setLocationRelativeTo(null); // appear middle of computer
    }
}