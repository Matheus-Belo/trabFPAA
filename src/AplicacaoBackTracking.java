import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import java.util.Random;

public class AplicacaoBackTracking {

    public static void main(String[] args) {
        int vertices = 10, tamanhoGrupo = 10;
        boolean continuar = true;
        long elapsedTimeFB = 0, mediaTempo = 0, elapsedTotalTimeFB = 0; //, elapsedTotalTimeGuloso = 0
        //int caminhosIguais = 0;
        String nomeArquivo = "backtracking.txt";
        List<Integer> melhorCaminho = null;
        //List<Integer> melhorCaminhoGuloso = null;

        while (continuar) {
            for (int tamanhoAtual = 0; tamanhoAtual < tamanhoGrupo; tamanhoAtual++) {

                BackTracking back = new BackTracking(null);

                int[][] grafo = back.grafoCompletoPonderado(vertices);
                BackTracking caixeiroViajante = new BackTracking(grafo);

                // Este bloco calcula o tempo gasto pro grafo em Força Bruta e escreve o arquivo
                long startTimeFB = System.currentTimeMillis();
                caixeiroViajante.encontrarMelhorCaminho();
                melhorCaminho = caixeiroViajante.getMelhorCaminho();
                elapsedTimeFB = System.currentTimeMillis() - startTimeFB;
                elapsedTotalTimeFB += elapsedTimeFB;
                if (melhorCaminho.get(0) != melhorCaminho.get(melhorCaminho.size() - 1)) {
                    melhorCaminho.add(melhorCaminho.get(0));
                }

                escreverSolucaoEmArquivo(tamanhoAtual, nomeArquivo, melhorCaminho, elapsedTimeFB, "backtracking");

               
                /*
                 * linha comentada pois não é mais utilizada para executar os 1000 algoritmos
                 * finais.
                 * Consta aqui para acompanhar como encontramos N-1
                 * if (elapsedTime > 240000) {
                 * System.out.println("O número de vértices que executa iterando por 4min é " +
                 * vertices);
                 * System.out.println("-----------------------------------------");
                 * System.out.println("Execução interrompida");
                 * System.out.println("-----------------------------------------");
                 * System.out.println("N-1: " + (vertices-1));
                 * continuar = false;
                 * break;
                 * }
                 */
            }
            continuar = false;
            mediaTempo = (elapsedTotalTimeFB / tamanhoGrupo);
            System.out.println("Tempo médio de backtracking: " + mediaTempo + " millisegundos.");
        }
    }

    private static void escreverSolucaoEmArquivo(int numGrafo, String nomeArquivo, List solucao, long tempo,
            String algoritmo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true))) {

            if (algoritmo == "backtracking") {
                writer.write("Backtracking: ");
                writer.write(solucao + " - ");
                writer.write(" tempo: " + tempo);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
