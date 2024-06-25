package AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Activation {
  // activation curve such as sigmoid or smth
  private double activationValue;

  /**
   * Store Activation of Nodes, does not calculate on construction
   */
  public Activation() {
    this.activationValue = 0.0;
  }

  /**
   * Calculate Weighted Sum Input Nodes and Links
   */
  public void updateActivation(List<Neuron> InputNeurons, List<Link> InputLinks) {
    //TODO REVISIT THIS -> may be wrong
    assert(InputNeurons.size() == InputLinks.size());

    // store input neurons in a map for easy access -> <NeuronID, ActivationValue>
    HashMap<Integer, Double> inputMap = new HashMap<Integer, Double>();
    for (int i = 0; i < InputNeurons.size(); i++) {
      inputMap.put(InputNeurons.get(i).getNID(), InputNeurons.get(i).getActivationValue());
    }
    for (Neuron n : InputNeurons) {
      // set all ouput neurons to 0
      if (n.getType() == Population.OUTPUT) {
        n.setActivationValue(0.0);
      }
      // calculate the weighted sum of all inputs and links
      double sum = 0.0;
      for (Link l : InputLinks) {
        sum += l.getWeight() * n.getActivationValue();
      }
      sum += n.getBias();
      // set the activation value of the neuron
      n.setActivationValue(sigmoid(sum));
    }
  }

  /**
   * Return activation Value of Node 
   * @return the activiation value
   */
  public double getActivationValue() {
    return this.activationValue;
  }

  /* Activation Function -> Sigmoid */

  /**
   * Sigmoid Activation Function
   * @param x input
   * @return sigmoid of x
   */
  public static double sigmoid(double x) {
    return 1 / (1 + Math.exp(-x));
  }

  /**
   * Set Activation Value of Node
   * @param value
   */
  public void setActivationValue(double value) {
    this.activationValue = value;
  }
}
