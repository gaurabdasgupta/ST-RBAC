package TIR_Miner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import MBRMA.*;
import javafx.util.Pair;

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
        System.out.println("\nDuration: ");
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
            MBRMA.BicliqueFinder b = new BicliqueFinder(new MBRMA.BipartiteGraph(kUPA));
            long startTime = System.currentTimeMillis();
            b.solve("roles");
            long endTime = System.currentTimeMillis();

            System.out.println("*******Finding Roles*******");
//            System.out.println("Roles:\n"+b.toStringBicliqueF(b.getRoles()));
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

        for(TemporalRole r:ROLES)
            r.display();
//        System.out.println(ROLES.size());

//        ROLES.clear();
        TemporalRole r1 = new TemporalRole();
        r1.permissions.add(99);
        r1.permissions.add(88);
        r1.duration = new Pair<Integer, Integer>(8,10);
        ROLES.add(r1);
        System.out.println("old size:"+ROLES.size());
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
                        System.out.println("In 1: a:"+aRole.duration.toString()+" b:"+bRole.duration.toString()+"users a: "+aRole.users.toString()+"users b: "+bRole.users.toString());

                        TemporalRole r = new TemporalRole();
                        r.users = (HashSet)aRole.users.clone(); // TODO check whether to add <T>
                        r.duration = aRole.duration;
                        System.out.println("duration check:"+r.duration);
                        r.permissions.addAll(aRole.permissions);
                        r.permissions.addAll(bRole.permissions);
                        aRole.isMerged = true;
                        bRole.isMerged = true;
                        newROLES.add(r);
                    }
                    else if(aRole.permissions.equals(bRole.permissions)&&aRole.duration.equals(bRole.duration)&&(!(aRole.isMerged&&bRole.isMerged)))
                    {
                        System.out.println("In 2: a:"+aRole.permissions.toString()+" b:"+bRole.permissions.toString());

                        TemporalRole r = new TemporalRole();
                        r.permissions = (HashSet)aRole.permissions.clone(); // TODO check whether to add <T>
                        r.duration = aRole.duration;
                        System.out.println("duration check:"+r.duration);
                        r.users.addAll(aRole.users);
                        r.users.addAll(bRole.users);
                        aRole.isMerged = true;
                        bRole.isMerged = true;
                        newROLES.add(r);
                    }
                    else if(aRole.permissions.equals(bRole.permissions)&&aRole.duration.equals(bRole.duration)&&(!(aRole.isMerged&&bRole.isMerged)))
                    {
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




    public static void main(String args[]) throws FileNotFoundException {
        File file = new File("datasets/TUPA.txt");
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


        uniquePeriods.remove(new Pair<Integer, Integer>(0,0));
        System.out.println(thirdPhase(secondPhase(TUPA, uniquePeriods)).size());

    }
}


