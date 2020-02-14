import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Random;

/**
 * The main class that handles the entire network
 * Has multiple attributes each with its own use
 */

public class NNImpl {
    private ArrayList<Node> inputNodes; //list of the output layer nodes.
    private ArrayList<Node> hiddenNodes;    //list of the hidden layer nodes
    private ArrayList<Node> outputNodes;    // list of the output layer nodes
    private Double[][] hiddenWeights;
    private Double[][] outputWeights;

    private ArrayList<Instance> trainingSet;    //the training set

    private double learningRate;    // variable to store the learning rate
    private int maxEpoch;   // variable to store the maximum number of epochs
    private Random random;  // random number generator to shuffle the training set

    /**
     * This constructor creates the nodes necessary for the neural network
     * Also connects the nodes of different layers
     * After calling the constructor the last node of both inputNodes and
     * hiddenNodes will be bias nodes.
     */

    NNImpl(ArrayList<Instance> trainingSet, int hiddenNodeCount, Double learningRate, int maxEpoch, Random random, Double[][] hiddenWeights, Double[][] outputWeights) {
        this.trainingSet = trainingSet;
        this.learningRate = learningRate;
        this.hiddenWeights = hiddenWeights;
        this.outputWeights = outputWeights;
        this.maxEpoch = maxEpoch;
        this.random = random;

        //input layer nodes
        inputNodes = new ArrayList<>();
        int inputNodeCount = trainingSet.get(0).attributes.size();
        int outputNodeCount = trainingSet.get(0).classValues.size();
        for (int i = 0; i < inputNodeCount; i++) {
            Node node = new Node(0);
            inputNodes.add(node);
        }

        //bias node from input layer to hidden
        Node biasToHidden = new Node(1);
        inputNodes.add(biasToHidden);

        //hidden layer nodes
        hiddenNodes = new ArrayList<>();
        for (int i = 0; i < hiddenNodeCount; i++) {
            Node node = new Node(2);
            //Connecting hidden layer nodes with input layer nodes
            for (int j = 0; j < inputNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(inputNodes.get(j), hiddenWeights[i][j]);
                node.parents.add(nwp);
            }
            hiddenNodes.add(node);
        }

        //bias node from hidden layer to output
        Node biasToOutput = new Node(3);
        hiddenNodes.add(biasToOutput);

        //Output node layer
        outputNodes = new ArrayList<>();
        for (int i = 0; i < outputNodeCount; i++) {
            Node node = new Node(4);
            //Connecting output layer nodes with hidden layer nodes
            for (int j = 0; j < hiddenNodes.size(); j++) {
                NodeWeightPair nwp = new NodeWeightPair(hiddenNodes.get(j), outputWeights[i][j]);
                node.parents.add(nwp);
            }
            outputNodes.add(node);
        }
    }

    /**
     * Get the prediction from the neural network for a single instance
     * Return the idx with highest output values. For example if the outputs
     * of the outputNodes are [0.1, 0.5, 0.2], it should return 1.
     * The parameter is a single instance
     */

    public int predict(Instance instance) {
    	for(int j = 0; j < inputNodes.size() - 1; j++) {
    		inputNodes.get(j).setInputNodeInputValue(instance.getAttributes().get(j));
    	}
    	inputNodes.get(inputNodes.size()-1).setInputValue(1);
    	// CALCULATING HIDDEN NODE VALUES
    	for(int k = 0; k < hiddenNodes.size() - 1; k ++) {
    		double value = 0.0;
    		Node currentHiddenNode = hiddenNodes.get(k);
    		for(NodeWeightPair pair : currentHiddenNode.getParents()) {
    			value = value + ((double)pair.getNode().getInputValue() * (double)pair.getWeight());
    		}
    		currentHiddenNode.calculateOutput(value, 0.0);
    	}
    	hiddenNodes.get(hiddenNodes.size()-1).setOutputValue(1);
    	//CALCULATING OUTPUT NODE VALUES
    	double value, summation = 0.0;
    	for(int k = 0; k < outputNodes.size(); k++) {
    		value = 0.0;
    		for(NodeWeightPair pair : outputNodes.get(k).getParents()) {
    			value = value + (pair.getNode().getOutputValue() * pair.getWeight());
    		}
    		outputNodes.get(k).setInputValue(value);
    		summation = summation + Math.exp(value);
    	}
    	for(int k = 0; k < outputNodes.size(); k++) {
    		outputNodes.get(k).calculateOutput(outputNodes.get(k).getInputValue(), summation);
    	}
    	Double maximumSeen = Double.MIN_VALUE;
    	int minIndex = -1;
    	for(int k = 0; k < outputNodes.size(); k++) {
    		Node outputNode = outputNodes.get(k);
    		if(outputNode.getOutputValue() > maximumSeen) {
    			maximumSeen = outputNode.getOutputValue();
    			minIndex = k;
    		}
    	}
    	if(minIndex == 0) {
    		return 0;
    	} else if (minIndex == 1) {
    		return 1;
    	} else if (minIndex == 2) {
    		return 2;
    	}
    	return 0;
    }


    /**
     * Train the neural networks with the given parameters
     * <p>
     * The parameters are stored as attributes of this class
     */

    public void train() {
	    	
    	for(int epoch = 0; epoch < maxEpoch; epoch++) {
    		// SHUFFLING TRAINING DATA
	    	Collections.shuffle(trainingSet, random);
	    	// LOOP OVER INSTANCES IN TRAINING SET
	        for(int i = 0; i < trainingSet.size(); i++) {
	        	Instance currentTrainingInstance = trainingSet.get(i);
	        	// FORWARD PASS BEGINS
	        	
	        	// SET INPUT NODE VALUES ON CURRENT TRAINING INSTANCE
	        	for(int j = 0; j < inputNodes.size() - 1; j++) {
	        		inputNodes.get(j).setInputNodeInputValue(currentTrainingInstance.getAttributes().get(j));
	        	}
	        	inputNodes.get(inputNodes.size()-1).setInputValue(1);
	        	// CALCULATING HIDDEN NODE VALUES
	        	for(int k = 0; k < hiddenNodes.size() - 1; k ++) {
	        		double value = 0.0;
	        		Node currentHiddenNode = hiddenNodes.get(k);
	        		for(NodeWeightPair pair : currentHiddenNode.getParents()) {
	        			value = value + ((double)pair.getNode().getInputValue() * (double)pair.getWeight());
	        		}
	        		currentHiddenNode.calculateOutput(value, 0.0);
	        	}
	        	hiddenNodes.get(hiddenNodes.size()-1).setOutputValue(1);
	        	//CALCULATING OUTPUT NODE VALUES
	        	double value, summation = 0.0;
	        	for(int k = 0; k < outputNodes.size(); k++) {
	        		value = 0.0;
	        		for(NodeWeightPair pair : outputNodes.get(k).getParents()) {
	        			value = value + (pair.getNode().getOutputValue() * pair.getWeight());
	        		}
	        		outputNodes.get(k).setInputValue(value);
	        		summation = summation + Math.exp(value);
	        	}
	        	for(int k = 0; k < outputNodes.size(); k++) {
	//        		System.out.println("Output: " + outputNodes.get(k).getInputValue());
	        		outputNodes.get(k).calculateOutput(outputNodes.get(k).getInputValue(), summation);
	//        		System.out.println("Output: " + outputNodes.get(k).getOutput());
	        	}
	        	// FORWARD PASS ENDS
	        	
	        	// BACKWARD PASS BEGINS
	        	// CALCULATING DELTA AT OUTPUT NODES	
	        	for(int k = 0; k < outputNodes.size(); k++) {
	        		outputNodes.get(k).calculateDelta(currentTrainingInstance.getClassValues().get(k));
	        	}
	        	// CALCULATION OF DELTA ENDS.
	        	
	        	int numberOutputNodes = outputNodes.size();
	        	int numberHiddenNodes = hiddenNodes.size();
	        	
	        	// DELTA W CALCULATION BETWEEN OUTPUT AND HIDDEN LAYER
	        	double[][] outputHiddenWeightChanges= new double[numberHiddenNodes][numberOutputNodes];
	        	for(int a = 0; a < numberHiddenNodes ; a++) {
	        		for(int b = 0; b < numberOutputNodes; b++) {
	        			outputHiddenWeightChanges[a][b] = this.learningRate * hiddenNodes.get(a).getOutput() * outputNodes.get(b).getDelta();
	        		}
	        	}
	        	
	        	// CALCULATION OF DELTA AT HIDDEN NODES
	        	for(int j = 0; j < hiddenNodes.size(); j++) {
	        		double summationPart = 0.0;
	        		for(int k = 0; k < outputNodes.size(); k++) {
	        			double weightJK= outputNodes.get(k).getParents().get(j).getWeight();
	        			summationPart = summationPart + (weightJK * outputNodes.get(k).getDelta());
	        		}
	        		hiddenNodes.get(j).calculateDeltaForHiddenNodes(summationPart);
	        	}
	        	// CALCULATION OF DELTA AT HIDDEN NODES ENDS
	        	
	        	// DELTA W CALCULATION BETWEEN HIDDEN LAYER AND INPUT LAYER
	        	int numberInputNodes = inputNodes.size();
	        	double[][] hiddenInputWeightChanges= new double[numberInputNodes][numberHiddenNodes];
	        	for(int a = 0; a < numberInputNodes; a++) {
	        		for(int j = 0; j < numberHiddenNodes; j++) {
	        			hiddenInputWeightChanges[a][j] = this.learningRate * inputNodes.get(a).getInputValue() * hiddenNodes.get(j).getDelta();
	        		}
	        	}
	        	// WEIGHT UPDATION - OUTPUT HIDDEN NODE LINKAGE
	        	for(int b = 0; b < outputNodes.size() ; b++) {
	        		ArrayList<NodeWeightPair> nodeWeightPairs = outputNodes.get(b).getParents();
	        		int iterator = 0;
	        		for(NodeWeightPair pair : nodeWeightPairs) {
	        			pair.setWeight(pair.getWeight() + outputHiddenWeightChanges[iterator][b]);
//	        			System.out.println(pair.getWeight());
	        			iterator++;
	        		}
	        	}
	        	
	        	//WEIGHT UPDATION - HIDDEN INPUT NODE LINKAGE
	        	for(int a = 0; a < hiddenNodes.size() - 1; a++) {
	        		ArrayList<NodeWeightPair> nodeWeightPairs = hiddenNodes.get(a).getParents();
	        		int iterator = 0;
	        		for(NodeWeightPair pair : nodeWeightPairs) {
	        			pair.setWeight(pair.getWeight() + hiddenInputWeightChanges[iterator][a]);
//	        			System.out.println(pair.getWeight());
	        			iterator++;
	        		}
	        	}
	        }
	        
	        double totalLoss = 0.0;
	        
	        // CALCULATE LOSS FUNCTION
	        for(int i = 0; i < trainingSet.size(); i++) {
	        	totalLoss = totalLoss + loss(trainingSet.get(i));
	        }
	        
	        totalLoss = (double)totalLoss /(double) trainingSet.size();
	        
	        Formatter fmt = new Formatter();
	        fmt.format("%.3e", totalLoss);
	        
	        System.out.println("Epoch: " + epoch + ", Loss: " + fmt);
	        fmt.close();
    	}
    }

    /**
     * Calculate the cross entropy loss from the neural network for
     * a single instance.
     * The parameter is a single instance
     */
    private double loss(Instance instance) {
        
    	double loss = 0.0;
    	for(int j = 0; j < inputNodes.size() - 1; j++) {
    		inputNodes.get(j).setInputNodeInputValue(instance.getAttributes().get(j));
    	}
    	inputNodes.get(inputNodes.size()-1).setInputValue(1);
    	// CALCULATING HIDDEN NODE VALUES
    	for(int k = 0; k < hiddenNodes.size() - 1; k ++) {
    		double value = 0.0;
    		Node currentHiddenNode = hiddenNodes.get(k);
    		for(NodeWeightPair pair : currentHiddenNode.getParents()) {
    			value = value + ((double)pair.getNode().getInputValue() * (double)pair.getWeight());
    		}
    		currentHiddenNode.calculateOutput(value, 0.0);
    	}
    	hiddenNodes.get(hiddenNodes.size()-1).setOutputValue(1);
    	//CALCULATING OUTPUT NODE VALUES
    	double value, summation = 0.0;
    	for(int k = 0; k < outputNodes.size(); k++) {
    		value = 0.0;
    		for(NodeWeightPair pair : outputNodes.get(k).getParents()) {
    			value = value + (pair.getNode().getOutputValue() * pair.getWeight());
    		}
    		outputNodes.get(k).setInputValue(value);
    		summation = summation + Math.exp(value);
    	}
    	for(int k = 0; k < outputNodes.size(); k++) {
    		outputNodes.get(k).calculateOutput(outputNodes.get(k).getInputValue(), summation);
    	}
    	
    	for(int z = 0; z < outputNodes.size(); z++) {
    		loss = loss + (-1) * (instance.getClassValues().get(z) * Math.log(outputNodes.get(z).getOutput()));
    	}
    	
        return loss;
    }
}
