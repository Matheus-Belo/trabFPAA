import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class TSP {
    private List<Integer> melhorCaminho;

    private static int tsp_dp(int[][] graph, int start, Set<Integer> visited, Map<String, Integer> dp) {
        // Verifica se todos os vértices foram visitados
        if (visited.size() == graph.length) {
            return graph[start][0]; // Retorna o custo de voltar para a cidade inicial
        }

        // Verifica se a solução para o subproblema atual já foi calculada
        String key = start + "_" + visited.toString();
        if (dp.containsKey(key)) {
            return dp.get(key);
        }

        int min_cost = Integer.MAX_VALUE; // Valor máximo para inicializar o custo mínimo
        List<Integer> melhorCaminho = new ArrayList<>();
        // Itera por todas as cidades
        for (int city = 0; city < graph.length; city++) {
            if (!visited.contains(city)) {
                // Marca a cidade como visitada
                visited.add(city);
                // Calcula o custo para visitar a cidade atual e seguir para as próximas cidades
                int cost = graph[start][city] + tsp_dp(graph, city, visited, dp);
                // Atualiza o custo mínimo
                // int cost_ref = min_cost;
                min_cost = Math.min(min_cost, cost);
                // if (cost_ref != min_cost) {
                // melhorCaminho.add(city);
                // }
                // Desmarca a cidade como visitada (backtracking)
                visited.remove(city);
            }
        }

        // Armazena a solução para o subproblema atual
        dp.put(key, min_cost);

        return min_cost;
    }

    /**
     * Aleatório "fixo" para geração de testes repetitíveis
     */
    static Random aleatorio = new Random(42);

    /**
     * Retorna uma matriz quadrada de "vertices" x "vertices" com números inteiros,
     * representando um grafo completo. A diagonal principal está preenchida com
     * valor -1, indicando que não há aresta.
     * 
     * @param vertices A quantidade de vértices do grafo.
     * @return Matriz quadrada com custos de movimentação entre os vértices.
     */
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

    public static void printMatrix(int[][] matrix) {

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println(" ");
        }

    }

    public static void main(String[] args) {
        // int[][] graph = { { 0, 10, 15, 20 },
        // { 10, 0, 35, 25 },
        // { 15, 35, 0, 30 },
        // { 20, 25, 30, 0 } };
        int elapsedTotalTime = 0, mediaTempo = 0;
        boolean continuar = true;
        int vertices = 10, tamanhoGrupo = 1000;

        // graph = grafoCompletoPonderado(20);

        // printMatrix(graph);

        // Set<Integer> visited = new HashSet<>();
        // visited.add(0); // Adicione a cidade inicial às cidades visitadas

        // Map<String, Integer> dp = new HashMap<>();

        // int minCost = tsp_dp(graph, 0, visited, dp);
        // System.out.println("Minimum cost: " + minCost);

        while (continuar) {
            for (int tamanhoAtual = 0; tamanhoAtual < tamanhoGrupo; tamanhoAtual++) {

                int[][] graph = grafoCompletoPonderado(vertices);

                // printMatrix(graph);
                System.out.println("-------------------------------------------------------");

                Set<Integer> visited = new HashSet<>();

                visited.add(0); // Adicione a cidade inicial às cidades visitadas

                Map<String, Integer> dp = new HashMap<>();

                // Este bloco calcula o tempo gasto pro grafo em PD e escreve o arquivo
                long elapsedTime = 0;
                long startTime = System.currentTimeMillis();
                int minCost = tsp_dp(graph, 0, visited, dp);
                System.out.println("Minimum cost: " + minCost);
                elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Execution time: " + elapsedTime + " milliseconds.");
                elapsedTotalTime += elapsedTime;
                // escreverSolucaoEmArquivo(tamanhoAtual, nomeArquivo, melhorCaminho,
                // elapsedTimeFB,
                // "programação dinâmica");
                // mediaTempo = (elapsedTime / tamanhoGrupo);

                System.out.println("-------------------------------------------------------");
            }
            continuar = false;
            mediaTempo = (elapsedTotalTime / tamanhoGrupo);
            System.out.println("Tempo médio de programação dinâmica: " + mediaTempo + " millisegundos.");
            System.out.println("-------------------------------------------------------");
        }

    }
}
