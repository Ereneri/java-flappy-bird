package App;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
  // setup global key presses
  boolean space, esc;
  GamePanel gp;

  /**
   * KeyHandler constructor, handles all keyboard input for game
   */
  public KeyHandler(GamePanel gp) {
    this.gp = gp;
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
