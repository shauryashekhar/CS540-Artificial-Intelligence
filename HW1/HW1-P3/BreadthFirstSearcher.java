import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Breadth-First Search (BFS)
 * 
 * You should fill the search() method of this class.
 */
public class BreadthFirstSearcher extends Searcher {

	/**
	 * Calls the parent class constructor.
	 * 
	 * @see Searcher
	 * @param maze initial maze.
	 */
	public BreadthFirstSearcher(Maze maze) {
		super(maze);
	}

	/**
	 * Main breadth first search algorithm.
	 * 
	 * @return true if the search finds a solution, false otherwise.
	 */
	public boolean search() {
		// FILL THIS METHOD

		// explored list is a 2D Boolean array that indicates if a state associated with a given position in the maze has already been explored.
		boolean[][] explored = new boolean[maze.getNoOfRows()][maze.getNoOfCols()];

		// ...

		// Queue implementing the Frontier list
		LinkedList<State> queue = new LinkedList<State>();
		
		Square startSquare = maze.getPlayerSquare();
		State startState = new State(startSquare);
		startState.setgValue(0);
		startState.setDepth(0);
		queue.add(startState);
		
		queue.add(null);
		
		int noOfNull = 1;
		
		Square goalSquare = maze.getGoalSquare();

		while (!queue.isEmpty()) {
			
			// TODO return true if find a solution
			// TODO maintain the cost, noOfNodesExpanded (a.k.a. noOfNodesExplored),
			// maxDepthSearched, maxSizeOfFrontier during
			// the search
			// TODO update the maze if a solution found
			
			if(queue.size() - noOfNull > maxSizeOfFrontier) {
				maxSizeOfFrontier = queue.size() - noOfNull;
			}
			
			State currentState = queue.pop();
			
			if(currentState == null) {
				noOfNull--;
				maxDepthSearched++;
				if(!queue.isEmpty()) {
					queue.add(null);
					noOfNull++;
				}
			} else {
			
				if(currentState.getSquare().X == goalSquare.X && currentState.getSquare().Y == goalSquare.Y) {
					cost = currentState.getgValue();
					
					State finalState = currentState.getParent();
					
					while(finalState.getParent() != null) {
						Square squareOnPath = finalState.getSquare();
						maze.setOneSquare(squareOnPath, '.');
						finalState = finalState.getParent();
					}
					
					return true;
				}
				
				Square playerSquare = currentState.getSquare();
				
			    maze.setPlayerSquare(playerSquare);
				
				ArrayList<State> currentStateSuccessors = state.getSuccessors(explored, maze);
				
				noOfNodesExpanded++;
				
				for(State currentStateSuccessor : currentStateSuccessors) {
					currentStateSuccessor.setParent(currentState);
					currentStateSuccessor.setgValue(currentState.getgValue()+1);
					currentStateSuccessor.setDepth(currentState.getDepth()+1);
					int nextSquareX = currentStateSuccessor.getSquare().X;
					int nextSquareY = currentStateSuccessor.getSquare().Y;
					explored[nextSquareX][nextSquareY] = true;
					queue.add(currentStateSuccessor);
				}
				
			}
			
			// use queue.pop() to pop the queue.
			// use queue.add(...) to add elements to queue
		}
		return false;
		// TODO return false if no solution
	}
}
