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
   * @return 0-Input, 1-Output, 3-Hidden (ref Population)
   */
  public int getType() {
    return this.type; // TODO should honestly be a enum
  }

  public String getTypeName() {
    if (type == Population.HIDDEN) return "Hidden";
    if (type == Population.OUTPUT) return "Output";
    if (type == Population.INPUT) return "Input";
    return "Invalid Type";
  }

  public Activation getActivation() {
    return this.activation;
  }

  public double getActivationValue() {
    return this.activation.getActivationValue();
  }

  public void setActivationValue(double value) {
    this.activation.setActivationValue(value);
  }

  @Override
  /**
   * Override toString to return the Neuron's ID
   */
  public String toString() {
    return "Neuron-" + this.nid;
  }
}