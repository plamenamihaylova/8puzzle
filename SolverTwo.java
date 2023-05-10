import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * ASSESSMENT SUMMARY
 * <p>
 * Compilation:  PASSED
 * API:          PASSED
 * <p>
 * SpotBugs:     FAILED (1 warning)
 * PMD:          PASSED
 * Checkstyle:   PASSED
 * <p>
 * Correctness:  52/52 tests passed
 * Memory:       20/22 tests passed
 * Timing:       44/125 tests passed
 * <p>
 * Aggregate score: 86.13%
 * [ Compilation: 5%, API: 5%, Style: 0%, Correctness: 60%, Timing: 10%, Memory: 20% ]
 */
public class SolverTwo {
    private SearchNode searchNode;
    private int moves;
    private boolean isSolvable;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode previous;
        private final int manhattan;
        private final int moves;

        public SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.previous = previous;
            this.manhattan = board.manhattan();
            this.moves = moves;
        }

        public int compareTo(SearchNode that) {
            int node1Manhattan = this.manhattan;
            int node2Manhattan = that.manhattan;
            int node1Priority = node1Manhattan + this.moves;
            int node2Priority = node2Manhattan + that.moves;

            if (node1Priority > node2Priority) return 1;
            if (node1Priority < node2Priority) return -1;

            return Integer.compare(node1Manhattan, node2Manhattan);
        }
    }

    /**
     * Find a solution to the initial board (using the A* algorithm)
     *
     * @param initial board of the slider puzzle
     */
    public SolverTwo(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        searchNode = new SearchNode(initial, 0, null);
        moves = 0;
        isSolvable = solvable();
        solution();
    }

    /**
     * Return the sequence of boards in the shortest solution; null if unsolvable.
     *
     * @return iterable sequence of boards in the shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        MinPQ<SearchNode> priorityQueue = new MinPQ<>();
        Stack<Board> result = new Stack<>();

        if (searchNode.previous == null) {
            priorityQueue.insert(searchNode);
            // result.push(searchNode.board);
        }

        SearchNode solution = findSolution(priorityQueue);
        SearchNode tmp = solution;
        moves = solution.moves;

        while (tmp.previous != null) {
            result.push(tmp.board);
            tmp = tmp.previous;
        }

        result.push(tmp.board);

        return result;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    private boolean solvable() {
        int inversions = numOfInversions();
        if (searchNode.board.dimension() % 2 == 0) {
            return (getBlankRow() + inversions) % 2 != 0;
        }
        else return inversions % 2 == 0;
    }

    private int numOfInversions() {
        int[] flatOriginalBoard = getFlatBoardTilesArray(searchNode.board.toString());

        int inversions = 0;
        for (int i = 0; i < flatOriginalBoard.length; i++) {
            for (int j = i + 1; j < flatOriginalBoard.length; j++) {
                if (flatOriginalBoard[i] > flatOriginalBoard[j] && flatOriginalBoard[i] != 0
                        && flatOriginalBoard[j] != 0) {
                    inversions++;
                }
            }
        }
        return inversions;
    }

    private int[] getFlatBoardTilesArray(String input) {
        String[] lines = input.split("\n");
        int[] elements = new int[(lines.length - 1) * (lines.length - 1)];

        int index = 0;
        for (int i = 1; i < lines.length; i++) {
            String[] row = lines[i].trim().split("\\s+");
            for (int j = 0; j < row.length; j++) {
                elements[index++] = Integer.parseInt(row[j]);
            }
        }
        return elements;
    }

    private int getBlankRow() {
        int[] flatBoard = getFlatBoardTilesArray(searchNode.board.toString());
        for (int i = 0; i < flatBoard.length; i++)
            if (flatBoard[i] == 0) return i / searchNode.board.dimension();
        return 0;
    }


    /**
     * Return the minimum number of moves to solve initial board; -1 if initial board is unsolvable.
     *
     * @return the minimum number of moves to solve initial board, -1 if unsolvable
     */
    public int moves() {
        if (!isSolvable()) return -1;
        return moves;
    }

    private SearchNode findSolution(MinPQ<SearchNode> currentPQ) {
        SearchNode node = currentPQ.delMin();
        if (node.board.isGoal()) return node;
        else return findSolution(getNeighborsPQ(node, currentPQ));
    }

    private MinPQ<SearchNode> getNeighborsPQ(SearchNode parent, MinPQ<SearchNode> neighbors) {
        for (Board neighborBoard : parent.board.neighbors()) {
            if (parent.previous != null && neighborBoard.equals(parent.previous.board)) continue;
            SearchNode neighborSearchNode = new SearchNode(neighborBoard, parent.moves + 1, parent);
            neighbors.insert(neighborSearchNode);
        }
        return neighbors;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        SolverTwo solver = new SolverTwo(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}