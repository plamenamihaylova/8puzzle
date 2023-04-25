/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class EvenSizePuzzleSolver {
    // Returns true if the given puzzle is solvable, false otherwise
    public static boolean isSolvable(int[][] puzzle) {
        int n = puzzle.length; // Get the size of the puzzle
        int[] flat = flatten(puzzle); // Flatten the 2D puzzle array into a 1D array
        int inversions = countInversions(flat); // Count the number of inversions

        // For a puzzle to be solvable, the following conditions must be met:
        // - The number of inversions must be even
        // - The row number of the blank tile from the bottom must be even
        int blankRow = n - getBlankRow(
                puzzle); // Get the row number of the blank tile from the bottom
        return (inversions % 2 == 0) == (blankRow % 2 == 0);
    }

    // Helper method to flatten a 2D array into a 1D array
    private static int[] flatten(int[][] puzzle) {
        int n = puzzle.length;
        int m = puzzle[0].length;
        int[] flat = new int[n * m];
        int k = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                flat[k++] = puzzle[i][j];
            }
        }
        return flat;
    }

    // Helper method to count the number of inversions in an array
    private static int countInversions(int[] arr) {
        int inversions = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] != 0 && arr[j] != 0 && arr[i] > arr[j]) {
                    inversions++;
                }
            }
        }
        return inversions;
    }

    // Helper method to get the row number of the blank tile from the bottom
    private static int getBlankRow(int[][] puzzle) {
        int n = puzzle.length;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < puzzle[i].length; j++) {
                if (puzzle[i][j] == 0) {
                    return i;
                }
            }
        }
        return -1; // Blank tile not found
    }

    public static void main(String[] args) {
        // Example usage for even-sized puzzle
        int[][] puzzle1 = {
                { 1, 2, 3 },
                { 4, 6, 5 },
                { 8, 7, 0 }
        };
        boolean solvable1 = isSolvable(puzzle1);
        System.out.println("Puzzle 1 is solvable: " + solvable1);
    }
}