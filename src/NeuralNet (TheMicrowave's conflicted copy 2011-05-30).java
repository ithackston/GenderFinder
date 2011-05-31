import java.util.Random;

/**
 * Reads and stores the valid face files of the given directory name.
 * @param dirName
 * @throws IOException
 */
public class NeuralNet {
	// Warning: modifying some of these values may cause errors in the code.
	private static final int LAYER_SIZE_INPUT = Face.width * Face.height;
	private static final int LAYER_SIZE_HIDDEN = 40;
	private static final int LAYER_SIZE_OUTPUT = 2;
	private static final int NUM_HIDDEN_LAYERS = 2;
	private static final double LEARNING_RATE = 0.2;
	
	/**
	 * Input node
	 */
	public class Input {
		public double value;
		public double[] weights;      
	};
	  
	/**
	 * Perceptron node
	 */
	public class Sigmoid {                
		public double inputSum;
		public double output;
		public double error;
		public double[] weights;
	};
	
	/**
	 * Output node
	 */          
	public class Output {                
		public double inputSum;
		public double output;
		public double error;
		public double target;
		public Face.Facetype value;
	};
	
	// network layers
	private Input[] layerInput;
	private Sigmoid[][] layerHidden;
	private Output[] layerOutput;
	
	/**
	 * Constructor. Initializes layer arrays.
	 */
	public NeuralNet() {
		layerInput = new Input[LAYER_SIZE_INPUT];
		layerHidden = new Sigmoid[NUM_HIDDEN_LAYERS][LAYER_SIZE_HIDDEN];
		layerOutput = new Output[LAYER_SIZE_OUTPUT];
	}
	
	/**
	 * Initialize nodes, weights, and output values.
	 */
	public void initialize() {
		Random random = new Random();
		
		// initialize input layer weights to a random number x | 0 < x < 8.1
		for(int i = 0; i < LAYER_SIZE_INPUT; i++) {
			layerInput[i] = new Input();
			layerInput[i].weights = new double[LAYER_SIZE_HIDDEN];
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				layerInput[i].weights[j] = 0.01 + random.nextDouble() * 8;
			}
		}
		
		// initialize hidden layer weights to a random number x | 0 < x < 8.1
		for(int i = 0; i < NUM_HIDDEN_LAYERS; i++) {
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				layerHidden[i][j] = new Sigmoid();
				if(i != NUM_HIDDEN_LAYERS - 1) {
					layerHidden[i][j].weights = new double[LAYER_SIZE_HIDDEN];
					for(int k = 0; k < LAYER_SIZE_HIDDEN; k++) {
						layerHidden[i][j].weights[k] = 0.01 + random.nextDouble() * 8;
					}
				} else {
					// last hidden layer connected to output layer
					layerHidden[i][j].weights = new double[LAYER_SIZE_OUTPUT];
					for(int k = 0; k < LAYER_SIZE_OUTPUT; k++) {
						layerHidden[i][j].weights[k] = 0.01 + random.nextDouble() * 8;
					}
				}
			}
		}
		
		// initialize possible output values
		layerOutput[0] = new Output();
		layerOutput[0].value = Face.Facetype.male;
		layerOutput[1] = new Output();
		layerOutput[1].value = Face.Facetype.female;
	}
	
	/**
	 * Sigmoid function.
	 * @param x
	 */
	private double sigmoid(double x) {
		return (1 / (1 + Math.exp(-x)));
	}
	
	/**
	 * Forward propagate a Face input through the network.
	 * @param face
	 */
	public void forwardPropagate(Face face) {
		// copy input
		for(int i = 0; i < LAYER_SIZE_INPUT; i++) {
			layerInput[i].value = face.grid[i];
		}
		
		// calculate hidden layers
		for(int i = 0; i < NUM_HIDDEN_LAYERS; i++) {
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				double total = 0.0;
				if(i > 0) {
					for(int k = 0; k < LAYER_SIZE_HIDDEN; k++) {
						total += layerHidden[i-1][k].output * layerHidden[i-1][k].weights[j];
					}
				} else {
					// first hidden layer; calculate from input layer
					for(int k = 0; k < LAYER_SIZE_INPUT; k++) {
						total += layerInput[k].value * layerInput[k].weights[j];
					}
				}
				layerHidden[i][j].inputSum = total;
				layerHidden[i][j].output = sigmoid(total);
			}
		}
		
		// calculate output
		for(int i = 0; i < LAYER_SIZE_OUTPUT; i++) {
			double total = 0.0;
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				total += layerHidden[NUM_HIDDEN_LAYERS - 1][j].output * layerHidden[NUM_HIDDEN_LAYERS-1][j].weights[i];
			}
			layerOutput[i].inputSum = total;
			layerOutput[i].output = sigmoid(total);
			layerOutput[i].target = layerOutput[i].value == face.type ? 1.0 : 0.0;
			layerOutput[i].error = (layerOutput[i].target - layerOutput[i].output) * layerOutput[i].output * (1 - layerOutput[i].output);
		}
	}
	
	/**
	 * Back propagate errors through the network to adjust weights.
	 */
	public void backPropagate() {
		// fix errors in hidden layers
		for(int i = 0; i < NUM_HIDDEN_LAYERS; i++) {
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				double total = 0.0;
				if(i != NUM_HIDDEN_LAYERS - 1) {
					for(int k = 0; k < LAYER_SIZE_HIDDEN; k++) {
						total += layerHidden[i][j].weights[k] * layerHidden[i+1][k].error;
					}
				} else {
					// last hidden layer; calculate from output layer
					for(int k = 0; k < LAYER_SIZE_OUTPUT; k++) {
						total += layerHidden[i][j].weights[k] * layerOutput[k].error;
					}
				}
				layerHidden[i][j].error = total;
			}
		}
		
		// fix weights in input layer
		for(int i = 0; i < LAYER_SIZE_INPUT; i++) {
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				layerInput[i].weights[j] += LEARNING_RATE * layerHidden[0][j].error * layerInput[i].value;
			}
		}
		
		// fix weights in hidden layers
		for(int i = 0; i < NUM_HIDDEN_LAYERS; i++) {
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				if(i != NUM_HIDDEN_LAYERS - 1) {
					for(int k = 0; k < LAYER_SIZE_HIDDEN; k++) {
						layerHidden[i][j].weights[k] = LEARNING_RATE * layerHidden[i+1][k].error * layerHidden[i][j].output;
					}
				} else {
					// last hidden layer; calculate from output layer
					for(int k = 0; k < LAYER_SIZE_OUTPUT; k++) {
						layerHidden[i][j].weights[k] = LEARNING_RATE * layerOutput[k].error * layerHidden[i][j].output;
					}
				}
			}
		}
	}
	
	/**
	 * Forward propagate a test Face and return the Facetype (gender).
	 */
	public Face.Facetype test(Face face) {
		// copy input
		for(int i = 0; i < LAYER_SIZE_INPUT; i++) {
			layerInput[i].value = face.grid[i];
		}
		
		// calculate hidden layers
		for(int i = 0; i < NUM_HIDDEN_LAYERS; i++) {
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				double total = 0.0;
				if(i > 0) {
					for(int k = 0; k < LAYER_SIZE_HIDDEN; k++) {
						total += layerHidden[i-1][k].output * layerHidden[i-1][k].weights[j];
					}
				} else {
					// first hidden layer; calculate from input layer
					for(int k = 0; k < LAYER_SIZE_INPUT; k++) {
						total += layerInput[k].value * layerInput[k].weights[j];
					}
				}
				layerHidden[i][j].inputSum = total;
				layerHidden[i][j].output = sigmoid(total);
			}
		}
		
		// calculate output
		double max = -1;
		Face.Facetype output = Face.Facetype.test;
		
		for(int i = 0; i < LAYER_SIZE_OUTPUT; i++) {
			double total = 0.0;
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
				total += layerHidden[NUM_HIDDEN_LAYERS - 1][j].output * layerHidden[NUM_HIDDEN_LAYERS-1][j].weights[i];
			}
			layerOutput[i].inputSum = total;
			layerOutput[i].output = sigmoid(total);
			if(layerOutput[i].output > max) {
				max = layerOutput[i].output;
				output = layerOutput[i].value;
			}
		}
		
		return output;
	}
	
	public double getError() {
		double total = 0.0;
		
		for(int i = 0; i < LAYER_SIZE_OUTPUT; i++) {
			total += Math.pow((layerOutput[i].target - layerOutput[i].output), 2) / 2;
		}
		
		return total;
	}
}
