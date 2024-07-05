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
  int numberOfAlivePlayers = 0;
  private int highScore = 0;
  private int gameSpeed = 3;
  private int gameScore = 0;

  // AI variables
  ArrayList<Player> players = new ArrayList<Player>();
  private final int populationSize = 100;
  private final int numberOfInputs = 5;
  private Population population;
  private long startTime = (long) -1.0;
  private Pipe targetPipe = null;
  private final int numberOfGenerations = 100;
  private int currentGeneration = 0;

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
   * @param screenWidth Given screen width from App
   * @param screenHeight Given screen height from App
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

    // initialize population here
    Config cfg = new Config(numberOfInputs, 1, populationSize);
    this.population = new Population(cfg);

    // add players
    List<Individual> populationIndividuals = population.getIndividuals();
    for (int i = 0; i < populationSize; i++) {
      players.add(new Player(this, i, populationIndividuals.get(i)));
    }
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
        gameState = PLAYING;
        highScore = Math.max(highScore, gameScore);
        newGame();
      } else {
        // first pipe in array that hasn't been crossed yet
        targetPipe = updatePipes(targetPipe);

        updatePlayer();

        // update ground
        updateGround();
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

    if (targetPipe != null) {
      for (Player player : players) {
        if (player.isAlive()) {
          g2.setColor(new Color((player.playerID)));
          g2.drawLine((int)player.getX(), (int)player.getY(), targetPipe.getMiddleOfOpening().get("x"), targetPipe.getMiddleOfOpening().get("y"));
        }
      }
    }

    // draw overlay on bottom for current generation
    g2.drawString("Generation " + currentGeneration + "/" + numberOfGenerations, 20, screenHeight - (ag.getGround().getHeight() / 2));
    g2.drawString("Agents Alive " + numberOfAlivePlayers + "/" + populationSize, 20, screenHeight - (ag.getGround().getHeight() / 3));

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
    // increment generation and check if we are done
    if (currentGeneration == numberOfGenerations) {
      gameState = DEAD;
      // print out all the individuals and their genomes
      for (Player player : players) {
        player.getPlayersAIIndividual().getGenome().printGenome();
      }
      return;
    }
    currentGeneration += 1;

    List<Individual> newIndividuals = population.getIndividuals();
    // if this isn't our first game then reproduce
    if (startTime != (long) -1.0) {
      // reproduce our population
      newIndividuals = population.reproduce();
    }

    // get start time
    startTime = System.currentTimeMillis();

    // Reset alive players when they all die
    numberOfAlivePlayers = players.size();
    for (int idx = 0; idx < players.size(); idx++) {
      players.get(idx).defaultValues();
      players.get(idx).setPlayersAIIndividual(newIndividuals.get(idx));
    }

    gameScore = 0;

    // Reset Pipes
    pipes.clear();
    for (int i = 0; i < 4; i++) {
      pipes.add(createPipe((i * (screenWidth / 2)) + screenWidth));
    }
    targetPipe = pipes.get(0);
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

    // need to set the player's fitness here using timestamps for number of pipes crossed
    double timeAlive = System.currentTimeMillis() - startTime;
    int pipesCrossed = p.getScore() + 1; // prevent zeroing everything out
    double fitness = (pipesCrossed * 100) * (timeAlive * 50);
    p.getPlayersAIIndividual().updateFitness(-fitness);
  }

  public int getGameScore() {
    return gameScore;
  }

  /**
   * Get all the input data for NN
   * @param player Player object to be used to get distances
   * @return List of doubles to be passed to NN
   */
  private List<Double> getInputs(Player player) {
    List<Double> ans = new ArrayList<>();

    double birdsVertical = player.getY();
    double topPipe = (double)targetPipe.getY().get("top");
    double bottomPipe = (double)targetPipe.getY().get("bottom");

    // add player velocity
    ans.add(player.getVelocity() / player.getTerminalVelocity());
    // add delta to top pipe (y)
    ans.add(((topPipe - birdsVertical) - screenHeight) / screenHeight);
    // add delta to bottom pipe (y)
    ans.add((birdsVertical - bottomPipe));
    // distance to pipe opening
    ans.add((getDistanceToPipeOpening(player)));
    // get distance to pipe
    ans.add((targetPipe.getX() - player.getX()) / screenWidth);
    return ans;
  }

  /**
   * Used to get distance to pipe opening, used as fitness
   * @param player player to be scored
   * @return double of distance to pipe, lower is better
   */
  private double getDistanceToPipeOpening(Player player) {
    double deltaX = targetPipe.getMiddleOfOpening().get("x") - player.getX();
    double deltaY = targetPipe.getMiddleOfOpening().get("y") - player.getY();
    if (Math.round(Math.sqrt(deltaX * deltaX + deltaY * deltaY)) <= 0) {
      System.err.println("dX: " + deltaX + " dY: " + deltaY + " == " + Math.sqrt(deltaX * deltaX + deltaY * deltaY));
    }
    return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
  }

  private void updateGround() {
    offScreen = false;
    for (int gidx = 0; gidx < grounds.size(); gidx++) {
      Ground ground = grounds.get(gidx);
      ground.update(gameSpeed);
      // trigger bool so we can remove first element from list
      if (ground.isOffScreen()) {
        offScreen = true;
      }
    }

    // remove the first element from the list and add a new tile
    if (offScreen) {
      grounds.remove(0);
      grounds.add(new Ground(grounds.get(grounds.size() - 1).getPos(), this));
    }
  }

  /**
   * Helper Function for Update()
   * @param targetPipe starting targetPipe
   * @return new or same targetPipe
   */
  private Pipe updatePipes(Pipe targetPipe) {
    offScreen = false;
    // Check if this pipe is pass our current bird y
    if ((screenWidth / 3) > targetPipe.getHorizontalPositon()) {
      gameScore += 1;
      int targetIdx = pipes.indexOf(targetPipe) + 1;
      targetPipe = pipes.get(targetIdx);
    }

    for (int pidx = 0; pidx < pipes.size(); pidx++) {
      Pipe pipe = pipes.get(pidx);
      pipe.update(gameSpeed);

      if ((screenWidth / 3) > pipe.getHorizontalPositon()) {
        pipe.crossed();
      }

      // delete old pipes if x is < 0
      if (pipe.isOffScreen()) {
        offScreen = true;
      }

      for (Player player : players) {
        // only update players that are alive
        if (player.isAlive()) {
          // check if pipes touch player
          if (pipe.getCollisions().get("top").touches(player.getCollision()) ||
                  pipe.getCollisions().get("bottom").touches(player.getCollision())) {
            playerDied(player);
          }
          // check if we have passed a pipe
          if (player.getX() > pipe.getHorizontalPositon() && pipe.hasBeenCrossed()) {
            player.incrementScore();
          }
        }
      }
    }
    // delete pipes off screen
    if (offScreen) {
      pipes.remove(0);
      pipes.add(createPipe((pipes.get(pipes.size() - 1)).getHorizontalPositon() + screenWidth / 3));
    }
    return targetPipe;
  }

  /**
   * Helper Function for Update()
   */
  private void updatePlayer() {
    // Update each player
    for (Player player : players) {
      if (player.isAlive()) {
        // get inputs for
        assert targetPipe != null;
        player.update();
        // should player jump
        if (player.getPlayersAIIndividual().compute(getInputs(player))) {
          player.jump();
        }
        // check if player hits ground
        if (player.getY() >= screenHeight - ag.getGround().getHeight() - player.getHeight()) {
          playerDied(player);
          player.getPlayersAIIndividual().setFitness(Integer.MAX_VALUE);
        }
        // if hitting ceiling
        if (player.getY() == 0) {
          player.getPlayersAIIndividual().setFitness(Integer.MAX_VALUE);
        }
      }
    }
  }

}
