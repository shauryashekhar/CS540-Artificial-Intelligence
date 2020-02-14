import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameState {
	private int size; // The number of stones
	private boolean[] stones; // Game state: true for available stones, false for taken ones
	private int lastMove; // The last move
	private int stonesRemoved;

	private Helper helper = new Helper();

	/**
	 * Class constructor specifying the number of stones.
	 */
	public GameState(int size) {

		this.size = size;

		// For convenience, we use 1-based index, and set 0 to be unavailable
		this.stones = new boolean[this.size + 1];
		this.stones[0] = false;

		// Set default state of stones to available
		for (int i = 1; i <= this.size; ++i) {
			this.stones[i] = true;
		}

		// Set the last move be -1
		this.lastMove = -1;

		this.stonesRemoved = 0;
	}

	/**
	 * Copy constructor
	 */
	public GameState(GameState other) {
		this.size = other.size;
		this.stones = Arrays.copyOf(other.stones, other.stones.length);
		this.lastMove = other.lastMove;
		this.stonesRemoved = other.stonesRemoved;
	}

	/**
	 * This method is used to compute a list of legal moves
	 *
	 * @return This is the list of state's moves
	 */
	public List<Integer> getMoves(GameState state) {
		boolean[] stones = state.getStones();
		int stonesRemoved = state.getStonesRemoved();
		boolean isFirstMove;
		if(stonesRemoved == 0) {
			isFirstMove = true;
		} else {
			isFirstMove = false;
		}
		int lastMove = state.getLastMove();
		List<Integer> nextMoves = new ArrayList<Integer>();
		if (isFirstMove) {
			for (int j = 1; j <= size / 2; j = j + 2) {
				nextMoves.add(j);
			}
		} else {
			for (int k = 1; k * lastMove <= size; k++) {
				if (stones[k * lastMove] == true) {
					nextMoves.add(k * lastMove);
				}
			}
			for (int i = 1; i <= size; i++) {
				if (stones[i] == true && lastMove % i == 0 && !nextMoves.contains(i)) {
					nextMoves.add(i);
				}
			}
		}
		Collections.sort(nextMoves);
		return nextMoves;
	}

	/**
	 * This method is used to generate a list of successors using the getMoves()
	 * method
	 *
	 * @return This is the list of state's successors
	 */
	public List<GameState> getSuccessors(GameState state, boolean[] stoneStatus, boolean isFirstMove) {
		return this.getMoves(state).stream().map(move -> {
			var tempState = new GameState(this);
			tempState.removeStone(move);
			return tempState;
		}).collect(Collectors.toList());
	}

	/**
	 * This method is used to evaluate a game state based on the given heuristic
	 * function
	 *
	 * @return int This is the static score of given state
	 */
	public double evaluate(GameState state) {
//		boolean isFirstMove;
//		if (state.getStonesRemoved() == 0) {
//			isFirstMove = true;
//		} else {
//			isFirstMove = false;
//		}
//
		boolean maxPlayer;
		if (state.getStonesRemoved() % 2 == 0) {
			maxPlayer = true;
		} else {
			maxPlayer = false;
		}

		if (maxPlayer) {
			if (getMoves(state).size() == 0) {
				return -1.0;
			}
		}
		if (!maxPlayer) {
			if (getMoves(state).size() == 0) {
				return 1.0;
			}
		}
		if (maxPlayer) {
			if (stones[1]) {
				return 0.0;
			} else {
				if (state.getLastMove() == 1) {
					int count = 0;
					for (int i = 2; i <= size; i++) {
						if (stones[i]) {
							count++;
						}
					}
					if (count % 2 == 1) {
						return 0.5;
					} else {
						return -0.5;
					}
				} else if (helper.isPrime(state.getLastMove())) {
					int count = 0;
					for(int k = 1; k * lastMove <= size; k++) {
						if(stones[k * lastMove] == true) {
							count++;
						}
					}
					if (count % 2 == 1) {
						return 0.7;
					} else {
						return -0.7;
					}
				} else {
					int largestPrimeFactor = helper.getLargestPrimeFactor(state.getLastMove());
					int count = 0;
					List<Integer> possibleMoves = getMoves(state);
					for(Integer possibleMove : possibleMoves) {
						if(stones[possibleMove] && possibleMove % largestPrimeFactor == 0) {
							count++;
						}
					}
					if (count % 2 == 1) {
						return 0.6;
					} else {
						return -0.6;
					}
				}
			}
		} else {
			if (stones[1]) {
				return 0.0;
			} else {
				if (state.getLastMove() == 1) {
					int count = 0;
					for (int i = 2; i <= size; i++) {
						if (stones[i]) {
							count++;
						}
					}
					if (count % 2 == 1) {
						return -0.5;
					} else {
						return 0.5;
					}
				} else if (helper.isPrime(state.getLastMove())) {
					int count = 0;
					for(int k = 1; k * lastMove <= size; k++) {
						if(stones[k * lastMove]) {
							count++;
						}
					}
					if (count % 2 == 1) {
						return -0.7;
					} else {
						return 0.7;
					}
				} else {
					int largestPrimeFactor = helper.getLargestPrimeFactor(state.getLastMove());
					int count = 0;
					List<Integer> possibleMoves = getMoves(state);
					for(Integer possibleMove : possibleMoves) {
						if(stones[possibleMove] && possibleMove % largestPrimeFactor == 0) {
							count++;
						}
					}
					if (count % 2 == 1) {
						return -0.6;
					} else {
						return 0.6;
					}
				}
			}

		}
	}

	/**
	 * This method is used to take a stone out
	 *
	 * @param idx Index of the taken stone
	 */
	public void removeStone(int idx) {
		this.stones[idx] = false;
		this.stonesRemoved++;
		this.lastMove = idx;
	}

	public GameState removeStone(GameState state, int idx) {
		GameState tempGameState = new GameState(state);
		boolean stone[] = tempGameState.getStones();
		stone[idx] = false;
		tempGameState.setStones(stone);
		int stonesRemoved = tempGameState.getStonesRemoved() + 1;
		tempGameState.setStonesRemoved(stonesRemoved);
		tempGameState.setLastMove(idx);
		return tempGameState;
	}

	/**
	 * These are get/set methods for a stone
	 *
	 * @param idx Index of the taken stone
	 */
	public void setStone(int idx) {
		this.stones[idx] = true;
	}

	public boolean getStone(int idx) {
		return this.stones[idx];
	}

	/**
	 * These are get/set methods for lastMove variable
	 *
	 * @param move Index of the taken stone
	 */
	public void setLastMove(int move) {
		this.lastMove = move;
	}

	public int getLastMove() {
		return this.lastMove;
	}

	/**
	 * This is get method for game size
	 *
	 * @return int the number of stones
	 */
	public int getSize() {
		return this.size;
	}

	public boolean[] getStones() {
		return stones;
	}

	public void setStones(boolean[] stones) {
		this.stones = stones;
	}

	public int getStonesRemoved() {
		return stonesRemoved;
	}

	public void setStonesRemoved(int stonesRemoved) {
		this.stonesRemoved = stonesRemoved;
	}

	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "GameState [size=" + size + ", stones=" + Arrays.toString(stones) + ", lastMove=" + lastMove
				+ ", stonesRemoved=" + stonesRemoved + "]";
	}

	public Helper getHelper() {
		return helper;
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

}
