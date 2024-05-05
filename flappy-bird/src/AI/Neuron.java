package AI;

public class Neuron {
  // Neuron Variables
  private int nid;
  private double bias;
  private TYPE type;
  
  // enum of all available types
  enum TYPE {
    INPUT,
    OUTPUT,
    HIDDEN;
  }

  public Neuron(int nid, double bias, TYPE type) {
    this.nid = nid;
    this.bias = bias;
    this.type = type;
  }
}
