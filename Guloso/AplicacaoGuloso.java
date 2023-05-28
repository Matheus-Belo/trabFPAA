package Guloso;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TesteAutomatizado {
    static Random aleatorio = new Random();
    static final long TEMPO_LIMITE = 4 * 60 * 1000; // 4 minutos em milissegundos

    public static void main(String[] args) {
        int tamanhoInicial = 5;
        int tamanhoMaximo = 100;
        int repeticoes = 70;

        int tamanhoConjunto = tamanhoInicial;

        while (tamanhoConjunto <= tamanhoMaximo) {
            List<Long> tempos = new ArrayList<>();

            for (int i = 0; i < repeticoes; i++) {
                int[][] grafo = grafoCompletoPonderado(tamanhoConjunto);
                long tempoInicial = System.currentTimeMillis();
                caixeiroViajanteGuloso(grafo);
                long tempoFinal = System.currentTimeMillis();
                long tempoExecucao = tempoFinal - tempoInicial;
                tempos.add(tempoExecucao);

                if (tempoExecucao > TEMPO_LIMITE) {
                    break;
                }
            }

            long tempoMedio = calcularTempoMedio(tempos);

            System.out.println("Tamanho do conjunto: " + tamanhoConjunto);
            System.out.println("Tempo médio de solução: " + tempoMedio + " ms");

            if (tempoMedio > TEMPO_LIMITE) {
                int verticesLimite = tamanhoConjunto - 1;
                System.out.println("Número N-1: " + verticesLimite);
                break;
            }

            tamanhoConjunto++;
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

    public static void caixeiroViajanteGuloso(int[][] grafo) {
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

        System.out.println("Caminho percorrido: " + caminho);
        System.out.println("Distância total: " + calcularDistanciaCaminho(grafo, caminho));
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

    public static int calcularDistanciaCaminho(int[][] grafo, List<Integer> caminho) {
        int distancia = 0;
        for (int i = 0; i < caminho.size() - 1; i++) {
            int origem = caminho.get(i);
            int destino = caminho.get(i + 1);
            distancia += grafo[origem][destino];
        }
        return distancia;
    }

    public static long calcularTempoMedio(List<Long> tempos) {
        long soma = 0;
        for (Long tempo : tempos) {
            soma += tempo;
        }
        return tempos.isEmpty() ? 0 : soma / tempos.size();
    }
}
