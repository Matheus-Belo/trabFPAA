import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AplicacaoGuloso {

    public static void main(String[] args) {
        int vertices = 5;
        boolean continua = true;
        long startTime = System.currentTimeMillis();

        while (continua) {
            long tamanho = 75;
            long tempoTotalTamanho = 0;

            long tamanhoInicioTempo = System.currentTimeMillis();

            for (int testeCadaTamanho = 0; testeCadaTamanho < tamanho; testeCadaTamanho++) {
                int[][] grafo = grafoCompletoPonderado(vertices);

                List<Integer> caminhoAtual = new ArrayList<>();
                caminhoAtual.add(0);

                List<Integer> melhorCaminho = encontrarMelhorCaminhoGuloso(grafo, caminhoAtual);
                int menorDistancia = calcularDistanciaCaminho(grafo, melhorCaminho);

                System.out.println("vertice - " + vertices);
                System.out.println("Teste " + (testeCadaTamanho + 1) + " Melhor caminho: " + melhorCaminho);
                System.out.println("Teste " + (testeCadaTamanho + 1) + " Menor distância: " + menorDistancia);

                long elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 4 * 60 * 1000) {
                    System.out.println("Tempo limite excedido");
                    continua = false;
                    break;
                }
            }

            long tamanhoFinalTempo = System.currentTimeMillis();
            long tamanhoTempo = tamanhoFinalTempo - tamanhoInicioTempo;
            tempoTotalTamanho += tamanhoTempo;
            long tempoMedio = tempoTotalTamanho / tamanho;

            System.out.println("Para N vértices = " + vertices + ", Tempo Médio Gasto: " + tempoMedio + " ms");

            if (tempoMedio >= 3500) {
                System.out.println("Tempo médio excedeu o limite");
                continua = false;
                break;
            }

            vertices++;

            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 4 * 60 * 1000) {
                System.out.println("Tempo limite excedido");
                continua = false;
                break;
            }
        }
    }

    public static int[][] grafoCompletoPonderado(int vertices) {
        int[][] matriz = new int[vertices][vertices];
        Random aleatorio = new Random();

        int valor;
        for (int i = 0; i < matriz.length; i++) {
            matriz[i][i] = -1;
            for (int j = i + 1; j < matriz.length; j++) {
                valor = aleatorio.nextInt(25) + 1;
                matriz[i][j] = valor;
                matriz[j][i] = valor;
                System.out.println("[" + i + "][" + j + "] - " + matriz[i][j]);
            }
        }
        return matriz;
    }

    public static List<Integer> encontrarMelhorCaminhoGuloso(int[][] grafo, List<Integer> caminhoAtual) {
        List<Integer> melhorCaminho = new ArrayList<>(caminhoAtual);
        int posicaoAtual = caminhoAtual.get(caminhoAtual.size() - 1);

        while (melhorCaminho.size() < grafo.length) {
            int proximoVertice = encontrarProximoVertice(grafo, posicaoAtual, melhorCaminho);
            melhorCaminho.add(proximoVertice);
            posicaoAtual = proximoVertice;
        }

        return melhorCaminho;
    }

    public static int encontrarProximoVertice(int[][] grafo, int posicaoAtual, List<Integer> caminho) {
        int proximoVertice = -1;
        int menorDistancia = Integer.MAX_VALUE;

        for (int i = 0; i < grafo.length; i++) {
            if (!caminho.contains(i) && grafo[posicaoAtual][i] != -1 && grafo[posicaoAtual][i] < menorDistancia) {
                proximoVertice = i;
                menorDistancia = grafo[posicaoAtual][i];
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
}
