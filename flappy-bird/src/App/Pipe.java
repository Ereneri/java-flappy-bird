package App;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Pipe {
  // Bottom Pipe
  int x1, y1;
  Collision c1;

  // Top Pipe
  int x2, y2;
  Collision c2;

  // misc
  int pipeOpeningSize = 60;
  int pipeHeight = 320;
  int pipeWidth = 52;

  /**
   * Pipe constructor, creates both pipes on given x
   * @param x X positon
   * @param y Y position
   */
  public Pipe(int x, int y) {
    int center = y;
    // create bottom pipe
    x1 = x;
    y1 = center + pipeOpeningSize + pipeHeight;
    c1 = new Collision(x1, y1, pipeWidth, pipeHeight);

    // create top pipe
    x2 = x;
    y2 = center - pipeOpeningSize;
    c2 = new Collision(x2, y2, pipeWidth, pipeHeight);
  }

  public void draw(Graphics2D g2, AssetGetter ag) {
    BufferedImage bottomPipe = ag.getPipe()[0];
    BufferedImage topPipe = ag.getPipe()[1];
    // draw top pipe -> adjust for pipe height
    g2.setColor(Color.BLUE);
    c1.render(g2);
    g2.drawImage(bottomPipe, x1, y1, null);
    // draw bottom pipe
    g2.setColor(Color.PINK);
    c2.render(g2);
    g2.drawImage(topPipe, x2, y2, null);
  }
}
