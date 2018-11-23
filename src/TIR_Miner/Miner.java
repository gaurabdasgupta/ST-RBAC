package TIR_Miner;

import java.io.*;
import java.util.*;

import MBRMA.*;
import javafx.util.Pair;
import org.omg.PortableInterceptor.INACTIVE;

import javax.management.relation.Role;

class TemporalRole{
    HashSet<Integer> users;
    HashSet<Integer> permissions;
    Pair<Integer, Integer> duration;
    boolean isMerged;
    List<Pair<Integer, Integer>> durations;
    boolean containsTwo;

    TemporalRole()
    {
        users = new HashSet<>();
        permissions = new HashSet<>();
        isMerged = false;
        durations = new ArrayList<>();
        containsTwo = false;
    }

    void display()
    {
        System.out.println("\nUsers: ");
        for(int x:users)
            System.out.print(x+" ");
        System.out.println("\nPermissions: ");
        for(int x:permissions)
            System.out.print(x+" ");
        System.out.println("\nDuration(s): ");
        if(containsTwo)
            for(Pair<Integer, Integer> pr:durations)
                System.out.print(pr+" ");
        else
            System.out.println(duration);
    }

}

public class Miner {
    static Pair<Integer, Integer> parseLine(String s) {
        String start = "", end = "", a;
        if (s.equals("0"))
            return new Pair<Integer, Integer>(0, 0);

        s = s.replaceAll("\\(", "").replaceAll("\\)", "");
        Scanner sc = new Scanner(s);
        sc.useDelimiter(",");

        while (sc.hasNext()) {
            a = sc.next();
            start = sc.next();
//            System.out.println("start: " + start);
            end = sc.next();
//            System.out.println("end: " + end);
        }

        return new Pair<Integer, Integer>(Integer.parseInt(start), Integer.parseInt(end));
    }

    static List<TemporalRole> secondPhase(List<List<Pair<Integer, Integer>>> TUPA,  HashSet<Pair<Integer, Integer>> uniquePeriods)
    {
        List<TemporalRole> ROLES = new ArrayList<>();
        for(Pair<Integer, Integer> period:uniquePeriods)
        {
            List<List<Integer>> kUPA = new ArrayList<>();
            for(int i=0;i<TUPA.size();i++)
            {
                List<Integer> kRow = new ArrayList<>();
                for(int j=0;j<TUPA.get(i).size();j++)
                {
                    if(TUPA.get(i).get(j).equals(period))
                        kRow.add(1);
                    else
                        kRow.add(0);
                }
                kUPA.add(kRow);
            }
            System.out.println("****Period: "+period+" ****");
            System.out.println("UPA :"+period);
            for(int i=0;i<kUPA.size();i++) {
                for (int j = 0; j < kUPA.get(i).size(); j++)
                    System.out.print(kUPA.get(i).get(j) + " ");
                System.out.println();
            }
            MBRMA.BicliqueFinder b = new BicliqueFinder(new MBRMA.BipartiteGraph(kUPA));
            long startTime = System.currentTimeMillis();
            b.solve("roles");
            long endTime = System.currentTimeMillis();

            System.out.println("Finding Roles...");
            System.out.println("Output of MBRMA: ");
            System.out.println("Roles:\n"+b.toStringBicliqueF(b.getRoles()));
            for(Biclique bq:b.getRoles())
            {
                TemporalRole role = new TemporalRole();
                role.duration = period;
                for(Vertex v:bq.getLeftNodes())
                    role.users.add(v.getLabel());
                for(Vertex v:bq.getRightNodes())
                    role.permissions.add(v.getLabel());
                ROLES.add(role);
            }
            System.out.println("Number of roles:"+b.getRoles().size());
            System.out.println("Total execution time: " + (endTime-startTime) + "ms");
        }
        System.out.println("Temporal Roles: ");
        for(TemporalRole r:ROLES)
            r.display();
//        System.out.println(ROLES.size());

// **********For testing merging of roles**********
//        ROLES.clear();
        TemporalRole r1 = new TemporalRole();
        r1.users.add(28);
        r1.permissions.add(51);
        r1.permissions.add(58);
        r1.duration = new Pair<Integer, Integer>(8,10);
//        ROLES.add(r1);
        System.out.println("Initial number of Temporal Roles: "+ROLES.size());
        return ROLES;
    }

    static List<TemporalRole> thirdPhase(List<TemporalRole> ROLES)
    {
        List<TemporalRole> newROLES = new ArrayList<>();
        for(int i=0;i<ROLES.size();i++)
        {
            for(int j=0;j<ROLES.size();j++)
            {
                if(j!=i)
                {
                    TemporalRole aRole = ROLES.get(i);
                    TemporalRole bRole = ROLES.get(j);
                    if(aRole.users.equals(bRole.users)&&aRole.duration.equals(bRole.duration)&&(!(aRole.isMerged&&bRole.isMerged)))
                    {
                        System.out.println("In 1: a_dur: "+aRole.duration.toString()+" b_dur: "+bRole.duration.toString()+"a_users: "+aRole.users.toString()+"b_users: "+bRole.users.toString());

                        TemporalRole r = new TemporalRole();
                        r.users = (HashSet)aRole.users.clone(); // TODO check whether to add <T>
                        r.duration = aRole.duration;
                        System.out.println("duration check:"+r.duration);
                        r.permissions.addAll(aRole.permissions);
                        r.permissions.addAll(bRole.permissions);
                        aRole.isMerged = true;
                        bRole.isMerged = true;
                        System.out.println("New Role Formed: ");
                        r.display();
                        newROLES.add(r);
                    }
                    else if(aRole.permissions.equals(bRole.permissions)&&aRole.duration.equals(bRole.duration)&&(!(aRole.isMerged&&bRole.isMerged)))
                    {
                        System.out.println("In 1: a_dur: "+aRole.duration.toString()+" b_dur: "+bRole.duration.toString()+"a_perm: "+aRole.permissions.toString()+"b_perm: "+bRole.permissions.toString());
                        TemporalRole r = new TemporalRole();
                        r.permissions = (HashSet)aRole.permissions.clone(); // TODO check whether to add <T>
                        r.duration = aRole.duration;
                        System.out.println("duration check:"+r.duration);
                        r.users.addAll(aRole.users);
                        r.users.addAll(bRole.users);
                        aRole.isMerged = true;
                        bRole.isMerged = true;
                        System.out.println("New Role Formed: ");
                        r.display();
                        newROLES.add(r);
                    }
                    else if(aRole.permissions.equals(bRole.permissions)&&aRole.users.equals(bRole.users)&&(!(aRole.isMerged&&bRole.isMerged)))
                    {
                        System.out.println("In 3: ");
                        if(   (Math.abs(aRole.duration.getKey()-bRole.duration.getValue())==1||Math.abs(aRole.duration.getValue()-bRole.duration.getKey())==1)||
                                (aRole.duration.getKey()>=bRole.duration.getKey()&&aRole.duration.getValue()<=bRole.duration.getValue())||
                                (bRole.duration.getKey()>=aRole.duration.getKey()&&bRole.duration.getValue()<=bRole.duration.getValue())
                                )
                        {
                            System.out.println("In 3.1: a:"+aRole.duration.toString()+" b:"+bRole.duration.toString());
                            TemporalRole r = new TemporalRole();
                            r.users = (HashSet)aRole.users.clone();
                            r.permissions = (HashSet)aRole.permissions.clone();
                            r.duration = new Pair<Integer, Integer>(Math.min(aRole.duration.getKey(), bRole.duration.getKey()),Math.max(aRole.duration.getValue(), bRole.duration.getValue()));
                            aRole.isMerged = true;
                            bRole.isMerged = true;
                            System.out.println("New Role Formed: ");
                            r.display();
                            newROLES.add(r);
                        }
                        else
                        {
                            System.out.println("In 3.2: a:"+aRole.duration.toString()+" b:"+bRole.duration.toString());
                            TemporalRole r = new TemporalRole();
                            r.users = (HashSet)aRole.users.clone();
                            r.permissions = (HashSet)aRole.permissions.clone();
                            r.durations.add(aRole.duration);
                            r.durations.add(bRole.duration);
                            aRole.isMerged = true;
                            bRole.isMerged = true;
                            r.containsTwo = true;
                            System.out.println("New Role Formed: ");
                            r.display();
                            newROLES.add(r);
                        }

                    } // TODO write data in 24 hr format

                }
                }

//            newROLES.add(r);
        }

        for(TemporalRole r:ROLES)
            if(!r.isMerged)
                newROLES.add(r);

        return newROLES;
    }

    public static void main(String args[]) throws FileNotFoundException, IOException {
        // *****for TUPA generator code*******
        File file = new File("datasets/TUPA_test.txt");
        Scanner sc = new Scanner(file);
        HashSet<Pair<Integer, Integer>> uniquePeriods = new HashSet<>();
        List<List<Pair<Integer, Integer>>> TUPA = new ArrayList<>();
        int num_users = sc.nextInt();
        int num_perm = sc.nextInt();
        int num_floors = sc.nextInt();
        int num_sessions = sc.nextInt();
        double density = sc.nextDouble();
        int c = 1;
        List<Pair<Integer, Integer>> row = new ArrayList<>();
        while (sc.hasNext()) {
            String t = sc.next();
//            System.out.println("here" + t);
            Pair<Integer, Integer> result = parseLine(t);
            uniquePeriods.add(result);
            row.add(result);
            if ((c % (num_perm) == 0)&&c!=0) {
                TUPA.add(row);
                row = new ArrayList<>();
            }
            c++;
        }

//        System.out.println(TUPA.size());
//        for (int i = 0; i < TUPA.size(); i++)
//        {
//            System.out.println(TUPA.get(i).size());
//            for (int j = 0; j < TUPA.get(i).size(); j++)
//                System.out.print(" "+TUPA.get(i).get(j));
//            System.out.println();
//        }

//        uniquePeriods.remove(new Pair<Integer, Integer>(0,0));
//
//        System.out.println("\nNumber of Roles after merging phase: "+thirdPhase(secondPhase(TUPA, uniquePeriods)).size());

        // **** For A.Ene et al datasets with temporal add ons generated below ******

        List<List<Integer>> UA = new ArrayList<>();
        List<List<Integer>> PA = new ArrayList<>();
        List<List<Integer>> UPA = new ArrayList<>();
        Scanner input = null;
        int UArowNum=0,UAcolNum=0,PAcolNum=0,PArowNum=0;
        int t=0,k=0;
        String fileName = null;
        fileName = "apj.txt";
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

        int cnt=0;
        for(int q=0;q<UArowNum;q++)
        {
            for (int p = 0; p <UAcolNum; p++)
                if(UA.get(q).get(p)==1)
                    cnt++;
//            System.out.println();
        }

        System.out.println(UPA.size());
        System.out.println(UPA.get(0).size());
        System.out.println("one count"+cnt);

        HashSet<Pair<Integer, Integer>> per = new HashSet<>();
        per.add(new Pair<Integer, Integer>(2,5));
        per.add(new Pair<Integer, Integer>(5,8));
        per.add(new Pair<Integer, Integer>(12,18));
        per.add(new Pair<Integer, Integer>(14,16));
        per.add(new Pair<Integer, Integer>(9,11));

//        Random gen = new Random();

//        File f = new File("datasets/greedyMBC_Ene_2008/temporal/"+fileName);
//        BufferedWriter writer = new BufferedWriter(new FileWriter("datasets/greedyMBC_Ene_2008/temporal/"+fileName));
//        while(cnt>0) {
//            int random = gen.nextInt(5);
//            String fileContent = "(1,"+per.get(random).getKey()+","+per.get(random).getValue()+")";
//            writer.write(fileContent);
//            writer.write('\n');
////            writer.close();
//            cnt--;
//        }
//        writer.close();
        File file2 = new File("datasets/greedyMBC_Ene_2008/temporal/"+fileName);
        Scanner scn = new Scanner(file2);
        List<List<Pair<Integer, Integer>>> currTUPA = new ArrayList<>();
        int count=0;
        for(int i=0;i<UPA.size();i++)
        {
            List<Pair<Integer, Integer>> mat_row = new ArrayList<>();
            for(int j=0;j<UPA.get(i).size();j++)
            {
                if(UPA.get(i).get(j)==1&&count!=cnt)
                {
                    String st = scn.next();
                    System.out.println("here" + st);
                    Pair<Integer, Integer> result = parseLine(st);
                    System.out.println(result);
                    mat_row.add(result);
                    count++;
                }
                else
                    mat_row.add(new Pair<Integer, Integer>(0,0));
//                System.out.println("check"+count);

            }
            currTUPA.add(mat_row);
        }
//        System.out.println(currTUPA.size());
//        System.out.println(currTUPA.get(0).size());
        System.out.println("\nNumber of Roles after merging phase: "+thirdPhase(secondPhase(currTUPA, per)).size());
    }
}

// exec time avg
// roles median
