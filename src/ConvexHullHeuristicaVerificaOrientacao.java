import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
public class ConvexHullHeuristicaVerificaOrientacao {
        public static java.util.List<Point> findConvexHull(java.util.List<Point> points) {

            if (points.size() < 3) {
                // O fecho convexo não é possível com menos de 3 pontos
                throw new IllegalArgumentException("O conjunto de pontos precisa ter pelo menos 3 pontos");
            }

            points.sort((Point p1, Point p2)-> p1.x - p2.x);

            java.util.List<Point> convexHull = new ArrayList<>();

            // Encontre o ponto mais à esquerda e o ponto mais à direita
            int leftmost = 0, rightmost = 0;
            for (int i = 0; i < points.size(); i++) {
                if (points.get(i).x < points.get(leftmost).x) {
                    leftmost = i;
                }
                if ((points.get(i).x > points.get(rightmost).x) ) {
                    rightmost = i;
                }
            }


            // Divide os pontos em dois conjuntos, acima e abaixo da linha formada pelos pontos mais à esquerda e mais à direita
            java.util.List<Point> upperHull = divideAndConquer(points, leftmost, rightmost, 1);
            java.util.List<Point> lowerHull = divideAndConquer(points, leftmost, rightmost, -1);



            // Combine os fechos convexos superior e inferior
            convexHull.addAll(upperHull);
            convexHull.addAll(lowerHull);


            return convexHull;
        }

        public static java.util.List<Point> parallelFindConvexHull(java.util.List<Point> points) {

            if (points.size() < 3) {
                // O fecho convexo não é possível com menos de 3 pontos
                throw new IllegalArgumentException("O conjunto de pontos precisa ter pelo menos 3 pontos");
            }

            points.sort((Point p1, Point p2)-> p1.x - p2.x);

            java.util.List<Point> convexHull = new ArrayList<>();

            // Encontre o ponto mais à esquerda e o ponto mais à direita
            int leftmost = 0, rightmost = 0;
            for (int i = 1; i < points.size(); i++) {
                if (points.get(i).x < points.get(leftmost).x) {
                    leftmost = i;
                }
                if (points.get(i).x > points.get(rightmost).x) {
                    rightmost = i;
                }
            }

            // Divide os pontos em dois conjuntos, acima e abaixo da linha formada pelos pontos mais à esquerda e mais à direita
            int finalLeftmost = leftmost;
            int finalRightmost = rightmost;
            ForkJoinTask<java.util.List<Point>> upperHull = ForkJoinTask.adapt(() -> divideAndConquerParallel(points, finalLeftmost, finalRightmost, 1));
            int finalLeftmost1 = leftmost;
            int finalRightmost1 = rightmost;
            ForkJoinTask<java.util.List<Point>> lowerHull = ForkJoinTask.adapt(() -> divideAndConquerParallel(points, finalLeftmost1, finalRightmost1, -1));

            ForkJoinTask.invokeAll(upperHull,lowerHull);

            java.util.List<Point> lower = lowerHull.join();
            java.util.List<Point> upper = upperHull.join();


            // Combine os fechos convexos superior e inferior
            convexHull.addAll(lower);
            convexHull.addAll(upper);

            return convexHull;
        }


        // Função auxiliar para realizar a divisão e conquista
        private static java.util.List<Point> divideAndConquerParallel(java.util.List<Point> points, int leftmost, int rightmost, int side) {
            java.util.List<Point> hull = new ArrayList<>();

            int index = -1;
            int maxDistance = 0;

            // Encontre o ponto mais distante da linha formada pelos pontos mais à esquerda e mais à direita
            for (int i = 0; i < points.size(); i++) {
                int distance = getDistance(points.get(leftmost), points.get(rightmost), points.get(i));
                int actualSide = getSide(points.get(leftmost), points.get(rightmost), points.get(i));  // distacia do ponto a reta

                if (actualSide == side && distance > maxDistance) {
                    maxDistance = distance;
                    index = i;
                }
            }

            if (index == -1) {
                // Não foram encontrados mais pontos, retornar o ponto encontrado anteriormente
                hull.add(points.get(leftmost));
                hull.add(points.get(rightmost));
                return hull;
            }

            // Recursivamente divide os pontos em dois conjuntos e combina os fechos convexos resultantes
            int finalIndex = index;
            ForkJoinTask<java.util.List<Point>> task1Hull1 = ForkJoinTask.adapt(() -> divideAndConquer(points, leftmost, finalIndex, -getSide(points.get(leftmost), points.get(finalIndex), points.get(rightmost))));
            int finalIndex1 = finalIndex;
            ForkJoinTask<java.util.List<Point>> task2Hull2 = ForkJoinTask.adapt(() -> divideAndConquer(points, finalIndex1, rightmost, -getSide(points.get(finalIndex1), points.get(rightmost), points.get(leftmost))));
            ForkJoinTask.invokeAll(task1Hull1,task2Hull2);

            java.util.List<Point> hull1 = task2Hull2.join();
            java.util.List<Point> hull2 = task1Hull1.join();


            hull.addAll(hull1);
            hull.addAll(hull2);

            return hull;
        }

        private static java.util.List<Point> divideAndConquer(java.util.List<Point> points, int leftmost, int rightmost, int side) {
            java.util.List<Point> hull = new ArrayList<>();

            int index = -1;
            int maxDistance = 0;

            // Encontre o ponto mais distante da linha formada pelos pontos mais à esquerda e mais à direita
            for (int i = 0; i < points.size(); i++) {
                int distance = getDistance(points.get(leftmost), points.get(rightmost), points.get(i));
                int actualSide = getSide(points.get(leftmost), points.get(rightmost), points.get(i)); // distancia do ponto a reta

                if ( actualSide == side && distance > maxDistance) {
                    maxDistance = distance;
                    index = i;
                }
            }

            if (index == -1) {
                // Não foram encontrados mais pontos, retornar o ponto encontrado anteriormente
                hull.add(points.get(leftmost));
                hull.add(points.get(rightmost));
                return hull;
            }

            // Recursivamente divide os pontos em dois conjuntos e combina os fechos convexos resultantes
            java.util.List<Point> hull1 = divideAndConquer(points, leftmost, index, -getSide(points.get(leftmost), points.get(index), points.get(rightmost)));
            java.util.List<Point> hull2 = divideAndConquer(points, index, rightmost, -getSide(points.get(index), points.get(rightmost), points.get(leftmost)));

            hull.addAll(hull1);
            hull.addAll(hull2);

            return hull;
        }

        // Função auxiliar para calcular a distância entre um ponto e uma linha
        private static int getDistance(Point a, Point b, Point c) {
            int val = (b.x - a.x) * (a.y - c.y) - (b.y - a.y) * (a.x - c.x);
            return val;
        }

        // Função auxiliar para determinar de qual lado de uma linha um ponto está
        private static int getSide(Point a, Point b, Point c) {
            int val = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
            if (val > 0) {
                return 1; // Ponto à esquerda da linha
            } else if (val <  0) {
                return -1; // Ponto à direita da linha
            } else {
                return 0; // Ponto na linha
            }
        }

        // Função auxiliar para imprimir os pontos do fecho convexo
        private static void printConvexHull(java.util.List<Point> convexHull) {
            System.out.println("Fecho Convexo:");
            for (Point point : convexHull) {
                System.out.println("(" + point.x + ", " + point.y + ")");
            }
        }

        private static java.util.List<Point> generatePoints(int tamArray) {

            java.util.List<Point> resp = new LinkedList<Point>();

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
            String nomeArquivo = "QUESTAO1 EXECUCAO" + System.currentTimeMillis();
            int contExec = 0;

            do {
                List<Point> generatedPoints = generatePoints(numberOfPointsToGenerate);

            /* para debugar System.out.println("Pontos Gerados");
            for (Point point : generatedPoints) {
                System.out.println("(" + point.x + ", " + point.y + ")");
            }
            System.out.println(" ------ ");*/

                startTimeCV = System.currentTimeMillis();
                List<Point> expectedPolygon = findConvexHull(generatedPoints);
                elapsedTimeCV = System.currentTimeMillis() - startTimeCV;

                stackTimeCV += elapsedTimeCV;


            /*System.out.println("ConvexHull");
            for (Point point : expectedPolygon) {
                System.out.println("(" + point.x + ", " + point.y + ")");
            }*/
                String controleParcial = ("-- fim parcial - tamanho do conjunto: " + numberOfPointsToGenerate + " - tempo: " + elapsedTimeCV + "ms --\n");
                System.out.println(controleParcial);

                String actualSet = "\nACTUAL SET-> ";
                int cont=0;
                for (Point p: generatedPoints ) { actualSet+="x:"+p.x+",y:"+p.y+"||"; cont++;if(cont == 100){actualSet+="\n";cont=0;}}

                String toTXT = actualSet+"\n"+controleParcial+"" +
                        "\nHull Não Paralelo: "+hullToString(expectedPolygon)+
                        "\nFIM ITERACAO: "+contExec;

                escreverSolucaoEmArquivo(toTXT,nomeArquivo);

                generatedPoints = new ArrayList<Point>();
                numberOfPointsToGenerate *= 2;
                contExec++;
            } while (elapsedTimeCV <= 10000);


            String result = "\nSolução Média: " + (stackTimeCV / contExec) + "ms. ";
            System.out.println(result);
            escreverSolucaoEmArquivo(result, nomeArquivo);
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


            java.util.List<java.util.List<Point>> setOfPoints = new LinkedList<java.util.List<Point>>();
            for (int i = 0; i < quantExec; i++) {
                setOfPoints.add(generatePoints(numberOfPointsToGenerate));
            }

            for (int i = 0; i < quantExec ; i++) {


                //testar convex hull convencional para o clone no i-ésimo (< 50) set de 10K-pontos
                startTimeCV = System.currentTimeMillis();
                java.util.List<Point> expectedPolygon = findConvexHull(setOfPoints.get(i));
                elapsedTimeCV = System.currentTimeMillis() - startTimeCV;
                //armazenar da i-ésima execução convencional
                singleExecutionsCV[i] = elapsedTimeCV;

                System.out.println("fim parcial do nao paralelo");

                //testar convex hull paralelo para o clone no i-ésimo (< 50) set de 10K-pontos
                startTimeParallelCV = System.currentTimeMillis();
                java.util.List<Point> expectedPolygonParallel = parallelFindConvexHull(setOfPoints.get(i));
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

            java.util.List<java.util.List<Point>> setOfPoints = new LinkedList<java.util.List<Point>>();
            for (int i = 0; i < quantExec; i++) {
                setOfPoints.add(generatePoints(numberOfPointsToGenerate));
            }

            for (int i = 0; i < quantExec ; i++) {


                //testar convex hull convencional para o clone no i-ésimo (< 50) set de 10K-pontos
                startTimeCV = System.currentTimeMillis();
                java.util.List<Point> expectedPolygon = findConvexHull(setOfPoints.get(i));
                elapsedTimeCV = System.currentTimeMillis() - startTimeCV;
                //armazenar da i-ésima execução convencional
                singleExecutionsCV[i] = elapsedTimeCV;

                System.out.println("fim parcial do nao paralelo");

                //testar convex hull paralelo para o clone no i-ésimo (< 50) set de 10K-pontos
                startTimeParallelCV = System.currentTimeMillis();
                java.util.List<Point> expectedPolygonParallel =parallelFindConvexHull(setOfPoints.get(i));
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

            ConvexHull convexHull = new ConvexHull();

            convexHull.questionBPartOne(10000);
            convexHull.questionBPartTwo(50,10000);
            convexHull.questionClass(1,1000000);


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


