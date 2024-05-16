package AI;

public class Neuron {
  // Neuron Variables
  private int nid;
  private double bias;
  private TYPE type;
  private Activation activation;
  
  // enum of all available types
  public enum TYPE {
    INPUT,
    OUTPUT,
    HIDDEN;
  }

  public Neuron(int nid, double bias, Activation activation) {
    this.nid = nid;
    this.bias = bias;
    // this.type = type;
    this.activation = activation;
  }

  public double getBias() {
    return this.bias;
  }

  public int getNID() {
    return this.nid;
  }

  public TYPE getType() {
    return this.type;
  }

  public Activation geActivation() {
    return this.activation;
  }
}
