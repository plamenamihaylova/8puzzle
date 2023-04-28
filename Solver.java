import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Objects;

public class Solver {
    private SearchNode searchNode;
    private int moves;

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

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        searchNode = new SearchNode(initial, 0, null);
        moves = 0;
        solution();
    }

    public boolean isSolvable() {
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
            // processed.add(searchNode.board);
        }

        SearchNode solution = findSolution(priorityQueue, result);

        for (SearchNode x = solution; Objects.requireNonNull(x).previous != null;
             x = solution.previous) {
            result.add(x.board);
            moves++;
        }
        return result;
    }

    private SearchNode findSolution(MinPQ<SearchNode> currentPQ,
                                    ArrayList<Board> processedBoards) {

        SearchNode node = currentPQ.delMin();
        processedBoards.add(node.board);

        if (node.board.isGoal()) return node;
        else findSolution(getNeighborsPQ(node, currentPQ), processedBoards);

        return node;
    }

    private MinPQ<SearchNode> getNeighborsPQ(SearchNode parent,
                                             MinPQ<SearchNode> neighbors) {
        moves++;
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
        Solver solver = new Solver(initial);

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