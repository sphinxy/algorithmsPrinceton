import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.HashSet;

public class WordNet {

    private HashMap<String, HashSet<Integer>> nouns = new HashMap<>();
    private HashMap<Integer, String> ids = new HashMap<>();
    private Digraph hypernymsDigraph = null;
    private SAP sap = null;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        validateNull(synsets);
        validateNull(hypernyms);

        int synsetsCount = parseSynsetsToNouns(synsets);
        parseHypernymsToDigraph(hypernyms, synsetsCount);
        validateRootedDAG(hypernymsDigraph);
        sap = new SAP(hypernymsDigraph);
    }

    private void parseHypernymsToDigraph(String hypernyms, int synsetsCount)
    {
        hypernymsDigraph = new Digraph(synsetsCount);
        In hypernymsIn = new In(hypernyms);
        while (!hypernymsIn.isEmpty()) {
            String[] hypernymLine = hypernymsIn.readLine().split(",");
            int synsetId = Integer.parseInt(hypernymLine[0]);
            for (int i = 1; i < hypernymLine.length; i++) {
                hypernymsDigraph.addEdge(
                        synsetId,
                        Integer.parseInt(hypernymLine[i]));
            }
        }

    }

    private int parseSynsetsToNouns(String synsets)
    {
        int synsetsCount = 0;
        In synsetsIn = new In(synsets);
        while (!synsetsIn.isEmpty()) {
            synsetsCount++;
            String[] synsetLine = synsetsIn.readLine().split(",");
            int synsetId = Integer.parseInt(synsetLine[0]);
            String[] synsetNouns = synsetLine[1].split(" ");

            for (String noun: synsetNouns)
            {
                // get existing or create new set of sunsetIds for this noun
                if (!nouns.containsKey(noun))
                    nouns.put(noun, new HashSet<Integer>());

                HashSet<Integer> nounIds =  nouns.get(noun);
                // add id to related Ids
                if (!nounIds.contains(synsetId))
                {
                    nounIds.add(synsetId);
                }

                // save all nouns for this id, useful in sap
                if (!ids.containsKey(synsetId))
                    ids.put(synsetId, synsetLine[1]);
            }
        }
        return synsetsCount;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        validateNull(word);
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        HashSet<Integer> nounAIds = validateAndGetNouns(nounA);
        HashSet<Integer> nounBIds = validateAndGetNouns(nounB);
        return sap.length(nounAIds, nounBIds);
    }

    private HashSet<Integer> validateAndGetNouns(String nounA) {
        validateNull(nounA);
        HashSet<Integer> nounAIds =  nouns.get(nounA);
        validateNoun(nounAIds);
        return nounAIds;
    }

    // a synset (second field of synsets.txt) that is the common ancestor
    // of nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        HashSet<Integer> nounAIds = validateAndGetNouns(nounA);
        HashSet<Integer> nounBIds = validateAndGetNouns(nounB);
        int ancestor = sap.ancestor(nounAIds, nounBIds);
        return ids.get(ancestor);
    }

    private void validateNoun(Object object)
    {
        if (object == null)
        {
            throw new IllegalArgumentException("argument is null");
        }
    }

    private void validateNull(Object object)
    {
        if (object == null)
        {
            throw new NullPointerException("Non a wordnet noun");
        }
    }

    private void validateRootedDAG(Digraph digraph) {
        int rootCount = 0;
        for (int v = 0; v < digraph.V(); v++)
        {
            if (digraph.outdegree(v) == 0 && digraph.indegree(v) > 0)
            {
                rootCount++;
            }
        }
        if (rootCount == 0)
        {
            throw new IllegalArgumentException("No root");
        }
        if (rootCount > 1)
        {
            throw new IllegalArgumentException("More than one root");
        }
    }

    // do unit testing of this class
    public static void main(String[] args)
    {
        try {

            WordNet testWordNetNonRootedDag = new WordNet("wordnet\\synsets3.txt",
                    "wordnet\\hypernyms3InvalidTwoRoots.txt");
        }
        catch (IllegalArgumentException ex)
        {
            StdOut.printf(ex.getMessage()+"\n");
        }



        WordNet testWordNet = new WordNet("wordnet\\synsets15.txt",
                "wordnet\\hypernyms15Tree.txt");

        // nouns asserts
        String fakeNoun = "fsdfsdfsdflgjdsfl;sd3hsdfg";
        String lastNoun = null;
        for (String noun: testWordNet.nouns())
        {
            StdOut.printf("Noun is = %s\n", noun);
            lastNoun = noun;
        }
        StdOut.printf("isNoun(LastNoun)= %b\n", testWordNet.isNoun(lastNoun));
        StdOut.printf("isNoun(FakeNoun)= %b\n", testWordNet.isNoun(fakeNoun));

        // hypernyms asserts
        StdOut.printf("sap is %s\n", testWordNet.sap("o", "m"));
        StdOut.printf("distance is %s\n", testWordNet.distance("o", "m"));

        try
        {
            StdOut.printf("fake noun sap is %s\n", testWordNet.sap("o", fakeNoun));
        }
        catch (IllegalArgumentException ex)
        {
            StdOut.printf(ex.getMessage()+"\n");
        }
        try
        {
            StdOut.printf("fake noun distance is %s\n",
                    testWordNet.distance("o", fakeNoun));
        }
        catch (IllegalArgumentException ex)
        {
            StdOut.printf(ex.getMessage()+"\n");
        }



    }
}