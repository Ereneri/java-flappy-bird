package AI;

import java.util.List;

public class Individual {
  private Genome genome;
  private double fitness;

  public Individual(Genome g, double fitness) {
    this.genome = g;
    this.fitness = fitness;
  }

  /**
   * Send input's into Genome and activate it
   * @param inputs List of data points for Genome
   * @return True if activation is greater than .5
   */
  public boolean compute(List<Double> inputs) {
    try {
      // get the output value and
      double res = genome.activate(inputs);
//      System.out.println("OUTPUT: " + res);
      return res > 0.5;
    } catch (Exception e) {
      return false;
    }
  }

  public void setGenome(Genome genome) {
    this.genome = genome;
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
