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

  public Genome(int gid, int numInputs, int numOutputs) {
    // set vars
    this.gid = gid;
    this.numInputs = numInputs;
    this.numOutputs = numOutputs;
    this.nextNID = 0;
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
  }

  /**
   * Compute the output of the neural network given the inputs
   *
   * @param inputValues the input data for the NN
   * @return The output of the neuron
   * @throws Exception
   */
  public double activate(List<Double> inputValues) throws Exception {
    assert(inputValues.size() == numInputs);

    Map<Integer, Double> neuronValues = new HashMap<>();

    // Initialize input neurons with the given input values
    for (int i = 0; i < numInputs; i++) {
      Neuron neuron = neurons.get(i);
      if (neuron.getType() == Population.INPUT) {
        double input = inputValues.get(i);
        neuron.setActivationValue(input);
        neuronValues.put(neuron.getNID(), input);
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

  /**
   * DEBUG FUNCTION - Returns graphical representation of the Genome
   */
  public void printGenome() {
    for (Neuron neuron : neurons) {
      System.out.println("Neuron #" + neuron.getNID() + " (" + neuron.getTypeName() + ") @ [" + neuron.getActivationValue() + "]");
      for (Link link : links) {
        if (link.getInputNeuron() == neuron.getNID()) {
          if (!link.getIsEnabled()) System.out.print("DISABLED-- ");
          System.out.println("Link: NID" + link.getInputNeuron() + " =[" + link.getWeight() + "=> NID" + link.getOutputNeuron());
        }
      }
    }
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
   * Get Next Neuron ID that would be used
   *
   * @return int of next Neuron ID
   */
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
   * Remove given Neuron from genome's Network
   *
   * @param removedNeuron Neuron to be removed
   */
  public void removeNeuron(Neuron removedNeuron) {
    neurons.remove(removedNeuron);
  }

  /**
   * Add given Neuron to genome's Network
   *
   * @param newNeuron to be added
   */
  public void addNeuron(Neuron newNeuron) {
    neurons.add(newNeuron);
    nextNID++; // means every NID is unique
  }

  /**
   * Add given Link to genome's Network
   *
   * @param newLink Link to be added
   */
  public void addLink(Link newLink) {
    links.add(newLink);
  }

  /**
   * Remove Link from genome's Network
   *
   * @param index Index of link to be removed
   */
  public void removeLink(int index) {
    links.remove(index);
  }

}
