package App;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Interface {
  // globals
  GamePanel gp;
  Graphics2D g2;
  int interfaceState = 0; // states are mapped to gp.gamestates

  public Interface(GamePanel gp) {
    this.gp = gp;

  }

  /**
   * Draws the current interface to g2
   * @param g2 Object to be drawn to
   */
  public void draw(Graphics2D g2) {
    this.g2 = g2;
    if (interfaceState == gp.NEWGAME) {
      drawNewGame();
    }
    if (interfaceState == gp.PAUSED) {
      drawPaused();
    }
    if (interfaceState == gp.DEAD) {
      drawDead();
    }
  }

  private void drawDead() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'drawDead'");
  }

  private void drawPaused() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'drawPaused'");
  }

  private void drawNewGame() {
    // tint screen
    g2.setColor(new Color(0, 0, 0, 128));
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
    // draw welcome banner
    BufferedImage banner = gp.ag.getBanner();
    g2.drawImage(banner,  (int)((gp.screenWidth - banner.getWidth()) / 2),
                          (int)((gp.screenHeight - banner.getHeight()) / 3), null);
  }

  /**
   * Getter for interfaceState
   * @return int value for interface State
   */
  public int getState() {
    return interfaceState;
  }

  /**
   * Sets interface state to new given state
   * @param newState new state for interface
   * @return boolean value for if it was set
   */
  public boolean setState(int newState) {
    // if newState doesn't equal any of the gameStates then abort
    if (newState == gp.NEWGAME || newState == gp.PLAYING || newState == gp.DEAD || newState == gp.PAUSED) {
      interfaceState = newState;
      return true;
    }
    return false;
  }

  public int getXforCenteringText(String text) {
    int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
    int x = gp.screenWidth/2 - length/2;
    return x;
  }
}
