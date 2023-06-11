import org.apache.commons.math4.legacy.linear.LUDecomposition;
import org.apache.commons.math4.legacy.linear.MatrixUtils;
import org.apache.commons.math4.legacy.linear.RealMatrix;

import java.awt.*;
import java.util.*;
import java.util.List;

public class ConvexHull {


    public Polygon convexHull (Point[]points){

        Point[]sortedPoints = sortPointsByXCoordinates(points);

        Polygon convexHull = searchConvexHull(sortedPoints,0,points.length-1 );

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

    private Polygon createBasePolygon(Point[] points, int start, int end) {

        Polygon basePoligon = new Polygon();

        for (Point actualPoint:
             points) {
            basePoligon.addPoint(actualPoint.getLocation().x, actualPoint.getLocation().y);
        }

        return basePoligon;
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

        resp[0] = new Point(1,2);
        resp[1] = new Point(3,16);
        resp[2] = new Point(1,5);
        resp[3] = new Point(7,10);
        resp[4] = new Point(6,8);
        resp[5] = new Point(4,5);

        /*for (int i = 0; i < tamArray ; i++) {

        }*/

        return resp;
    }


    public Polygon getBiggestPointOfPolygon(Polygon polygon){
        return null;
    }

    public Polygon getLowestPointOfPolygon(Polygon polygon){
        return null;
    }

    public static void main(String[] args){
            ConvexHull callConvex = new ConvexHull();

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



            Point[] generatedPoints = generatePoints(6);
            Polygon expectedPolygon = callConvex.convexHull(generatedPoints);

            //Polygon testPolygon = callConvex.mergePolygons(p1,p2);//new Polygon();

           // testPolygon.addPoint(generatedPoints[0].getLocation().x, generatedPoints[1].getLocation().y);


    }



}
