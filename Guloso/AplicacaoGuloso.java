package Guloso;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AplicacaoGuloso {
    static Random aleatorio = new Random(42);

    public static void main(String[] args) {
        int vertices = 6;
        int[][] grafo = grafoCompletoPonderado(vertices);

        List<Integer> caminho = caixeiroViajanteGuloso(grafo);

        System.out.println("Caminho percorrido: " + caminho);
        System.out.println("Distância total: " + calcularDistanciaCaminho(grafo, caminho));
    }

    public static int[][] grafoCompletoPonderado(int vertices) {
        int[][] matriz = new int[vertices][vertices];
        //Random aleatorio = new Random(); - > receber valores diferentes na matriz toda vez que rodar o código
        int valor;
        for (int i = 0; i < matriz.length; i++) {
            matriz[i][i] = -1;
            for (int j = i + 1; j < matriz.length; j++) {
                valor = aleatorio.nextInt(25) + 1;
                matriz[i][j] = valor;
                matriz[j][i] = valor;
                System.out.println("["+i+"]["+j+"] - "+matriz[i][j]);
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
