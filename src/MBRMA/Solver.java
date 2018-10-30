package MBRMA;

import javafx.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Solver {
    public static void main(String args[])
    {

    // ****TEST 1: To test MBEA/MBC with small manually generated adjacency matrices****

//        List<List<Integer>> adjMatrix = new ArrayList<>();
//        Scanner input = null;
//
//        try
//        {
//            input = new Scanner(new File("matrix1.txt"));
//        }
//        catch (FileNotFoundException e)
//        {
//            e.printStackTrace();
//        }
//
//        while(input.hasNextLine())
//        {
//            Scanner colReader = new Scanner(input.nextLine());
//            List<Integer> col = new ArrayList<>();
//            while(colReader.hasNextInt())
//            {
//                col.add(colReader.nextInt());
//            }
//            adjMatrix.add(col);
//        }
//
//        BicliqueFinder bicliqueFinder = new BicliqueFinder(new BipartiteGraph(adjMatrix));
//        bicliqueFinder.solve("new");
//        System.out.println("maximal bicliques: \n"+bicliqueFinder.toStringBicliqueF(bicliqueFinder.getMaximalBicliques()));
//        System.out.println("Total execution time: " + (endTime-startTime) + "ms");


    //    ****TEST 2: Benchmark Testing for Random Input****

//        List<List<Integer>> adjMatrix = new ArrayList<>();
//        Random random = new Random();
//
//        int rows = 20;
//        int columns = 20;
//        double density = 0.7;
//        for(int i=0;i<rows;i++)
//        {
//            adjMatrix.add(new ArrayList<>());
//            for(int j=0;j<columns;j++)
//            {
//                if(random.nextDouble() < density)
//                    adjMatrix.get(i).add(1);
//                else
//                    adjMatrix.get(i).add(0);
//            }
//        }
//
//        BicliqueFinder bicliqueFinder = new BicliqueFinder(new BipartiteGraph(adjMatrix));
//
//        long startTime = System.currentTimeMillis();
//        bicliqueFinder.solve("roles");
//        long endTime = System.currentTimeMillis();
//
//        System.out.println("*******Finding Roles*******");
//        System.out.println("Roles:\n"+bicliqueFinder.toStringBicliqueF(bicliqueFinder.getRoles()));
//        System.out.println("Number of roles:"+bicliqueFinder.getRoles().size());
////        System.out.println("maximal bicliques: \n"+bicliqueFinder.toStringBicliqueF(bicliqueFinder.getMaximalBicliques()));
//        System.out.println("Total execution time: " + (endTime-startTime) + "ms");

    //    ****TEST 3: Comparing |R| with A. Ene et al., 2008 datasets used for testing their greedy MBC****


        List<List<Integer>> UA = new ArrayList<>();
        List<List<Integer>> PA = new ArrayList<>();
        List<List<Integer>> UPA = new ArrayList<>();
        Scanner input = null;
        int UArowNum=0,UAcolNum=0,PAcolNum=0,PArowNum=0;
        int t=0,k=0;
        String fileName = null;
        fileName = args[0];
        try {
            input = new Scanner(new File("datasets/greedyMBC_Ene_2008/UA_"+fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        while(input.hasNextLine())
        {
            if(t==0){
                UArowNum = Integer.parseInt(input.nextLine());
                UAcolNum = Integer.parseInt(input.nextLine());
                t++;
            }
            else
                {
                    Scanner colReader = new Scanner(input.nextLine());
                    List<Integer> col = new ArrayList<>();
                    while (colReader.hasNextInt()) {
                        col.add(colReader.nextInt());
                    }
                    UA.add(col);
            }
        }

        try {
            input = new Scanner(new File("datasets/greedyMBC_Ene_2008/PA_"+fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (input.hasNextLine())
        {

            if(k==0) {
                  PArowNum = Integer.parseInt(input.nextLine());
                PAcolNum = Integer.parseInt(input.nextLine());
                k++;
            }
            else
                {
                    Scanner colReader = new Scanner(input.nextLine());
                    List<Integer> col = new ArrayList<>();
                    while (colReader.hasNextInt()) {
                        col.add(colReader.nextInt());
                    }
                    PA.add(col);
                }


        }
//        System.out.println("row"+UArowNum);
//        System.out.println("col"+UAcolNum);
//        System.out.println(UA.size());
//        for(int q=0;q<UArowNum;q++)
//        {
//            for (int p = 0; p <UAcolNum; p++)
//                System.out.print(UA.get(q).get(p));
//            System.out.println();
//        }
        for(int i=0;i<UArowNum;i++)
        {
            List<Integer> index = new ArrayList<>();
            List<Integer> UPArow = new ArrayList<>();
            for(int j=0;j<UAcolNum ;j++)
            {
                if(UA.get(i).get(j)==1)
                {
                    index.add(j);
                }
            }

            for(int q=0;q<PAcolNum;q++)
            {
                int element = 0;
                for(int p:index)
                    element |= PA.get(p).get(q);
                UPArow.add(element);
            }
            UPA.add(UPArow);
        }

        System.out.println(UPA.size());
        System.out.println(UPA.get(0).size());


        BicliqueFinder bicliqueFinder = new BicliqueFinder(new BipartiteGraph(UPA));

        long startTime = System.currentTimeMillis();
        bicliqueFinder.solve("roles");
        long endTime = System.currentTimeMillis();

        System.out.println("*******Finding Roles*******");
        System.out.println("Roles:\n"+bicliqueFinder.toStringBicliqueF(bicliqueFinder.getRoles()));
        System.out.println("Number of roles:"+bicliqueFinder.getRoles().size());
//        System.out.println("maximal bicliques: \n"+bicliqueFinder.toStringBicliqueF(bicliqueFinder.getMaximalBicliques()));
        System.out.println("Total execution time: " + (endTime-startTime) + "ms");
//

}
}

