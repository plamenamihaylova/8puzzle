import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private final int dimension;
    private final int firstIndexToSwap;
    private final int secondIndextoSwap;

    /**
     * Create a board from n by n array of tiles
     *
     * @param tiles array of tiles
     */
    public Board(int[][] tiles) {
        if (tiles == null || !isBoardSymetic(tiles)) throw new IllegalArgumentException();
        this.tiles = copyBoardTiles(tiles);

        dimension = this.tiles.length;

        int[] twinIndicies = getRandomPairOfIndicesInBoard();
        firstIndexToSwap = twinIndicies[0];
        secondIndextoSwap = twinIndicies[1];
    }

    private boolean isBoardSymetic(int[][] inputTiles) {
        for (int[] row : inputTiles) if (row.length != inputTiles.length) return false;
        return true;
    }

    /**
     * Return string representation of current board.
     * First line denotes the size of the puzzle.
     *
     * @return string representation of current board.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension());
        s.append("\n");
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                s.append(String.format("%2d ", tiles[row][col]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * Return board dimension.
     *
     * @return board size
     */
    public int dimension() {
        return dimension;
    }

    /**
     * Give the distance between the board and the goal board.
     * The distance is measured by the number of tiles that are in the wrong position.
     *
     * @return number of tiles out of place
     */
    public int hamming() {
        int hamming = 0;
        int i = 0;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                i++;
                if (i == dimension() * dimension()) i = 0;
                if (tiles[row][col] != i && tiles[row][col] != 0) hamming++;
            }
        }
        return hamming;
    }

    /**
     * Calculate Manhattan distance between current tiles and goal tiles
     *
     * @return sum of Manhattan distances between tiles and goal tiles
     */
    // public int manhattan() {
    //     int manhattan = 0;
    //
    //     for (int row = 0; row < dimension(); row++) {
    //         for (int col = 0; col < dimension(); col++) {
    //             int currentElement = tiles[row][col];
    //             if (currentElement == 0) continue;
    //
    //             int singleDistance = Math.abs(goalCoordinates.get(currentElement).row - row)
    //                     + Math.abs(
    //                     goalCoordinates.get(currentElement).col - col);
    //             manhattan += singleDistance;
    //
    //         }
    //     }
    //     return manhattan;
    // }
    public int manhattan() {
        int manhattan = 0;
        int[] flat = flattenTiles();
        for (int i = 0, k = 1; i < flat.length; i++, k++) {
            int element = flat[i];
            if (element == 0 || element == k) continue;

            if (element % dimension() == 0) {
                manhattan += Math.abs(i / dimension() - (element / dimension() - 1)) + Math.abs(
                        i % dimension() - (dimension() - 1));
            }
            else {
                manhattan += Math.abs(i / dimension() - flat[i] / dimension()) + Math.abs(
                        i % dimension() - Math.abs(flat[i] % dimension() - 1));
            }
            // int rowDistance = Math.abs(i / dimension() - flatTiles[i] / dimension());
            // int colDistance = Math.abs(
            //         i % dimension() - Math.abs(flatTiles[i] % dimension() - 1));
            // // int diff = Math.abs(flatTiles[i] - k);
            // manhattan += (rowDistance) + (colDistance);

        }
        return manhattan;
    }
    // public int manhattan() {
    //     int manhattan = 0;
    //     for (int row = 0, k = 1; row < dimension(); row++) {
    //         for (int col = 0; col < dimension(); col++, k++) {
    //             int element = tiles[row][col];
    //             if (element == 0 || element == k) continue;
    //
    //             if (element % dimension() == 0) {
    //                 manhattan += Math.abs(row - (element / dimension() - 1)) + Math.abs(
    //                         col - (dimension() - 1));
    //             }
    //             else {
    //                 manhattan += Math.abs(row - (element / dimension())) + Math.abs(
    //                         col - Math.abs(element % dimension() - 1));
    //             }
    //         }
    //     }
    //     return manhattan;
    // }

    /**
     * Check if current board is equal to the goal board.
     *
     * @return {@code true} if current board is equal to the goal one,
     * {@code false} otherwise
     */
    public boolean isGoal() {
        for (int row = 0, i = 1; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                if (i == dimension() * dimension()) i = 0;
                if (this.tiles[row][col] != i++) return false;
            }
        }
        return true;
    }

    /**
     * Check if current board is equal to an argument board.
     *
     * @param y argument board
     * @return {@code true} if the boards are equal, {@code false} otherwise
     */
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        return Arrays.deepEquals(this.tiles, that.tiles);
    }

    /**
     * Generate all board's neighbors boards.
     *
     * @return ArrayList of boards containing all the board's neighbors
     */
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        int[] emptyTile = getEmptyTileCoordinates();
        if (emptyTile.length == 0) return neighbors;

        if (emptyTile[0] < tiles.length - 1) {
            neighbors.add(
                    newNeighborByRow(emptyTile[0], emptyTile[1], emptyTile[0] + 1));
        }

        if (emptyTile[0] > 0) {
            neighbors.add(
                    newNeighborByRow(emptyTile[0], emptyTile[1], emptyTile[0] - 1));
        }

        if (emptyTile[1] < tiles.length - 1) {
            neighbors.add(
                    newNeighborByColumn(emptyTile[0], emptyTile[1], emptyTile[1] + 1));
        }

        if (emptyTile[1] > 0) {
            neighbors.add(
                    newNeighborByColumn(emptyTile[0], emptyTile[1], emptyTile[1] - 1));
        }

        return neighbors;
    }

    private int[] getEmptyTileCoordinates() {
        int[] result = new int[2];
        for (int row = 0; row < tiles.length; row++)
            for (int col = 0; col < tiles[row].length; col++)
                if (tiles[row][col] == 0) {
                    result[0] = row;
                    result[1] = col;
                }

        return result;
    }

    private Board newNeighborByRow(int row, int col, int newRow) {
        int[][] neighbor = copyBoardTiles();
        neighbor[row][col] = neighbor[newRow][col];
        neighbor[newRow][col] = 0;
        return new Board(neighbor);
    }

    private Board newNeighborByColumn(int row, int col, int newCol) {
        int[][] neighbor = copyBoardTiles();
        neighbor[row][col] = neighbor[row][newCol];
        neighbor[row][newCol] = 0;
        return new Board(neighbor);
    }

    private int[][] copyBoardTiles() {
        int[][] copy = new int[tiles.length][];
        for (int i = 0; i < tiles.length; i++) {
            copy[i] = Arrays.copyOf(tiles[i], tiles[i].length);
        }
        return copy;
    }

    private int[][] copyBoardTiles(int[][] tilesToCopy) {
        int[][] copy = new int[tilesToCopy.length][];
        for (int row = 0; row < tilesToCopy.length; row++) {
            copy[row] = Arrays.copyOf(tilesToCopy[row], tilesToCopy[row].length);
        }
        return copy;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[] board = flattenTiles();
        int[] twinBoard = board.clone();

        int swap = twinBoard[firstIndexToSwap];
        twinBoard[firstIndexToSwap] = twinBoard[secondIndextoSwap];
        twinBoard[secondIndextoSwap] = swap;

        return new Board(unflattenBoard(twinBoard));
    }

    private int[] flattenTiles() {
        int[] flattenTiles = new int[dimension() * dimension()];
        int index = 0;
        for (int[] row : tiles)
            for (int item : row)
                flattenTiles[index++] = item;
        return flattenTiles;
    }

    private int[] getRandomPairOfIndicesInBoard() {
        int[] result = new int[2];
        result[0] = getRandomBoardIndex();
        do {
            result[1] = getRandomBoardIndex();
        }
        while (result[0] == result[1]);

        return result;
    }

    private int getRandomBoardIndex() {
        int i;
        int[] board = flattenTiles();
        do {
            i = StdRandom.uniformInt(0, board.length);
        } while (board[i] == 0);
        return i;
    }

    private int[][] unflattenBoard(int[] inputBoard) {
        int tilesSize = (int) Math.round(Math.sqrt(inputBoard.length));
        int[][] result = new int[tilesSize][tilesSize];
        int i = 0;
        for (int row = 0; row < tilesSize; row++)
            for (int col = 0; col < tilesSize; col++)
                result[row][col] = inputBoard[i++];

        return result;
    }

    public static void main(String[] args) {
        // test values for tiles
        // int[] a = { 0, 1, 3 };
        // int[] b = { 4, 2, 5 };
        // int[] c = { 7, 8, 6 };

        int[] a = { 5, 8, 7 };
        int[] b = { 1, 4, 6 };
        int[] c = { 3, 0, 2 };

        // test values for tiles
        // int[] a = { 1, 5, 3, 8 };
        // int[] b = { 4, 2, 0, 9 };
        // int[] c = { 7, 15, 6, 10 };
        // int[] d = { 11, 12, 13, 14 };

        // test values for tiles
        // int[] a = { 8, 1, 3 };
        // int[] b = { 4, 0, 2 };
        // int[] c = { 7, 6, 5 };

        // goal tiles
        // int[] ga = { 1, 2, 3 };
        // int[] gb = { 4, 5, 6 };
        // int[] gc = { 7, 8, 0 };


        int[][] tiles = { a, b, c };
        // int[][] goal = { ga, gb, gc };

        Board board = new Board(tiles);
        // Board goalBoard = new Board(goal);
        System.out.println("Original board");
        System.out.println(board.toString());

        // Board twin = board.twin();
        // System.out.println("Twin board");
        // System.out.println(twin.toString());

        System.out.println("Hamming distance in the original board is: " + board.hamming());
        System.out.println();
        System.out.println("Manhattan distance in the original board is: " + board.manhattan());
        // System.out.println("Manhattan two distance is: " + board.manhattanTwo());
        System.out.println();
        // System.out.println("Hamming distance in the twin board is: " + twin.hamming());
        // System.out.println("Manhattan distance in the twin board is: " + twin.manhattan());

        System.out.println("Neighbors of the original board are:");
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor.toString());
            System.out.println("---------");
        }

        Board secondTwin = board.twin();
        System.out.println("Test if second twin board is the same as the first one:");
        System.out.println(secondTwin.toString());

        System.out.println("Is puzzle solved: " + board.isGoal());
        // System.out.println("Are both boards equal: " + board.equals(goalBoard));
    }

}
