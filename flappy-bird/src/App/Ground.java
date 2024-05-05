package App;

import java.awt.Graphics2D;

public class Ground {
  private int x;
  private GamePanel gp;

  /**
   * Public Ground Constructor
   * @param x X cordinate of new ground object
   * @param gp GamePanel being added to
   */
  public Ground(int x, GamePanel gp) {
    this.x = x;
    this.gp = gp;
  }

  /**
   * Draw ground object
   * @param g2 Graphics2D object to be drawn to
   */
  public void draw(Graphics2D g2) {
    g2.drawImage(gp.ag.getGround(), x, gp.screenHeight-gp.ag.getGround().getHeight(), null);
  }

  /**
   * Animates the ground for draw updates
   * @param speed how fast the ground moves
   */
  public void update(int speed) {
    x -= speed;
  }

  /**
   * Used for checking if it needs to be deleted
   * @return boolean of if it's off the viewable area
   */
  public boolean isOffScreen() {
    return getPos() < 0;
  }

  /**
   * Returns the rightmost x cordinate of the ground object
   * @return X cordinate
   */
  public int getPos() {
    return x + gp.ag.getGround().getWidth();
  }
}
