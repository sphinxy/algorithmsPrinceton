import edu.princeton.cs.algs4.MinPQ;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {

    private SearchNode solutionNode;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial)
    {
        if (initial == null) {
            throw new NullPointerException("initial argument is null");
        }

        MinPQ<SearchNode> pq = new MinPQ<>();
        MinPQ<SearchNode> pqTwin = new MinPQ<>();
        SearchNode node = new SearchNode(initial, 0, null);
        pq.insert(node);

        SearchNode nodeTwin = new SearchNode(initial.twin(), 0, null);
        pqTwin.insert(nodeTwin);
        int pqId = 1;
        while (solutionNode == null)
        {
            SearchNode currentNode;
            if (pqId > 0)
            {
                currentNode = pq.delMin();
            }
            else
            {
                currentNode = pqTwin.delMin();
            }

            // StdOut.println(currentNode.moves*pqId);
            if (currentNode.board.isGoal()) {
                if (pqId > 0) {
                    solutionNode = currentNode;
                }
                break;
            }
            for (Board neighbor : currentNode.board.neighbors())
            {
                SearchNode newNode =
                        new SearchNode(neighbor, currentNode.moves+1, currentNode);
                if (currentNode.previous ==  null ||
                        (!newNode.board.equals(currentNode.previous.board)))
                {
                    if (pqId > 0)
                    {
                        pq.insert(newNode);
                    }
                    else
                    {
                        pqTwin.insert(newNode);
                    }
                }
            }
            pqId = -1*pqId;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable()
    {
        return solutionNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()
    {
        if (!isSolvable())
        {
            return -1;
        }
        if (solutionNode == null)
        {
            return -1;
        }
        return solutionNode.moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()
    {
        ArrayList<Board> solution = new ArrayList<>();
        SearchNode currentNode = solutionNode;
        if (currentNode == null)
            return null;
        while (currentNode != null)
        {
            solution.add(currentNode.board);
            currentNode = currentNode.previous;
        }
        Collections.reverse(solution);
        return solution;
    }

    private class SearchNode implements Comparable<SearchNode>
    {
        private int moves;

        private Board board;

        private SearchNode previous;

        public SearchNode(Board board, int moves, SearchNode previous)
        {
            this.moves = moves;
            this.board = board;
            this.previous = previous;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.board.manhattan() + this.moves >
                    that.board.manhattan() + that.moves)
            {
                return +1;
            }
            return -1;
        }
    }
    // solve a slider puzzle (given below)
    public static void main(String[] args)
    {

    }
}
