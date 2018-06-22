/******************************************************************************
 *  Compilation:  javac Board.java
 *  Execution:    java Board filename1.txt filename2.txt ...
 *
 *  Immutable data structure to represent a single state of a board.
 ******************************************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Board {
  private int _n;
  private int[][] _blocks;
  private int _hamming = -1;
  private int _manhattan = -1;


  private class Neighbors implements Iterable<Board>, Iterator<Board> {
    private Board[] _boards;
    private int _length;
    private int _i = 0;
    public Neighbors() {
      int[] pos = findZero();
      int row = pos[0];
      int col = pos[1];

      _boards = new Board[4];
      if (row > 0) {
        _boards[_length++] = boardExch(row, col, row-1, col);
      }
      if (col < (_n-1)) {
        _boards[_length++] = boardExch(row, col, row, col+1);
      }
      if (row < (_n-1)) {
        _boards[_length++] = boardExch(row, col, row+1, col);
      }
      if (col > 0) {
        _boards[_length++] = boardExch(row, col, row, col-1);
      }
    }
    public Iterator<Board> iterator() {
      return this;
    }
    public boolean hasNext() {
      return _i < _length;
    }
    public Board next() {
      if (!hasNext()) {
        throw new NoSuchElementException("There are no next Boards in Neighbors.");
      }
      return _boards[_i++];
    }

    private int[] findZero() {
      for (int i = 0; i < _n; ++i) {
        for (int j = 0; j < _n; ++j) {
          if (_blocks[i][j] == 0) {
            int[] pos = new int[2];
            pos[0] = i;
            pos[1] = j;
            return pos;
          }
        }
      }
      return new int[0];
    }
  }

  /**
   * construct a board from an n-by-n array of blocks
   * (where blocks[i][j] = block in row i, column j)
   */
  public Board(int[][] blocks) {
    _n = blocks.length;
    _blocks = new int[_n][_n];
    // This loop is unnecessary if arraycopy does a deep copy.
    for (int i = 0; i < _n; ++i) {
      for (int j = 0; j < _n; ++j) {
        _blocks[i][j] = blocks[i][j];
      }
    }
  }
  /**
   * board dimension n
   */
  public int dimension() {
    return _n;
  }
  /**
   * number of blocks out of place
   */
  public int hamming() {
    if (_hamming == -1) {
      _hamming = 0;
      for (int i = 0; i < _n; ++i) {
        for (int j = 0; j < _n; ++j) {
          if (i == _n-1 && j == _n-1) {
            // Don't check the last spot.
            continue;
          }
          if (_blocks[i][j] == 0 || _blocks[i][j] != (i*_n)+j+1) {
            _hamming += 1;
          }
        }
      }
    }
    return _hamming;
  }
  /**
   * sum of Manhattan distances between blocks and goal
   */
  public int manhattan() {
    if (_manhattan == -1) {
      _manhattan = 0;
      for (int i = 0; i < _n; ++i) {
        for (int j = 0; j< _n; ++j) {
          if (_blocks[i][j] == 0) {
            //_manhattan += ((_n-1)-i) + ((_n-1)-j);
            // Don't check zero.
          }
          else {
            _manhattan += Math.abs(((_blocks[i][j]-1)/_n)-i);
            _manhattan += Math.abs(((_blocks[i][j]-1)%_n)-j);
          }
        }
      }
    }
    return _manhattan;
  }
  /**
   * is this board the goal board?
   */
  public boolean isGoal() {
    return hamming() == 0;
  }
  /**
   * a board that is obtained by exchanging any pair of blocks
   */
  public Board twin() {
    int fromRow = 0;
    int fromCol = 0;
    int toRow = 0;
    int toCol = 0;
    boolean fromFound = false;
    for (int i = 0; i < _n; ++i) {
      for (int j = 0; j < _n; ++j) {
        if (_blocks[i][j] != 0) {
          if (fromFound == false) {
            fromRow = i;
            fromCol = j;
            fromFound = true;
          }
          else {
            toRow = i;
            toCol = j;
            return boardExch(fromRow, fromCol, toRow, toCol);
          }
        }
      }
    }
    return boardExch(0, 0, 0, 1);
  }
  /**
   * does this board equal y?
   */
  public boolean equals(Object y) {
    if (y == null) {
      return false;
    }
    if (this.getClass() != y.getClass()) {
      return false;
    }
    Board that = (Board) y;
    if (this._n != that._n) {
      return false;
    }
    for (int i = 0; i < _n; ++i) {
      for (int j = 0; j < _n; ++j) {
        if (this._blocks[i][j] != that._blocks[i][j]) {
          return false;
        }
      }
    }
    return true;
  }
  /**
   * all neighboring boards
   */
  public Iterable<Board> neighbors() {
    return new Neighbors();
  }
  /**
   * string representation of this board (in the output format specified below)
   */
  public String toString() {
    StringBuilder out = new StringBuilder();
    int padding = 1;
    int n = _n * _n;
    while(n >= 10) {
      padding += 1;
      n /= 10;
    }
    out.append(_n);
    for (int i = 0; i < _n; ++i) {
      out.append("\n");
      for (int j = 0; j < _n; ++j) {
        if (j > 0) {
          out.append(" ");
        }
        String v = Integer.toString(_blocks[i][j]);
        for (int p = v.length(); p < padding; ++p) {
          out.append(" ");
        }
        out.append(v);
      }
    }
    return out.toString();
  }

  private Board boardExch(int fromRow, int fromCol, int toRow, int toCol) {
    int first = _blocks[fromRow][fromCol];
    int second = _blocks[toRow][toCol];
    _blocks[fromRow][fromCol] = second;
    _blocks[toRow][toCol] = first;
    Board newBoard = new Board(_blocks);
    _blocks[fromRow][fromCol] = first;
    _blocks[toRow][toCol] = second;
    return newBoard;
  }


  /**
   * unit tests (not graded)
   */
  public static void main(String[] args) {
    // for each command-line argument
    for (String filename : args) {
      // read in the board specified in the filename
      In in = new In(filename);
      int n = in.readInt();
      int[][] tiles = new int[n][n];
      for (int i = 0; i < n; i++) {
          for (int j = 0; j < n; j++) {
              tiles[i][j] = in.readInt();
          }
      }

      // solve the slider puzzle
      Board initial = new Board(tiles);
      // Solver solver = new Solver(initial);
      // StdOut.println(filename + ": " + solver.moves());
      StdOut.println(initial);

      StdOut.println("isGoal:");
      StdOut.println(initial.isGoal());

      StdOut.println("Hamming:");
      StdOut.println(initial.hamming());

      StdOut.println("Manhattan:");
      StdOut.println(initial.manhattan());

      StdOut.println("Twin:");
      StdOut.println(initial.twin());

      StdOut.println("Neighbors:");
      for (Board neighbor : initial.neighbors()) {
        StdOut.println(neighbor);
      }
    }
  }
}
