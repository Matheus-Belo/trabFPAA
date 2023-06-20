import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

import static java.util.concurrent.ForkJoinTask.invokeAll;

public class ConvexHullDivideAndConquerorPlusGrahamScan {

    private List<Point> sortPointsByXCoordinates(List<Point> points) {
        points.sort((Point p1, Point p2)-> p1.x - p2.x);
        return points;
    }
    public List<Point> convexHull (List<Point> points){

        List<Point>sortedPoints = sortPointsByXCoordinates(points);
        List<Point> convexHull = searchConvexHull(sortedPoints,0,points.size() );



        return convexHull;
    }

    private List<Point> parallelConvexHull(List<Point> points) {

        List<Point>sortedPoints = sortPointsByXCoordinates(points);
        List<Point> convexHull = searchParallelConvexHull(sortedPoints,0,points.size() );

        return convexHull;
    }

    public List<Point> searchConvexHull (List<Point> points, int start, int end) {

        if ( (end-start) > 3){
            int mid = (start+end) / 2;

            List<Point> esq = searchConvexHull(points, start, mid);
            List<Point> dir = searchConvexHull(points, mid+1, end);

            return mergeConvexHull(esq,dir);
        }else{
            return createBasePolygon(points,start,end);
        }

    }




    public List<Point> searchParallelConvexHull (List<Point> points, int start, int end)  {
        if ( (end-start) > 3){
            int mid = (start+end) / 2;

            ForkJoinTask<List<Point>> task1Esq = ForkJoinTask.adapt(() -> searchParallelConvexHull(points,start, mid));
            ForkJoinTask<List<Point>> task2Dir = ForkJoinTask.adapt(() -> searchParallelConvexHull(points,mid + 1, end));
            ForkJoinTask.invokeAll(task1Esq,task2Dir);

            List<Point> esq = task1Esq.join();
            List<Point> dir = task2Dir.join();

            return mergeConvexHull(esq,dir);
        }else{
            return createBasePolygon(points,start,end);
        }

    }

    private List<Point> createBasePolygon(List<Point> points, int start, int end) {

        List<Point> basePolygon = new LinkedList<Point>();

        for (int i = start; i < end ; i++) {

            basePolygon.add(new Point(points.get(i).x, points.get(i).y));
        }
        return basePolygon;
    }

    private static List<Point> generatePoints(int tamArray) {

        List<Point> resp = new LinkedList<Point>();

        for (int i = 0; i < tamArray ; i++) {
            resp.add(RandomPointGenerator.nextRandom());
        }

        return resp;
    }
    public static List<Point> mergeConvexHull(List<Point> pointsLeft,List<Point> pointsRight ) {

        List<Point> points = new LinkedList<Point>();

        points.addAll(pointsLeft);
        points.addAll(pointsRight);

        if (points.size() <= 3) {
            return points;
        }

        int n = points.size();
        int leftmost = 0;
        for (int i = 1; i < n; i++) {
            if (points.get(i).x < points.get(leftmost).x) {
                leftmost = i;
            }
        }

        List<Point> hull = new ArrayList<>();
        int p = leftmost;
        int q;
        do {
            hull.add(points.get(p));
            q = (p + 1) % n;

            for (int i = 0; i < n; i++) {
                if (orientation(points.get(p), points.get(i), points.get(q)) == 2) {
                    q = i;
                }
            }

            p = q;
        } while (p != leftmost);

        return hull;
    }

    private static int orientation(Point p, Point q, Point r) {
        int value = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

        if (value == 0) {
            return 0;  // Colinear
        }

        return (value > 0) ? 1 : 2; // 1 para horário, 2 para anti-horário
    }

    public void questionBPartOne(int numberOfPointsToGenerate) {

        long startTimeCV;
        long elapsedTimeCV;
        long stackTimeCV = 0L;
        Date dataAtual = new Date();
        String nomeArquivo= "QUESTAO1 EXECUCAO"+System.currentTimeMillis();
        int contExec = 0;

        do{
            List<Point> generatedPoints = generatePoints(numberOfPointsToGenerate);

            /* para debugar System.out.println("Pontos Gerados");
            for (Point point : generatedPoints) {
                System.out.println("(" + point.x + ", " + point.y + ")");
            }
            System.out.println(" ------ ");*/

            startTimeCV = System.currentTimeMillis();
            List<Point>  expectedPolygon = convexHull(generatedPoints);
            elapsedTimeCV = System.currentTimeMillis() - startTimeCV;

            stackTimeCV+=elapsedTimeCV;




            /*System.out.println("ConvexHull");
            for (Point point : expectedPolygon) {
                System.out.println("(" + point.x + ", " + point.y + ")");
            }*/
            String controleParcial = ("-- fim parcial - tamanho do conjunto: "+numberOfPointsToGenerate+" - tempo: "+elapsedTimeCV+ "ms --\n");
            System.out.println(controleParcial);

            String actualSet = "\nACTUAL SET-> ";
            int cont=0;
            for (Point p: generatedPoints ) { actualSet+="x:"+p.x+",y:"+p.y+"||"; cont++;if(cont == 100){actualSet+="\n";cont=0;}}

            String toTXT = actualSet+"\n"+controleParcial+"" +
                    "\nHull Não Paralelo: "+hullToString(expectedPolygon)+
                    "\nFIM ITERACAO: "+contExec;

            escreverSolucaoEmArquivo(toTXT,nomeArquivo);

            generatedPoints = new ArrayList<Point>();
            numberOfPointsToGenerate*=2;
            contExec++;
        }while (elapsedTimeCV <= 10000);


        String result = "\nSolução Média: "+(stackTimeCV/contExec)+"ms. ";
        System.out.println(result);
        escreverSolucaoEmArquivo(result,nomeArquivo);


    }

    public void questionBPartTwo(int quantExec, int numberOfPointsToGenerate) {

        long startTimeCV;
        long elapsedTimeCV;
        long startTimeParallelCV;
        long elapsedTimeParallelCV;
        long stackTimeCV = 0L;
        long stackTimeParallelCV=0L;
        long[] singleExecutionsCV = new long[quantExec+1];
        long[] singleExecutionsParallelCV = new long[quantExec+1];

        Date dataAtual = new Date();
        String nomeArquivo= "QUESTAO2 EXECUCAO"+System.currentTimeMillis();


        List<List<Point>> setOfPoints = new LinkedList<List<Point>>();
        for (int i = 0; i < quantExec; i++) {
            setOfPoints.add(generatePoints(numberOfPointsToGenerate));
        }

        for (int i = 0; i < quantExec ; i++) {


            //testar convex hull convencional para o clone no i-ésimo (< 50) set de 10K-pontos
            startTimeCV = System.currentTimeMillis();
            List<Point> expectedPolygon = convexHull(setOfPoints.get(i));
            elapsedTimeCV = System.currentTimeMillis() - startTimeCV;
            //armazenar da i-ésima execução convencional
            singleExecutionsCV[i] = elapsedTimeCV;

            System.out.println("fim parcial do nao paralelo");

            //testar convex hull paralelo para o clone no i-ésimo (< 50) set de 10K-pontos
            startTimeParallelCV = System.currentTimeMillis();
            List<Point> expectedPolygonParallel = convexHull(setOfPoints.get(i));
            elapsedTimeParallelCV = System.currentTimeMillis() - startTimeParallelCV;
            //armazenar da i-ésima execução paralela
            singleExecutionsParallelCV[i] = elapsedTimeParallelCV;


            //Somar o tempo das execuções para ter o tempo total de execução de
            //cada algoritmo separadamente
            stackTimeCV+=elapsedTimeCV;
            stackTimeParallelCV+= elapsedTimeParallelCV;


            String controleParcial = ("-- fim parcial: "+i+". - tamanho do conjunto: "+numberOfPointsToGenerate+" - " +
                    "tempo normal: "+elapsedTimeCV+ "ms - "+
                    "tempo paralelo: "+elapsedTimeParallelCV+ "ms --");

            System.out.println(controleParcial);




            //anotar dados num arquivo

            String actualSet = "\nACTUAL SET-> ";
            int cont=0;
            for (Point p: setOfPoints.get(i) ) { actualSet+="x:"+p.x+",y:"+p.y+"||"; cont++;if(cont == 100){actualSet+="\n";cont=0;}}

            System.out.println(actualSet);

            String toTXT = actualSet+"\n"+controleParcial+"" +
                    "\nHull Paralelo: "+hullToString(expectedPolygonParallel)+
                    "\nHull Não Paralelo: "+hullToString(expectedPolygon)+
                    "\nFIM ITERACAO: "+i;

            escreverSolucaoEmArquivo(toTXT,nomeArquivo);

        }
        //colocar o tempo de execuçao total na ultima posiçao do array de tempos individuais.
        singleExecutionsCV[quantExec] = stackTimeCV;
        singleExecutionsParallelCV[quantExec] = stackTimeParallelCV;

        String result = "\nSolução Média não paralela: "+(stackTimeCV/quantExec)+"; \nSolução Média paralela: "+(stackTimeParallelCV/quantExec)+". ";
        escreverSolucaoEmArquivo(result,nomeArquivo);

    }

    public void questionClass(int quantExec, int numberOfPointsToGenerate) {

        long startTimeCV;
        long elapsedTimeCV;
        long startTimeParallelCV;
        long elapsedTimeParallelCV;
        long stackTimeCV = 0L;
        long stackTimeParallelCV=0L;
        long[] singleExecutionsCV = new long[quantExec+1];
        long[] singleExecutionsParallelCV = new long[quantExec+1];

        Date dataAtual = new Date();
        String nomeArquivo= "QUESTAO2 EXECUCAO"+System.currentTimeMillis();

        List<List<Point>> setOfPoints = new LinkedList<List<Point>>();
        for (int i = 0; i < quantExec; i++) {
            setOfPoints.add(generatePoints(numberOfPointsToGenerate));
        }

        for (int i = 0; i < quantExec ; i++) {


            //testar convex hull convencional para o clone no i-ésimo (< 50) set de 10K-pontos
            startTimeCV = System.currentTimeMillis();
            List<Point> expectedPolygon = convexHull(setOfPoints.get(i));
            elapsedTimeCV = System.currentTimeMillis() - startTimeCV;
            //armazenar da i-ésima execução convencional
            singleExecutionsCV[i] = elapsedTimeCV;

            System.out.println("fim parcial do nao paralelo");

            //testar convex hull paralelo para o clone no i-ésimo (< 50) set de 10K-pontos
            startTimeParallelCV = System.currentTimeMillis();
            List<Point> expectedPolygonParallel =parallelConvexHull(setOfPoints.get(i));
            elapsedTimeParallelCV = System.currentTimeMillis() - startTimeParallelCV;
            //armazenar da i-ésima execução paralela
            singleExecutionsParallelCV[i] = elapsedTimeParallelCV;


            //Somar o tempo das execuções para ter o tempo total de execução de
            //cada algoritmo separadamente
            stackTimeCV+=elapsedTimeCV;
            stackTimeParallelCV+= elapsedTimeParallelCV;


            String controleParcial = ("-- fim parcial: "+i+". - tamanho do conjunto: "+numberOfPointsToGenerate+" - " +
                    "tempo normal: "+elapsedTimeCV+ "ms - "+
                    "tempo paralelo: "+elapsedTimeParallelCV+ "ms --");

            System.out.println(controleParcial);




            //anotar dados num arquivo

            String actualSet = "\nACTUAL SET-> ";
            int cont=0;
            for (Point p: setOfPoints.get(i) ) { actualSet+="x:"+p.x+",y:"+p.y+"||"; cont++;if(cont == 100){actualSet+="\n";cont=0;}}

            System.out.println(actualSet);

            String toTXT = actualSet+"\n"+controleParcial+"" +
                    "\nHull Paralelo: "+hullToString(expectedPolygonParallel)+
                    "\nHull Não Paralelo: "+hullToString(expectedPolygon)+
                    "\nFIM ITERACAO: "+i;

            escreverSolucaoEmArquivo(toTXT,nomeArquivo);

        }
        //colocar o tempo de execuçao total na ultima posiçao do array de tempos individuais.
        singleExecutionsCV[quantExec] = stackTimeCV;
        singleExecutionsParallelCV[quantExec] = stackTimeParallelCV;

        String result = "\nSolução Média não paralela: "+(stackTimeCV/quantExec)+"; \nSolução Média paralela: "+(stackTimeParallelCV/quantExec)+". ";
        escreverSolucaoEmArquivo(result,nomeArquivo);

    }

    private static void escreverSolucaoEmArquivo(String solucao, String nomeArquivo) {


        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true))) {

            writer.write(solucao);
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String hullToString(List<Point> convexHull) {
        String resp = "";
        resp+=("Fecho Convexo: \n");
        for (Point point : convexHull) {
            resp+=("(" + point.x + ", " + point.y + ")\n");
        }
        resp+="\n";
        return resp;
    }
    public static void main(String[] args) {
        ConvexHullDivideAndConquerorPlusGrahamScan convexHull = new ConvexHullDivideAndConquerorPlusGrahamScan();
        convexHull.questionBPartOne(10000);
        convexHull.questionBPartTwo(50,10000);
        convexHull.questionClass(1,1000000);


        /*List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 4));
        points.add(new Point(1, 1));
        points.add(new Point(2, 2));
        points.add(new Point(3, 1));
        points.add(new Point(4, 4));
        points.add(new Point(4, 0));


        List<Point> p1 = new ArrayList<>();
        p1.add (new Point(1, 2));
        p1.add (new Point(4, 5));
        p1.add (new Point(3, 16));
        p1.add (new Point(2, 7));
        p1.add (new Point(7, 10));
        p1.add (new Point(5, 8));
        p1.add (new Point(5, 5));
        p1.add (new Point(5, -3));
        p1.add (new Point(-4, 5));

        List<Point> generatedPoints = generatePoints(500);
        List<Point> generatedPoints2 = generatePoints(5000);
        List<Point> convexHullExpected = convexHull.convexHull(generatedPoints2);

        for (Point point : convexHullExpected) {
            System.out.println("(" + point.x + ", " + point.y + ")");
        }*/

    }





}
