package App;

import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class AssetGetter {
  private BufferedImage background;
  private BufferedImage ground;
  private BufferedImage banner;
  private BufferedImage gameover;
  private BufferedImage highScore;
  private BufferedImage[] pipe;
  private BufferedImage[] bird;
  private BufferedImage[] numbers;
  private BufferedImage[] smallNumbers;

  /**
   * AssetGetter constructor, gets all assets from dir folder
   * @param dir String to asset folder
   */
  public AssetGetter(String dir) {
    // get background image asset
    try {
      this.background = ImageIO.read(getClass().getResource(dir + "/background-day.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // get ground asset
    try {
      this.ground = ImageIO.read(getClass().getResource(dir + "/base.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // get pipe asset
    this.pipe = new BufferedImage[2];
    try {
      this.pipe[0] = ImageIO.read(getClass().getResource(dir + "/pipe-green.png"));
      this.pipe[1] = ImageIO.read(getClass().getResource(dir + "/pipe-green-flipped.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // get bird
    this.bird = new BufferedImage[3];
    try {
      this.bird[0] = ImageIO.read(getClass().getResource(dir + "/yellowbird-downflap.png"));
      this.bird[1] = ImageIO.read(getClass().getResource(dir + "/yellowbird-midflap.png"));
      this.bird[2] = ImageIO.read(getClass().getResource(dir + "/yellowbird-upflap.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // get banner asset
    try {
      this.banner = ImageIO.read(getClass().getResource(dir + "/message.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // get gameover asset
    try {
      this.gameover = ImageIO.read(getClass().getResource(dir + "/gameover.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // get highscore asset
    try {
      this.highScore = ImageIO.read(getClass().getResource(dir + "/highScore2.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    // get Big numbers
    this.numbers = new BufferedImage[10];
    try {
      for (int i = 0; i < 10; i++) {
        this.numbers[i] = ImageIO.read(getClass().getResource(dir + "/" + i + ".png"));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    // get small numbers
    this.smallNumbers = new BufferedImage[10];
    try {
      for (int i = 0; i < 10; i++) {
        this.smallNumbers[i] = ImageIO.read(getClass().getResource(dir + "/s" + i + ".png"));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Getter for background asset
   * @return BufferedImage of background
   */
  public BufferedImage getBackground() {
    return background;
  }

  /**
   * Getter for ground asset
   * @return BufferedImage of Ground
   */
  public BufferedImage getGround() {
    return ground;
  }
  /**
   * Getter for pipe asset
   * @return BufferedImage of background
   */
  public BufferedImage[] getPipe() {
    return pipe;
  }
  /**
   * Getter for bird assets
   * @return BufferedImage of bird assets
   */
  public BufferedImage[] getBird() {
    return bird;
  }

  /**
   * Getter for banner asset
   * @return BufferedImage of banner
   */
  public BufferedImage getBanner() {
    return banner;
  }

  /**
   * Getter for gameover asset
   * @return BufferedImage of gameover
   */
  public BufferedImage getGameOver() {
    return gameover;
  }

  /**
   * Getter for number assets
   * @return BufferedImage of numbers
   */
  public BufferedImage getNumbers(int num) {
    return numbers[num];
  }

  /**
   * Getter for number assets
   * @return BufferedImage of numbers
   */
  public BufferedImage getSmallNumbers(int num) {
    return smallNumbers[num];
  }

  /**
   * Getter for highscore banner
   * @return BufferedImage of score board
   */
  public BufferedImage getHighScore() {
    return highScore;
  }
}
