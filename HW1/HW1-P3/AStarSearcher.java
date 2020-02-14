import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * A* algorithm search
 * 
 * You should fill the search() method of this class.
 */
public class AStarSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public AStarSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main a-star search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {

		// FILL THIS METHOD

		// explored list is a Boolean array that indicates if a state associated with a given position in the maze has already been explored. 
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];
		// ...

		PriorityQueue<StateFValuePair> frontier = new PriorityQueue<StateFValuePair>();

		// TODO initialize the root state and add
		// to frontier list
		// ...
		
		Square startSquare = maze.getPlayerSquare();
		Square goalSquare = maze.getGoalSquare();
		
		State startState = new State(startSquare);
		startState.setgValue(0);
		startState.setDepth(0);
		
		double fValue = Math.sqrt(Math.pow(goalSquare.X-startSquare.X,2) + Math.pow(goalSquare.Y-startSquare.Y,2)) + 0;
		
		StateFValuePair stateFPairRoot = new StateFValuePair(startState, fValue);
		
		frontier.add(stateFPairRoot);
		
		while (!frontier.isEmpty()) {
			// TODO return true if a solution has been found
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
			
			if(frontier.size() > maxSizeOfFrontier) {
				maxSizeOfFrontier = frontier.size();
			}
			
			StateFValuePair currentStatePair = frontier.poll();
			
			if(currentStatePair.getState().getSquare().X == goalSquare.X && currentStatePair.getState().getSquare().Y == goalSquare.Y) {
					cost = currentStatePair.getState().getgValue();
					maxDepthSearched = cost;
					
					StateFValuePair finalStatePair = currentStatePair;
					
					State finalState = finalStatePair.getState().getParent();
					
					while(finalState.getParent() != null) {
						Square squareOnPath = finalState.getSquare();
						maze.setOneSquare(squareOnPath, '.');
						finalState = finalState.getParent();
					}
					
					return true;
			}
				
			Square playerSquare = currentStatePair.getState().getSquare();
						
			maze.setPlayerSquare(playerSquare);
				
			ArrayList<State> currentStateSuccessors = state.getSuccessors(explored, maze);
			
			noOfNodesExpanded++;
			
			for(State currentStateSuccessor : currentStateSuccessors) {
				currentStateSuccessor.setParent(currentStatePair.getState());
				currentStateSuccessor.setgValue(currentStatePair.getState().getgValue()+1);
				currentStateSuccessor.setDepth(currentStatePair.getState().getDepth()+1);
				int nextSquareX = currentStateSuccessor.getSquare().X;
				int nextSquareY = currentStateSuccessor.getSquare().Y;
					
				double nextFValue = Math.sqrt(Math.pow(goalSquare.X - nextSquareX,2) + Math.pow(goalSquare.Y - nextSquareY,2)) + currentStateSuccessor.getgValue();
					
				StateFValuePair nextStatePair = new StateFValuePair(currentStateSuccessor, nextFValue);
					
				int entered = 0;
				for(StateFValuePair pair : frontier) {
					if((pair.getState().getSquare().X == nextStatePair.getState().getSquare().X) && (pair.getState().getSquare().Y == nextStatePair.getState().getSquare().Y)) {
						if(pair.getFValue() > nextStatePair.getFValue()) {
							frontier.remove(pair);
							frontier.add(nextStatePair);
							entered = 1;
							break;
						}
					}
				}
					
				if(entered == 0) {
					frontier.add(nextStatePair);
				}
				explored[nextSquareX][nextSquareY] = true;
			}	
		}
			// use frontier.poll() to extract the minimum stateFValuePair.
			// use frontier.add(...) to add stateFValue pairs
		return false;
		// TODO return false if no solution
	}

}
