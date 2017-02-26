import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordnet = null;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[][] distances = new int[nouns.length][nouns.length];
        // triaglular matrix of distances, without main diagonal
        for (int r = 0; r < nouns.length; r++)
        {
            for (int c = r + 1; c < nouns.length; c++)
            {
                // StdOut.println(nouns[r] + " " + nouns[c]);
                distances[r][c] = wordnet.distance(nouns[r], nouns[c]);
            }
        }
        String outcast = nouns[0];
        int maxdistance = 0;
        int distance = 0;
        for (int r = 0; r < nouns.length; r++)
        {
            for (int c = 0; c < nouns.length; c++)
            {
                if (r != c)
                {
                    distance += distances[Math.min(r, c)][Math.max(r, c)];
                }
            }
            if (distance >= maxdistance)
            {
                maxdistance = distance;
                outcast = nouns[r];
            }
        }

        return outcast;
    }


    // test client
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}