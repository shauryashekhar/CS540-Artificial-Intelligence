import java.util.*;

/**
 * Holds data for a particular instance.
 * Attributes are represented as an ArrayList of Doubles
 * Class labels are represented as an ArrayList of Integers. For example,
 * a 3-class instance will have classValues as [0 1 0] meaning this
 * instance has class 1.
 * Do NOT modify
 */


public class Instance {
    public ArrayList<Double> attributes;
    public ArrayList<Integer> classValues;

    Instance() {
        attributes = new ArrayList<>();
        classValues = new ArrayList<>();
    }

	public ArrayList<Double> getAttributes() {
		return attributes;
	}

	public void setAttributes(ArrayList<Double> attributes) {
		this.attributes = attributes;
	}

	public ArrayList<Integer> getClassValues() {
		return classValues;
	}

	public void setClassValues(ArrayList<Integer> classValues) {
		this.classValues = classValues;
	}
    
    

}
