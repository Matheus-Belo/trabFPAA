import java.awt.*;
import java.util.ArrayList;

public class MergePolygons {

    public Polygon mergePolygons(Polygon p1, Polygon p2) {
        // Verifica se algum dos polígonos é vazio
        if (p1.isEmpty()) {
            return p2;
        } else if (p2.isEmpty()) {
            return p1;
        }

        // Encontra o ponto mais à direita do polígono p1 e o ponto mais à esquerda do polígono p2
        Point rightmostP1 = findRightmostPoint(p1);
        Point leftmostP2 = findLeftmostPoint(p2);

        // Encontra a tangente inferior entre os polígonos
        Point tangent1 = findTangent(rightmostP1, p1, leftmostP2, p2);
        // Encontra a tangente superior entre os polígonos
        Point tangent2 = findTangent(leftmostP2, p2, rightmostP1, p1);

        // Mescla os polígonos a partir das tangentes encontradas
        List<Point> mergedPoints = new ArrayList<Point>();

        // Adiciona os pontos do polígono p1 à lista de pontos mesclados
        addPointsInRange(p1, rightmostP1, tangent1, mergedPoints);

        // Adiciona os pontos do polígono p2 à lista de pontos mesclados
        addPointsInRange(p2, leftmostP2, tangent2, mergedPoints);

        // Retorna o novo polígono mesclado
        return new Polygon(mergedPoints.toArray(new Point[0]));
    }

    private Point findRightmostPoint(Polygon polygon) {
        Point rightmostPoint = polygon.getPoint(0);
        for (int i = 1; i < polygon.getNumPoints(); i++) {
            Point currentPoint = polygon.getPoint(i);
            if (currentPoint.getX() > rightmostPoint.getX()) {
                rightmostPoint = currentPoint;
            }
        }
        return rightmostPoint;
    }

    private Point findLeftmostPoint(Polygon polygon) {
        Point leftmostPoint = polygon.getPoint(0);
        for (int i = 1; i < polygon.getNumPoints(); i++) {
            Point currentPoint = polygon.getPoint(i);
            if (currentPoint.getX() < leftmostPoint.getX()) {
                leftmostPoint = currentPoint;
            }
        }
        return leftmostPoint;
    }

    private Point findTangent(Point start, Polygon polygon1, Point pivot, Polygon polygon2) {
        // Encontra o índice do ponto inicial no polígono 1
        int startIndex = polygon1.indexOf(start);

        // Inicializa o melhor ponto da tangente e o melhor ângulo encontrado
        Point bestTangentPoint = null;
        double bestAngle = Double.MAX_VALUE;

        // Itera através dos pontos do polígono 1
        int numPoints = polygon1.getNumPoints();
        for (int i = 0; i < numPoints; i++) {
            Point currentPoint = polygon1.getPoint((startIndex + i) % numPoints);

            // Calcula o ângulo entre o ponto atual, o pivô e o eixo x positivo
            double angle = Math.atan2(currentPoint.getY() - pivot.getY(), currentPoint.getX() - pivot.getX());

            // Verifica se o ângulo é menor que o melhor ângulo encontrado até agora
            if (angle < bestAngle) {
                // Verifica se o segmento entre o ponto atual e o pivô não intersecta o polígono 2
                if (!intersectsPolygon(currentPoint, pivot, polygon2)) {
                    bestAngle = angle;
                    bestTangentPoint = currentPoint;
                }
            }
        }

        return bestTangentPoint;
    }

    private boolean intersectsPolygon(Point p1, Point p2, Polygon polygon) {
        int numPoints = polygon.getNumPoints();
        for (int i = 0; i < numPoints; i++) {
            Point currentPoint = polygon.getPoint(i);
            Point nextPoint = polygon.getPoint((i + 1) % numPoints);
            if (intersectsSegment(p1, p2, currentPoint, nextPoint)) {
                return true;
            }
        }
        return false;
    }

    private boolean intersectsSegment(Point p1, Point p2, Point p3, Point p4) {
        double d1 = direction(p3, p4, p1);
        double d2 = direction(p3, p4, p2);
        double d3 = direction(p1, p2, p3);
        double d4 = direction(p1, p2, p4);

        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            return true;
        } else if (d1 == 0 && onSegment(p3, p4, p1)) {
            return true;
        } else if (d2 == 0 && onSegment(p3, p4, p2)) {
            return true;
        } else if (d3 == 0 && onSegment(p1, p2, p3)) {
            return true;
        } else if (d4 == 0 && onSegment(p1, p2, p4)) {
            return true;
        }

        return false;
    }

    private double direction(Point p1, Point p2, Point p3) {
        return (p2.getX() - p1.getX()) * (p3.getY() - p1.getY()) - (p3.getX() - p1.getX()) * (p2.getY() - p1.getY());
    }

    private boolean onSegment(Point p1, Point p2, Point p3) {
        double minX = Math.min(p1.getX(), p2.getX());
        double maxX = Math.max(p1.getX(), p2.getX());
        double minY = Math.min(p1.getY(), p2.getY());
        double maxY = Math.max(p1.getY(), p2.getY());
        return p3.getX() >= minX && p3.getX() <= maxX && p3.getY() >= minY && p3.getY() <= maxY;
    }

    private void addPointsInRange(Polygon polygon, Point start, Point end, List<Point> mergedPoints) {
        int startIndex = polygon.indexOf(start);
        int endIndex = polygon.indexOf(end);

        if (endIndex < startIndex) {
            endIndex += polygon.getNumPoints();
        }

        for (int i = startIndex; i <= endIndex; i++) {
            Point currentPoint = polygon.getPoint(i % polygon.getNumPoints());
            mergedPoints.add(currentPoint);
        }
    }
}
