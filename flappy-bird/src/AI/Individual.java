package AI;

import java.util.List;

public class Individual {
  private Genome genome;
  private double fitness;

  public Individual(Genome g, double fitness) {
    this.genome = g;
    this.fitness = fitness;
  }

  public boolean compute(List<Double> inputs) {
    try {
      // get the output value and
      double res = genome.computeOutput(inputs);
      return res > 0.5;
    } catch (Exception e) {
      return false;
    }
  }

  public Genome getGenome() {
    return this.genome;
  }

  public double getFitness() {
    return this.fitness;
  }

  public void setFitness(double newFitness) {
    this.fitness = newFitness;
  }

  public double run() {
    // should run a game of gamepanel
    return 0.0;
  }
}
