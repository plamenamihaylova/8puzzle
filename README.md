<img height="400" src="logo.png" title="8 puzzle logo" width="1000"/>

### Slider puzzle

**Algorithms Part 1 | Coursera course | Week 4
Assignment | [my coursera profile](https://www.coursera.org/user/045cf702be8b31ef1aa039e2b4f07db6)**

---
<!-- TOC -->

* [Slider puzzle](#slider-puzzle)
    * [Task specification](#task-specification)
        * [The problem](#the-problem)
        * [Best-first search](#best-first-search)
        * [Board API](#board-api-)
        * [Solver API:](#solver-api)
    * [Solution exploanation](#solution-exploanation)
    * [Useful resources that have helped me in completing this assignment:](#useful-resources-that-have-helped-me-in-completing-this-assignment)

<!-- TOC -->

---

### Task specification

🔗Detailed specifications for the assignment can be
found [here](https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/specification.php).

The task is to write a program that solves the 8-puzzle problem (and its natural generalizations) using the A\*
search algorithm.

#### The problem

The 8-puzzle problem is a puzzle invented and popularized by Noyes Palmer Chapman in the 1870s.
It is played on a 3-by-3 grid with 8 square blocks labeled 1 through 8 and a blank square.
The goal of the program is to rearrange the blocks so that they are in order, using as few moves as possible.
The permitted directions for slide movement are horizontal or vertical into the blank square.
For example:

```agsl
    1  3        1     3        1  2  3        1  2  3        1  2  3
 4  2  5   =>   4  2  5   =>   4     5   =>   4  5      =>   4  5  6
 7  8  6        7  8  6        7  8  6        7  8  6        7  8 

 initial        1 left          2 up          5 left          goal
 ```

---

#### Best-first search

Description of solution to the problem that illustrates a general artificial intelligence methodology known as the A*
search algorithm.
Search node definition of the game is composed of board, the number of moves made to reach the board, and the
predecessor
search node.
First, insert the initial search node (the initial board, 0 moves, and a null predecessor search node) into a priority
queue.
Then, delete from the priority queue the search node with the minimum priority, and insert onto the priority queue all
neighboring search nodes (those that can be reached in one move from the dequeued search node).
Repeat this procedure until the search node dequeued corresponds to a goal board.
The success of this approach hinges on the choice of priority function for a search node.
We consider two priority functions:

* *Hamming priority function.*
  The number of blocks in the wrong position, plus the number of moves made so far to get to the search node.
  Intuitively, a search node with a small number of blocks in the wrong position is close to the goal, and we prefer a
  search node that have been reached using a small number of moves.
* *Manhattan priority function.*
  The sum of the Manhattan distances (sum of the vertical and horizontal distance) from the blocks to their goal
  positions, plus the number of moves made so far to get to the search node.
  For example, the Hamming and Manhattan priorities of the initial search node below are 5 and 10, respectively.

```agsl
 8  1  3        1  2  3     1  2  3  4  5  6  7  8    1  2  3  4  5  6  7  8
 4     2        4  5  6     ----------------------    ----------------------
 7  6  5        7  8        1  1  0  0  1  1  0  1    1  2  0  0  2  2  0  3

 initial          goal         Hamming = 5 + 0          Manhattan = 10 + 0
```

---

#### Board API

```
public class Board {
    public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    public int dimension()                 // board dimension n
    public int hamming()                   // number of blocks out of place
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    public boolean isGoal()                // is this board the goal board?
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    public boolean equals(Object y)        // does this board equal y?
    public Iterable<Board> neighbors()     // all neighboring boards
    public String toString()               // string representation of this board (in a specific output format)
    public static void main(String[] args) // unit tests (not graded)
}
```

---

#### Solver API:

```
public class Solver {
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    public boolean isSolvable()            // is the initial board solvable?
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    public static void main(String[] args) // solve a slider puzzle
}
```

---

### Solution exploanation

I have implemented two different solutions.
I have firstly solved the task using the approach in SolverTwo class.
In the process of trying to get the maximum score I realized thisapproach wasn't solving the problem in the best way.  
I wasn't using all board's methods that could have helped me when checking wheather a puzzle is solvable or not.
So, I redid my logic to check sumultaneously if the current board or its twin are solvable or not.
If the twin board is solvable, the main board can't be solvable.

---

### Useful resources that have helped me in completing this assignment:

🔗[Prinston's 8puzzle assignment specification](https://www.cs.princeton.edu/courses/archive/spr08/cos226/assignments/8puzzle.html)

🔗[Prinston's 8puzzle assignment FAQ](https://www.cs.princeton.edu/courses/archive/spring11/cos226/checklist/8puzzle.html)

🔗[Prinston's 8puzzle assignment checkilist](https://www.cs.princeton.edu/courses/archive/fall19/cos226/assignments/8puzzle/checklist.php)

🔗[Prinstion's video explonation of the assignment](https://www.youtube.com/watch?v=d6aRjJKDfpY&list=LL&index=6)

🔗[8 Puzzle - an application using artificial intelliggence](https://www.d.umn.edu/~jrichar4/8puz.html)

---
