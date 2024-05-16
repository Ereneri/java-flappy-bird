package AI;

import java.util.List;

public class Genome {
  // Genome Variables
  private int gid;
  private int numInputs;
  private int numOutputs;
  private List<Neuron> neurons;
  private List<Link> links;

  public Genome(int gid, int numInputs, int numOutputs) {
    // set vars
    this.gid = gid;
    this.numInputs = numInputs;
    this.numOutputs = numOutputs; 
    Activation activation = new Activation();
  }

  public Genome(Genome g) {
    this.gid = g.getGID();
    this.numInputs = g.getNumInputs();
    this.numOutputs = g.getNumOutputs();
    this.neurons = g.getNeurons();
    this.links = g.getLinks();
  }

  public int getGID() {
    return this.gid;
  }

  public int getNumInputs() {
    return this.numInputs;
  }

  public int getNumOutputs() {
    return this.numOutputs;
  }

  public List<Neuron> getNeurons() {
    return this.neurons;
  }

  public List<Link> getLinks() {
    return this.links;
  }

  /**
  * Finds the matching neuron for the given neuron id
  * @param id neuron id to be found
  * @return matching neuron or null if not found 
   */
  public Neuron findNeuron(int id) {
    for (Neuron n : neurons) {
      if (n.getNID() == id) return n;
    }
    return null;
  }

  /**
   * Find the matching links for the given link ids
   * @param inputID link input id to be found
   * @param outputID link output id to be found
   * @return matching link or null if not found
   */
  public Link findLink(int inputID, int outputID) {
    for (Link l : links) {
      int[] ids = l.getLinkIDs();
      if (ids[0] == inputID && ids[1] == outputID) return l;
    }
    return null;
  }

  public void addNeuron(Neuron newNeuron) {
    neurons.add(newNeuron);
    // TODO may need to update num of inputs and outputs
  }

  public void addLinks(Link newLink) {
    links.add(newLink);
  }
}
