package Guloso;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TesteCaixeiroViajante {

    static Random aleatorio = new Random();

    public static void main(String[] args) {
        int iteracoes = 70;
        int limiteVertices = 100;
        int incrementoVertices = 5;

        System.out.println("Teste do Caixeiro Viajante");

        for (int vertices = 5; vertices <= limiteVertices; vertices += incrementoVertices) {
            System.out.println("Número de Vértices: " + vertices);
            long tempoTotal = 0;
            int iteracao;

            for (iteracao = 1; iteracao <= iteracoes; iteracao++) {
                int[][] grafo = grafoCompletoPonderado(vertices);

                long tempoInicial = System.currentTimeMillis();
                long tempoLimite = tempoInicial + 4 * 60 * 1000; // 4 minutos
                List<Integer> caminho = null;

                while (System.currentTimeMillis() < tempoLimite) {
                    caminho = caixeiroViajanteGuloso(grafo);
                }

                long tempoFinal = System.currentTimeMillis();

                long tempoIteracao = tempoFinal - tempoInicial;
                tempoTotal += tempoIteracao;

                System.out.println("Iteração " + iteracao + ": Tempo de Solução: " + tempoIteracao + " ms");

                if (tempoIteracao >= 4 * 60 * 1000) {
                    break;
                }
            }

            if (iteracao > iteracoes) {
                System.out.println("Tempo Limite Excedido");
            } else {
                long tempoMedio = tempoTotal / iteracao;
                System.out.println("Tempo Médio: " + tempoMedio + " ms");
            }

            System.out.println("------------------------------------");
        }
    }

    public static int[][] grafoCompletoPonderado(int vertices) {
        int[][] matriz = new int[vertices][vertices];
        int valor;
        for (int i = 0; i < matriz.length; i++) {
            matriz[i][i] = -1;
            for (int j = i + 1; j < matriz.length; j++) {
                valor = aleatorio.nextInt(25) + 1;
                matriz[i][j] = valor;
                matriz[j][i] = valor;
            }
        }
        return matriz;
    }

    public static List<Integer> caixeiroViajanteGuloso(int[][] grafo) {
        List<Integer> caminho = new ArrayList<>();
        int vertices = grafo.length;

        boolean[] visitados = new boolean[vertices];
        caminho.add(0);
        visitados[0] = true;

        for (int i = 1; i < vertices; i++) {
            int proximoVertice = encontrarProximoVertice(grafo, caminho.get(caminho.size() - 1), visitados);
            caminho.add(proximoVertice);
            visitados[proximoVertice] = true;
        }

        caminho.add(0); // Voltar para a cidade de origem

        return caminho;
    }

    public static int encontrarProximoVertice(int[][] grafo, int verticeAtual, boolean[] visitados) {
        int proximoVertice = -1;
        int menorDistancia = Integer.MAX_VALUE;

        for (int i = 0; i < grafo.length; i++) {
            if (!visitados[i] && grafo[verticeAtual][i] != -1 && grafo[verticeAtual][i] < menorDistancia) {
                menorDistancia = grafo[verticeAtual][i];
                proximoVertice = i;
            }
        }

        return proximoVertice;
    }
}
