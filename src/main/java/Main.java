import GreedyEdges.GreedyAlgorithm;
import ILP.SAT34;
import InputOutputHandler.Writer;
import Points.Point;
import Points.PointGenerator;
import TwoApproximation.ApproxBrute;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Main {

    //this is needed for threading

    private static double sat(List<Point> points)
    {
        return new SAT34(points).binarySearch();
    }

    private static double greedyTriangles(List<Point> points)
    {
        return ApproxBrute.orient(points);
    }

    private static double greedyEdges(List<Point> points)
    {
        return GreedyAlgorithm.orient(points);
    }

    /**
     * This functions generates a hundred point-sets of size n in indices.
     * Every point-set is oriented with sat, greedyTriangles and greedyEdges.
     * Each method runs on its own thread for computation efficiency.
     * It calculates min,max and avg odil for every size of point-sets and every algorithm.
     * The data is written into the corresponding files.
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int[] indices = new int[]{4,5,6,7,8,9,10};

        String filePathSAT = "src/main/resources/sat.txt";
        String filePathApprox = "src/main/resources/greedyTriangles.txt";
        String filePathGreedy = "src/main/resources/greedyEdges.txt";

        double dilationSAT;
        double minSAT;
        double maxSAT;

        double dilationApprox;
        double minApprox;
        double maxApprox;

        double dilationGreedy;
        double minGreedy;
        double maxGreedy;

        for(int i:indices) {
            int j=0;

            dilationSAT=0;
            minSAT=99999;
            maxSAT=0;

            dilationApprox=0;
            minApprox=99999;
            maxApprox=0;

            dilationGreedy=0;
            minGreedy=99999;
            maxGreedy=0;

            while(j<100) {
                List<Point> points = PointGenerator.generateUniquePoints(i);

                ExecutorService executor = Executors.newFixedThreadPool(3);

                Future<Double> satFuture = executor.submit(() -> sat(points));
                Future<Double> approxFuture = executor.submit(() -> greedyTriangles(points));
                Future<Double> greedyFuture = executor.submit(() -> greedyEdges(points));


                try{

                double sat = satFuture.get();
                double approx = approxFuture.get();
                double greedy = greedyFuture.get();


                dilationSAT+= sat;
                minSAT=Math.min(minSAT,sat);
                maxSAT=Math.max(maxSAT,sat);

                dilationApprox+=approx;
                minApprox=Math.min(minApprox,approx);
                maxApprox=Math.max(maxApprox,approx);

                dilationGreedy+=greedy;
                minGreedy=Math.min(minGreedy,greedy);
                maxGreedy=Math.max(maxGreedy,greedy);}

                catch (InterruptedException | ExecutionException e) {
                        System.out.println("Error during the execution of a task:" + e.getCause());
                    }
                executor.shutdown();

                j++;
            }

            dilationSAT=dilationSAT/100;
            dilationApprox=dilationApprox/100;
            dilationGreedy=dilationGreedy/100;

            Writer.saveResult(i,dilationSAT,minSAT,maxSAT,filePathSAT);
            Writer.saveResult(i,dilationApprox,minApprox,maxApprox,filePathApprox);
            Writer.saveResult(i,dilationGreedy,minGreedy,maxGreedy,filePathGreedy);

            System.out.println(i+" done");
        }
    }

}