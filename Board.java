import edu.princeton.cs.algs4.BinarySearchST;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private BinarySearchST<Integer, TileCoordinates> goalCoordinates = new BinarySearchST<>();

    private int[][] tiles;
    private int[][] twin;
    private int dimension;


    // create a board from an n-by-n array of tiles,
    public Board(int[][] tiles) {
        this.tiles = copyBoardTiles(tiles);
        //         new int[tiles.length][tiles.length];
        //
        // for (int row = 0; row < tiles.length; row++) {
        //     for (int col = 0; col < tiles.length; col++) {
        //         // immutable tiles
        //         this.tiles[row][col] = tiles[row][col];
        //
        //     }
        // }

        dimension = this.tiles.length;
        goalCoordinates = generateGoalCoordinates();
    }

    private BinarySearchST<Integer, TileCoordinates> generateGoalCoordinates() {
        BinarySearchST<Integer, TileCoordinates> result = new BinarySearchST<>();
        // int i = 1;
        for (int x = 0, i = 1; x < dimension(); x++) {
            for (int y = 0; y < dimension(); y++, i++) {
                if (i == dimension() * dimension()) result.put(0, new TileCoordinates(x, y));
                else result.put(i, new TileCoordinates(x, y));
                // i++;
            }
        }
        return result;
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
        // int i = 1;
        for (int row = 0, i = 1; row < dimension(); row++) {
            for (int col = 0; col < dimension(); col++, i++) {
                if (i == dimension() * dimension()) i = 0;
                if (this.tiles[row][col] != i) return false;
                i++;
            }
        }
        return true;
        // return this.equals(new Board(goalTiles));
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
        if (dimension() != ((Board) y).dimension()) return false;
        if (!Arrays.deepEquals(this.tiles, that.tiles)) return false;
        return true;
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> neighbors = new ArrayList<Board>();

        TileCoordinates emptyTile = getEmptyTileCoordinates();
        if (emptyTile == null) return neighbors;

        if (emptyTile.row < tiles.length - 1) {
            neighbors.add(
                    newNeighborByRow(emptyTile.row, emptyTile.col, emptyTile.row + 1));
        }

        if (emptyTile.col < tiles.length - 1) {
            neighbors.add(
                    newNeighborByColumn(emptyTile.row, emptyTile.col, emptyTile.col + 1));
        }

        if (emptyTile.row > 0) {
            neighbors.add(
                    newNeighborByRow(emptyTile.row, emptyTile.col, emptyTile.row - 1));
        }

        if (emptyTile.col > 0) {
            neighbors.add(
                    newNeighborByColumn(emptyTile.row, emptyTile.col, emptyTile.col - 1));
        }

        return neighbors;
    }

    private TileCoordinates getEmptyTileCoordinates() {
        for (int row = 0; row < tiles.length; row++) {
            for (int col = 0; col < tiles[row].length; col++)
                if (tiles[row][col] == 0) return new TileCoordinates(row, col);

        }
        return null;
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
        if (twin == null) {
            twin = copyBoardTiles();
            int row1, col1, row2, col2;

            do {
                row1 = StdRandom.uniformInt(dimension());
                col1 = StdRandom.uniformInt(dimension());
            } while (twin[row1][col1] == 0);

            do {
                row2 = StdRandom.uniformInt(dimension());
                col2 = StdRandom.uniformInt(dimension());
            } while (twin[row2][col2] == 0
                    || twin[row1][col1] == twin[row2][col2]);

            int swap = twin[row1][col1];
            twin[row1][col1] = twin[row2][col2];
            twin[row2][col2] = swap;
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
