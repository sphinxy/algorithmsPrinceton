import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;

public class Board {
    private int n = 0;
    private int[][] blocks;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks)
    {
        n = blocks.length;
        int[][] copyBlocks = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                copyBlocks[i][j] = blocks[i][j];
            }
        }
        this.blocks = copyBlocks;
    }

    // board dimension n
    public int dimension()
    {
        return n;
    }

    // number of blocks out of place
    public int hamming()
    {
        int hamming = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                int placeIndex = getPlaceIndex(i, j);
                if (this.blocks[i][j] != placeIndex && placeIndex != 0)
                {
                    hamming++;
                }
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan()
    {
        int manhattan = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                int placeIndex = getPlaceIndex(i, j);
                if (blocks[i][j] != placeIndex && blocks[i][j] != 0)
                {
                    manhattan += getManhattanIndex(i, j, blocks[i][j]);

                }
            }
        }
        return manhattan;
    }

    // is this board the goal board?
    public boolean isGoal()
    {
        boolean goal = true;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                int placeIndex = getPlaceIndex(i, j);
                if (blocks[i][j] != placeIndex)
                {
                    goal = false;
                    break;
                }
            }
        }
        return goal;
    }

    // calculate right number for place in 2-dimensional array
    private int getPlaceIndex(int i, int j) {
        int placeIndex = (i)*n+j+1;
        if (placeIndex == n * n)
        {
            placeIndex = 0;
        }
        return placeIndex;
    }

    private int getManhattanIndex(int currentI, int currentJ,
                                  int desiredPlaceIndex)
    {
        int manhattanIndex = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (getPlaceIndex(i, j) == desiredPlaceIndex)
                {
                    manhattanIndex += Math.max(currentI, i) - Math.min(currentI, i);
                    manhattanIndex += Math.max(currentJ, j) - Math.min(currentJ, j);
                }
            }

        }
        return manhattanIndex;
    }


    // a board that is obtained by exchanging any pair of blocks
    public Board twin()
    {
        int[][] newBlocks = getBlocksCopy();

        while (true)
        {
            int randomFirstRow = StdRandom.uniform(n);
            int randomFirstCol = StdRandom.uniform(n);
            int randomSecondRow = StdRandom.uniform(n);
            int randomSecondCol = StdRandom.uniform(n);
            if (newBlocks[randomFirstRow][randomFirstCol] > 0 &&
                    newBlocks[randomSecondRow][randomSecondCol] > 0 &&
                    randomFirstRow != randomSecondRow &&
                    randomFirstCol != randomSecondCol)
            {
                swap(newBlocks, randomFirstRow, randomFirstCol,
                        randomSecondRow, randomSecondCol);
                return new Board(newBlocks);
            }
        }

    }

    private int[][] getBlocksCopy() {
        int[][] newBlocks = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                newBlocks[i][j] = this.blocks[i][j];
            }
        }
        return newBlocks;
    }

    private void swap(int[][] bewBlocks, int firstCol, int firstRow,
                      int secondCol, int secondRow)
    {
        int swapOld = bewBlocks[secondCol][secondRow];
        bewBlocks[secondCol][secondRow] = bewBlocks[firstCol][firstRow];
        bewBlocks[firstCol][firstRow] = swapOld;
    }

    // does this board equal y?
    public boolean equals(Object y)
    {
        if (y == null)
        {
            return false;
        }

        boolean equal =  y.toString().equals(this.toString());
        return equal;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()
    {
        ArrayList<Board> neighbors = new ArrayList<>();
        int zeroRow = 0;
        int zeroCol = 0;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (blocks[i][j] == 0)
                {
                    zeroRow = i;
                    zeroCol = j;
                }
            }
        }
        for (int shiftRow = -1; shiftRow <= 1; shiftRow = shiftRow + 2)
        {
            Board nextNeighbor = getNeighborsBoard(zeroRow, zeroCol, shiftRow, 0);
            if (nextNeighbor != null)
            {
                neighbors.add(nextNeighbor);
            }
        }
        for (int shiftCol = -1; shiftCol <= 1; shiftCol = shiftCol + 2)
        {
            Board nextNeighbor = getNeighborsBoard(zeroRow, zeroCol, 0, shiftCol);
            if (nextNeighbor != null)
            {
                neighbors.add(nextNeighbor);
            }
        }


        return neighbors;
    }

    private Board getNeighborsBoard(int zeroRow, int zeroCol,
                                    int shiftRow, int shiftCol)
    {
        int neighborRow = zeroRow + shiftRow;
        int neighborCol = zeroCol + shiftCol;
        if (neighborRow < 0 || neighborRow >= n
                || neighborCol < 0 || neighborCol >= n)
        {
            return null;
        }
        int[][] newBlocks = getBlocksCopy();
        swap(newBlocks, zeroRow, zeroCol, neighborRow, neighborCol);
        return new Board(newBlocks);


    }

    // string representation of this board (in the output format specified below)
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                blocks[i][j] = in.readInt();
            }
        }

         Board initial = new Board(blocks);

        // StdOut.println(initial);
//        StdOut.println(initial.hamming());
//        StdOut.println(initial.hamming());
//        blocks[2][0]=100;
//        StdOut.println(initial.hamming());
//        for (Board neighbor : initial.neighbors())
//        {
//            StdOut.println(neighbor);
//        }

         //solve the puzzle
        // StdOut.println("Initial");
        Solver solver = new Solver(initial);
         StdOut.println(solver.solution());
        // print solution to standard output
//        if (!solver.isSolvable())
//            StdOut.println("No solution possible");
//        else {
//            StdOut.println("Minimum number of moves = " + solver.moves());
//            for (Board board : solver.solution())
//                StdOut.println(board);
//        }
    }
}