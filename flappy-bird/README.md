## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## NEAT Setup
- `Individual`: contains the genome, or neural network, along with the fitness
- `Genome`: contains list of the neurons and links that connect the neurons
- `Neuron`: Contains a bias, type (0-input, 1-output, 3-hidden), and activation
- `Link`: contains the two IDs of the input and output of link, along with a weight and boolean for if it's enabled
- `Activation`: contains the activation value and has other helper functions for updating the activation value and sigmoid
- `Mutation`: contains the all the mutations functions that will alter the Genome
- `Population`: contains overall individual and NEAT setup code
- `Config`: contains the NEAT variables needed for the *Population* setup

### NEAT Logic
The population contains all the individuals along with the helper functions for some NEAT operations. The individuals are created during startup of the population, the genome is initalized with just the input neurons mapped directly to the output neurons. In the case of the flappy bird game we have just 1 output. This is then rounded into a boolean value of true or false for whether the bird should jump or not. Additionally the individual has a fitness value. The fitness will be how long the bird survives. Once all the individuals are dead, the top preformers are used to repopulate the population using their genome. This is all handled in the *Population* code along with the *Mutation* code.

The core of the logic is based around the neural network within the NEAT algorithm. The inputs given to the genome which passes the inputs into the

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).
