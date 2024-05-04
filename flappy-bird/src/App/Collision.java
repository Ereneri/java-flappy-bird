package App;

import java.awt.Graphics2D;

public class Collision {
  // store rectanglular cordinates
  private int x, y, w, h;

  public Collision(int x, int y, int w, int h) {
    this.x = x;
    this.y = y;
    this.w = w;
    this.h = h;
  }

  public Collision getCol() {
    return this;
  }
  public int getXCol() {
    return x;
  }
  
  public void setXCol(int x) {
    this.x = x;
  }
  
  public int getYCol() {
    return y;
  }
  
  public void setYCol(int y) {
    this.y = y;
  }
  
  public int getWidthCol() {
    return w;
  }
  
  public void setWidthCol(int width) {
    this.w = width;
  }
  
  public int getHeightCol() {
    return h;
  }
  
  public void setHeightCol(int height) {
    this.h = height;
  }
  
  
  /**
   * Render the collision box of the object
   * @param g
   */
  public void render(Graphics2D g) {
    g.drawRect(x, y, w, h);
  }
  
  /**
   * Checks if too collision boxes are touching
   * @param c other collision to check
   * @return boolean value if touching
   */
  public boolean touches(Collision c) {
    int tw = this.w;
      int th = this.h;
      int rw = c.w;
      int rh = c.h;
      if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
          return false;
      }
      int tx = this.x;
      int ty = this.y;
      int rx = c.x;
      int ry = c.y;
      rw += rx;
      rh += ry;
      tw += tx;
      th += ty;
      //      overflow || intersect
      return ((rw < rx || rw > tx) &&
              (rh < ry || rh > ty) &&
              (tw < tx || tw > rx) &&
              (th < ty || th > ry));
  }
}
