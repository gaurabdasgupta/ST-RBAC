package MBEA;

import java.util.List;

public class Biclique extends BipartiteGraph {
    boolean isMaximal;
    Biclique() {}
    Biclique(List<List<Integer>> incMat)
    {
        super(incMat);
    }

    Biclique(List<Vertex> leftV, List<Vertex> rightV)
    {
        super();
        leftNodes = leftV;
        rightNodes = rightV;
        // TODO check for duplicate vertices

        for(int i=0;i<leftNodes.size();i++)
        {
            for(int j=0;j<rightNodes.size();j++)
            {
                Vertex left = leftNodes.get(i);
                Vertex right = rightNodes.get(i);
                Vertex.addEdge(left,right);
            }
        }

        for(int i=0;i<leftNodes.size();i++)
        {
            Vertex leftVer = leftNodes.get(i);
            leftNeighbours.add(leftVer.getNeighbours());
        }

        for(int i=0;i<rightNodes.size();i++)
        {
            Vertex rightVer = rightNodes.get(i);
            rightNeighbours.add(rightVer.getNeighbours());
        }

    }

    String toStringBiclique()
    {
        String res = "";
        for(int i=0;i< leftNodes.size();i++)
        {
            res += Integer.toString(leftNodes.get(i).getLabel()) + " ";
        }

        res += "| ";

        for(int i=0;i< rightNodes.size();i++)
        {
            res += Integer.toString(rightNodes.get(i).getLabel()) + " ";
        }
        return res;
    }
}
