package App;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
  // game objects
  ArrayList<Pipe> pipes = new ArrayList<Pipe>();
  int lastPipeX;
  Player player = null;

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
  boolean offScreenPipe = false;

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
    newGame();
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
      if (!player.isAlive()) {
        gameState = DEAD;
      }
      // player update
      player.update();

      // update all pipes
      offScreenPipe = false;
      for (int pidx = 0; pidx < pipes.size(); pidx++) {
        Pipe p = pipes.get(pidx);
        p.update();

        // delete old pipes if x is < 0
        if (p.isOffScreen()) {
          offScreenPipe = true;
        }

        // check if crossed pipe
        if (player.horizontalPositon() > p.horizontalPositon() && !p.hasBeenCrossed()) {
          player.incrementScore();
          p.crossed();
        }

        // check if pipes touch player
        if (p.getCollisions()[0].touches(player.getCollision()) ||
        p.getCollisions()[1].touches(player.getCollision())) {
              player.playerDied();
              gameState = DEAD;
            }

      }
      // delete pipes off screen
      if (offScreenPipe) {
        pipes.remove(0);
        pipes.add(createPipe((pipes.get(pipes.size()-1)).horizontalPositon() + screenWidth/3));
      }
    }
  }

  // Draw things on JPanel
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    // caste graphics to 2D
    Graphics2D g2 = (Graphics2D) g;

    // Draw Background
    for (int x = 0; x < screenWidth; x += background.getWidth()) {
      g2.drawImage(background, x, 0, null);
    }
    
    // Draw Pipes
    for (Pipe p : pipes) {
      p.draw(g2, ag);
    }
    
    // Draw Ground
    for (int x = 0; x < screenWidth; x += ground.getWidth()) {
      g2.drawImage(ground, x, screenHeight-ground.getHeight(), null);
    }
    
    // Update Player
    player.draw(g2);
    
    // draw Score
    String scoreStr = player.getScore() + "";
    int xPos = (screenWidth - (scoreStr.length() * ag.getNumbers(0).getWidth())) / 2;
    for (int i = 0; i < scoreStr.length(); i++) {
      int scoreNum = Character.getNumericValue(scoreStr.charAt(i));
      g2.drawImage(ag.getNumbers(scoreNum), xPos, screenHeight / 10, null);
      xPos += ag.getNumbers(scoreNum).getWidth();
    }

    // Draw Correct UI
    if (gameState != PLAYING) {
      ui.setState(gameState);
      ui.draw(g2);
    }
  }

  /**
   * Get Time delta from gameThread
   * @return nanosecond time delta from last frame
   */
  public double getDelta() {
    return delta;
  }

  public void newGame() {
    // Reset Player
    if (player == null) this.player = new Player(this);
    this.player.defaultValues();

    // Reset Pipes
    pipes.clear();
    for (int i = 0; i < 4; i++) {
      pipes.add(createPipe((i * (screenWidth/2)) + screenWidth));
    }
  }

  /**
   * Create a new pipe at given x cord, y is random
   * @param x horizontal positon of pipe
   * @return
   */
  private Pipe createPipe(int x) {
    int randY = (int)(Math.random() * (ag.getPipe()[0].getHeight() - 60)) + 75;
    return new Pipe(x, randY);
  }
}
