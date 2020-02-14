import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AlphaBetaPruning {
	
	int depthCutOff;
	double value;
	int noOfNodesVisited;
	int noOfNodesEvaluated;
	int maxDepth;
	int lastMove;
	double averageBranchingFactor;
	double noOfInternalNodes;
	double noOfBranchesNotPruned;
	TreeMap<Integer, Double> map;

    public AlphaBetaPruning(int depth) {
    	this.lastMove = 0;
    	this.depthCutOff = depth;
    	this.noOfNodesVisited = 0;
    	this.noOfNodesEvaluated = 0;
    	this.noOfInternalNodes = 0;
    	this.noOfBranchesNotPruned = 0;
    	map = new TreeMap<>();
    }

    /**
     * This function will print out the information to the terminal,
     * as specified in the homework description.
     */
    public void printStats() {
        System.out.println("Move: "+lastMove);
        System.out.println("Value: "+value);
        System.out.println("Number of Nodes Visited: "+ noOfNodesVisited);
        System.out.println("Number of Nodes Evaluated: " + noOfNodesEvaluated);
        System.out.println("Max Depth Reached: " + maxDepth);
        System.out.print("Avg Effective Branching Factor: "+String.format("%.1f", averageBranchingFactor));
    }

    /**
     * This function will start the alpha-beta search
     * @param state This is the current game state
     * @param depth This is the specified search depth
     */
    public void run(GameState state, int depth) {
    	
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        boolean maxPlayer;
        
        if(state.getStonesRemoved() % 2 == 0) {
        	maxPlayer = true;
        } else {
        	maxPlayer = false;
        }
        
        double returnValue = alphabeta(state, 0, alpha, beta, maxPlayer);
  
        for(Map.Entry<Integer, Double> mapElement : map.entrySet()) {
        	Integer move = (Integer) mapElement.getKey();
        	Double value = (Double) mapElement.getValue();
        	if(value == returnValue) {
        		this.value = value;
        		this.lastMove = move;
        		break;
        	}
        }
        
        this.averageBranchingFactor = this.noOfBranchesNotPruned / this.noOfInternalNodes;
   }


    /**
     * This method is used to implement alpha-beta pruning for both 2 players
     * @param state This is the current game state
     * @param depth Current depth of search
     * @param alpha Current Alpha value
     * @param beta Current Beta value
     * @param maxPlayer True if player is Max Player; Otherwise, false
     * @return int This is the number indicating score of the best next move
     */
    private double alphabeta(GameState state, int depth, double alpha, double beta, boolean maxPlayer) {
    	this.noOfNodesVisited++;
    	if(depth > this.maxDepth) {
        	this.maxDepth = depth;
        }
    	if(depth == depthCutOff) {
    		this.noOfNodesEvaluated++;
    		return state.evaluate(state);
    	}
        
        List<Integer> possibleSuccessorMoves = state.getMoves(state);
//        System.out.println("For last move: "+ state.getLastMove()+" "+possibleSuccessorMoves);
        
        if(maxPlayer) {  
        	double value = Double.NEGATIVE_INFINITY;
        	if(possibleSuccessorMoves.size() == 0) {
//        		System.out.println("No more moves after "+state.getLastMove());
        		this.noOfNodesEvaluated++;
        		return state.evaluate(state);
        	} else {
        		this.noOfInternalNodes++;
        		TreeMap<Integer, Double> tempMap = new TreeMap<>();
	        	for(Integer possibleMove : possibleSuccessorMoves) {
	        		this.noOfBranchesNotPruned++;
	        		GameState tempGameState = state.removeStone(state, possibleMove);
//	        		System.out.println("Move Made "+ possibleMove);
	            	value = Double.max(value,alphabeta(tempGameState, depth+1, alpha, beta, !maxPlayer));
	            	tempMap.put(possibleMove, value);
	            	if(value >= beta) {
	            		if(depth == 0) {
	            			map.putAll(tempMap);
	            		}
	            		return value;
	            	}
	            	alpha = Double.max(alpha, value);
	        	}
	        	if(depth == 0) {
	        		map.putAll(tempMap);
	        	}
	        	return value;
        	}
        }else {
        	double value = Double.POSITIVE_INFINITY;
        	if(possibleSuccessorMoves.size() == 0) {
//        		System.out.println("No more moves after "+state.getLastMove());
        		this.noOfNodesEvaluated++;
        		return state.evaluate(state);
        	} else {
        		this.noOfInternalNodes++;
        		TreeMap<Integer, Double> tempMap = new TreeMap<>();
	        	for(Integer possibleMove : possibleSuccessorMoves) {
	        		this.noOfBranchesNotPruned++;
	        		GameState tempGameState = state.removeStone(state, possibleMove);
//	        		System.out.println("Move Made "+ possibleMove);
	            	value = Double.min(value,alphabeta(tempGameState, depth+1, alpha, beta, !maxPlayer));
	            	tempMap.put(possibleMove, value);
	            	if(value <= alpha) {
	            		if(depth == 0) {
	            			map.putAll(tempMap);
	            		}
	            		return value;
	            	}
	            	beta = Double.min(beta, value);
	            }
	        	if(depth == 0) {
        			map.putAll(tempMap);
        		}
	        	return value;
	        }
        }
    }
}

