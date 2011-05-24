import java.util.ArrayList;

public class NeuralNet {
	private static final int LAYER_SIZE_INPUT = Face.width * Face.height;
	private static final int LAYER_SIZE_HIDDEN = 400;
	private static final int LAYER_SIZE_OUTPUT = 1;
	private static final int NUM_HIDDEN_LAYERS = 2;
	
	public class Input {
		public double value;
		public double[] weights;      
	};
	                    
	public class Sigmoid {                
		public double inputSum;
		public double output;
		public double error;
		public double[] weights;
	};
	                  
	public class Output {                
		public double inputSum;
		public double output;
		public double error;
		public double target;
		public double value;
	};
	 
	 // network layers
	 private Input[] layerInput;
	 private Sigmoid[][] layerHidden;
	 private Output[] layerOutput;
	 
	 public NeuralNet() {
		 layerInput = new Input[LAYER_SIZE_INPUT];
		 layerHidden = new Sigmoid[NUM_HIDDEN_LAYERS][LAYER_SIZE_HIDDEN];
		 layerOutput = new Output[LAYER_SIZE_OUTPUT];
	 }
	 
	 public void initialize(ArrayList<Face> trainSet) {
		// TODO initialize weights, InputLayer[i].Weights[j] = 0.01 + ((double)rand.Next(0, 5) / 100);
		 // TODO initialize output values
		 	// foreachvalue = face.type == Face.male ? 1.0 : 0.0
	 }
	 
	 private double sigmoid(double x) {
		 return (1 / (1 + Math.exp(-x)));
	 }
	 
	 private void forwardPropagate(Face face) {
		// copy input
		for(int i = 0; i < LAYER_SIZE_INPUT; i++) {
			layerInput[i].value = face.grid[i];
		}
		
		// calculate hidden layers
		for(int i = 0; i < NUM_HIDDEN_LAYERS; i++) {
			double total = 0.0;
			for(int j = 0; j < LAYER_SIZE_HIDDEN; j++) {
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
			layerOutput[i].target = layerOutput[i].value == layerOutput[i].output ? 1.0 : 0.0;
			layerOutput[i].error = (layerOutput[i].target - layerOutput[i].output) * layerOutput[i].output * (1 - layerOutput[i].output);
		}
	 }
	 
	 private void backPropagate() {
		// TODO fix errors in hidden layers
		// TODO fix errors in output layer
		// TODO fix weights in input layer
		// TODO fix weights in hidden layers
		// TODO fix weights in output layer
	 }
}
