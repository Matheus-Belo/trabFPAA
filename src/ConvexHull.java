import org.apache.commons.math4.legacy.linear.LUDecomposition;
import org.apache.commons.math4.legacy.linear.MatrixUtils;
import org.apache.commons.math4.legacy.linear.RealMatrix;

import java.awt.*;

public class ConvexHull {






    public Polygon connvexHull (Point[]points, int start, int end){

        if ( (end-start+1) > 3){
            int mid = (start+end) / 2;
            Polygon esq = connvexHull(points, start, mid);
            Polygon dir = connvexHull(points, mid+1, end);

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






    private static Point[] generatePoints() {
            return null;
    }


    public Polygon getBiggestPointOfPolygon(Polygon polygon){
        return null;
    }

    public Polygon getLowestPointOfPolygon(Polygon polygon){
        return null;
    }

    public static void main(String[] args){
            ConvexHull callConvex = new ConvexHull();
            /*Polygon expectedPolygon ;

            Point[] generatedPoints = generatePoints();

            expectedPolygon = callConvex.connvexHull(generatedPoints, 0, generatedPoints.length );*/

            Polygon p1 = new Polygon();
            p1.addPoint(1,2);
            p1.addPoint(4,5);
            p1.addPoint(6,10);

            Polygon p2 = new Polygon();
            p2.addPoint(1,2);
            p2.addPoint(4,5);
            p2.addPoint(6,10);

            Polygon testPolygon = callConvex.mergePolygons(p1,p2);//new Polygon();

           // testPolygon.addPoint(generatedPoints[0].getLocation().x, generatedPoints[1].getLocation().y);


    }



}
