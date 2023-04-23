public class SearchNode implements Comparable<SearchNode> {
    Board board;
    int priority;
    SearchNode previous;

    public SearchNode(Board board, int priority, SearchNode previous) {
        this.board = board;
        this.priority = priority;
        this.previous = previous;
    }


    public int compareTo(SearchNode x) {
        return Integer.compare(this.priority, x.priority);
    }

    public int compare(SearchNode o1, SearchNode o2) {
        return Integer.compare(o1.priority, o2.priority);
    }


    // public boolean equals(Object y) {
    //     // optimize for object equality
    //     if (y == this) return true;
    //     // check for null
    //     if (y == null) return false;
    //     // objects must be in the same class
    //     if (y.getClass() != this.getClass()) return false;
    //
    //     // cast is guaranteed to succeed
    //     SearchNode that = (SearchNode) y;
    //     // check if all significant fields are the same
    //     if (this.priority != that.priority) return false;
    //     if (!this.board.equals(that.board)) return false;
    //     return true;
    // }
}
