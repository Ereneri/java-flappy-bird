package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Population {
  // basic population variabkes
  private List<Individual> individuals;

  // agent variables
  private int numInputs;
  private int numOutputs;

  // Genome variables
  private int highestGID;

  public Population(Config c) {
    this.numInputs = c.getNumInputs();
    this.numOutputs = c.getNumOutputs();
    for (int i = 0; i < c.getPopulationSize(); i++) {
      individuals.add(new Individual(newGenome(), 0.0));
    }
  }

  //* Misc Helper */
  private Genome newGenome() {
    Genome g = new Genome(highestGID++, numInputs, numOutputs);
    // TODO fix new Neuron setup 
    for (int neuronID = 0; neuronID < numOutputs; neuronID++) {
      g.addNeuron(new Neuron(neuronID, 0.0, null));
    }
    // Connect all the neurons with links
    for (int i = 0; i < numInputs; i++) {
      int inputID = -i - 1;
      for (int outputID = 0; outputID < numOutputs; outputID++) {
        g.addLinks(new Link(inputID, outputID));
      }
    }
    return g;
  }

  private Neuron randomNeuron(Genome g) {
    int randomIDX = (int)(Math.random() * g.getNeurons().size());
    return g.findNeuron(randomIDX);
  }

  /**
   * Helper Function to check if adding a new link would cause a cycle
   * @param g Genome graph to be check
   * @param inputID Input Id for link to be added
   * @param outputID output id for link to be added
   * @return Boolean value whether this new link would cause a cycle
   */
  private boolean wouldCreateCycle(Genome g, int inputID, int outputID) {
    // create map of all the nodes and their children
    HashMap<Integer, List<Integer>> childMap = new HashMap<Integer, List<Integer>>();
    for (Link l : g.getLinks()) {
      addLinkToMap(childMap, l);
    }
    addLinkToMap(childMap, new Link(inputID, outputID));

    // Bulk of DFS cycle check for directed graph -> https://www.geeksforgeeks.org/detect-cycle-in-a-graph/
    int length = g.getNeurons().size();
    // store both on visited nodes and recursively visited nodes
    boolean[] visited = new boolean[length];
    boolean[] recStack = new boolean[length];

    // loop over all neurons checking for cycles
    for (Neuron n : g.getNeurons()) {
      if (isCycle(n.getNID(), visited, recStack, childMap)) return true;
    }
    return false;
  }

  /**
   * Helper Function to add a Link to a given hashmap, will create list if map doesn't already have one
   * @param childMap Hashmap to have link added to
   * @param l Link to be inserted into the hashmap
   */
  private void addLinkToMap(HashMap<Integer, List<Integer>> childMap, Link l) {
    int in = l.getInputNeuron();
    int out = l.getOutputNeuron();
    // get or create list and add out to it, update map
    List<Integer> tmp = childMap.getOrDefault(in, new ArrayList<Integer>());
    tmp.add(out);
    childMap.put(in, tmp);
  }

  /**
   * Helper function to check if there is a cycle within the hashmap
   * @param i neuron id
   * @param visited boolean array of if that neuron has been completely visited
   * @param recStack boolean array of if a neuron has been visited within the recursion stack
   * @param map mapping of all the neurons from in to out
   * @return if there is a cycle
   */
  private boolean isCycle(int i, boolean[] visited, boolean[] recStack, HashMap<Integer, List<Integer>> map) {
    // Checks if we have seen the same node already in the rec stack or have marked it
    if (recStack[i]) return true;
    if (visited[i]) return false;

    visited[i] = true;
    recStack[i] = true;

    // loop over all children of the current node
    for (Integer output : map.get(i)) {
      // check neurons child recurively
      if (isCycle(output, visited, recStack, map)) return true;
    }
    // if we haven't returned yet means there wasn't a cycle for this neuron, thus reset recStack to false
    recStack[i] = false;
    return false;
  }

  //* Runner Code */

  // TODO -> tbh dunno what this needs to do yet
  public Individual run() {
    return null;
  }

  //* Crossover Logic */

  public Neuron crossoverNeurons(Neuron a, Neuron b) {
    assert(a.getNID() == b.getNID());
    int neuronID = a.getNID();
    // randomly pick one of the bias and activations
    double bias = (Math.random() % 1) == 0 ? a.getBias() : b.getBias();
    Activation activation = (Math.random() % 1) == 0 ? a.geActivation() : b.geActivation();
    return new Neuron(neuronID, bias, activation);
  }

  public Link crossoverLinks(Link a, Link b) {
    assert(a.getInputNeuron() == b.getInputNeuron() && a.getOutputNeuron() == b.getOutputNeuron());
    int inputID = a.getInputNeuron();
    int outputID = a.getOutputNeuron();
    // randomly pick one of the bias and activations
    double weight = (Math.random() % 1) == 0 ? a.getWeight() : b.getWeight();
    boolean isEnabled = (Math.random() % 1) == 0 ? a.getIsEnabled() : b.getIsEnabled();
    return new Link(inputID, outputID, weight, isEnabled);
  }

  public Genome crossover(Individual dominant, Individual recessive) {
    // create offspring of both indivudals
    Genome offspring = new Genome(highestGID++, 
                                  dominant.getGenome().getNumInputs(), 
                                  dominant.getGenome().getNumOutputs());
    // the offspring inherits the neurons
    for (Neuron n : dominant.getGenome().getNeurons()) {
      int nid = n.getNID();
      Neuron recessiveNeuron = recessive.getGenome().findNeuron(nid);
      if (recessiveNeuron == null) offspring.addNeuron(n);
      else offspring.addNeuron(crossoverNeurons(n, recessiveNeuron));
    }
    // the offspring inherits the links
    for (Link l : dominant.getGenome().getLinks()) {
      int[] ids = l.getLinkIDs();
      Link recessiveLink = recessive.getGenome().findLink(ids[0], ids[1]);
      if (recessiveLink == null) offspring.addLinks(l);
      else offspring.addLinks(crossoverLinks(recessiveLink, l));
    }
    return offspring;
  }

  //* Mutation Logic */

  public void mutateAddLink(Genome g) {
    int inputID = randomNeuron(g).getNID();
    int outputID = randomNeuron(g).getNID();

    // don't dupe links
    Link l = g.findLink(inputID, outputID);
    if (l != null) {
      l.enable();
    }

    // check if it would create cycle
    if (wouldCreateCycle(g, inputID, outputID)) return;

    Link newLink = new Link(inputID, outputID);
    g.addLinks(newLink);
  }

  public void mutateRemoveLink(Genome g) {
    // TOOD
  }

  public void mutateNewHiddenLink(Genome g) {
    // TOOD
  }

  public void mutateRemoveHiddenLink(Genome g) {
    // TOOD
  }
}