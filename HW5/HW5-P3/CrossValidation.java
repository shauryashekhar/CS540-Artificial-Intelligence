import java.util.ArrayList;
import java.util.List;

public class CrossValidation {
    /*
     * Returns the k-fold cross validation score of classifier clf on training data.
     */
    public static double kFoldScore(Classifier clf, List<Instance> trainData, int k, int v) {
    	double result = 0.0;
    	int totalInstances = trainData.size();
    	int perInstanceSize = totalInstances / k;
    	int currentPos = 0;
    	List<Double> resultList = new ArrayList<>();
    	for(int j = 0; j < k; j++) {
    		int correctlyClassified = 0;
    		int total = 0;
    		int endPos = currentPos + perInstanceSize - 1;
    		List<Instance> currentTrainInstances = new ArrayList<>();
    		List<Instance> currentTestInstances = new ArrayList<>();
    		for(int i = 0; i < trainData.size(); i++) {
    			if(i >= currentPos && i <= endPos) {
    				currentTestInstances.add(trainData.get(i));
    			} else {
    				currentTrainInstances.add(trainData.get(i));
    			}
    		}
    		currentPos = endPos + 1;
    		clf.train(currentTrainInstances, v);
    		for(Instance currentInstance : currentTestInstances) {
    			ClassifyResult classificationResult = clf.classify(currentInstance.getWords());
    			if(classificationResult.getLabel() == currentInstance.getLabel()) {
    				correctlyClassified++;
    			}
    			total++;
    		}
    		double foldAccuracy = (double) correctlyClassified / (double) total;
    		resultList.add(foldAccuracy);
    	}
//    	System.out.println(resultList);
    	double summation = 0.0;
    	for(Double currentDouble : resultList) {
    		summation = summation + currentDouble;
    	}
    	result = summation / (double) k;
        return result;
    }
}
