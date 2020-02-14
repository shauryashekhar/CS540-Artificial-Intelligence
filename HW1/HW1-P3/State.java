import java.util.ArrayList;

/**
 * A state in the search represented by the (x,y) coordinates of the square and
 * the parent. In other words a (square,parent) pair where square is a Square,
 * parent is a State.
 * 
 * You should fill the getSuccessors(...) method of this class.
 * 
 */
public class State {

	private Square square;
	private State parent;

	// Maintain the gValue (the distance from start)
	// You may not need it for the BFS but you will
	// definitely need it for AStar
	private int gValue;

	// States are nodes in the search tree, therefore each has a depth.
	private int depth;

	/**
	 * @param square
	 *            current square
	 * @param parent
	 *            parent state
	 * @param gValue
	 *            total distance from start
	 */
	public State(Square square, State parent, int gValue, int depth) {
		this.square = square;
		this.parent = parent;
		this.gValue = gValue;
		this.depth = depth;
	}
	
	public State(Square square) {
		this.square = square;
		this.parent = null;
		this.gValue = 0;
		this.depth = 0;
	}
	
	public State() {
		
	}

	/**
	 * @param visited
	 *            explored[i][j] is true if (i,j) is already explored
	 * @param maze
	 *            initial maze to get find the neighbors
	 * @return all the successors of the current state
	 */
	public ArrayList<State> getSuccessors(boolean[][] explored, Maze maze) {
		// FILL THIS METHOD
		ArrayList<State> successors = new ArrayList<State>();
		
		Square currentSquare = maze.getPlayerSquare();
		
		int x = currentSquare.X;
		int y = currentSquare.Y;
		
		char[][] mazeMatrix = maze.getMazeMatrix();
		
		//LEFT
		if(x >= 0 && y-1 >= 0 && x <= maze.getNoOfRows()-1 && y-1 <= maze.getNoOfCols()-1 && explored[x][y-1] != true && mazeMatrix[x][y-1] != '%') {
			Square newSuccessorSquare = new Square(x,y-1);
			State newState = new State(newSuccessorSquare);
			successors.add(newState);
		}
		
		//DOWN
		if(x+1 >= 0 && y >= 0 && x+1 <= maze.getNoOfRows()-1 && y <= maze.getNoOfCols()-1 && explored[x+1][y] != true && mazeMatrix[x+1][y] != '%') {
			Square newSuccessorSquare = new Square(x+1,y);
			State newState = new State(newSuccessorSquare);
			successors.add(newState);
		}
		
		//RIGHT
		if(x >= 0 && y+1 >= 0 && x <= maze.getNoOfRows()-1 && y+1 <= maze.getNoOfCols()-1 && explored[x][y+1] != true && mazeMatrix[x][y+1] != '%') {
			Square newSuccessorSquare = new Square(x,y+1);
			State newState = new State(newSuccessorSquare);
			successors.add(newState);
		}
		
		//UP
		if(x-1 >= 0 && y >= 0 && x-1 <= maze.getNoOfRows()-1 && y <= maze.getNoOfCols()-1 && explored[x-1][y] != true && mazeMatrix[x-1][y] != '%') {
			Square newSuccessorSquare = new Square(x-1,y);
			State newState = new State(newSuccessorSquare);
			successors.add(newState);
		}
		
		// TODO check all four neighbors in left, down, right, up order
		// TODO remember that each successor's depth and gValue are
		// +1 of this object.
		
		return successors;
	}

	/**
	 * @return x coordinate of the current state
	 */
	public int getX() {
		return square.X;
	}

	/**
	 * @return y coordinate of the current state
	 */
	public int getY() {
		return square.Y;
	}

	/**
	 * @param maze initial maze
	 * @return true is the current state is a goal state
	 */
	public boolean isGoal(Maze maze) {
		if (square.X == maze.getGoalSquare().X
				&& square.Y == maze.getGoalSquare().Y)
			return true;

		return false;
	}

	/**
	 * @return the current state's square representation
	 */
	public Square getSquare() {
		return square;
	}

	/**
	 * @return parent of the current state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * You may not need g() value in the BFS but you will need it in A-star
	 * search.
	 * 
	 * @return g() value of the current state
	 */
	public int getGValue() {
		return gValue;
	}

	/**
	 * @return depth of the state (node)
	 */
	public int getDepth() {
		return depth;
	}

	public int getgValue() {
		return gValue;
	}

	public void setgValue(int gValue) {
		this.gValue = gValue;
	}

	public void setSquare(Square square) {
		this.square = square;
	}

	public void setParent(State parent) {
		this.parent = parent;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	@Override
	public String toString() {
		return "State [square=" + square.X +" "+square.Y + ", parent=" + parent + ", gValue=" + gValue + ", depth=" + depth + "]";
	}
	
}
