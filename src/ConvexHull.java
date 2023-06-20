import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;

public class ConvexHull {

    // Função auxiliar para imprimir os pontos do fecho convexo
    private static String hullToString(List<Point> convexHull) {
        String resp = "";
        resp+=("Fecho Convexo: \n");
        for (Point point : convexHull) {
            resp+=("(" + point.x + ", " + point.y + ")\n");
        }
        resp+="\n";
        return resp;
    }

    private static List<Point> generatePoints(int tamArray) {

        List<Point> resp = new LinkedList<Point>();

        for (int i = 0; i < tamArray ; i++) {
            resp.add(RandomPointGenerator.nextRandom());
        }

        return resp;
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
            List<Point>  expectedPolygon = findConvexHull2(generatedPoints);
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
            List<Point> expectedPolygon = findConvexHull2(setOfPoints.get(i));
            elapsedTimeCV = System.currentTimeMillis() - startTimeCV;
            //armazenar da i-ésima execução convencional
            singleExecutionsCV[i] = elapsedTimeCV;

            System.out.println("fim parcial do nao paralelo");

            //testar convex hull paralelo para o clone no i-ésimo (< 50) set de 10K-pontos
            startTimeParallelCV = System.currentTimeMillis();
            List<Point> expectedPolygonParallel = findParallelConvexHull2(setOfPoints.get(i));
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

            //System.out.println(actualSet);

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
            List<Point> expectedPolygon = findConvexHull2(setOfPoints.get(i));
            elapsedTimeCV = System.currentTimeMillis() - startTimeCV;
            //armazenar da i-ésima execução convencional
            singleExecutionsCV[i] = elapsedTimeCV;

            System.out.println("fim parcial do nao paralelo question class");

            //testar convex hull paralelo para o clone no i-ésimo (< 50) set de 10K-pontos
            startTimeParallelCV = System.currentTimeMillis();
            List<Point> expectedPolygonParallel =findParallelConvexHull2(setOfPoints.get(i));
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

            //System.out.println(actualSet);

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

    public static List<Point> findConvexHull2(List<Point> points) {
        if (points.size() <= 3) {
            return points;
        }

        int mid = points.size() / 2;
        List<Point> leftPoints = points.subList(0, mid);
        List<Point> rightPoints = points.subList(mid, points.size());

        List<Point> leftHull = findConvexHull2(leftPoints);
        List<Point> rightHull = findConvexHull2(rightPoints);

        return mergeHulls2(leftHull, rightHull);
    }

    public static List<Point> findParallelConvexHull2(List<Point> points) {
        if (points.size() <= 3) {
            return points;
        }

        int mid = points.size() / 2;
        List<Point> leftPoints = points.subList(0, mid);
        List<Point> rightPoints = points.subList(mid, points.size());

        // Recursivamente divide os pontos em dois conjuntos e combina os fechos convexos resultantes

        ForkJoinTask<List<Point>> task1Hull1 = ForkJoinTask.adapt(() -> findConvexHull2(leftPoints));
        ForkJoinTask<List<Point>> task2Hull2 = ForkJoinTask.adapt(() -> findConvexHull2(rightPoints));
        ForkJoinTask.invokeAll(task1Hull1,task2Hull2);


        List<Point> leftHull = task1Hull1.join();
        List<Point> rightHull = task2Hull2.join();

        return mergeHulls2(leftHull, rightHull);
    }

    private static List<Point> mergeHulls2(List<Point> leftHull, List<Point> rightHull) {
        int leftSize = leftHull.size();
        int rightSize = rightHull.size();

        // Encontra o ponto mais alto do envoltorio esquerdo
        Point highestLeft = leftHull.get(0);
        for (int i = 0; i < leftSize; i++) {
            if (leftHull.get(i).y > highestLeft.y) {
                highestLeft = leftHull.get(i);
            }
        }

        // Encontra o ponto mais baixo do envoltorio esquerdo
        Point lowestLeft = leftHull.get(0);
        for (int i = 0; i < leftSize; i++) {
            if (leftHull.get(i).y < lowestLeft.y) {
                lowestLeft = leftHull.get(i);
            }
        }

        // Encontra o ponto mais alto do envoltorio direito
        Point highestRight = rightHull.get(0);
        for (int i = 0; i < rightSize; i++) {
            if (rightHull.get(i).y > highestRight.y) {
                highestRight = rightHull.get(i);
            }
        }

        // Encontra o ponto mais baixo do envoltorio direito
        Point lowestRight = rightHull.get(0);
        for (int i = 0; i < rightSize; i++) {
            if (rightHull.get(i).y < lowestRight.y) {
                lowestRight = rightHull.get(i);
            }
        }

        List<Point> hull = new ArrayList<>();
        hull.add(highestLeft);
        hull.add(lowestRight);
        hull.add(highestRight);
        hull.add(lowestLeft);

        return hull;
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


    public static void main(String[] args) {

        ConvexHull convexHull = new ConvexHull();

        convexHull.questionBPartOne(10000);
        System.out.println("controle - fim parte1 questao b");
        convexHull.questionBPartTwo(50,10000);
        System.out.println("controle - fim parte2 questao b");
        convexHull.questionClass(1,1000000);
        System.out.println("controle - fim parte1 questao classe");


        /*List<Point> points = new ArrayList<>();
        points.add(new Point(0, 3));
        points.add(new Point(2, 2));
        points.add(new Point(1, 1));
        points.add(new Point(2, 1));
        points.add(new Point(3, 0));
        points.add(new Point(0, 0));
        points.add(new Point(3, 3));

        List<Point> points2 = new ArrayList<>();
        points2.add(new Point(0, 0));
        points2.add(new Point(0, 4));
        points2.add(new Point(1, 1));
        points2.add(new Point(2, 2));
        points2.add(new Point(3, 1));
        points2.add(new Point(4, 4));
        points2.add(new Point(4, 0));


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

        List<Point> points3 = new ArrayList<>();
        points3.add(new Point(0, 0));
        points3.add(new Point(1, 1));
        points3.add(new Point(2, 2));
        points3.add(new Point(1, 2));
        points3.add(new Point(2, 1));
        points3.add(new Point(0, 2));


        //List<Point> convexHulll = parallelFindConvexHull(generatePoints(10000));
        //List<Point> convexHullNormal = findConvexHull(points);

        p1.sort((Point sortPoint1, Point sortPoint2)-> sortPoint1.x - sortPoint2.x);
        List<Point> convexHullNormal = findParallelConvexHull2(generatePoints(1000000));
        printConvexHull(convexHullNormal);//*/


    }
}
