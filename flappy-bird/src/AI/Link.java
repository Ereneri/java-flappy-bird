package AI;

public class Link {
  // Link Variables
  private int inputID;
  private int outputID;
  private double weight;
  private boolean isEnabled;

  /**
   * Neuron Link Constructor
   * @param inputID id of the input neuron
   * @param outputID id of the output neuron
   * @param weight weight of the link
   * @param isEnabled is used in NN
   */
  public Link(int inputID, int outputID, double weight, boolean isEnabled) {
    this.inputID = inputID;
    this.outputID = outputID;
    this.weight = weight;
    this.isEnabled = isEnabled;
  }

  /**
   * Updates the link's weight
   * @param newWeight
   */
  public void updateWeight(double newWeight) {
    this.weight = newWeight;
  }

  public double getWeight() {
    return weight;
  }
}
