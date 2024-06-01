package AI;

import App.GamePanel;

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

  public double run() {
    // should run a game of gamepanel
    return 0.0;
  }
}
