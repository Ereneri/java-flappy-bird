package App;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
  // setup global key presses
  private boolean space, esc;
  GamePanel gp;

  /**
   * KeyHandler constructor, handles all keyboard input for game
   */
  public KeyHandler(GamePanel gp) {
    this.gp = gp;
  }
  
  /**
   * Gets state of keyboard jumping input
   * @return boolean value whether user Jumped
   */
  public boolean didJump() {
    return space;
  }

  /**
   * Gets state of keyboard pause input
   * @return boolean value whether user paused game
   */
  public boolean Pause() {
    return esc;
  }

  /**
   * Sets Jump key to false
   */
  public void stopJump() {
    space = false;
  }

  @Override
  /**
   * On keypress do resulting action
   */
  public void keyPressed(KeyEvent e) {
    int code = e.getKeyCode();
    if (gp.gameState == gp.NEWGAME) {
      // if space is pressed start game
      if (code == KeyEvent.VK_SPACE) {
        space = true;
        gp.gameState = gp.PLAYING;
      }
    }
    if (gp.gameState == gp.PLAYING) {
      // if esc is pressed pause game
      if (code == KeyEvent.VK_ESCAPE) {
        esc = true;
        gp.gameState = gp.PAUSED;
      }

      // if space is pressed player jump
      if (code == KeyEvent.VK_SPACE) {
        space = true;
      }
    }
    if (gp.gameState == gp.PAUSED) {
      // if esc is pressed resume game
      if (code == KeyEvent.VK_ESCAPE) {
        esc = true;
        gp.gameState = gp.PAUSED;
      }
    }
    if (gp.gameState == gp.DEAD) {
      // is space is pressed new game
      if (code == KeyEvent.VK_SPACE) {
        space = true;
        gp.gameState = gp.NEWGAME;
        gp.newGame();
      }
    }
  }

  @Override
  /**
   * On key relase, do resulting action
   */
  public void keyReleased(KeyEvent e) {
    int code = e.getKeyCode();
    if (code == KeyEvent.VK_SPACE) {
      space = false;
    }
    if (code == KeyEvent.VK_ESCAPE) {
      esc = false;
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {
  }
}
