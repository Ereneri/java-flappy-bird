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
   * Basic Link with no weight or isenabled set
   * @param inputID Neuron id of input
   * @param outputID Neuron id of output
   */
  public Link(int inputID, int outputID) {
    this.inputID = inputID;
    this.outputID = outputID;
    this.weight = 0.0;
    this.isEnabled = true;
  }

  /**
   * Updates the link's weight
   * @param newWeight
   */
  public void updateWeight(double newWeight) {
    this.weight = newWeight;
  }

  public double getWeight() {
    return this.weight;
  }

  public int getInputNeuron() {
    return this.inputID;
  }

  public int getOutputNeuron() {
    return this.outputID;
  }

  public boolean getIsEnabled() {
    return this.isEnabled;
  }

  public void enable() {
    this.isEnabled = true;
  }

  public void disable() {
    this.isEnabled = false;
  }

  /**
   * Get input and output IDs
   * @return index 0 is inputID and index 1 is outputID
   */
  public int[] getLinkIDs() {
    return new int[]{inputID, outputID};
  }

}
