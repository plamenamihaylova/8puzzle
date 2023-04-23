import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

public class Solver {
    private SearchNode searchNode;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        searchNode = new SearchNode(initial, 0, null);
        moves = 0;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (searchNode.board.isGoal()) return false;
        return true;
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
        Queue<Board> result = new Queue<>();

        if (searchNode.previous == null) priorityQueue.insert(searchNode);


        while (true) {
            SearchNode node = priorityQueue.delMin();
            result.enqueue(node.board);

            if (node.board.isGoal()) return result;

            moves++;
            for (Board neighbor : node.board.neighbors()) {

                // if (node.board.equals(neighbor) || (node.previous != null
                //         && node.previous.board.equals(neighbor))) continue;
                if (help(neighbor, priorityQueue)) continue;
                else {
                    int priority = neighbor.manhattan() + moves();

                    SearchNode neighborSearchNode = new SearchNode(neighbor, priority, node);
                    priorityQueue.insert(neighborSearchNode);
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
