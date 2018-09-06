package MBEA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class VertexSet {
    ArrayList<Vertex> setV = new ArrayList<>();

    VertexSet()
    {

    }

    VertexSet(ArrayList<Vertex> nodesIn)
    {
        setV = nodesIn;
    }

    void addVertex(Vertex v)
    {
        if(!setV.contains(v))
            setV.add(v);
    }

    void removeVertex(Vertex v)
    {
        if(setV.contains(v))
            setV.remove(v);
    }

    boolean isEqual(VertexSet other)
    {
        return setV.equals(other.setV);
    }

    void sortByNumOfNeighbours()
    {
        Collections.sort(setV, new Comparator<Vertex>() {
            @Override
            public int compare(Vertex o1, Vertex o2) {
                return o1.getNeighboursSize() - o2.getNeighboursSize();
            }
        });
    }

    ArrayList<Vertex> getSetV()
    {
        return setV;
    }

    int getSize()
    {
        return setV.size();
    }

    Vertex getVertex(int i)
    {
        return setV.get(i);
    }
}
