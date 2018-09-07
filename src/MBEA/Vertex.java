package MBEA;

import java.util.ArrayList;
import java.util.List;

class Vertex implements Comparable<Vertex>{
    private int label;
    private List<Vertex> neighbours = new ArrayList<>();

    Vertex(){}
    Vertex(int label) { this.label = label; }

    List<Vertex> getNeighbours() { return neighbours; }

    int getLabel() { return label; }

    private void addNeighbour(Vertex v)
    {
        if(!neighbours.contains(v))
            neighbours.add(v);

    }

    private void removeNeighbour(Vertex v)
    {
        if(neighbours.contains(v))
            System.out.println("vertex not present");
        else
            neighbours.remove(v);
    }

    static void addEdge(Vertex v1, Vertex v2)
    {
        v1.addNeighbour(v2);
        v2.addNeighbour(v1);
    }

    void removeEdge(Vertex v1, Vertex v2)
    {
            v1.removeNeighbour(v2);
            v2.removeNeighbour(v1);
    }

    boolean isEqual(Vertex otherV)
    {
        return this.label == otherV.getLabel();
    }

    int getNeighboursSize()
    {
        return neighbours.size();
    }

    boolean isNeighbour(Vertex otherV)
    {
        return neighbours.contains(otherV);
    }

    int numberOfNeighboursOfVInSet(List<Vertex> set)
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

    boolean isMember(List<Vertex> set)
    {
        boolean out = false;
        for(Vertex v:set)
        {
            if(this.equals(v)) //TODO DOUBT
                out = true;

        }
        return out;
    }


    public int compareTo(Vertex other)
    {
        return this.getNeighboursSize() - other.getNeighboursSize();
    }




}
