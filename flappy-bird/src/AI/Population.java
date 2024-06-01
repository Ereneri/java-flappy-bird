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
  private Config cfg;
  private Mutation mutation;

  // agent variables
  private int numInputs;
  private int numOutputs;

  // Genome variables
  private int highestGID;

  // Types of Neurons 
  public final static int INPUT = 0;
  public final static int OUTPUT = 1;
  public final static int HIDDEN = 3;

  public Population(Config c) {
    this.cfg = c;
    this.numInputs = c.getNumInputs();
    this.numOutputs = c.getNumOutputs();
    for (int i = 0; i < c.getPopulationSize(); i++) {
      individuals.add(new Individual(newGenome(), 0.0));
    }
  }

  //* Runner Code */

  public void run(int generation) {
    for (int i = 0; i < generation; i++) {
      // run the simulation
      for (Individual ind : this.individuals) {
        ind.setFitness(ind.run()); // boots us back to GamePanel.java
      }
      // reproduce the individuals
      this.individuals = reproduce();
    }
  }

  /**
   * Helper function to reproduce the individuals for new generation
   * @return new Generation of individuals
   */
  private List<Individual> reproduce() {
    // get old generation and sort by fitness
    individuals.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));
    int cutoff = (int)(Math.ceil(cfg.getSurvivalThreshold() * individuals.size()));

    // create new generation
    List<Individual> newGeneration = new ArrayList<Individual>();
    int spawnSize = individuals.size();
    
    // create new individuals
    while (spawnSize-- >= 0) {
      Individual dominant = individuals.get((int)(Math.random() * cutoff));
      Individual recessive = individuals.get((int)(Math.random() * cutoff));
      Individual offspring = new Individual(crossover(dominant, recessive), 0.0);
      // using cfg mutate rate to determine if we should mutate
      if (cfg.newValue() < cfg.getMutationRate()) {
        mutation.mutate(dominant.getGenome());
      }
      newGeneration.add(offspring);
    }
    return newGeneration;
  }

  /**
   * Helper function to create a new Genome
   * @return New Genome with input and output neurons
   */
  private Genome newGenome() {
    Genome g = new Genome(highestGID++, numInputs, numOutputs);
    // create input neurons -> may not need to create new neurons?
    for (int i = 0; i < numInputs; i++) {
      g.addNeuron(new Neuron(g.getNextNID(), 0.0, INPUT, new Activation()));
    }
    // create output neurons
    for (int i = 0; i < numOutputs; i++) {
      g.addNeuron(new Neuron(g.getNextNID(), 0.0, OUTPUT, new Activation()));
    }
    // create links between input and output neurons
    for (Neuron in : g.getNeurons()) {
      for (Neuron out : g.getNeurons()) {
        if (in.getType() == INPUT && out.getType() == OUTPUT) {
          g.addLinks(new Link(in.getNID(), out.getNID()));
        }
      }
    }
    return g;
  }

  //* Crossover Logic */

  /**
   * Helper Function for crossover of Neurons
   * @return Crossed over Neuron -> randomly picks bias and activation
   */
  private Neuron crossoverNeurons(Neuron a, Neuron b) {
    assert(a.getNID() == b.getNID());
    int neuronID = a.getNID();
    // randomly pick one of the bias and activations
    double bias = (Math.random() % 1) == 0 ? a.getBias() : b.getBias();
    Activation activation = (Math.random() % 1) == 0 ? a.getActivation() : b.getActivation();
    return new Neuron(neuronID, bias, activation);
  }

  /**
   * Helper Function for crossover of Links
   * @return Crossed over Link -> randomly picks weight and isEnabled
   */
  private Link crossoverLinks(Link a, Link b) {
    assert(a.getInputNeuron() == b.getInputNeuron() && a.getOutputNeuron() == b.getOutputNeuron());
    int inputID = a.getInputNeuron();
    int outputID = a.getOutputNeuron();
    // randomly pick one of the bias and activations
    double weight = (Math.random() % 1) == 0 ? a.getWeight() : b.getWeight();
    boolean isEnabled = (Math.random() % 1) == 0 ? a.getIsEnabled() : b.getIsEnabled();
    return new Link(inputID, outputID, weight, isEnabled);
  }

  /**
   * Crossover of two individuals to create a new offspring
   * @param dominant Individual with higher fitness
   * @param recessive Individual with lower fitness
   * @return Offspring of the two individuals
   */
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
}