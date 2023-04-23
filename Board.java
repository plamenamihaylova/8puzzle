import edu.princeton.cs.algs4.BinarySearchST;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private int[][] goalTiles;
    private BinarySearchST<Integer, TileCoordinates> goalCoordinates = new BinarySearchST<>();
    private int[][] tiles;
    private int[][] twin;
    private int dimension;
    private TileCoordinates emptyTile;


    // create a board from an n-by-n array of tiles,
    public Board(int[][] tiles) {
        this.tiles = new int[tiles.length][tiles.length];
        this.goalTiles = new int[tiles.length][tiles.length];
        int i = 1;
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles.length; col++) {
                // immutable tiles
                this.tiles[row][col] = tiles[row][col];

                // locate empty tile
                if (this.tiles[row][col] == 0) emptyTile = new TileCoordinates(row, col);

                // generate goal tiles
                if (i == tiles.length * tiles.length) i = 0;
                goalTiles[row][col] = i;
                i++;
            }
        }

        dimension = this.tiles.length;
        generateGoalCoordinates();
    }

    private void generateGoalCoordinates() {
        int i = 1;
        for (int x = 0; x < dimension(); x++) {
            for (int y = 0; y < dimension(); y++) {
                if (i == dimension() * dimension())
                    goalCoordinates.put(0, new TileCoordinates(x, y));
                else goalCoordinates.put(i, new TileCoordinates(x, y));
                i++;
            }
        }
    }

    // string representation of this board
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
                if (i == dimension() * dimension()) i = 0;
                if (tiles[row][col] != i && tiles[row][col] != 0) hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattan = 0;

        for (int row = 0; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++) {
                int currentElement = tiles[row][col];
                if (currentElement == 0) continue;

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
        return this.equals(new Board(goalTiles));
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
        if (!Arrays.deepEquals(this.tiles, that.tiles)) return false;
        return true;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<>();

        if (emptyTile.row != 0) {
            neighbors.add(generateNeighborBoard(emptyTile.row - 1, emptyTile.col));
        }
        if (emptyTile.row != dimension() - 1) {
            neighbors.add(generateNeighborBoard(emptyTile.row + 1, emptyTile.col));
        }
        if (emptyTile.col != 0) {
            neighbors.add(generateNeighborBoard(emptyTile.row, emptyTile.col - 1));
        }
        if (emptyTile.col != dimension() - 1) {
            neighbors.add(generateNeighborBoard(emptyTile.row, emptyTile.col + 1));
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

        if (twin == null) {
            twin = new int[dimension()][dimension()];

            for (int row = 0; row < dimension(); row++) {
                for (int col = 0; col < dimension(); col++) {
                    twin[row][col] = tiles[row][col];
                }
            }

            int randRow1;
            int randCol1;
            int randRow2;
            int randCol2;

            do {
                randRow1 = StdRandom.uniformInt(dimension());
                randCol1 = StdRandom.uniformInt(dimension());
            } while (twin[randRow1][randCol1] == 0);

            do {
                randRow2 = StdRandom.uniformInt(dimension());
                randCol2 = StdRandom.uniformInt(dimension());
            } while (twin[randRow2][randCol2] == 0
                    || twin[randRow1][randCol1] == twin[randRow2][randCol2]);

            int swap = twin[randRow1][randCol1];
            twin[randRow1][randCol1] = twin[randRow2][randCol2];
            twin[randRow2][randCol2] = swap;
        }


        return new Board(twin);
    }


    // public static void main(String[] args) {
    //     // int[][] tiles = {
    //     //         { 0, 1, 3 },
    //     //         { 4, 2, 5 },
    //     //         { 7, 8, 6 }
    //     // };
    //
    //     int[][] tiles = {
    //             { 1, 5, 3, 8 },
    //             { 4, 2, 0, 9 },
    //             { 7, 15, 6, 10 },
    //             { 11, 12, 13, 14 }
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
    //     for (int i = 0; i < tiles.length; i++) {
    //         System.out.println(Arrays.toString(board.goalTiles[i]));
    //     }
    //
    //     for (Integer i : board.goalCoordinates.keys()) {
    //         System.out.println(
    //                 board.goalCoordinates.get(i).row + " " + board.goalCoordinates.get(i).col);
    //         System.out.println("-----");
    //     }
    //     System.out.println(board.hamming());
    //     System.out.println(board.manhattan());
    //     System.out.println();
    //
    //     System.out.println("******");
    //     for (Board b : board.neighbors()) {
    //         System.out.println(b.toString());
    //         System.out.println("000000");
    //     }
    //     System.out.println();
    //     System.out.println();
    //     System.out.println();
    //
    //
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
