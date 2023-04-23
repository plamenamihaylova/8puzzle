import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.TreeMap;

public class Solver {

    // BinarySearchST gameTree;
    private TreeMap<Integer, Board> gameTree;
    private MinPQ<SearchNode> priorityQueue;

    private SearchNode searchNode;
    private int moves;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        searchNode = new SearchNode(initial, 0, null);
        priorityQueue = new MinPQ<>();
        priorityQueue.insert(searchNode);
        gameTree = new TreeMap<Integer, Board>();
        gameTree.put(moves(), initial);
        // gameTree = new BinarySearchST<>();
        // gameTree.put(searchNode.priority, searchNode);
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
        Queue<Board> result = new Queue<>();
        while (true) {
            SearchNode node = priorityQueue.delMin();
            result.enqueue(node.board);
            if (node.board.isGoal()) break;
            moves++;
            for (Board neighbor : node.board.neighbors()) {
                int priority = neighbor.manhattan();
                SearchNode neighborSearchNode = new SearchNode(neighbor, priority + moves(), node);
                if (gameTree.containsValue(neighbor)) continue;
                priorityQueue.insert(neighborSearchNode);
                gameTree.put(moves(), neighbor);
                // }

            }
        }

        return result;
    }

    public static void main(String[] args) {

    }
}
