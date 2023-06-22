import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BackTracking {
    
static  Random aleatorio = new Random(42);

    private final int[][] grafo;
    private final int numCidades;
    private List<Integer> melhorCaminho;
    //private int menorDistancia;

    public BackTracking(int[][] grafo) {
        this.grafo = grafo;
        this.numCidades = grafo.length;
        this.melhorCaminho = new ArrayList<>();
       // this.menorDistancia = Integer.MAX_VALUE;
        
    }

    public void encontrarMelhorCaminho() {
        int[] caminhoAtual = new int[numCidades];
        //int[] todasCidades = new int[numCidades];
        boolean[] visitado = new boolean[numCidades];

        /*for (int i = 0; i < numCidades; i++) {
            todasCidades[i] = i;
        }

        permutarCaminhos(todasCidades, caminhoAtual, 0);
        */

        caminhoAtual[0] = 0; // Começa no vértice 0
        visitado[0] = true;

        backtrack(caminhoAtual, visitado, 1, 0);

        melhorCaminho.add(0); // Adiciona o vértice inicial ao final do caminho
    }

    /*private void permutarCaminhos(int[] todasCidades, int[] caminhoAtual, int indice) {
        if (indice == numCidades) {
            calcularCustoCaminho(caminhoAtual);
            return;
        }

        for (int i = 0; i < numCidades; i++) {
            if (!visitado(todasCidades[i], caminhoAtual, indice)) {
                caminhoAtual[indice] = todasCidades[i];
                permutarCaminhos(todasCidades, caminhoAtual, indice + 1);
            }
        }
    }*/

   private void backtrack(int[] caminhoAtual, boolean[] visitado, int posicao, int custoTotal) {
        if (custoTotal < calcularCustoTotal(melhorCaminho))
            return;

        if (posicao == numCidades) {
            // Chegou ao final do caminho
            int ultimaCidade = caminhoAtual[posicao - 1];
            int cidadeInicial = caminhoAtual[0];

            if (grafo[ultimaCidade][cidadeInicial] != 0) {
                // Calcula o custo total do caminho
                custoTotal = calcularCustoTotal(caminhoAtual);

                if (melhorCaminho.isEmpty() || custoTotal < calcularCustoTotal(melhorCaminho)) {
                    melhorCaminho = new ArrayList<>(Arrays.stream(caminhoAtual).boxed().toList());
                }
            }
            return;
        }

        for (int proximo = 0; proximo < numCidades; proximo++) {
            if (!visitado[proximo] && grafo[caminhoAtual[posicao - 1]][proximo] != 0) {
                caminhoAtual[posicao] = proximo;
                visitado[proximo] = true;

                backtrack(caminhoAtual, visitado, posicao + 1, custoTotal + grafo[caminhoAtual[posicao - 1]][proximo]);

                caminhoAtual[posicao] = 0;
                visitado[proximo] = false;
            }
        }
    }

    /*private boolean visitado(int cidade, int[] caminho, int indice) {
        for (int i = 0; i < indice; i++) {
            if (caminho[i] == cidade) {
                return true;
            }
        }
        return false;
    }*/

    private int calcularCustoTotal(int[] caminho) {
        int custoTotal = 0;

        for (int i = 0; i < numCidades - 1; i++) {
            int origem = caminho[i];
            int destino = caminho[i + 1];
            custoTotal += grafo[origem][destino];
        }

        int ultimaCidade = caminho[numCidades - 1];
        int cidadeInicial = caminho[0];
        custoTotal += grafo[ultimaCidade][cidadeInicial];

        return custoTotal;
    }


    /*private void calcularCustoCaminho(int[] caminho) {
        int custoTotal = 0;

        for (int i = 0; i < numCidades - 1; i++) {
            int origem = caminho[i];
            int destino = caminho[i + 1];
            custoTotal += grafo[origem][destino];
        }

        int ultimaCidade = caminho[numCidades - 1];
        int cidadeInicial = caminho[0];
        custoTotal += grafo[ultimaCidade][cidadeInicial];

        if (melhorCaminho.isEmpty() || custoTotal < calcularCustoTotal(melhorCaminho)) {
            melhorCaminho = new ArrayList<>(Arrays.asList(Arrays.stream(caminho).boxed().toArray(Integer[]::new)));
        }
    }*/
    
    private int calcularCustoTotal(List<Integer> caminho) {
    int custoTotal = 0;

    if (!caminho.isEmpty()) { // Verifica se a lista não está vazia
        for (int i = 0; i < numCidades - 1; i++) {
            int origem = caminho.get(i);
            int destino = caminho.get(i + 1);
            custoTotal += grafo[origem][destino];
        }

        int ultimaCidade = caminho.get(numCidades - 1);
        int cidadeInicial = caminho.get(0);
        custoTotal += grafo[ultimaCidade][cidadeInicial];
    }

    return custoTotal;
}

    public List<Integer> getMelhorCaminho() {
        return melhorCaminho;
    }
//static
    public int[][] grafoCompletoPonderado(int vertices) {
        int[][] matriz = new int[vertices][vertices];
        //Random aleatorio = new Random(); // - > receber valores diferentes na matriz toda vez que rodar o código
        int valor;
        for (int i = 0; i < matriz.length; i++) {
            matriz[i][i] = -1;
            escreverGrafoEmArquivo(i, i, matriz[i][i], "grafos-backtracking.txt");
            for (int j = i + 1; j < matriz.length; j++) {
                valor = aleatorio.nextInt(25) + 1;
                matriz[i][j] = valor;
                matriz[j][i] = valor;
                // System.out.println("[" + i + "][" + j + "] - " + matriz[i][j]);
                escreverGrafoEmArquivo(i, j, matriz[i][j], "grafos-backtracking.txt");
            }
        }
        return matriz;
    }

    private static void escreverGrafoEmArquivo(int i, int j, int grafo, String nomeArquivo) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true))) {

            writer.write((i + ", " + j + ", " + grafo));
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
