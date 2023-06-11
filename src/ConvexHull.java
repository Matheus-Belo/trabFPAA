import org.apache.commons.math4.legacy.linear.LUDecomposition;
import org.apache.commons.math4.legacy.linear.MatrixUtils;
import org.apache.commons.math4.legacy.linear.RealMatrix;

import javax.xml.transform.Result;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.ForkJoinTask.invokeAll;

public class ConvexHull {


    public Polygon convexHull (Point[]points){

        Point[]sortedPoints = sortPointsByXCoordinates(points);

        Polygon convexHull = searchConvexHull(sortedPoints,0,points.length-1 );

        return convexHull;
    }

    private Polygon parallelConvexHull(Point[] points) {

        Point[]sortedPoints = sortPointsByXCoordinates(points.clone());
        Polygon convexHull = searchParallelConvexHull(sortedPoints,0,points.length-1);

        return convexHull;
    }

    private Point[] sortPointsByXCoordinates(Point[] points) {

        ArrayList<Point> pointList = new ArrayList<Point>();
        pointList.addAll(List.of(points));
        pointList.sort((Point p1, Point p2)-> p1.x - p2.x);
        Point[] resp = pointList.toArray(new Point[pointList.size()]);
        return resp;
    }

    public Polygon searchConvexHull (Point[]points, int start, int end) {

        if ( (end-start+1) > 3){
            int mid = (start+end) / 2;
            Polygon esq = searchConvexHull(points, start, mid);
            Polygon dir = searchConvexHull(points, mid+1, end);

            return mergePolygons(esq,dir);
        }else{
            return createBasePolygon(points,start,end);
        }

    }

    public Polygon searchParallelConvexHull (Point[]points, int start, int end) {
        if ( (end-start+1) > 3){
            int mid = (start+end) / 2;

            ForkJoinTask<Polygon> task1Esq = ForkJoinTask.adapt(() -> searchParallelConvexHull(points,start, mid));
            ForkJoinTask<Polygon> task2Dir = ForkJoinTask.adapt(() -> searchParallelConvexHull(points,mid + 1, end));
            ForkJoinTask.invokeAll(task1Esq,task2Dir);

            Polygon esq = task1Esq.join();
            Polygon dir = task2Dir.join();

            return mergePolygons(esq,dir);
        }else{
            return createBasePolygon(points,start,end);
        }

    }

    private Polygon createBasePolygon(Point[] points, int start, int end) {

        Polygon basePolygon = new Polygon();

        for (int i = start; i < end ; i++) {

            basePolygon.addPoint(points[i].getLocation().x, points[i].getLocation().y);
        }
        System.out.println("debug");
        return basePolygon;
    }

    private Polygon mergePolygons(Polygon esq, Polygon dir) {


        Polygon tempPolygon = new Polygon();

        for (int i = 0; i < esq.xpoints.length; i++) {
            tempPolygon.addPoint(esq.xpoints[i],esq.ypoints[i]);
        }


        double[][] matrixOfPoints = getMatrixOfSelectedPolygon(tempPolygon);

        RealMatrix m = MatrixUtils.createRealMatrix(matrixOfPoints);

        Double determinant = getDeterminant(m);
        Double determinant2 = det(m);
        System.out.println(determinant+", "+determinant2);


        return null;
    }

    private double[][] getMatrixOfSelectedPolygon(Polygon actualPolygon) {

       double[][] matrixOfPoints = new double[3][3];

        for (int i = 0; i < matrixOfPoints.length; i++) {
            for (int j = 0; j < matrixOfPoints.length; j++) {
                if(j == 0){
                    matrixOfPoints[i][j] = 1;
                }else if (j == 1){
                    matrixOfPoints[i][j] = actualPolygon.xpoints[i];
                }else {
                    matrixOfPoints[i][j] = actualPolygon.ypoints[i];
                }
            }
        }
        return matrixOfPoints;
    }

    public static double getDeterminant(RealMatrix cov) {

        double resp = (new LUDecomposition(cov)).getDeterminant();
        return resp;
    }
    public static double det(final RealMatrix m) {
        LUDecomposition LU = new LUDecomposition(m);
        return LU.getDeterminant();
    }






    private static Point[] generatePoints(int tamArray) {

        Point[] resp = new Point[tamArray];

        for (int i = 0; i < tamArray ; i++) {
            resp[i] = RandomPointGenerator.nextRandom();
        }

        return resp;
    }


    public Polygon getBiggestPointOfPolygon(Polygon polygon){
        return null;
    }

    public Polygon getLowestPointOfPolygon(Polygon polygon){
        return null;
    }

    public void questionBPartOne() {

        long startTimeCV;
        long elapsedTimeCV;
        int numberOfPointsToGenerate = 10000;
        do{
            startTimeCV = System.currentTimeMillis();
            Point[] generatedPoints = generatePoints(numberOfPointsToGenerate);
            Polygon expectedPolygon = convexHull(generatedPoints);
            elapsedTimeCV = System.currentTimeMillis() - startTimeCV;
            numberOfPointsToGenerate*=2;

        }while (elapsedTimeCV <= 10000);



    }

    public void questionBPartTwo() {

        long startTimeCV;
        long elapsedTimeCV;
        long startTimeParallelCV;
        long elapsedTimeParallelCV;
        long stackTimeCV = 0L;
        long stackTimeParallelCV=0L;
        long[] singleExecutionsCV = new long[51];
        long[] singleExecutionsParallelCV = new long[51];

        int numberOfPointsToGenerate = 10000;
        Point[][] setOfPoints = new Point[50][numberOfPointsToGenerate];
        for (int i = 0; i < 50; i++) {
            setOfPoints[i] =  generatePoints(numberOfPointsToGenerate);
        }

        for (int i = 0; i < 50 ; i++) {


            //testar convex hull convencional para o clone no i-ésimo (< 50) set de 10K-pontos
            startTimeCV = System.currentTimeMillis();
            Polygon expectedPolygon = convexHull(setOfPoints[i].clone());
            elapsedTimeCV = System.currentTimeMillis() - startTimeCV;
            //armazenar da i-ésima execução convencional
            singleExecutionsCV[i] = elapsedTimeCV;

            //testar convex hull paralelo para o clone no i-ésimo (< 50) set de 10K-pontos
            startTimeParallelCV = System.currentTimeMillis();
            Polygon expectedPolygonParallel = parallelConvexHull(setOfPoints[i].clone());
            elapsedTimeParallelCV = System.currentTimeMillis() - startTimeParallelCV;
            //armazenar da i-ésima execução paralela
            singleExecutionsParallelCV[i] = elapsedTimeParallelCV;


            //Somar o tempo das execuções para ter o tempo total de execução de
            //cada algoritmo separadamente
            stackTimeCV+=elapsedTimeCV;
            stackTimeParallelCV+= elapsedTimeParallelCV;

            //conferir se os poligonos sao iguais ( sempre deverao ser iguais )
            if(expectedPolygon.equals(expectedPolygonParallel)){
                System.out.println("igual");
            }else{
                System.out.println("diferente, algo errrado");
            }

            //anotar dados num arquivo ?


        }

        //colocar o tempo de execuçao total na ultima posiçao do array de tempos individuais.
        singleExecutionsCV[50] = stackTimeCV;
        singleExecutionsParallelCV[50] = stackTimeParallelCV;


    }



    public static void main(String[] args){
            ConvexHull callConvex = new ConvexHull();

            //callConvex.questionBPartOne();
            callConvex.questionBPartTwo();

            Point[] generatedPoints = generatePoints(10);
            Polygon expectedPolygon = callConvex.convexHull(generatedPoints);




        /* teste Merge Poligons
            Polygon p1 = new Polygon();
            p1.addPoint(1,2);
            p1.addPoint(4,5);
            p1.addPoint(3,16);
            p1.addPoint(2,5);
            p1.addPoint(7,10);
            p1.addPoint(6,8);

            Polygon p2 = new Polygon();
            p2.addPoint(1,2);
            p2.addPoint(4,5);
            p2.addPoint(6,10);
            //Polygon testPolygon = callConvex.mergePolygons(p1,p2);*/


    }



}
