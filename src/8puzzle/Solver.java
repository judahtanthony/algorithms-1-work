/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Execution:    java Solver filename1.txt filename2.txt ...
 *
 *  Solve a list of puzzles.
 ******************************************************************************/
import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;

public class Solver {
  private SearchNode _goal = null;

  private class SearchNode implements Comparable<SearchNode> {
    private Board _value = null;
    private SearchNode _parent = null;
    private int _priority = -1;
    private int _moves = -1;

    public SearchNode(Board v, SearchNode p) {
      _value = v;
      _parent = p;
    }
    public Board value() {
      return _value;
    }
    public SearchNode parent() {
      return _parent;
    }
    public int moves() {
      if (_moves == -1) {
        SearchNode step = this;
        while (step != null) {
          _moves += 1;
          step = step.parent();
        }
      }
      return _moves;
    }
    public int priority() {
      if (_priority == -1) {
        _priority = _value.manhattan() + moves();
      }
      return _priority;
    }
    public int compareTo(SearchNode that) {
      int first = this.priority();
      int second = that.priority();
      return first < second ? -1 : second < first ? 1 : 0;
    }
  }

  /**
   * find a solution to the initial board (using the A* algorithm)
   */
  public Solver(Board initial) {
    MinPQ<SearchNode> _pq = new MinPQ<SearchNode>();
    MinPQ<SearchNode> _pq2 = new MinPQ<SearchNode>();
    _pq.insert(new SearchNode(initial, null));
    _pq2.insert(new SearchNode(initial.twin(), null));
    while (true) {
      SearchNode goalSN = _pq.delMin();
      if (goalSN == null || goalSN.value().isGoal()) {
        _goal = goalSN;
        break;
      }
      SearchNode twinSN = _pq2.delMin();
      if (twinSN.value().isGoal()) {
        break;
      }
      for (Board neighbor : goalSN.value().neighbors()) {
        if (goalSN.parent() == null || !goalSN.parent().value().equals(neighbor)) {
          _pq.insert(new SearchNode(neighbor, goalSN));
        }
      }
      for (Board neighbor : twinSN.value().neighbors()) {
        if (twinSN.parent() == null || !twinSN.parent().value().equals(neighbor)) {
          _pq2.insert(new SearchNode(neighbor, twinSN));
        }
      }
    }
  }
  /**
   * is the initial board solvable?
   */
  public boolean isSolvable() {
    return _goal != null;
  }
  /**
   * min number of moves to solve initial board; -1 if unsolvable
   */
  public int moves() {
    int m = -1;
    if (isSolvable()) {
      m = _goal.moves();
    }
    return m;
  }
  /**
   * sequence of boards in a shortest solution; null if unsolvable
   */
  public Iterable<Board> solution() {
    Stack<Board> stack = null;
    if (isSolvable()) {
      stack = new Stack<Board>();
      SearchNode step = _goal;
      while (step != null) {
        stack.push(step.value());
        step = step.parent();
      }
    }
    return stack;
  }


  /**
   * solve a slider puzzle (given below)
   */
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

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
