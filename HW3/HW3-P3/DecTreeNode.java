/**
 * Possible class for internal organization of a decision tree. Included to show standardized output
 * method, print().
 * 
 * Do not modify. If you use, create child class DecTreeNodeImpl that inherits the methods.
 * 
 */
public class DecTreeNode {
	//If leaf, label to return.
	public int classLabel;
	//Attribute split label.
	public int attribute;
	//Threshold that attributes are split on.
	public int threshold;
	//Left child. Can directly access and update. <= threshold.
	public DecTreeNode left = null;
	//Right child. Can directly access and update. > threshold.
	public DecTreeNode right = null;
	
	public DecTreeNode() {
		super();
	}

	public int getClassLabel() {
		return classLabel;
	}

	public void setClassLabel(int classLabel) {
		this.classLabel = classLabel;
	}

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public DecTreeNode getLeft() {
		return left;
	}

	public void setLeft(DecTreeNode left) {
		this.left = left;
	}

	public DecTreeNode getRight() {
		return right;
	}

	public void setRight(DecTreeNode right) {
		this.right = right;
	}

	DecTreeNode(int classLabel, int attribute, int threshold) {
		this.classLabel = classLabel;
		this.attribute = attribute;
		this.threshold = threshold;
		this.left = null;
		this.right = null;
	}
	
	public boolean isLeaf() {
		return this.left == null && this.right == null;
	}
}
