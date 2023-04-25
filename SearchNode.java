public class SearchNode implements Comparable<SearchNode> {
    Board board;
    int priorityManhattan;
    int priorityHamming;
    int priority;
    int moves;
    SearchNode previous;

    public SearchNode(Board board, int moves, SearchNode previous) {
        this.board = board;
        this.priorityManhattan = board.manhattan();
        this.priorityHamming = board.hamming();
        this.priority = priorityManhattan + this.moves;
        // this.priorityManhattan += moves;
        this.moves = moves;
        this.previous = previous;
    }


    public int compareTo(SearchNode x) {
        if (this.priority == x.priority) {
            if (this.priorityHamming > x.priorityHamming) return 1;
            else if (this.priorityHamming < x.priorityHamming) return -1;
            else return 0;
        }
        else if (this.priority > x.priority) return 1;
        else return 0;
        // return Integer.compare(this.priority, x.priority);
    }

    // public static int compare(int x, int y) {
    //     return (x < y) ? -1 : ((x == y) ? 0 : 1);
    // }

    // public int compare(SearchNode o1, SearchNode o2) {
    //     return Integer.compare(o1.priority, o2.priority);
    // }
    //
}
