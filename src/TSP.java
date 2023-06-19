import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TSP {

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

        // Itera por todas as cidades
        for (int city = 0; city < graph.length; city++) {
            if (!visited.contains(city)) {
                // Marca a cidade como visitada
                visited.add(city);
                // Calcula o custo para visitar a cidade atual e seguir para as próximas cidades
                int cost = graph[start][city] + tsp_dp(graph, city, visited, dp);
                // Atualiza o custo mínimo
                min_cost = Math.min(min_cost, cost);
                // Desmarca a cidade como visitada (backtracking)
                visited.remove(city);
            }
        }

        // Armazena a solução para o subproblema atual
        dp.put(key, min_cost);

        return min_cost;
    }

    public static void main(String[] args) {
        int[][] graph = { { 0, 10, 15, 20 },
                { 10, 0, 35, 25 },
                { 15, 35, 0, 30 },
                { 20, 25, 30, 0 } };

        Set<Integer> visited = new HashSet<>();
        visited.add(0); // Adicione a cidade inicial às cidades visitadas

        Map<String, Integer> dp = new HashMap<>();

        int minCost = tsp_dp(graph, 0, visited, dp);
        System.out.println("Minimum cost: " + minCost);
    }
}
