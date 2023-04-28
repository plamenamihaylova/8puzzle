import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SolverTwo {
    private SearchNode searchNode;
    private ArrayList<Board> processed;
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

        // public int compare(SearchNode node1, SearchNode node2) {
        //     int node1Manhattan = node1.manhattan;
        //     int node2Manhattan = node2.manhattan;
        //     int node1Priority = node1Manhattan + node1.moves;
        //     int node2Priority = node2Manhattan + node2.moves;
        //
        //     if (node1Priority > node2Priority) return 1;
        //     if (node1Priority < node2Priority) return -1;
        //
        //     return Integer.compare(node1Manhattan, node2Manhattan);
        // }

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
    public SolverTwo(Board initial) {
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

    private int getBlankRow() {
        int[] flatBoard = getFlatBoardTilesArray(searchNode.board.toString());
        for (int i = 0; i < flatBoard.length; i++)
            if (flatBoard[i] == 0) return i / searchNode.board.dimension();
        return 0;
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

        result = findSolution(priorityQueue, result);

        return result;
    }

    // private SearchNode findNodeWithMinimalPriority(MinPQ<SearchNode> priorityQueue,
    //                                                ArrayList<Board> processedBoards) {
    //     // private SearchNode findNodeWithMinimalPriority(MinPQ<SearchNode> priorityQueue) {
    //     if (priorityQueue.size() == 1) return priorityQueue.delMin();
    //
    //     SearchNode nodeWithMinPriority = priorityQueue.min();
    //
    //     List<SearchNode> nodesWithEqualMinValues = new ArrayList<>();
    //     if (processedBoards == null) processedBoards = new ArrayList<>();
    //
    //     while (!priorityQueue.isEmpty()
    //             && priorityQueue.min().priority == nodeWithMinPriority.priority) {
    //         SearchNode node = priorityQueue.delMin();
    //         processedBoards.add(node.board);
    //         nodesWithEqualMinValues.add(node);
    //     }
    //
    //     if (nodesWithEqualMinValues.size() > 1) {
    //
    //         for (int i = 0; i < nodesWithEqualMinValues.size() - 1; i++) {
    //             SearchNode currentNode = nodesWithEqualMinValues.get(i);
    //             SearchNode nextNode = nodesWithEqualMinValues.get(i + 1);
    //
    //             MinPQ<SearchNode> currentNodeNeighbors = getNodeNeighbors(currentNode,
    //                                                                       processedBoards);
    //             MinPQ<SearchNode> nextNodeNeighbors = getNodeNeighbors(nextNode, processedBoards);
    //
    //
    //             SearchNode firstChild = Objects.requireNonNull(
    //                     findNodeWithMinimalPriority(currentNodeNeighbors,
    //                                                 processedBoards));
    //             SearchNode secondChild = Objects.requireNonNull(
    //                     findNodeWithMinimalPriority(nextNodeNeighbors,
    //                                                 processedBoards));
    //
    //             if (firstChild.priorityManhattan == 0) return currentNode;
    //             if (secondChild.priorityManhattan == 0) return nextNode;
    //
    //
    //             if (firstChild.priorityManhattan < secondChild.priorityManhattan)
    //                 return currentNode;
    //             else if (firstChild.priorityManhattan > secondChild.priorityManhattan)
    //                 return nextNode;
    //             else {
    //                 MinPQ<SearchNode> temp = new MinPQ<>();
    //                 temp.insert(firstChild);
    //                 temp.insert(secondChild);
    //                 // processedBoards.add(firstChild.previous.board);
    //                 // processedBoards.add(secondChild.previous.board);
    //
    //                 findNodeWithMinimalPriority(temp, processedBoards);
    //             }
    //
    //
    //             // SearchNode firstGrandChild = findNodeWithMinimalPriority(
    //             //         getNodeNeighbors(firstChild, processedBoards),
    //             //         processedBoards);
    //             // SearchNode secondGrandChild = findNodeWithMinimalPriority(
    //             //         getNodeNeighbors(secondChild, processedBoards),
    //             //         processedBoards);
    //             //
    //             // if (firstGrandChild.priorityManhattan > secondGrandChild.priorityManhattan)
    //             //     return firstGrandChild;
    //             // if (firstGrandChild.priorityManhattan < secondGrandChild.priorityManhattan)
    //             //     return secondGrandChild;
    //         }
    //
    //     }
    //
    //     processedBoards.add(nodesWithEqualMinValues.get(0).previous.board);
    //     return nodesWithEqualMinValues.get(0);
    // }


    private ArrayList<Board> findSolution(MinPQ<SearchNode> currentPQ,
                                          ArrayList<Board> processedBoards) {

        // SearchNode node = findNodeWithMinimalPriority(currentPQ, null);
        // SearchNode node = findNodeWithMinimalPriority(currentPQ); //, processedBoards);

        SearchNode node = currentPQ.delMin();
        processedBoards.add(node.board);
        // result.add(node.board);

        if (node.board.isGoal()) return processedBoards;

        moves++;
        MinPQ<SearchNode> neighbors = getNeighborsPQ(node, new MinPQ<>(), processedBoards);

        // for (Board neighbor : node.board.neighbors()) {
        //
        //     if (isCurrentBoardAlreadyProcessed(neighbor, processedBoards)) continue;
        //     // int priority = neighbor.manhattan() + moves();
        //
        //     SearchNode neighborSearchNode = new SearchNode(neighbor, moves(), node);
        //     if (neighbors.min().compareTo(neighborSearchNode) == 0) {
        //
        //     }
        //     neighbors.insert(neighborSearchNode); // new SearchNode(neighbor, moves(), node));
        // }

        return findSolution(neighbors, processedBoards);
    }

    private MinPQ<SearchNode> getNeighborsPQ(SearchNode node,
                                             MinPQ<SearchNode> neighbors,
                                             ArrayList<Board> processedBoards) {

        for (Board neighbor : node.board.neighbors()) {
            if (isCurrentBoardAlreadyProcessed(neighbor, processedBoards)) continue;

            SearchNode neighborSearchNode = new SearchNode(neighbor, moves(), node);

            if (!neighbors.isEmpty() && neighbors.min().compareTo(neighborSearchNode) == 0) {

                for (SearchNode newNeighbor : getNeighborsPQ(neighborSearchNode, neighbors,
                                                             processedBoards)) {
                    if (isCurrentBoardAlreadyProcessed(newNeighbor.board, processedBoards))
                        continue;
                    neighbors.insert(newNeighbor);
                }
                for (SearchNode newNeighbor : getNeighborsPQ(neighbors.min(), neighbors,
                                                             processedBoards)) {
                    if (isCurrentBoardAlreadyProcessed(newNeighbor.board, processedBoards))
                        continue;
                    neighbors.insert(newNeighbor);
                }
            }
            else {

                neighbors.insert(neighborSearchNode);
            }


        }
        return neighbors;
    }

    private boolean isCurrentBoardAlreadyProcessed(Board neighborBoard,
                                                   ArrayList<Board> processedBoards) {
        for (Board n : processedBoards) {
            if (n.equals(neighborBoard)) return true;
        }
        return false;
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