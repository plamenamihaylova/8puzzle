import edu.princeton.cs.algs4.BinarySearchST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {
    private static final int[][] GOAL_TILES = {
            { 1, 2, 3 },
            { 4, 5, 6 },
            { 7, 8, 0 }
    };
    
    private BinarySearchST<Integer, TileCoordinates> goalCoordinates = new BinarySearchST<>();

    private int[][] tiles;
    private int dimension;
    private TileCoordinates emptyTile;


    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                this.tiles[row][col] = tiles[row][col];
                if (this.tiles[row][col] == 0) emptyTile = new TileCoordinates(row, col);
            }
        }

        dimension = this.tiles.length;
        generateGoalCoordinates();
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension());
        s.append("\n");
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                s.append(String.format("%2d", tiles[row][col]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
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
                if (i == 9) i = 0;
                if (tiles[row][col] != i && tiles[row][col] != 0) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;
        // int i = 1;
        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                int currentElement = tiles[row][col];
                if (currentElement == 0) continue;
                // int rowDiff = Math.abs(goal.get(currentElement).row - row);
                // int colDiff = Math.abs(goal.get(currentElement).col - col);

                int singleDistance = Math.abs(goalCoordinates.get(currentElement).row - row)
                        + Math.abs(
                        goalCoordinates.get(currentElement).col - col);
                manhattan += singleDistance;

            }
        }
        return manhattan;
    }


    /**
     * Check if current board is equal to the goal board.
     *
     * @return {@code true} if current board is equal to the goal one,
     * {@code false} otherwise
     */
    public boolean isGoal() {
        return this.equals(new Board(GOAL_TILES));
    }

    /**
     * Check if current board is equal to an argument board.
     *
     * @param y argument board
     * @return {@code true} if the boards are equal, {@code false} otherwise
     */
    public boolean equals(Object y) {
        // optimize for object equality
        if (y == this) return true;
        // check for null
        if (y == null) return false;
        // objects must be in the same class
        if (y.getClass() != this.getClass()) return false;

        // cast is guaranteed to succeed
        Board that = (Board) y;
        // check if all significant fields are the same
        if (!Arrays.deepEquals(this.tiles, that.tiles)) return false;
        return true;
    }


    // private int compareTo(Board o) {
    //     if (this.dimension > o.dimension) return 1;
    //     else if (this.dimension < o.dimension) return -1;
    //     else return 0;
    // }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();

        if (emptyTile.row != 0) {
            neighbors.enqueue(generateNeighborBoard(emptyTile.row - 1, emptyTile.col));
        }
        if (emptyTile.row != dimension() - 1) {
            neighbors.enqueue(generateNeighborBoard(emptyTile.row + 1, emptyTile.col));
        }
        if (emptyTile.col != 0) {
            neighbors.enqueue(generateNeighborBoard(emptyTile.row, emptyTile.col - 1));
        }
        if (emptyTile.col != dimension() - 1) {
            neighbors.enqueue(generateNeighborBoard(emptyTile.row, emptyTile.col + 1));
        }

        return neighbors;
    }

    private Board generateNeighborBoard(int emptyRowCoordinate, int emptyColCoordinate) {

        int[][] tilescopy = new int[dimension()][dimension()];
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                tilescopy[row][col] = tiles[row][col];
            }
        }
        int swap = tilescopy[emptyTile.row][emptyTile.col];
        tilescopy[emptyTile.row][emptyTile.col] = tilescopy[emptyRowCoordinate][emptyColCoordinate];
        tilescopy[emptyRowCoordinate][emptyColCoordinate] = swap;

        return new Board(tilescopy);

    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {

        int[][] tilescopy = new int[dimension()][dimension()];

        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                tilescopy[row][col] = tiles[row][col];
            }
        }

        int randRow1;
        int randCol1;
        int randRow2;
        int randCol2;

        do {
            randRow1 = StdRandom.uniformInt(dimension());
            randCol1 = StdRandom.uniformInt(dimension());
        } while (tilescopy[randRow1][randCol1] == 0);

        do {
            randRow2 = StdRandom.uniformInt(dimension());
            randCol2 = StdRandom.uniformInt(dimension());
        } while (tilescopy[randRow2][randCol2] == 0
                || tilescopy[randRow1][randCol1] == tilescopy[randRow2][randCol2]);

        int swap = tilescopy[randRow1][randCol1];
        tilescopy[randRow1][randCol1] = tilescopy[randRow2][randCol2];
        tilescopy[randRow2][randCol2] = swap;

        return new Board(tilescopy);
    }

    private void generateGoalCoordinates() {
        int i = 1;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (i == 9) goalCoordinates.put(0, new TileCoordinates(x, y));
                else goalCoordinates.put(i, new TileCoordinates(x, y));
                i++;
            }
        }
    }

    // public static void main(String[] args) {
    //     int[][] tiles = {
    //             { 0, 1, 3 },
    //             { 4, 2, 5 },
    //             { 7, 8, 6 }
    //     };
    //
    //     int[][] tiles_second = {
    //             { 8, 1, 3 },
    //             { 4, 0, 2 },
    //             { 7, 6, 5 }
    //     };
    //
    //     int[][] goal = {
    //             { 1, 2, 3 },
    //             { 4, 5, 6 },
    //             { 7, 8, 0 }
    //     };
    //     Board board = new Board(tiles);
    //     board.generateGoalCoordinates();
    //     // for (Integer i : board.goal.keys(0, 9)) {
    //     //     System.out.println(board.goal.get(i).toString());
    //     // }
    //
    //     // System.out.println(board.goalCoordinates.get(1).row);
    //     // System.out.println(board.goalCoordinates.get(1).col);
    //     // System.out.println("-----");
    //     // System.out.println(board.goalCoordinates.get(2).row);
    //     // System.out.println(board.goalCoordinates.get(2).col);
    //     // System.out.println("-----");
    //     // System.out.println(board.goalCoordinates.get(3).row);
    //     // System.out.println(board.goalCoordinates.get(3).col);
    //     // System.out.println("-----");
    //     // System.out.println(board.goalCoordinates.get(4).row);
    //     // System.out.println(board.goalCoordinates.get(4).col);
    //     // System.out.println("-----");
    //     // System.out.println(board.goalCoordinates.get(5).row);
    //     // System.out.println(board.goalCoordinates.get(5).col);
    //     // System.out.println("-----");
    //     // System.out.println(board.goalCoordinates.get(6).row);
    //     // System.out.println(board.goalCoordinates.get(6).col);
    //     // System.out.println("-----");
    //     // System.out.println(board.goalCoordinates.get(7).row);
    //     // System.out.println(board.goalCoordinates.get(7).col);
    //     // System.out.println("-----");
    //     // System.out.println(board.goalCoordinates.get(8).row);
    //     // System.out.println(board.goalCoordinates.get(8).col);
    //     // System.out.println("-----");
    //     // System.out.println(board.goalCoordinates.get(0).row);
    //     // System.out.println(board.goalCoordinates.get(0).col);
    //     // System.out.println("-----");
    //
    //     // System.out.println(board.goal.toString());
    //     Board board1 = new Board(tiles_second);
    //     System.out.println(board1.manhattan());
    //     System.out.println(board1.hamming());
    //     System.out.println(board1.toString());
    //     for (Board neighbor : board1.neighbors()) {
    //         System.out.println(neighbor.toString());
    //     }
    //
    //
    //     // System.out.println(board.manhattan());
    //
    //     Board new_board = new Board(goal);
    //     System.out.println(new_board.hamming());
    //     System.out.println(new_board.manhattan());
    //     System.out.println(new_board.isGoal());
    //
    // }

}
