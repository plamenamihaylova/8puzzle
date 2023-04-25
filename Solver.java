import edu.princeton.cs.algs4.BTree;
import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;

public class Solver {
    private SearchNode searchNode;
    private int moves;
    private BTree<Integer, MinPQ<SearchNode>> gameTree;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        searchNode = new SearchNode(initial, 0, null);
        moves = 0;
        solution();
        gameTree = new BTree<>();
    }

    public boolean isSolvable() {
        int[][] board = getBoardArray();
        // Flatten the 2D puzzle into a 1D array
        int[] flatBoard = new int[board.length * board.length];
        int index = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                flatBoard[index++] = board[i][j];
            }
        }

        // Count the number of inversions
        int inversions = 0;
        for (int i = 0; i < flatBoard.length; i++) {
            for (int j = i + 1; j < flatBoard.length; j++) {
                if (flatBoard[i] > flatBoard[j] && flatBoard[i] != 0 && flatBoard[j] != 0) {
                    inversions++;
                }
            }
        }

        // If the grid size is odd, the puzzle is solvable if the number of inversions is even
        if (board.length % 2 != 0) {
            return inversions % 2 == 0;
        }
        // If the grid size is even, the puzzle is solvable if both conditions are met
        else {
            int blankRow = findBlankRow(board);
            return (inversions % 2 == 1 && blankRow % 2 == 0) || (inversions % 2 == 0
                    && blankRow % 2 == 1);
        }
    }

    private int findBlankRow(int[][] board) {
        // Find the row index of the blank tile (0) in the puzzle
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == 0) {
                    return i;
                }
            }
        }
        return -1; // return -1 if not found (should not happen in a valid puzzle)
    }

    private int[][] getBoardArray() {
        String[] rows = searchNode.board.toString().split("\n"); // Split by newline character
        int n = Integer.parseInt(rows[0]); // Get the size of the puzzle
        int[][] flatBoard = new int[n][n]; // Initialize the 2D array

        for (int i = 1; i <= n; i++) {
            String[] values = rows[i].trim().split("\\s+"); // Split each row by whitespace
            for (int j = 0; j < n; j++) {
                flatBoard[i - 1][j] = Integer.parseInt(
                        values[j]); // Parse the integers and store in the array
            }
        }
        return flatBoard;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        MinPQ<SearchNode> priorityQueue = new MinPQ<>();

        ArrayList<Board> result = new ArrayList<>();

        if (searchNode.previous == null) {
            priorityQueue.insert(searchNode);
            gameTree.put(moves(), priorityQueue);
        }

        while (true) {

            MinPQ<SearchNode> currentPQ = gameTree.get(moves());


            SearchNode node = currentPQ.delMin();

            result.add(node.board);

            if (node.board.isGoal()) return result;

            moves++;
            for (Board neighbor : node.board.neighbors()) {

                // if (node.board.equals(neighbor) || (node.previous != null
                //         && node.previous.board.equals(neighbor))) continue;

                if (help(neighbor, priorityQueue)) continue;
                else {
                    // int priority = neighbor.manhattan() + moves();

                    SearchNode neighborSearchNode = new SearchNode(neighbor, moves(), node);
                    currentPQ.insert(neighborSearchNode);
                    gameTree.put(moves(), currentPQ);
                }

            }
        }
    }


    private boolean help(Board neighborBoard, MinPQ<SearchNode> priorityQueue) {
        for (SearchNode n : priorityQueue) {
            if (n.board.equals(neighborBoard)) return true;
        }
        return false;
    }

    public static void main(String[] args) {

    }
}