import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final SearchNode initialNode;
    private SearchNode searchNode;
    private int moves;
    private boolean solved;

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
        moves = 0;
        initialNode = new SearchNode(initial, 0, null);
        solved = false;
        findSolution();
        solution();
    }

    private void findSolution() {
        boolean twinSolved = false;

        MinPQ<SearchNode> priorityQueue = new MinPQ<>();
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<>();

        priorityQueue.insert(initialNode);
        twinPriorityQueue.insert(new SearchNode(initialNode.board.twin(), 0, null));

        SearchNode node;
        SearchNode twinNode;

        while (!solved && !twinSolved) {
            node = priorityQueue.delMin();
            solved = node.board.isGoal();

            twinNode = twinPriorityQueue.delMin();
            twinSolved = twinNode.board.isGoal();

            for (Board board : node.board.neighbors()) {
                if (node.previous != null && node.previous.board.equals(board)) continue;
                priorityQueue.insert(new SearchNode(board, node.moves + 1, node));
            }

            for (Board board : twinNode.board.neighbors()) {
                if (twinNode.previous != null && twinNode.previous.board.equals(board)) continue;
                twinPriorityQueue.insert(new SearchNode(board, twinNode.moves + 1, twinNode));
            }

            searchNode = node;
        }
    }


    // sequence of boards in the shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

        moves = searchNode.moves;
        SearchNode tmp = searchNode;

        Stack<Board> solution = new Stack<>();

        while (tmp.previous != null) {
            solution.push(tmp.board);
            tmp = tmp.previous;
        }

        solution.push(tmp.board);

        return solution;
    }

    public boolean isSolvable() {
        return solved;
    }


    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return moves;
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