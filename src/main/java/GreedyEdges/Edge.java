package GreedyEdges;

public class Edge {
    private final int startVertex;

    private final int endVertex;

    private final double weight;

    public Edge(int start, int end, double weight) {
        startVertex = start;
        endVertex=end;
        this.weight= weight;
    }

    public double getWeight()
    {
        return weight;
    }

    public int getStart()
    {
        return startVertex;
    }

    public int getEnd()
    {
        return endVertex;
    }

    @Override
    public String toString()
    {
        return "(" + startVertex + "," + endVertex + ")";
    }

    public Boolean equals(Edge edge) {
        boolean start = this.startVertex== edge.startVertex;
        boolean end = this.endVertex == edge.endVertex;
        boolean weight =  this.weight== edge.weight;

        return start && end && weight;
    }
}
