package App;

import java.awt.Graphics2D;

public class Ground {
  private int x;
  private GamePanel gp;

  public Ground(int x, GamePanel gp) {
    this.x = x;
    this.gp = gp;
  }

  public void draw(Graphics2D g2) {
    g2.drawImage(gp.ag.getGround(), x, gp.screenHeight-gp.ag.getGround().getHeight(), null);
  }

  public void update(int speed) {
    x -= speed;
  }

  public boolean isOffScreen() {
    return getPos() < 0;
  }

  public int getPos() {
    return x + gp.ag.getGround().getWidth();
  }
}
