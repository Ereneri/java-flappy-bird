package AI;

import java.util.Random;

public class Config {
  // Individual Variables
  private int numInputs;
  private int numOutputs;
  private int populationSize;

  // Mutation Values
  private double initMean = 0.0;
  private double initSD = 1.0;
  private double min = -20.0;
  private double max = 20.0;
  private double mutationRate = 0.2;
  private double mutatePower = 1.2;
  private double replaceRate = 0.05;

  // Misc.
  private Random rnd;

  // Setup NEAT config here
  public Config(int numInputs, int numOutputs, int populationSize) {
    this.numInputs = numInputs;
    this.numOutputs = numOutputs;
    this.populationSize = populationSize;
    this.rnd = new Random();
  }

  /**
   * Gets new value based on standard mean of 0.0 and SD of 1.0, min and max of +/- 20
   * @return new value based on gaussion dist.
   */
  public double newValue() {
    // randomnly select a value from gaussion distribution based on mutation values
    return clamp(rnd.nextGaussian(initMean, initSD));
  }

  public double mutateDelta(double value) {
    double delta = clamp(rnd.nextGaussian(0.0, mutatePower));
    return clamp(value + delta);
  }

  /**
   * Cap our values based on config max and min
   * @param val value to be clamped
   * @return Value capped between min and max, or itself
   */
  private double clamp(double val) {
    return Math.min(max, Math.max(min, val));
  }

  public int getNumInputs() {
    return this.numInputs;
  }

  public int getNumOutputs() {
    return this.numOutputs;
  }

  public int getPopulationSize() {
    return this.populationSize;
  }
}
