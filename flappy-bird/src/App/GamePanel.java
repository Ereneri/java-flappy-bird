package App;

// import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
// import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
// import java.io.IOException;
import java.util.ArrayList;
import java.io.IOException;

import javax.imageio.ImageIO;
// import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
  // game objects
  ArrayList<Pipe> Pipes = new ArrayList<Pipe>();
  Player player;

  // screen control
  double ratio;
  int screenWidth;
  int screenHeight;

  // assets
  AssetGetter ag;
  String dir = "/assets";
  BufferedImage background;
  BufferedImage ground;

  // game vars
  int gameState = 0;
  final int NEWGAME = 0;
  final int PLAYING = 1;
  final int PAUSED = 2;
  final int DEAD = 3;

  // misc objects
  public Interface ui = new Interface(this);
  KeyHandler keyH = new KeyHandler(this);
  
  // rendering vars
  public Thread gameThread;
  final int FPS = 60;
  double delta = 0;

  /**
   * Used by App/main as main game panel (JPanel)
   * @param screenWidth
   * @param screenHeight
   */
  public GamePanel(int screenWidth, int screenHeight) {
    // set screen vars
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;

    // background loading
    ag = new AssetGetter(dir);
    this.background = ag.getBackground();
    this.ground = ag.getGround();
    
    // Panel setup
    this.setBounds(0, 0, screenWidth, screenHeight);
    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    this.setBackground(Color.red);
    this.setDoubleBuffered(true);
    this.setFocusable(true);
    this.setLayout(null);
    // setup globals as needed
    this.addKeyListener(keyH);
    this.player = new Player(this, ag.getBird(), keyH);
    this.Pipes.add(new Pipe(300, 0));
  }

  /**
   * Start a new thread that will contain game
   */
  public void startGameThread() {
    gameThread = new Thread(this);
    try {
      //waits for this current thread to die before beginning execution
      gameThread.join();
    } catch(InterruptedException e){
      e.printStackTrace();
    }
    gameThread.start();
  }

  /**
   * Main game loop logic
   */
  @Override
  public void run() {
    // Delta method vars
    double drawInterval = 1000000000 / FPS; // .01666 seconds 
    delta = 0;
    
    long lastTime = System.nanoTime();
    long currentTime;

    while (gameThread != null) {
      // get current time and set delta to the difference between current time and last time divided by the frame rate
      currentTime = System.nanoTime();
      delta += (currentTime - lastTime) / drawInterval;
      // then set of time to current
      lastTime = currentTime;
      // if delta is greater than 1, then update
      if (delta >= 1) {
        // Update and paint components
        update();
        repaint();
        delta--;
      }
    }
    this.requestFocusInWindow();
  }

  /**
   * Updates frame with key input
   */
  public void update() {
    if (gameState == PLAYING) {
      // player update
      player.update();
      // check if player is touching ground

      // Pipes logic
      // updates pipe

      // delete old pipes if x is < 0

      // spawn new pipes
    }
  }

  // Draw things on JPanel
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;

    
    // draws the background image
    for (int x = 0; x < screenWidth; x += background.getWidth()) {
      g2.drawImage(background, x, 0, null);
    }
    
    // draw pipes
    for (Pipe p : Pipes) {
      p.draw(g2, ag);
    }
    
    // draw baseline
    for (int x = 0; x < screenWidth; x += ground.getWidth()) {
      g2.drawImage(ground, x, screenHeight-ground.getHeight(), null);
    }
    
    // bird
    player.draw(g2);
    
    if (gameState == NEWGAME) ui.draw(g2);
  }

  public double getDelta() {
    return delta;
  }
}
