package AI;

public class Activation {
  private double activationValue;

  /**
   * Store Activation of Nodes, does not calculate on construction
   */
  public Activation() {
    this.activationValue = Math.random();
  }

  /**
   * Take input for Activation value, used for output nodes
   * @param activationValue Activation value to be set
   */
  public Activation(double activationValue) {
    this.activationValue = activationValue;
  }

  /**
   * Return activation Value of Node 
   * @return the activation value
   */
  public double getActivationValue() {
    return this.activationValue;
  }

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
   * @param value Activation value to be set
   */
  public void setActivationValue(double value) {
    this.activationValue = value;
  }
}
