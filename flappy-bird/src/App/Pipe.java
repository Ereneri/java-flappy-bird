package App;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Pipe {
  // Bottom Pipe
  private int y1;
  private Collision c1;

  // Top Pipe
  private int y2;
  private Collision c2;

  // Share X cordinate
  private int x;
  private boolean hasBeenCrossed = false;

  // Sizing Variables
  private final int pipeOpeningSize = 60;
  private final int pipeHeight = 320;
  private final int pipeWidth = 52;

  // pipe movement
  private int pipeSpeed = 3;

  /**
   * Pipe constructor, creates both pipes on given x
   * @param x X positon
   * @param y Y position
   */
  public Pipe(int x, int y) {
    // center at -10 from 0
    int center = -pipeHeight + y;
    // create bottom pipe
    this.x = x;
    this.y1 = center + pipeOpeningSize + pipeHeight;
    this.c1 = new Collision(x, y1, pipeWidth, pipeHeight);

    // create top pipe
    this.y2 = center - pipeOpeningSize;
    this.c2 = new Collision(x, y2, pipeWidth, pipeHeight);
  }

  /**
   * Draws the pipe object, both top and bottom
   * @param g2 Graphics object to be written to
   * @param ag Assetgetter for drawing pipes
   */
  public void draw(Graphics2D g2, AssetGetter ag) {
    BufferedImage bottomPipe = ag.getPipe()[0];
    BufferedImage topPipe = ag.getPipe()[1];
    // draw top pipe -> adjust for pipe height
    g2.setColor(Color.BLUE);
    // c1.render(g2);
    g2.drawImage(bottomPipe, x, y1, null);
    // draw bottom pipe
    g2.setColor(Color.PINK);
    // c2.render(g2);
    g2.drawImage(topPipe, x, y2, null);
  }

  /**
   * 
   */
  public void update() {
    x -= pipeSpeed;
    c1.setXCol(x);
    c2.setXCol(x);
  }

  /**
   * Checks if whole width of pipes are off screen
   * @return boolean value if off screen
   */
  public boolean isOffScreen() {
    return (x + pipeWidth) < 0 && (x + pipeWidth) < 0;
  }

  /**
   * Gets rightmost X cordinate of pipes
   * @return int value of X
   */
  public int horizontalPositon() {
    return x + pipeWidth;
  }
  /**
   * Gets both collision boxs from the top and bottom pipe
   * @return index 0 contains bottom pipe, index 1 contains top pipe
   */
  public Collision[] getCollisions() {
    return new Collision[]{c1, c2};
  }

  /**
   * Marks pipe has been crossed
   */
  public void crossed() {
    this.hasBeenCrossed = true;
  }

  /**
   * Checks if pipe has been crossed
   * @return boolean of has been crossed
   */
  public boolean hasBeenCrossed() {
    return hasBeenCrossed;
  }
}
