package MBEA;

import java.util.ArrayList;

public class BicliqueFinder extends Biclique {
    private boolean foundAll = false;
    private int maxPossible;
    private BipartiteGraph graph = new BipartiteGraph();
    private VertexSet initL;
    private VertexSet initP;
    private VertexSet initR = new VertexSet();
    private VertexSet initQ = new VertexSet();
    ArrayList<Biclique> maximalBicliques = new ArrayList<Biclique>();


    BicliqueFinder(BipartiteGraph inGraph)
    {
        graph = inGraph;
        initL = new VertexSet(graph.getLeftNodes());
        initP = new VertexSet(graph.getRightNodes());

        int n = Math.min(initL.getSize(), initP.getSize());
        maxPossible = (int)(Math.pow(2, n) - 2);
    }

    void findMaximalBicliques(String algType)
    {
        if(algType.equals("standard"))
        {
            bicliqueFind(initL, initR, initP, initQ);
            foundAll = true;
        } //TODO add improved version
    }

    ArrayList<Biclique> getMaximalBicliques()
    {
        if(foundAll)
            return maximalBicliques;
        else
            System.out.println("not found yet");
        return null;
    }

    String getLRPQinit()
    {
        String res = null;
        for (int i = 0; i < initL.getSize(); i++){
            res += Integer.toString((initL.getVertex(i).getLabel())) + " ";
        }
        res += "\n";
        for (int i = 0; i < initR.getSize(); i++){
            res += Integer.toString((initR.getVertex(i).getLabel())) + " ";
        }
        res += "\n";
        for (int i = 0; i < initP.getSize(); i++){
            res += Integer.toString((initP.getVertex(i).getLabel())) + " ";
        }
        res += "\n";
        for (int i = 0; i < initQ.getSize(); i++){
            res += Integer.toString((initQ.getVertex(i).getLabel())) + " ";
        }
        res += "\n";

        return res;
    }

    void bicliqueFind(VertexSet inL, VertexSet inR, VertexSet inP, VertexSet inQ)
    {
        VertexSet L = inL;
        VertexSet R = inR;
        VertexSet P = inP;
        VertexSet Q = inQ;

        while (!P.isSetEmpty())
        {
            Vertex x = P.getVertex(0);
            VertexSet Rprime = R;
            Rprime.addVertex(x);

            VertexSet Lprime = new VertexSet();

            for(int j=0;j<L.getSize();j++)
            {
                Vertex u = L.getVertex(j);
                if(u.isNeighbour(x))
                {
                    Lprime.addVertex(u);
                }
            }

            VertexSet Pprime = new VertexSet();
            VertexSet Qprime = new VertexSet();

            isMaximal = true;

            for(int j=0;j<Q.getSize();j++)
            {
                Vertex v = Q.getVertex(j);
                int numLprimeNeighbours = v.numberOfNeighboursOfVInSet(Lprime.getSetV());

                if(numLprimeNeighbours == Lprime.getSize())
                {
                    isMaximal = false;
                    break;
                }
                if (numLprimeNeighbours > 0)
                {
                    Qprime.addVertex(v);
                }
            }

            if(isMaximal)
            {
                for(int j=0;j<P.getSize();j++)
                {
                    Vertex v = P.getVertex(j);
                    if(v.isEqual(x)) // doubt equals
                    {
                        continue;
                    }

                    int numLprimeNeighbours = v.numberOfNeighboursOfVInSet(Lprime.getSetV());
                    if(numLprimeNeighbours == Lprime.getSize())
                        Rprime.addVertex(v);
                    else if(numLprimeNeighbours > 0)
                        Pprime.addVertex(v);
                }

                Biclique bcq = new Biclique(Lprime.getSetV(), Rprime.getSetV());
                bcq.isMaximal = true;
                maximalBicliques.add(bcq);

                if(!Pprime.isSetEmpty())
                    bicliqueFind(Lprime,Rprime,Pprime,Qprime);
            }
            P.removeVertex(x);
            Q.addVertex(x);
        }
    }

    int getNumBicliques()
    {
        if(foundAll)
            return maximalBicliques.size();
        return 0;
    }

    String toStringBicliqueF()
    {

        Biclique b;
        if(foundAll)
        {
            String res = "";
            for(int i=0;i<maximalBicliques.size();i++)
            {
                b = maximalBicliques.get(i);
                res += b.toStringBiclique();
                res += "\n";
            }
            return res;
        }
        else
            return null;

    }
}
