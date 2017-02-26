import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;

public class SAP {
    private final Digraph g;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G)
    {
        if (G == null) {
            throw new NullPointerException("argument a nouns");
        }
        g = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w)
    {
        Iterable<Integer> vList = getList(v);
        Iterable<Integer> wList = getList(w);
        validateBorder(vList);
        validateBorder(wList);
        return findMinPath(vList, wList)[0];

    }

    // find minimal ancestral path and ancestor between v and w,
    // return array respectively, [-1,-1] if now such path;
    private int[] findMinPath(Iterable<Integer> v, Iterable<Integer> w) {
        int minPathLength = -1;
        int minPathAncestor = -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(g, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(g, w);
        for (int ancestor  = 0; ancestor  < g.V(); ancestor++) {
            if (bfsV.hasPathTo(ancestor) && bfsW.hasPathTo(ancestor))
            {
                int ancestorPathLength = bfsV.distTo(ancestor)
                        + bfsW.distTo(ancestor);
                if (minPathLength < 0 || minPathLength > ancestorPathLength)
                {
                    minPathLength = ancestorPathLength;
                    minPathAncestor = ancestor;
                }
            }
        }
        return new int[] { minPathLength, minPathAncestor };
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w)
    {
        Iterable<Integer> vList = getList(v);
        Iterable<Integer> wList = getList(w);
        validateBorder(vList);
        validateBorder(wList);
       return findMinPath(vList, wList)[1];
    }
    // create Iterable<Integer> for single int
    private ArrayList<Integer> getList(int data) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(data);
        return list;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w;
    // -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w)
    {
        validateNull(v);
        validateNull(w);
        validateBorder(v);
        validateBorder(w);

        return findMinPath(v, w)[0];
    }


    // a common ancestor that participates in shortest ancestral path;
    // -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        validateNull(v);
        validateNull(w);
        validateBorder(v);
        validateBorder(w);
        return findMinPath(v, w)[1];
    }

    private void validateNull(Object object)
    {
        if (object == null) {
            throw new NullPointerException("argument is null");
        }
    }

    private void validateBorder(Iterable<Integer> vertexes)
    {
        for (int vertex : vertexes) {
            if (vertex < 0 || vertex > g.V() - 1) {
                throw new IndexOutOfBoundsException("not between 0 and G.V() - 1");
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        StdOut.printf("Sap started, type two numbers");
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
        StdOut.printf("Sap finished");
    }
}