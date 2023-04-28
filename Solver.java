import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    private SearchNode searchNode;
    private ArrayList<Board> processed;
    private int moves;

    private class SearchNode {
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

        private class SearchNodeComparator implements Comparator<SearchNode> {
            public int compare(SearchNode node1, SearchNode node2) {
                int node1Manhattan = node1.manhattan;
                int node2Manhattan = node2.manhattan;
                int node1Priority = node1Manhattan + node1.moves;
                int node2Priority = node2Manhattan + node2.moves;

                if (node1Priority > node2Priority) return 1;
                if (node1Priority < node2Priority) return -1;

                return Integer.compare(node1Manhattan, node2Manhattan);
            }
        }
    }


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        searchNode = new SearchNode(initial, 0, null);
        processed = new ArrayList<>();
        moves = 0;
        solution();
    }

    public boolean isSolvable() {
        int inversions = numOfInversions();
        if (searchNode.board.dimension() % 2 == 0) {
            return (getBlankRow() + inversions) % 2 == 1;
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
        // BTree<Integer, MinPQ<SearchNode>> gameTree = new BTree<>();

        MinPQ<SearchNode> priorityQueue = new MinPQ<>();
        ArrayList<Board> result = new ArrayList<>();
        if (searchNode.previous == null) {
            priorityQueue.insert(searchNode);
            // gameTree.put(moves(), priorityQueue);
        }
        result = findSolution(priorityQueue, result);
        // Stack<SearchNode> processedNodes = new Stack<>();


        // while (true) {
        //     MinPQ<SearchNode> currentPQ = gameTree.get(moves());
        //     SearchNode node = currentPQ.delMin();
        //     processedNodes.push(node);
        //     result.add(node.board);
        //     MinPQ<SearchNode> neighborsPQ = new MinPQ<>();
        //     if (node.priorityHamming == 0 && node.priorityManhattan == 0) return result;
        //     moves++;
        //     for (Board neighbor : node.board.neighbors()) {
        //         // if (node.board.equals(neighbor) || (node.previous != null
        //         //         && node.previous.board.equals(neighbor))) continue;
        //         if (isCurrentBoardAlreadyProcessed(neighbor, processedNodes)) continue;
        //         else {
        //             // int priority = neighbor.manhattan() + moves();
        //             SearchNode neighborSearchNode = new SearchNode(neighbor, moves(), node);
        //             currentPQ.insert(neighborSearchNode);
        //             gameTree.put(moves(), currentPQ);
        //             neighborsPQ.insert(neighborSearchNode);
        //         }
        //     }
        // }
        return result;
    }

    private ArrayList<Board> findSolution(MinPQ<SearchNode> currentPQ,
                                          ArrayList<Board> processedBoards) {
        SearchNode node = currentPQ.delMin();
        processedBoards.add(node.board);
        // result.add(node.board);

        if (node.board.isGoal()) return processedBoards;
        moves++;

        MinPQ<SearchNode> neighbors = new MinPQ<>();
        for (Board neighbor : node.board.neighbors()) {
            if (isCurrentBoardAlreadyProcessed(neighbor, processedBoards)) continue;
            int priority = neighbor.manhattan() + moves();

            SearchNode neigborSearchNode = new SearchNode(neighbor, priority, node);

            neighbors.insert(neigborSearchNode);
        }
        // find if there are neighbors with the same priority


        // for (int n = 0; n < neighbors.size() - 1; n++) {
        //     if (checkEqualNeighbors[n].priority == checkEqualNeighbors[n + 1].priority)
        //         findMinPriorityNeighbor(checkEqualNeighbors[n], checkEqualNeighbors[n + 1]);
        // }
        return findSolution(neighbors, processedBoards);
        // return processedBoards;
    }
    
    private boolean isCurrentBoardAlreadyProcessed(Board neighborBoard,
                                                   ArrayList<Board> processedBoards) {
        for (Board n : processedBoards) {
            if (n.equals(neighborBoard)) return true;
        }
        return false;
    }

    public static void main(String[] args) {

    }
}