package AI;

import java.util.List;

public class Genome {
  // Genome Variables
  private int gid;
  private int numInputs;
  private int numOutputs;
  private List<Neuron> neurons;
  private List<Link> links;

  // Misc Variables
  private int nextNID;
  private int numberOfNeurons;
  private int numberOfLinks;
  private int numberOfHidden;

  public Genome(int gid, int numInputs, int numOutputs) {
    // set vars
    this.gid = gid;
    this.numInputs = numInputs;
    this.numOutputs = numOutputs; 
    Activation activation = new Activation();
    this.nextNID = 0;
    this.numberOfNeurons = 0;
    this.numberOfLinks = 0;
    this.numberOfHidden = 0;
  }

  public Genome(Genome g) {
    this.gid = g.getGID();
    this.numInputs = g.getNumInputs();
    this.numOutputs = g.getNumOutputs();
    this.neurons = g.getNeurons();
    this.links = g.getLinks();
    this.nextNID = 0;
    this.numberOfNeurons = 0;
    this.numberOfLinks = 0;
    this.numberOfHidden = 0;
  }

  public int getGID() {
    return this.gid;
  }

  public int getNumberOfHidden() {
    return this.numberOfHidden;
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

  public void removeNeuron(Neuron removedNeuron) {
    neurons.remove(removedNeuron);
    numberOfNeurons--;
  }

  public List<Link> getLinks() {
    return this.links;
  }

  public int getNextNID() {
    return nextNID;
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

  /**
   * Add given Neuron to genome's Network
   * @param newNeuron
   */
  public void addNeuron(Neuron newNeuron) {
    neurons.add(newNeuron);
    numberOfNeurons++;
    nextNID++;
  }

  /**
   * Add a new neuron to genome's Network
   * @apiNote bias is 1.0 by default and base activiation
   */
  public void addNeuron() {
    neurons.add(new Neuron(nextNID++, 1.0, null));
    numberOfNeurons++;
    nextNID++;
  }

  public void addLinks(Link newLink) {
    links.add(newLink);
    numberOfLinks++;
  }

  public void removeLink(Link removedLink) {
    links.remove(removedLink);
    numberOfLinks--;
  }

  public void removeLink(int index) {
    links.remove(index);
    numberOfLinks--;
  }

  public int getNumberOfNeurons() {
    return numberOfNeurons;
  }

  public int getNumberOfLinks() {
    return numberOfLinks;
  }
}
