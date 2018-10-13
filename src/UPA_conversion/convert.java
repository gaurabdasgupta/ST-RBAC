package UPA_conversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class convert{
    public static void main(String args[]) {
        List<List<Integer>> UA = new ArrayList<>();
        List<List<Integer>> PA = new ArrayList<>();
        List<List<Integer>> UPA = new ArrayList<>();
        Scanner input = null;

        try {
            input = new Scanner(new File("UA_hc.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (input.hasNextLine()) {
            Scanner colReader = new Scanner(input.nextLine());
            List<Integer> col = new ArrayList<>();
            while (colReader.hasNextInt()) {
                col.add(colReader.nextInt());
            }
            UA.add(col);
         }

        try {
            input = new Scanner(new File("PA_hc.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (input.hasNextLine()) {
            Scanner colReader = new Scanner(input.nextLine());
            List<Integer> col = new ArrayList<>();
            while (colReader.hasNextInt()) {
                col.add(colReader.nextInt());
            }
            PA.add(col);
        }

        for(int i=0;i<46;i++)
        {
            List<Integer> index = new ArrayList<>();
            List<Integer> UPArow = new ArrayList<>();
            for(int j=0;j<15;j++)
            {
                if(UA.get(i).get(j)==1)
                {
                    index.add(j);
                }
            }
//            System.out.println(index);

            for(int q=0;q<46;q++)
            {
                int element = 0;
//
                for(int p:index)
                {
                    element |= PA.get(p).get(q);
//                    System.out.print(element);
                }
//                System.out.println();
                UPArow.add(element);
            }
            UPA.add(UPArow);
//            System.out.println(UPArow);
        }
//
//        for(int i=0;i<46;i++)
//        {
//            for(int j=0;j<46;j++)
//            {
//                System.out.print(UPA.get(i).get(j));
//            }
//            System.out.print("\n");
//        }


    }
}
