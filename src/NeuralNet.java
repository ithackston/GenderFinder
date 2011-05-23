
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
		public boolean value;
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
	 
	 public void train(Face[] trainingSet) {
		 
	 }
	 
	 public boolean test(Face face) {
		 return false;
	 }
	 
	 private void forwardPropagate(Face face) {
		// copy input
		for(int i = 0; i < LAYER_SIZE_INPUT; i++) {
			layerInput[i].value = face.grid[i];
		}
		
		// TODO calculate hidden layers
		// TODO calculate output
	 }
	 
	 private void backPropagate() {
		// TODO fix errors in hidden layers
		// TODO fix errors in output layer
		// TODO fix weights in input layer
		// TODO fix weights in hidden layers
		// TODO fix weights in output layer
	 }
}
