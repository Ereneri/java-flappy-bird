package App;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Player {
  // Player variables
  private double x, y;
  private int score;
  private int width, height;
  private boolean isAlive;

  // AI vars
  private boolean jump = false;

  // object vars
  private GamePanel gp;
  private Collision c;
  
  // sprint rendering
  private int imageCounter = 0;
  private BufferedImage[] bird;
  private BufferedImage currentBirdImage;
  
  // physics vars
  private double vertSpeed          = 0;
  private final double gravity      = 0.4;
  private final double jumpForce    = 9;
  private final double terminalVelo = 8;

  // misc vars
  long lastTime = System.currentTimeMillis();

  /** 
   * Player Constructor
   */
  public Player(GamePanel gp) {
    this.gp = gp;
    this.bird = gp.ag.getBird();
    // set players pos
    this.x = gp.screenWidth / 3;
    this.y = gp.screenHeight / 2;
    // set player dims
    this.width = bird[0].getWidth() - 5;
    this.height = bird[0].getHeight() - 5;
    // misc
    this.isAlive = true;
    this.score = 0;
    this.currentBirdImage = bird[0];
    this.c = new Collision((int)x+4, (int)y+4, width, height);
  }

  /**
   * Update player object based on time delta
   */
  public void update() {
    if (isAlive) {
      // Basic Physics
      if (gp.keyH.didJump() || jump) {
        // apply upwards force
        vertSpeed -= jumpForce;
        // prevents user from holding
        gp.keyH.stopJump();
        // reset jump
        jump = false;
      }
      y += vertSpeed * gp.getDelta();
      vertSpeed += gravity * gp.getDelta();
      // check if hit floor
      if (y >= gp.screenHeight - gp.ag.getGround().getHeight() - height) {
        y = gp.screenHeight - gp.ag.getGround().getHeight() - height;
        isAlive = false;
      }
      // check if it hit ceiling
      if (y < 0) {
        y = 0;
        vertSpeed = 0;
      }
      // cap vertSpeed to terminal velo
      if (Math.abs(vertSpeed) > terminalVelo) {
        if (vertSpeed < 0) vertSpeed = -terminalVelo;
        else vertSpeed = terminalVelo;
      }
      // update collision box
      c.setXCol((int)x+2);
      c.setYCol((int)y+4);
    }
  }

  /**
   * Draws the player with animations and rotation control
   * @param g2 Graphics object to be drawn to
   */
  public void draw(Graphics2D g2) {
    // c.render(g2);
    long currentTime = System.currentTimeMillis();
    // updates sprite every 60 milliseconds > 1 frame
    if (isAlive && currentTime - (gp.FPS) >= lastTime) {
      lastTime = System.currentTimeMillis();
      imageCounter = (imageCounter + 1) % 3;
      currentBirdImage = bird[imageCounter];
    }
    // get old rotation
    AffineTransform old = g2.getTransform();
    // rotate bird image with respect to the vertical speed
    g2.rotate(vertSpeed / 9, (int)x + (width/2), (int)y + (height/2));
    g2.drawImage(currentBirdImage, (int)x, (int)y, null);
    // reset rotation to old -> leaves g2 consistent 
    g2.setTransform(old);
  }

  /**
   * Returns the player score
   * @return int of score
   */
  public int getScore() {
    return this.score;
  }

  /**
   * Increment the score of the bird
   */
  public void incrementScore() {
    this.score++;
  }

  /**
   * Returns boolean status if player is alive
   * @return
   */
  public boolean isAlive() {
    return this.isAlive;
  }

  public void playerDied() {
    this.isAlive = false;
  }

  /**
   * Gets player's collision
   * @return Collision object
   */
  public Collision getCollision() {
    return c;
  }

  public void defaultValues() {
    // set players pos
    this.x = gp.screenWidth / 3;
    this.y = gp.screenHeight / 2;
    this.c.setXCol((int)x);
    this.c.setYCol((int)y);
    // set misc
    this.isAlive = true;
    this.score = 0;
    this.vertSpeed = 0;
  }

  /**
   * Return's an int of the player's horizontal positon
   * @return int casted postion
   */
  public int horizontalPositon() {
    return (int)this.x;
  }

  /**
   * Make's Player Jump
   */
  public void jump() {
    this.jump = true;
  }
}
