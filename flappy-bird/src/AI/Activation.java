package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Activation {
  // activation curve such as sigmoid or smth
  private double activationValue;

  /**
   * Store Activation of Nodes, does not calculate on construction
   */
  public Activation() {
    this.activationValue = Math.random();
//    System.out.println("Activation of " + activationValue);
  }

  /**
   * Return activation Value of Node 
   * @return the activiation value
   */
  public double getActivationValue() {
    return this.activationValue;
  }

  /* Activation Function -> Sigmoid */

  /**
   * Sigmoid Activation Function
   * @param x input
   * @return sigmoid of x
   */
  public static double sigmoid(double x) {
    return 1 / (1 + Math.exp(-x));
  }

  /**
   * Set Activation Value of Node
   * @param value
   */
  public void setActivationValue(double value) {
    this.activationValue = value;
  }
}
