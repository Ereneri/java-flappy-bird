package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestGenome {

  public static void main(String[] args) {
    if (test_SimpleNN()) {
      System.out.println("✅ - TEST_SIMPLE");
    } else {
      System.out.println("❌ - TEST_SIMPLE");
    }
    System.out.println();
    if (test_ComplexNN()) {
      System.out.println("✅ - TEST_COMPLEX");
    } else {
      System.out.println("❌ - TEST_COMPLEX");
    }

  }

  private static boolean test_SimpleNN() {
    // Create neurons
    Neuron inputNeuron1 = new Neuron(0, 0.0, Population.INPUT, new Activation());
    Neuron inputNeuron2 = new Neuron(1, 0.0, Population.INPUT, new Activation());
    Neuron hiddenNeuron = new Neuron(2, 0.0, Population.HIDDEN, new Activation());
    Neuron outputNeuron = new Neuron(3, 0.0, Population.OUTPUT, new Activation());

    // Create genome and add neurons
    Genome genome = new Genome(1, 2, 1);
    genome.addNeuron(inputNeuron1);
    genome.addNeuron(inputNeuron2);
    genome.addNeuron(hiddenNeuron);
    genome.addNeuron(outputNeuron);

    // Create links and add to genome
    Link link1 = new Link(0, 2, 0.5, true);
    Link link2 = new Link(1, 2, 0.5, true);
    Link link3 = new Link(2, 3, 1.0, true);
    genome.addLinks(link1);
    genome.addLinks(link2);
    genome.addLinks(link3);

    // Input values
    List<Double> inputValues = Arrays.asList(1.0, 1.0);

    // Compute the output
    double output;
    try {
      output = genome.computeOutput(inputValues);
    } catch (Exception e) {
      return false;
    }

    // Print the output
    System.out.println("Computed Output: " + output);

    // Check the output
    double expectedOutput = 0.675;
    return Math.abs(output - expectedOutput) < 0.01;
  }

  private static boolean test_ComplexNN() {
    // Create neurons
    Neuron inputNeuron1 = new Neuron(0, 0.0, Population.INPUT, new Activation());
    Neuron inputNeuron2 = new Neuron(1, 0.0, Population.INPUT, new Activation());
    Neuron hiddenNeuron1 = new Neuron(2, 0.0, Population.HIDDEN, new Activation());
    Neuron hiddenNeuron2 = new Neuron(3, 0.0, Population.HIDDEN, new Activation());
    Neuron hiddenNeuron3 = new Neuron(4, 0.0, Population.HIDDEN, new Activation());
    Neuron hiddenNeuron4 = new Neuron(5, 0.0, Population.HIDDEN, new Activation());
    Neuron hiddenNeuron5 = new Neuron(6, 0.0, Population.HIDDEN, new Activation());
    Neuron outputNeuron = new Neuron(7, 0.0, Population.OUTPUT, new Activation());

    // Create genome and add neurons
    Genome genome = new Genome(1, 2, 1);
    genome.addNeuron(inputNeuron1);
    genome.addNeuron(inputNeuron2);
    genome.addNeuron(hiddenNeuron1);
    genome.addNeuron(hiddenNeuron2);
    genome.addNeuron(hiddenNeuron3);
    genome.addNeuron(hiddenNeuron4);
    genome.addNeuron(hiddenNeuron5);
    genome.addNeuron(outputNeuron);

    // Create links and add to genome
    // Connecting input neurons to hidden neurons
    genome.addLinks(new Link(0, 2, 0.5, true));
    genome.addLinks(new Link(1, 2, 0.5, true));
    genome.addLinks(new Link(0, 3, 0.5, true));
    genome.addLinks(new Link(1, 3, 0.5, true));
    genome.addLinks(new Link(0, 4, 0.5, true));
    genome.addLinks(new Link(1, 4, 0.5, true));
    genome.addLinks(new Link(0, 5, 0.5, true));
    genome.addLinks(new Link(1, 5, 0.5, true));
    genome.addLinks(new Link(0, 6, 0.5, true));
    genome.addLinks(new Link(1, 6, 0.5, true));
    
    // Connecting hidden neurons to output neuron
    genome.addLinks(new Link(2, 7, 1.0, true));
    genome.addLinks(new Link(3, 7, 1.0, true));
    genome.addLinks(new Link(4, 7, 1.0, true));
    genome.addLinks(new Link(5, 7, 1.0, true));
    genome.addLinks(new Link(6, 7, 1.0, true));

    // Input values
    List<Double> inputValues = Arrays.asList(1.0, 1.0);

    // Compute the output
    double output;
    try {
      output = genome.computeOutput(inputValues);
    } catch (Exception e) {
      return false;
    }

    // Print the output
    System.out.println("Computed Output: " + output);

    double expectedOutput = 0.974;
    return Math.abs(output - expectedOutput) < 0.01;
  }


}
