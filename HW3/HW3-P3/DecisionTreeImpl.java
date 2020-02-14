import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		int currentDepth = 0;
		this.root = buildTree(trainData,currentDepth, maxPerLeaf);
	}
	
	private DecTreeNode buildTree(List<List<Integer>> data, int currentDepth, int maxPerLeaf) {
		DecTreeNode current = new DecTreeNode();
		if(currentDepth == this.maxDepth) {
				int zeroCount = 0;
				int oneCount = 0;
				for(List<Integer> list : data) {
					if(list.get(numAttr) == 0) {
						zeroCount++;
					} else {
						oneCount++;
					}
				}
				if(zeroCount > oneCount) {
					current.setClassLabel(0); 
				} else if (oneCount > zeroCount){
					current.setClassLabel(1);
				} else if (zeroCount == oneCount) {
					current.setClassLabel(1);
				}
				return current;
		} else {
			int size = data.size();
			int zeroClassCount = 0;
			int oneClassCount = 0;
			List<Integer> breakOn = new ArrayList<>();
			for(List<Integer> dataInstance : data) {
				if(dataInstance.get(numAttr) == 0) {
					zeroClassCount++;
				} else if(dataInstance.get(numAttr) == 1) {
					oneClassCount++;
				}
			}
			if(zeroClassCount == size) {
				current = new DecTreeNode(0,0,0);
				return current;
			} else if(oneClassCount == size) {
				current = new DecTreeNode(1,0,0);
				return current;
			} else {
				breakOn = bestGain(data);
				int attributeNumber = breakOn.get(0);
				int threshold = breakOn.get(1);
				if(attributeNumber == -1 && threshold == -1) {
					int zeroCount = 0;
					int oneCount = 0;
					for(List<Integer> list : data) {
						if(list.get(numAttr) == 0) {
							zeroCount++;
						} else {
							oneCount++;
						}
					}
					if(zeroCount > oneCount) {
						current.setClassLabel(0); 
					} else if (oneCount > zeroCount){
						current.setClassLabel(1);
					} else if (oneCount == zeroCount) {
						current.setClassLabel(1);
					}
					return current;
				}
				current.setAttribute(attributeNumber);
				current.setThreshold(threshold);
				List<List<Integer>> leftChildData = new ArrayList<>();
				List<List<Integer>> rightChildData = new ArrayList<>();
//				System.out.println("At " + currentDepth + " attributeNumber is " + attributeNumber + " & threshold is " + threshold);
				for(List<Integer> dataPoint : data) {
					if(dataPoint.get(attributeNumber) <= threshold) {
						leftChildData.add(dataPoint);
					} else {
						rightChildData.add(dataPoint);
					}
				}
				if(leftChildData.size() != 0) {
//					System.out.println("At " + currentDepth + " left child size is " + leftChildData.size());
					if(leftChildData.size() <= maxPerLeaf) {
						int zeroCount = 0;
						int oneCount = 0;
						for(List<Integer> list : leftChildData) {
							if(list.get(numAttr) == 0) {
								zeroCount++;
							} else {
								oneCount++;
							}
						}
						if(zeroCount > oneCount) {
							current.setLeft(new DecTreeNode(0,0,0)); 
						} else {
							current.setLeft(new DecTreeNode(1,0,0));
						}
					} else {
						current.setLeft(buildTree(leftChildData, currentDepth + 1, maxPerLeaf));
					}
				} else {
					current.setLeft(null);
				}
				if(rightChildData.size() != 0) {
					if(rightChildData.size() <= maxPerLeaf) {
						if(rightChildData.size() <= maxPerLeaf) {
							int zeroCount = 0;
							int oneCount = 0;
							for(List<Integer> list : rightChildData) {
								if(list.get(numAttr) == 0) {
									zeroCount++;
								} else {
									oneCount++;
								}
							}
							if(zeroCount > oneCount) {
								current.setRight(new DecTreeNode(0,0,0));
							} else {
								current.setRight(new DecTreeNode(1,0,0));
							}
						}
					} else {
						current.setRight(buildTree(rightChildData, currentDepth + 1, maxPerLeaf));
					}
//					System.out.println("At " + currentDepth + " right child size is " + rightChildData.size());
				} else {
					current.setRight(null);
				}
			}
			return current;
		}
	}
	
	
	
	private List<Integer> bestGain(List<List<Integer>> data) {
		List<Integer> result = new ArrayList<>();
		double initialEntropy = entropyCalculation(data);
//		System.out.println(initialEntropy);
		double maxInformationGain = Double.NEGATIVE_INFINITY;
		int attributeNumberToBeReturned = -1;
		int thresholdToBeReturned = -1;
		Map<Integer, TreeMap<Integer, Double>> globalMap = new TreeMap<>();
		for(int i = 0; i <= 8; i++) {
			Map<Integer, TreeMap<Integer, Double>> tempResult = new TreeMap<>();
			tempResult = informationGainForAttribute(data, initialEntropy, i);
			for(Entry<Integer, TreeMap<Integer, Double>> entry : tempResult.entrySet()) {
				globalMap.put(entry.getKey(), entry.getValue());
			}
			tempResult.clear();
		}
		for(Entry<Integer, TreeMap<Integer, Double>> entry : globalMap.entrySet()) {
			TreeMap<Integer, Double> tempMap = entry.getValue();
			for(Entry<Integer, Double> treeMapEntry : tempMap.entrySet()) {
				double currentInformationGain = treeMapEntry.getValue();
				if(currentInformationGain > maxInformationGain) {
					maxInformationGain = treeMapEntry.getValue();
					attributeNumberToBeReturned = entry.getKey();
					thresholdToBeReturned = treeMapEntry.getKey();
				}
			}
		}
		if(maxInformationGain == 0.0) {
			attributeNumberToBeReturned = -1;
			thresholdToBeReturned = -1;
		}
		result.add(attributeNumberToBeReturned);
		result.add(thresholdToBeReturned);
		return result;
	}

	private Map<Integer,TreeMap<Integer, Double>> informationGainForAttribute(List<List<Integer>> data, double initialEntropy, int attributeIndex) {
		Map<Integer, TreeMap<Integer, Double>> finalResult = new HashMap<>();
		TreeMap<Integer, Double> result = new TreeMap<>();
		List<List<Integer>> leftChild = new ArrayList<>();
		List<List<Integer>> rightChild = new ArrayList<>();
		double maxInformationGainSeen = Double.NEGATIVE_INFINITY;
		for(int thresholdTemp = 1; thresholdTemp <= 9 ; thresholdTemp++) {
			for(List<Integer> dataPoint : data) {
				if(dataPoint.get(attributeIndex) <= thresholdTemp) {
					leftChild.add(dataPoint);
				} else {
					rightChild.add(dataPoint);
				}
			}
//			System.out.println("For attribute " + attributeIndex + " and threshold " + thresholdTemp + " left child size is " + leftChild.size() + " and right child size is "+ rightChild.size());
			double leftEntropy = entropyCalculation(leftChild);
			double rightEntropy = entropyCalculation(rightChild);
			int leftSideCount = leftChild.size();
			int rightSideCount = rightChild.size();
			int totalSize = data.size();
			double leftEntropyCalculated = ((double)leftSideCount / (double) totalSize) * (double)leftEntropy;
			double rightEntropyCalculated = ((double)rightSideCount / (double) totalSize) * (double)rightEntropy;
			double entropyCombined = leftEntropyCalculated + rightEntropyCalculated;
//			System.out.println("Left entropy " + leftEntropyCalculated + " right entropy " + rightEntropyCalculated);
			double currentInformationGain = initialEntropy - entropyCombined;
//			System.out.println("For attribute " + attributeIndex + " and threshold " + thresholdTemp + " calculated gain is " + currentInformationGain);
			if(currentInformationGain > maxInformationGainSeen) {
				maxInformationGainSeen = currentInformationGain;
				if(result.size() != 0) {
					result.clear();
					result.put(thresholdTemp, currentInformationGain);
				} else if(result.size() == 0){
					result.put(thresholdTemp, currentInformationGain);
				}
			}
			leftChild.clear();
			rightChild.clear();
		}
//		System.out.println("For attribute " + attributeIndex + " and final map holds " + result.toString()); 
		finalResult.put(attributeIndex, result);
		return finalResult;
	}
	
	private double entropyCalculation(List<List<Integer>> data) {
		double initialEntropy = 0;
		int zeroClassCount = 0;
		int oneClassCount = 0;
		for(List<Integer> dataInstance : data) {
			if(dataInstance.get(numAttr) == 0) {
				zeroClassCount++;
			} else if(dataInstance.get(numAttr) == 1) {
				oneClassCount++;
			}
		}
		initialEntropy = calculateEntropy(zeroClassCount, oneClassCount);
		return initialEntropy;
	}

	private double calculateEntropy(int zeroClassCount, int oneClassCount) {
		double entropy = 0;
		double zeroFraction = 0;
		double oneFraction = 0;
		double zeroFractionLog = 0;
		double oneFractionLog = 0;
		
		if(zeroClassCount != 0) {
			zeroFraction = (double)((double)(zeroClassCount))/((double)(zeroClassCount + oneClassCount));
			zeroFractionLog = Math.log(zeroFraction)/Math.log((double)2);
		}
		
		if(oneClassCount != 0) {
			oneFraction = (double)((double)(oneClassCount))/((double)(zeroClassCount + oneClassCount));
			oneFractionLog = Math.log(oneFraction)/Math.log((double)2);
		}
		
		entropy = -(zeroFraction*zeroFractionLog + oneFraction*oneFractionLog);
		return entropy;
	}

	public int classify(List<Integer> instance, DecTreeNode root) {
		int attributeNumber = root.getAttribute();
		int threshold = root.getThreshold();
		if(root.getLeft() == null && root.getRight() == null) {
			return root.getClassLabel();
		}
		if(instance.get(attributeNumber) <= threshold) {
			return classify(instance, root.getLeft());
		} else {
			return classify(instance, root.getRight());
		}
	}
	
	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i), root);
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
