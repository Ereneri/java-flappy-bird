package AI;

public class Neuron {
  // Neuron Variables
  private int nid;
  private double bias;
  private int type;
  private Activation activation;

  public Neuron(int nid, double bias, int type, Activation activation) {
    this.nid = nid;
    this.bias = bias;
    this.type = type;
    this.activation = activation;
  }

  public Neuron(int nid, double bias, Activation activation) {
    this.nid = nid;
    this.bias = bias;
    this.type = Population.HIDDEN;
    this.activation = activation;
  }

  public Neuron(int nid) {
    this.nid = nid;
    this.bias = 1.0;
    this.activation = null;
  }

  public double getBias() {
    return this.bias;
  }

  public int getNID() {
    return this.nid;
  }

  /**
   * Get's the type of the neuron
   * @return 0-Input, 1-Output, 3-Hidden
   */
  public int getType() {
    return this.type;
  }

  /**
   * Gets the activation object of this neuron
   * @return Activation object
   */
  public Activation getActivation() {
    return this.activation;
  }

  public double getActivationValue() {
    return this.activation.getActivationValue();
  }

  public void setActivationValue(double value) {
    this.activation.setActivationValue(value);
  }
}