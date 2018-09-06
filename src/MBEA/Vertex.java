package MBEA;

import java.util.ArrayList;

class Vertex implements Comparable<Vertex>{
    int label;
    ArrayList<Vertex> neighbours = new ArrayList<>();
    Vertex(int label)
    {
        this.label = label;
    }

    void addNeighbour(Vertex v) throws RuntimeException
    {
        if(neighbours.contains(v))
            throw new RuntimeException("already a neighbour");
        else
            neighbours.add(v);
    }

    void removeNeighbour(Vertex v)
    {
        if(neighbours.contains(v))
        {
            neighbours.remove(v);
        }
        else
            System.out.println("vertex not present");
    }

    boolean isEqual(Vertex otherV)
    {
        return this.label == otherV.label;
    }

    static void addEdge(Vertex v1, Vertex v2)
    {
        try{
            v1.addNeighbour(v2);
        }
        catch (RuntimeException e)
        {
            System.out.println(e);
        }

        try{
            v2.addNeighbour(v1);
        }
        catch (RuntimeException e)
        {
            System.out.println(e);
        }
    }

    void removeEdge(Vertex v1, Vertex v2)
    {
        try{
            v1.removeNeighbour(v2);
        }
        catch (RuntimeException e)
        {
            System.out.println(e);
        }

        try{
            v2.removeNeighbour(v1);
        }
        catch (RuntimeException e)
        {
            System.out.println(e);
        }
    }

    ArrayList<Vertex> getNeighbours()
    {
        return neighbours;
    }

    int getNeighboursSize()
    {
        return neighbours.size();
    }

    boolean isNeighbour(Vertex otherV)
    {
        return neighbours.contains(otherV);
    }

    int numberOfNeighboursOfVInSet(ArrayList<Vertex> set)
    {
        int out = 0;
        for (Vertex aSet : set)
        {
            if (isNeighbour(aSet))
            {
                out += 1;
            }
        }
        return out;
    }

    boolean isMember(ArrayList<Vertex> set)
    {
        boolean out = false;
        for(Vertex v:set)
        {
            if(this.label == v.label) //TODO DOUBT
                out = true;

        }
        return out;
    }

    public int compareTo(Vertex other)
    {
        return this.getNeighboursSize() - other.getNeighboursSize();
    }




}
