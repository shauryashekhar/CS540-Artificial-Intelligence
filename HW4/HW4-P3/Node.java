import java.util.*;

/**
 * Class for internal organization of a Neural Network.
 * There are 5 types of nodes. Check the type attribute of the node for details.
 * Feel free to modify the provided function signatures to fit your own implementation
 */

public class Node {
    private int type = 0; //0=input,1=biasToHidden,2=hidden,3=biasToOutput,4=Output
    public ArrayList<NodeWeightPair> parents = null; //Array List that will contain the parents (including the bias node) with weights if applicable

    private double inputValue = 0.0;
    private double outputValue = 0.0;
    private double outputGradient = 0.0;
    private double delta = 0.0; //input gradient

    //Create a node with a specific type
    Node(int type) {
        if (type > 4 || type < 0) {
            System.out.println("Incorrect value for node type");
            System.exit(1);

        } else {
            this.type = type;
        }

        if (type == 2 || type == 4) {
            parents = new ArrayList<>();
        }
    }

    //For an input node sets the input value which will be the value of a particular attribute
    public void setInputNodeInputValue(double inputValue) {
        if (type == 0) {    //If input node
            this.inputValue = inputValue;
        }
    }

    /**
     * Calculate the output of a node.
     * You can get this value by using getOutput()
     */
    public void calculateOutput(double value, double summation) {
        if (this.type == 2) {
        	this.setInputValue(value);
            this.setOutputValue(Double.max(0.0, value));
        } else if(this.type == 4) {
        	this.setInputValue(value);
        	this.setOutputValue(Math.exp(value) / summation);
        }
    }

    //Gets the output value
    public double getOutput() {

        if (type == 0) {    //Input node
            return inputValue;
        } else if (type == 1 || type == 3) {    //Bias node
            return 1.00;
        } else {
            return outputValue;
        }

    }

    //Calculate the delta value of a node.
    public void calculateDelta(Integer Tk) {
        if (type == 4)  {
        	double deltaValue = ((double)Tk - this.outputValue);
        	this.setDelta(deltaValue);
        }
    }
    
    public void calculateDeltaForHiddenNodes(double summationPart) {
    	double deltaValue = 1.0;
    	if(this.inputValue <= 0) {
    		deltaValue = 0.0;
    	} else {
    		deltaValue = 1.0;
    	}
    	deltaValue = deltaValue * summationPart;
    	this.setDelta(deltaValue);
    }


    //Update the weights between parents node and current node
    public void updateWeight(int type, double[][] weightChanges) {
        if (type == 4) {
        	
        }
    }

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<NodeWeightPair> getParents() {
		return parents;
	}

	public void setParents(ArrayList<NodeWeightPair> parents) {
		this.parents = parents;
	}

	public double getInputValue() {
		return inputValue;
	}

	public void setInputValue(double inputValue) {
		this.inputValue = inputValue;
	}

	public double getOutputValue() {
		return outputValue;
	}

	public void setOutputValue(double outputValue) {
		this.outputValue = outputValue;
	}

	public double getOutputGradient() {
		return outputGradient;
	}

	public void setOutputGradient(double outputGradient) {
		this.outputGradient = outputGradient;
	}

	public double getDelta() {
		return delta;
	}

	public void setDelta(double delta) {
		this.delta = delta;
	}
    
    
    
}


