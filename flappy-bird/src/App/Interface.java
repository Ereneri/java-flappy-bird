package App;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Interface {
  // globals
  private GamePanel gp;
  private Graphics2D g2;
  private int interfaceState = 0; // states are mapped to gp.gamestates

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
    BufferedImage gameOver = gp.ag.getGameOver();
    BufferedImage hs = gp.ag.getHighScore();
    // Draw Gameover
    g2.drawImage(gameOver,  (int)((gp.screenWidth - gameOver.getWidth()) / 2),
                            (int)((gp.screenHeight - gameOver.getHeight()) / 3), null);
    // Draw background highscore
    g2.drawImage(hs,  (gp.screenWidth - hs.getWidth()) / 2,
                      (gp.screenHeight/3) + gameOver.getHeight(), null);

    /**
     * Some of the grossest code i have ever written, please eren ffs fix this
     */
    // print current score
    String scoreStr = gp.player.getScore() + "";
    int xPos = ((gp.screenWidth - hs.getWidth()) / 2) + hs.getWidth() - (scoreStr.length() * gp.ag.getSmallNumbers(0).getWidth()) - 25;
    for (int i = 0; i < scoreStr.length(); i++) {
      int scoreNum = Character.getNumericValue(scoreStr.charAt(i));
      g2.drawImage( gp.ag.getSmallNumbers(scoreNum),
                    xPos,
                    (gp.screenHeight/3) + gameOver.getHeight() + 37,
                  null);
      xPos += gp.ag.getSmallNumbers(scoreNum).getWidth();
    }
    // print highscore
    String highScore = gp.getHighScore() + "";
    xPos = ((gp.screenWidth - hs.getWidth()) / 2) + hs.getWidth() - (highScore.length() * gp.ag.getSmallNumbers(0).getWidth()) - 25;
    for (int i = 0; i < highScore.length(); i++) {
      int scoreNum = Character.getNumericValue(highScore.charAt(i));
      g2.drawImage( gp.ag.getSmallNumbers(scoreNum),
                    xPos,
                    (gp.screenHeight/3) + gameOver.getHeight() + 78,
                  null);
      xPos += gp.ag.getSmallNumbers(scoreNum).getWidth();
    }
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
}
