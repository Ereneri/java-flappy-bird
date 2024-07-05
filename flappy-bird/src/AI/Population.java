package AI;

import java.util.*;

public class Population {
  // basic population variabkes
  private List<Individual> individuals;
  private Config cfg;
  private Mutation mutation = new Mutation();

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
    this.individuals = new ArrayList<>();
    for (int i = 0; i < c.getPopulationSize(); i++) {
      individuals.add(new Individual(newGenome()));
    }
  }

  public List<Individual> getIndividuals() {
    return individuals;
  }

  /**
   * Helper function to reproduce the individuals for new generation
   * @return new Generation of individuals
   */
  public List<Individual> reproduce() {
    // get old generation and sort by fitness
    individuals.sort(Comparator.comparingDouble(Individual::getFitness));
    for (Individual i : individuals) {
//      System.out.printf("Individual #%d - Fitness:%f - Neurons:%d - Links:%d\n", i.getGenome().getGID(), i.getFitness(), i.getGenome().getNeurons().size(), i.getGenome().getLinks().size());
      i.setFitness(0.0); // reset fitness
    }
    int cutoff = (int)(Math.ceil(cfg.getSurvivalThreshold() * individuals.size()));

    // create new generation
    List<Individual> newGeneration = new ArrayList<Individual>();
    int spawnSize = individuals.size();
    
    // create new individuals
    while (spawnSize-- >= 0) {
      Individual dominant = individuals.get((int)(Math.random() * cutoff));
      Individual recessive = individuals.get((int)(Math.random() * cutoff));
      Individual offspring = new Individual(crossover(dominant, recessive));
      // using cfg mutate rate to determine if we should mutate
      if (offspring.getGenome().getLinks().isEmpty() || offspring.getGenome().getNeurons().isEmpty()) {
        System.err.println("Invalid Offspring!");
        offspring = new Individual(dominant.getGenome());
      } else {
        if (cfg.newValue() < cfg.getMutationRate()) {
          mutation.mutate(offspring.getGenome());
        }
      }
      newGeneration.add(offspring);
    }
    // set new individuals as well as return it to our gamePanel
    this.individuals = newGeneration;
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
      g.addNeuron(new Neuron(g.getNextNID(), 0.0, OUTPUT, new Activation(0.0)));
    }
    // create links between input and output neurons
    for (Neuron in : g.getNeurons()) {
      for (Neuron out : g.getNeurons()) {
        if (in.getType() == INPUT && out.getType() == OUTPUT) {
          g.addLink(new Link(in.getNID(), out.getNID()));
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
    // if our dom neuron is an input keep it
    int type = a.getType() == INPUT ? INPUT : HIDDEN;
    return new Neuron(neuronID, bias, type, activation);
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
    // create offspring of both individuals
    Genome offspring = new Genome(highestGID++, 
                                  dominant.getGenome().getNumInputs(), 
                                  dominant.getGenome().getNumOutputs());
    // the offspring inherits the neurons
    for (Neuron n : dominant.getGenome().getNeurons()) {
      // if it's an output neuron, do not edit
      if (n.getType() == OUTPUT) {
        offspring.addNeuron(n);
      } else {
        int nid = n.getNID();
        Neuron recessiveNeuron = recessive.getGenome().findNeuron(nid);
        if (recessiveNeuron == null) offspring.addNeuron(n);
        else offspring.addNeuron(crossoverNeurons(n, recessiveNeuron));
      }
    }
    // the offspring inherits the links
    for (Link l : dominant.getGenome().getLinks()) {
      int[] ids = l.getLinkIDs();
      Link recessiveLink = recessive.getGenome().findLink(ids[0], ids[1]);
      if (recessiveLink == null) offspring.addLink(l);
      else offspring.addLink(crossoverLinks(recessiveLink, l));
    }
    return offspring;
  }

}