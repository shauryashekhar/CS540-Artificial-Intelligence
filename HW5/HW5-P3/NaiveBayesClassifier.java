import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


/**
 * Your implementation of a naive bayes classifier. Please implement all four methods.
 */

public class NaiveBayesClassifier implements Classifier {
	
	Map<String, Integer> positiveWordsCount;
	Map<String, Integer> negativeWordsCount;
	Map<Label, Integer> wordsPerLabel;
	Map<Label, Integer> documentsPerLabel;
	double logPositive;
	double logNegative;
	int v;

	/**
     * Trains the classifier with the provided training data and vocabulary size
     */
    @Override
    public void train(List<Instance> trainData, int v) {
        // Hint: First, calculate the documents and words counts per label and store them. 
        // Then, for all the words in the documents of each label, count the number of occurrences of each word.
        // Save these information as you will need them to calculate the log probabilities later.
        //
        // e.g.
        // Assume m_map is the map that stores the occurrences per word for positive documents
        // m_map.get("catch") should return the number of "catch" es, in the documents labeled positive
        // m_map.get("asdasd") would return null, when the word has not appeared before.
        // Use m_map.put(word,1) to put the first count in.
        // Use m_map.replace(word, count+1) to update the value
    	this.wordsPerLabel = getWordsCountPerLabel(trainData);
    	this.documentsPerLabel = getDocumentsCountPerLabel(trainData);
    	
    	positiveWordsCount = new HashMap<String, Integer>();
    	negativeWordsCount = new HashMap<String, Integer>();
    	
    	for(Instance currentInstance : trainData) {
    		List<String> currentInstanceWords = currentInstance.getWords();
    		if(currentInstance.getLabel() == Label.NEGATIVE) {
    			for(String word : currentInstanceWords) {
	    				if(negativeWordsCount.containsKey(word)) {
	    					negativeWordsCount.replace(word, negativeWordsCount.get(word) + 1);
	    				} else {
	    					negativeWordsCount.put(word, 1);
	    				}
    			}
    		} else {
    			for(String word : currentInstanceWords) {
    				if(positiveWordsCount.containsKey(word)) {
    					positiveWordsCount.replace(word, positiveWordsCount.get(word) + 1);
    				} else {
    					positiveWordsCount.put(word, 1);
    				}
    			}
    		}
    	}
    	this.logNegative = Math.log(p_l(Label.NEGATIVE, trainData.size()));
    	this.logPositive = Math.log(p_l(Label.POSITIVE, trainData.size()));
    	this.v = v;
    }

    /*
     * Counts the number of words for each label
     */
    @Override
    public Map<Label, Integer> getWordsCountPerLabel(List<Instance> trainData) {
        Map<Label, Integer> m = new HashMap<Label, Integer>();
        int positiveWordCount = 0;
        int negativeWordCount = 0;
        for(Instance currentInstance : trainData) {
        	if(currentInstance.getLabel() == Label.NEGATIVE) {
        		negativeWordCount = negativeWordCount + currentInstance.getWords().size();
        	} else {
        		positiveWordCount = positiveWordCount + currentInstance.getWords().size();
        	}
        }
    	m.put(Label.NEGATIVE, negativeWordCount);
    	m.put(Label.POSITIVE, positiveWordCount);
        return m;
    }


    /*
     * Counts the total number of documents for each label
     */
    @Override
    public Map<Label, Integer> getDocumentsCountPerLabel(List<Instance> trainData) {
    	Map<Label, Integer> m = new HashMap<Label, Integer>();
    	int positiveCount = 0;
    	int negativeCount = 0;
    	for(Instance currentInstance : trainData) {
    		if(currentInstance.getLabel() == Label.NEGATIVE) {
    			negativeCount++;
    		} else {
    			positiveCount++;
    		}
    	}
    	m.put(Label.NEGATIVE, negativeCount);
    	m.put(Label.POSITIVE, positiveCount);
        return m;
    }


    /**
     * Returns the prior probability of the label parameter, i.e. P(POSITIVE) or P(NEGATIVE)
     */
    private double p_l(Label label, int trainDataSize) {
        // Calculate the probability for the label. No smoothing here.
        // Just the number of label counts divided by the number of documents.
    	double result;
    	result = documentsPerLabel.get(label)  / (double) trainDataSize;
        return result;
    }

    /**
     * Returns the smoothed conditional probability of the word given the label, i.e. P(word|POSITIVE) or
     * P(word|NEGATIVE)
     */
    private double p_w_given_l(String word, Label label) {
        // Calculate the probability with Laplace smoothing for word in class(label)
    	Map<String, Integer> mapToUse = new HashMap<String, Integer>();
    	if(label == Label.NEGATIVE) {
    		mapToUse = negativeWordsCount;
    	} else {
    		mapToUse = positiveWordsCount;
    	}
    	double numerator = 0;
    	if(mapToUse.containsKey(word)) {
    		numerator = mapToUse.get(word) + 1;
    	} else {
    		numerator = 1;
    	}
    	double denominator = this.v;
    	denominator = denominator + wordsPerLabel.get(label);
        return Math.log(numerator / denominator);
    }

    /**
     * Classifies an array of words as either POSITIVE or NEGATIVE.
     */
    @Override
    public ClassifyResult classify(List<String> words) {
        // Sum up the log probabilities for each word in the input data, and the probability of the label
        // Set the label to the class with larger log probability
    	ClassifyResult result = new ClassifyResult();
    	Map<Label, Double> returnMap = new HashMap<Label, Double>();
    	double positiveProbability, negativeProbability;
    	positiveProbability = logPositive;
    	negativeProbability = logNegative;
    	HashSet<String> wordsSet = new HashSet<String>();
    	wordsSet.addAll(words);
    	for(String word : words) {
    		negativeProbability = negativeProbability + p_w_given_l(word, Label.NEGATIVE);
    		positiveProbability = positiveProbability + p_w_given_l(word, Label.POSITIVE);
    	}
    	Label resultLabel = Label.POSITIVE;
    	if(positiveProbability > negativeProbability || negativeProbability == positiveProbability) {
    		resultLabel = Label.POSITIVE;
    	} else if (negativeProbability > positiveProbability){
    		resultLabel = Label.NEGATIVE;
    	}
    	returnMap.put(Label.NEGATIVE, negativeProbability);
    	returnMap.put(Label.POSITIVE, positiveProbability);
    	result.setLabel(resultLabel);
    	result.setLogProbPerLabel(returnMap);
        return result;
    }
    
    public Map<String, Integer> getPositiveWordsCount() {
		return positiveWordsCount;
	}

	public void setPositiveWordsCount(Map<String, Integer> positiveWordsCount) {
		this.positiveWordsCount = positiveWordsCount;
	}

	public Map<String, Integer> getNegativeWordsCount() {
		return negativeWordsCount;
	}

	public void setNegativeWordsCount(Map<String, Integer> negativeWordsCount) {
		this.negativeWordsCount = negativeWordsCount;
	}
    

}
