package App;

import AI.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
  // Game Objects and vars
  ArrayList<Pipe> pipes = new ArrayList<Pipe>();
  ArrayList<Ground> grounds = new ArrayList<Ground>();
  ArrayList<Player> players = new ArrayList<Player>();
  int numberOfAlivePlayers = 0;
  private int highScore = 0;
  private int gameSpeed = 3;
  private int gameScore = 0;
  private int populationSize = 1;

  // screen control
  double ratio;
  int screenWidth;
  int screenHeight;

  // assets
  AssetGetter ag;
  String dir = "/assets";
  BufferedImage background;

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
  boolean offScreen = false;

  /**
   * Used by App/main as main game panel (JPanel)
   * 
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

    // Panel setup
    this.setBounds(0, 0, screenWidth, screenHeight);
    this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    this.setBackground(Color.red);
    this.setDoubleBuffered(true);
    this.setFocusable(true);
    this.setLayout(null);
    // setup globals as needed
    this.addKeyListener(keyH);
    for (int i = 0; i < screenWidth + ag.getGround().getWidth(); i += ag.getGround().getWidth()) {
      grounds.add(new Ground(i, this));
    }
    // add players
    for (int i = 0; i < populationSize; i++) {
      players.add(new Player(this, i)); // TODO need to map players to a individual...
    }
    // initalize population here
    Config cfg = new Config(3, 1, populationSize);
    Population population = new Population(cfg);
    System.out.println("Population created with " + population.getHighestGID() + " individuals");
    newGame();
  }

  /**
   * Start a new thread that will contain game
   */
  public void startGameThread() {
    gameThread = new Thread(this);

    try {
      // waits for this current thread to die before beginning execution
      gameThread.join();
    } catch (InterruptedException e) {
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
      // get current time and set delta to the difference between current time and
      // last time divided by the frame rate
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
   * Updates frame
   */
  public void update() {
    if (gameState == PLAYING) {
      // check if player died
      if (numberOfAlivePlayers == 0) {
        gameState = DEAD;
      }
      // first pipe in array that hasn't been crossed yet
      Pipe targetPipe = null;

      // update all pipes
      offScreen = false;
      for (int pidx = 0; pidx < pipes.size(); pidx++) {
        Pipe pipe = pipes.get(pidx);
        pipe.update(gameSpeed);

        // set out target
        if (targetPipe == null && pipe.hasBeenCrossed()) {
          targetPipe = pipe;
        }

        // delete old pipes if x is < 0
        if (pipe.isOffScreen()) {
          offScreen = true;
        }

        // Check if this pipe is pass our current bird y
        if ((screenWidth / 3) > pipe.getHorizontalPositon() && pipe.hasBeenCrossed()) {
          // update score here ideally
          gameScore += 1;
          pipe.crossed();
        }

        for (Player player : players) {
          // only update players that are alive
          if (player.isAlive()) {
            // check if pipes touch player
            if (pipe.getCollisions().get("top").touches(player.getCollision()) ||
                    pipe.getCollisions().get("bottom").touches(player.getCollision())) {
              playerDied(player);
            }
          }
        }
      }
      // delete pipes off screen
      if (offScreen) {
        pipes.remove(0);
        pipes.add(createPipe((pipes.get(pipes.size() - 1)).getHorizontalPositon() + screenWidth / 3));
      }

      // Update each player
      for (Player player : players) {
        if (player.isAlive()) {
          // get inputs for
          assert targetPipe != null;
          List<Double> inputs = getInputs(player, targetPipe);
          System.out.print("Data: ");
          for (Double d : inputs) {
            System.out.print(d + ",");
          }
          System.out.println();
          // update player
          player.update();
          // should player jump
          if (Math.random() < 0.10) {
            player.jump(); // TODO make this call the AI NEAT shouldJump Function and pass in all inputs
          }
          // check if player hits ground
          if (player.getY() >= screenHeight - ag.getGround().getHeight() - player.getHeight()) {
            playerDied(player);
          }
        }
      }

      // update ground
      offScreen = false;
      for (int gidx = 0; gidx < grounds.size(); gidx++) {
        Ground ground = grounds.get(gidx);
        ground.update(gameSpeed);
        // trigger bool so we can remove first element from list
        if (ground.isOffScreen()) {
          offScreen = true;
        }
      }
      // TODO ideally we could just update the position instead of removing and adding
      // remove the first element from the list and add a new tile
      if (offScreen) {
        grounds.remove(0);
        grounds.add(new Ground(grounds.get(grounds.size() - 1).getPos(), this));
      }
    }

  }

  /**
   * Draws components to viewable area
   */
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    // cast graphics to 2D
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
    for (Ground gr : grounds) {
      gr.draw(g2);
    }

    // Update Player
    for (Player player : players) {
      if (player.isAlive()) {
        player.draw(g2);
      }
    }

    // draw Score
    String scoreStr = gameScore + "";
    int xPos = (screenWidth - (scoreStr.length() * ag.getNumbers(0).getWidth())) / 2;
    // draw each number in scoreStr
    for (int i = 0; i < scoreStr.length(); i++) {
      int scoreNum = Character.getNumericValue(scoreStr.charAt(i));
      // use number to get correct big number image
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
   * 
   * @return nanosecond time delta from last frame
   */
  public double getDelta() {
    return delta;
  }

  /**
   * Resets Environment for new game
   */
  public void newGame() {
    // Reset alive players when they all die
    numberOfAlivePlayers = players.size();
    for (Player player : players) {
      player.defaultValues();
    }
    gameScore = 0;
    System.out.println("New Game");
    System.out.println("Players: " + players.size());
    System.out.println("Alive Players: " + numberOfAlivePlayers);

    // Reset Pipes
    pipes.clear();
    for (int i = 0; i < 4; i++) {
      pipes.add(createPipe((i * (screenWidth / 2)) + screenWidth));
    }
  }

  /**
   * Create a new pipe at given x cord, y is random
   * 
   * @param x horizontal positon of pipe
   * @return
   */
  private Pipe createPipe(int x) {
    int randY = (int) (Math.random() * (ag.getPipe()[0].getHeight() - 60)) + 75;
    return new Pipe(x, randY);
  }

  /**
   * Gets the current highest score
   * 
   * @return highest score
   */
  public int getHighScore() {
    return highScore;
  }

  public void playerDied(Player p) {
    numberOfAlivePlayers -= 1;
    p.setAliveStatus(false);

    if (numberOfAlivePlayers == 0) {
      gameState = DEAD;
    }
  }

  public int getGameScore() {
    return gameScore;
  }

  /**
   * Get all the input data for NN
   * @param player Player object to be used to get distances
   * @param pipe Nearest Pipe that hasn't been crossed
   * @return List of doubles to be passed to NN
   */
  private List<Double> getInputs(Player player, Pipe pipe) {
    List<Double> ans = new ArrayList<>();

    double birdsVertical = player.getY();
    double birdsHorizontal = player.getX();
    double topPipe = (double)pipe.getY().get("top");
    double bottomPipe = (double)pipe.getY().get("bottom");
    double distanceToPipes = (double)pipe.getHorizontalPositon();

    // add player's y positon
    ans.add(birdsVertical);
    // add player velo
    ans.add(player.getVelocity());
    // add delta to top pipe (y)
    ans.add(topPipe - birdsVertical);
    // add delta to bottom pipe (y)
    ans.add(bottomPipe - birdsVertical);
    // add distance to pipes (x)
    ans.add(distanceToPipes - birdsHorizontal);
    return ans;
  }
}
