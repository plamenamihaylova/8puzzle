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

    // public int compare(SearchNode o1, SearchNode o2) {
    //     return Integer.compare(o1.priority, o2.priority);
    // }
    //
}
