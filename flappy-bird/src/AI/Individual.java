package AI;

import java.util.List;

public class Individual {
  private Genome genome;
  private double fitness;

  public Individual(Genome g) {
    this.genome = g;
    this.fitness = 0.0;
  }

  /**
   * Send input's into Genome and activate it
   * @param inputs List of data points for Genome
   * @return True if activation is greater than .5
   */
  public boolean compute(List<Double> inputs) {
    try {
      // get the output value and
      double res = Activation.sigmoid(genome.activate(inputs));
      return res > 0.5;
    } catch (Exception e) {
      System.err.println("Failed to Compute Activation " + e);
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

  public void updateFitness(double amount) {
    this.fitness += amount;
  }

  @Override
  /**
   * Override toString to return the Individuals Genome ID
   */
  public String toString() {
    return "Individual-" + this.getGenome().getGID();
  }
}
