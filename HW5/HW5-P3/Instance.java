import java.util.List;

/**
 * A movie review instance.
 * <p>
 * DO NOT MODIFY.
 */
public class Instance {
    /**
     * Positive or negative
     */
    public Label label;
    /**
     * The movie review text segmented into a list of words.
     */
    public List<String> words;
    
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public List<String> getWords() {
		return words;
	}
	public void setWords(List<String> words) {
		this.words = words;
	}
	
	@Override
	public String toString() {
		return "Instance [label=" + label + ", words=" + words + ", getLabel()=" + getLabel() + ", getWords()="
				+ getWords() + ", getClass()=" + getClass();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((words == null) ? 0 : words.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instance other = (Instance) obj;
		if (label != other.label)
			return false;
		if (words == null) {
			if (other.words != null)
				return false;
		} else if (!words.equals(other.words))
			return false;
		return true;
	}
    
    
}
