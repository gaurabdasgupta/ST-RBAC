package TIR_Miner;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.Random;

public class tupaGenerator {
    
    static int num_users ;
    static int num_permissions ;
    static int num_floors;
    static double density;
    static int num_sessions;
    static String path;

    void generateData() throws IOException
    {
        int i,j;
        float counter=0;
        double number;
        BufferedReader rd = new BufferedReader(new FileReader(path));
        Random randomGenerator = new Random();
        BufferedWriter wr = new BufferedWriter(new FileWriter("C:\\Users\\HP\\Desktop\\Role Based Access Control\\DATASETS\\TUPA\\u5p5f1d20\\TUPA.txt"));
        int data_matrix[][] = new int[num_users][num_permissions];
       
        
        wr.write(num_users+"\n"+num_permissions+"\n"+num_floors+"\n"+num_sessions+"\n"+density+"\n\n");
         String data="";

        try {

            String currentSession;

            while ((currentSession = rd.readLine()) != null) {
                data=data+currentSession+"@";
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (rd != null)
                    rd.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }

        }

        String session[]=data.split("@"); int len=session.length;
                
        for(i=0;i<num_users;i++)
        {
            counter=0;
            for(j=0;j<num_permissions;j++)
            {
                number =Math.random();
                
                if(number > density)
                {
                    data_matrix[i][j]=0;
                    wr.write("0\n");
                    counter++;
                }
                
                else
                {
                    int floor=randomGenerator.nextInt(num_floors)+1;
                    int index=randomGenerator.nextInt(len);
                    //int l=session[index].length();
                    wr.write("("+floor+","+session[index]+")\n");
                }
            }
            
            wr.write("\n");
        }
        
        System.out.println("Done!");
        
       /* for(i=0;i<num_users;i++)
        {
            for(j=0;j<num_permissions;j++)
            {
                System.out.print(data_matrix[i][j]);
            }
            System.out.print("\n");
        }*/
        wr.close();
        
    }
    
    
    
    public static void main(String args[]) throws IOException
    {
        Scanner sc=new Scanner(System.in);
        System.out.println("Enter number of users:");
        num_users=sc.nextInt();    
        System.out.println("Enter number of permissions:");
        num_permissions=sc.nextInt();
        System.out.println("Enter number of floors:");
        num_floors=sc.nextInt();
        System.out.println("Enter number of sessions:");
        num_sessions=sc.nextInt();     
        System.out.println("Enter percent of density of access:");
        density=sc.nextDouble()/100;
        /*System.out.println("Enter path to sessions file:");
        path=sc.nextLine();*/
        path="C:\\Users\\HP\\Desktop\\Role Based Access Control\\DATASETS\\sessions.txt";
        tupaGenerator sp = new tupaGenerator();
        sp.generateData();
    }
}
