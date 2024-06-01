package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Mutation {


  /* Mutation functions */
  
  /**
   * Mutates the given genome
   * @param g Genome to mutate
   */
  public void mutate(Genome g) {
    // randomly select a mutation to perform
    double mutation = Math.random();
    if (mutation < 0.25) {
      mutateAddLink(g);
    } else if (mutation < 0.5) {
      mutateRemoveLink(g);
    } else if (mutation < 0.75) {
      mutateNewHiddenNeuron(g);
    } else {
      mutateRemoveHiddenNeuron(g);
    }
  }

  /**
   * Adds a new Link between two random Neurons
   * @apiNote Very costly as we need to check for cycles before adding link
   * @param g Genome to mutate
   */
  private void mutateAddLink(Genome g) {
    int inputID = randomNeuron(g).getNID();
    int outputID = randomNeuron(g).getNID();
    // don't dupe links
    Link l = g.findLink(inputID, outputID);
    if (l != null) {
      l.enable();
    }
    // check if it would create cycle
    if (wouldCreateCycle(g, inputID, outputID)) return;
    // if no cycle is created we can add it
    Link newLink = new Link(inputID, outputID);
    g.addLinks(newLink);
  }

  /**
   * Removes the a random Link
   * @param g Genome to mutate
   */
  private void mutateRemoveLink(Genome g) {
    // cant remove links that do not exist
    int size = g.getLinks().size();
    if (size == 0) {
      return;
    }
    // randomly selects one of the links to be removed
    g.removeLink((int)(Math.random() * size));
  }

  /**
   * Pick a Random Link to be split so a neuron can be added between them
   * @param g Genome to mutate
   */
  private void mutateNewHiddenNeuron(Genome g) {
    // can't split links if they do not exist
    int size = g.getLinks().size();
    if (size == 0) return;

    // get a random link and disable it
    Link linkToSplit = randomLink(g);
    linkToSplit.disable();

    // create new neuron for links to attach to
    Neuron newNeuron = new Neuron(g.getNextNID());
    g.addNeuron(newNeuron);

    int in = linkToSplit.getInputNeuron();
    int out = linkToSplit.getOutputNeuron();
    double weight = linkToSplit.getWeight();
    // create new links
    g.addLinks(new Link(in, newNeuron.getNID(), 1.0, true));
    g.addLinks(new Link(newNeuron.getNID(), out, weight, true));
  }

  private void mutateRemoveHiddenNeuron(Genome g) {
    if (g.getNeurons().size() == 0) {
      return;
    }
    // get a random neuron
    Neuron n = randomNeuron(g);
    // delete the associated links
    for (Link l : g.getLinks()) {
      if (l.getInputNeuron() == n.getNID() || l.getOutputNeuron() == n.getNID()) {
        g.removeLink(l);
      }
    }
    // remove the neuron
    g.removeNeuron(n);
  }


  /* Cycle Helpers */

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

  /* RANDOM HELPERS */

  /**
   * Selects a random neuron from Genome
   * @param g Genome to select from
   * @return Random Neuron
   */
  private Neuron randomNeuron(Genome g) {
    int randomIDX = (int)(Math.random() * g.getNeurons().size());
    return g.findNeuron(randomIDX);
  }

  /**
   * Selects a random link from Genome
   * @param g Genome to select from
   * @return Random Neuron
   */
  private Link randomLink(Genome g) {
    int randomIDX = (int)(Math.random() * g.getLinks().size());
    return g.getLinks().get(randomIDX);
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

}