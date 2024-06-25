package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    this.neurons = new ArrayList<>();
    this.links = new ArrayList<>();
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

  /**
   * Compute the output of the neural network given the inputs
   * 
   * @param inputValues the input data for the NN
   * @return The output of the neuron
   * @throws Exception
   */
  public double computeOutput(List<Double> inputValues) throws Exception {
    if (inputValues.size() != numInputs) {

    }

    Map<Integer, Double> neuronValues = new HashMap<>();

    // Initialize input neurons with the given input values
    for (int i = 0; i < numInputs; i++) {
      Neuron neuron = neurons.get(i);
      if (neuron.getType() == Population.INPUT) {
        neuron.setActivationValue(inputValues.get(i));
        neuronValues.put(neuron.getNID(), inputValues.get(i));
      }
    }

    // Propagate through the network
    for (Link link : links) {
      if (link.getIsEnabled()) {
        int inputID = link.getInputNeuron();
        int outputID = link.getOutputNeuron();
        double weight = link.getWeight();

        double inputValue = neuronValues.getOrDefault(inputID, 0.0);
        Neuron outputNeuron = findNeuron(outputID);

        if (outputNeuron != null) {
          double activationValue = neuronValues.getOrDefault(outputNeuron.getNID(), 0.0);
          activationValue += inputValue * weight;
          neuronValues.put(outputNeuron.getNID(), activationValue);
        }
      }
    }

    // Compute activation for hidden and output neurons
    for (Neuron neuron : neurons) {
      if (neuron.getType() == Population.HIDDEN || neuron.getType() == Population.OUTPUT) {

        // Calculate the weighted sum of input activations
        double sum = 0.0;
        for (Link link : links) {
          if (link.getOutputNeuron() == neuron.getNID() && link.getIsEnabled()) {
            double inputActivation = neuronValues.getOrDefault(link.getInputNeuron(), 0.0);
            sum += link.getWeight() * inputActivation;
          }
        }
        sum += neuron.getBias();

        // Apply activation function
        double activatedValue = Activation.sigmoid(sum);
        neuron.setActivationValue(activatedValue);
        neuronValues.put(neuron.getNID(), activatedValue);
      }
    }

    // get output neuron, only one by default in our NN
    for (Neuron neuron : neurons) {
      if (neuron.getType() == Population.OUTPUT) {
        return neuronValues.get(neuron.getNID());
      }
    }

    throw new Exception("No Output Neuron Found"); // Default return if no output neuron found
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
   * 
   * @param id neuron id to be found
   * @return matching neuron or null if not found
   */
  public Neuron findNeuron(int id) {
    for (Neuron n : neurons) {
      if (n.getNID() == id)
        return n;
    }
    return null;
  }

  /**
   * Find the matching links for the given link ids
   * 
   * @param inputID  link input id to be found
   * @param outputID link output id to be found
   * @return matching link or null if not found
   */
  public Link findLink(int inputID, int outputID) {
    for (Link l : links) {
      int[] ids = l.getLinkIDs();
      if (ids[0] == inputID && ids[1] == outputID)
        return l;
    }
    return null;
  }

  /**
   * Add given Neuron to genome's Network
   * 
   * @param newNeuron
   */
  public void addNeuron(Neuron newNeuron) {
    neurons.add(newNeuron);
    numberOfNeurons++;
    nextNID++;
  }

  /**
   * Add a new neuron to genome's Network
   * 
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
