package MBEA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solver {
    public static void main(String args[])
    {
        List<List<Integer>> adjMatrix = new ArrayList<List<Integer>>();
        adjMatrix.add(Arrays.asList(0, 1, 0, 1));
        adjMatrix.add(Arrays.asList(0, 1, 1, 0));
        adjMatrix.add(Arrays.asList(0, 0, 0, 1));
        adjMatrix.add(Arrays.asList(0, 0, 0, 1));

//        for(List<Integer> v:adjMatrix)
//            System.out.println(v);

        BipartiteGraph bg = new BipartiteGraph(adjMatrix);
        bg.printNeighbourhoods();
    }
}
