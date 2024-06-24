# Flappy Bird in Java
Basic Flappy Bird Clone built in Java used as a base for NEAT algorithm implementation (work in progress). Currently, the game portion is mostly finished, besides some quality-of-life things along with some refactoring. The AI algorithm used is a genetic algorithm, currently in the works.

# Setup
1. Compile Java files with `javac -d bin src/**/*.java`
2. Run the App with `java -cp bin App.App`
3. Play!

# General Design
* App -> Game Window Control main point of entry
* GamePanel -> Main Game Update Loop, Rendering, Game Setup, Game Variables, etc
* Player -> Contain's score, rendering, updates, misc.
* Collision -> Contain object collision coordinates along with collision functions
* Pipe -> Holds both the top and bottom pipes within 1 pipe object, rendering, updates, misc.
* KeyHandler -> Gets user input from the keyboard
* Interface -> handles GUI menus and misc.

# TODOS
- [ ] Pausing and Pause Screen
- [ ] Fix high-score positioning
- [ ] Crossover Functions (NEAT)
- [ ] Mutation Functions (NEAT)
- [ ] Fitness Function (NEAT)
- [ ] Population Functions (NEAT)

# AI Resources
* [Efficient Evolution of Neural Network Topologies (whitepaper)](https://nn.cs.utexas.edu/downloads/papers/stanley.cec02.pdf)
* [Evolving Neural Networks through Augmenting Topologies (MIT)](https://nn.cs.utexas.edu/downloads/papers/stanley.ec02.pdf)
* [Snake learns with NEUROEVOLUTION (implementing NEAT from scratch in C++)](https://www.youtube.com/watch?v=lAjcH-hCusg&t=1242s)
* [Implementing NEAT algorithm in java](https://www.youtube.com/watch?v=1I1eG-WLLrY&list=PLVOwyy-CHLypKpXgSnm5fZjvQrxffLOwg)
