package App;

import javax.swing.JFrame;
import java.awt.*;


public class App {
  public static int screenWidth = 420;
  public static int screenHeight = 560;
  public static void main(String[] args) throws Exception {
    // Create a new JFrame container
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setResizable(false);
    frame.setTitle("Flappy Bird");

    // Sets bounds for frame
    frame.setLayout(new BorderLayout());
    frame.setSize(screenWidth, screenHeight);
    frame.setBounds(0, 0, screenWidth, screenHeight);

    // Create a new GamePanel
    GamePanel gamePanel = new GamePanel(screenWidth, screenHeight);
    frame.add(gamePanel);

    // Start the game thread for timing the frame rate
    gamePanel.startGameThread();

    // Make the frame sized to the game panel along with accepting input
    frame.pack();

    // Make the frame visible
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
