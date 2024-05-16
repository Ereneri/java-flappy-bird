package AI;

public class Individual {
  private Genome genome;
  private double fitness;

  public Individual(Genome g, double fitness) {
    this.genome = g;
    this.fitness = fitness;
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
}
