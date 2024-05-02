package App;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player {
  // Player variables
  double x, y;
  int score = 0;
  int width, height;
  double vertSpeed = 0;
  boolean isAlive = true;
  KeyHandler keyH;
  GamePanel gp;
  Collision c;
  
  // sprint rendering
  int imageCounter = 0;
  BufferedImage[] bird;
  BufferedImage currentBirdImage;

  // physics vars
  double gravity = 0.3;
  double jumpForce = 9;
  double terminalVelo = 8;

  // misc vars
  long lastTime = System.nanoTime();

  /** 
   * Player Constructor
   */
  public Player(GamePanel gp, BufferedImage[] bird, KeyHandler keyH) {
    this.gp = gp;
    this.bird = bird;
    this.keyH = keyH;
    // set players pos
    x = gp.screenWidth / 3;
    y = gp.screenHeight / 2;
    width = bird[0].getWidth();
    height = bird[0].getHeight();
    currentBirdImage = bird[0];
    this.c = new Collision((int)x, (int)y, width, height);
  }

  /**
   * Update player object based on time delta
   * @param delta time difference
   */
  public void update() {
    if (isAlive) {
      // Basic Physics
      if (keyH.space) {
        // apply upwards force
        vertSpeed -= jumpForce;
        keyH.space = false;
      }
      y += vertSpeed * gp.getDelta();
      vertSpeed += gravity * gp.getDelta();
      // check if hit floor
      if (y >= gp.screenHeight - gp.ground.getHeight() - height) {
        y = gp.screenHeight - gp.ground.getHeight() - height;
        vertSpeed = 0;
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
      c.setXCol((int)x);
      c.setYCol((int)y);
    }
  }

  public void draw(Graphics2D g2) {
    c.render(g2);
    long currentTime = System.nanoTime();
    if (isAlive && currentTime - (gp.FPS * 2000000) >= lastTime) {
      lastTime = System.nanoTime();
      imageCounter = (imageCounter + 1) % 3;
      currentBirdImage = bird[imageCounter];
    }
    g2.drawImage(currentBirdImage, (int)x, (int)y, null);
  }

  /**
   * Returns the player score
   * @return int of score
   */
  public int getScore() {
    return this.score;
  }
}
