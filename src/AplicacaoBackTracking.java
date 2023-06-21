import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.List;
//import java.util.Random;

public class AplicacaoBackTracking {

    public static void main(String[] args) {
        int vertices = 10, tamanhoGrupo = 1;
        boolean continuar = true;
        long elapsedTimeFB = 0, mediaTempo = 0, elapsedTotalTimeFB = 0; //, elapsedTotalTimeGuloso = 0
        //int caminhosIguais = 0;
        String nomeArquivo = "backtracking.txt";
        List<Integer> melhorCaminho = null;
        //List<Integer> melhorCaminhoGuloso = null;

        while (continuar) {
            for (int tamanhoAtual = 0; tamanhoAtual < tamanhoGrupo; tamanhoAtual++) {
                int teste = 0;
                int [][]teste2 = new int[teste][teste];

                BackTracking back = new BackTracking(teste2);


                int[][] grafoDeTeste = { 
                    { -1,6,14,24,10,21,1,6,19,20,19,8,3,2,18,2,8,7,21,19 },
                { 6,-1,10,1,14,2,14,19,17,6,9,13,22,6,7,6,11,18,3,13 },
                { 14,10,-1,19,14,15,5,16,24,13,1,10,8,11,8,19,19,16,9,7 },
                { 24,1,19,-1,11,11,14,10,12,4,23,20,3,2,20,20,24,20,1,25 },
                { 10,14,14,11,-1,16,2,4,5,24,9,12,11,1,19,1,20,4,18,8 },
                { 21,2,15,11,16,-1,19,10,20,24,5,23,22,24,21,10,24,5,2,2 },
                { 1,14,5,14,2,19,-1,4,21,16,8,5,9,12,10,22,1,23,22,5 },
                { 6,19,16,10,4,10,4,-1,23,2,7,21,7,21,7,5,22,4,9,24 },
                { 19,17,24,12,5,20,21,23,-1,3,8,23,24,9,3,3,23,14,8,23 },
                { 20,6,13,4,24,24,16,2,3,-1,19,1,18,3,9,24,3,22,24,5 },
                { 19,9,1,23,9,5,8,7,8,19,-1,4,4,19,23,17,6,11,22,6 },
                { 8,13,10,20,12,23,5,21,23,1,4,-1,7,25,1,2,18,17,10,22 },
                { 3,22,8,3,11,22,9,7,24,18,4,7,-1,16,11,16,21,6,5,5 },
                { 2,6,11,2,1,24,12,21,9,3,19,25,16,-1,17,25,10,17,9,7 },
                { 18,7,8,20,19,21,10,7,3,9,23,1,11,17,-1,7,13,22,5,18 },
                { 2,6,19,20,1,10,22,5,3,24,17,2,16,25,7,-1,22,15,11,25 },
                { 8,11,19,24,20,24,1,22,23,3,6,18,21,10,13,22,-1,25,11,12 },
                { 7,18,16,20,4,5,23,4,14,22,11,17,6,17,22,15,25,-1,21,18 },
                { 21,3,9,1,18,2,22,9,8,24,22,10,5,9,5,11,11,21,-1,7 },
                { 19,13,7,25,8,2,5,24,23,5,6,22,5,7,18,25,12,18,7,-1} 
             };

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
