import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CaixeiroViajanteForcaBruta {

    private final int[][] grafo;
    private final int numCidades;
    private List<Integer> melhorCaminho;

    public CaixeiroViajanteForcaBruta(int[][] grafo) {
        this.grafo = grafo;
        this.numCidades = grafo.length;
        this.melhorCaminho = new ArrayList<>();
    }

    public void encontrarMelhorCaminho() {
        int[] caminhoAtual = new int[numCidades];
        int[] todasCidades = new int[numCidades];

        for (int i = 0; i < numCidades; i++) {
            todasCidades[i] = i;
        }

        permutarCaminhos(todasCidades, caminhoAtual, 0);
    }

    private void permutarCaminhos(int[] todasCidades, int[] caminhoAtual, int indice) {
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
    }

    private boolean visitado(int cidade, int[] caminho, int indice) {
        for (int i = 0; i < indice; i++) {
            if (caminho[i] == cidade) {
                return true;
            }
        }
        return false;
    }

    private void calcularCustoCaminho(int[] caminho) {
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
    }

    private int calcularCustoTotal(List<Integer> caminho) {
        int custoTotal = 0;

        for (int i = 0; i < numCidades - 1; i++) {
            int origem = caminho.get(i);
            int destino = caminho.get(i + 1);
            custoTotal += grafo[origem][destino];
        }

        int ultimaCidade = caminho.get(numCidades - 1);
        int cidadeInicial = caminho.get(0);
        custoTotal += grafo[ultimaCidade][cidadeInicial];

        return custoTotal;
    }

    public List<Integer> getMelhorCaminho() {
        return melhorCaminho;
    }

    public static int[][] grafoCompletoPonderado(int vertices) {
        int[][] matriz = new int[vertices][vertices];
        Random aleatorio = new Random(); // - > receber valores diferentes na matriz toda vez que rodar o código
        int valor;
        for (int i = 0; i < matriz.length; i++) {
            matriz[i][i] = -1;
            escreverGrafoEmArquivo(i, i, matriz[i][i], "grafos-caixeiro-viajante.txt");
            for (int j = i + 1; j < matriz.length; j++) {
                valor = aleatorio.nextInt(25) + 1;
                matriz[i][j] = valor;
                matriz[j][i] = valor;
                // System.out.println("[" + i + "][" + j + "] - " + matriz[i][j]);
                escreverGrafoEmArquivo(i, j, matriz[i][j], "grafos-caixeiro-viajante.txt");
            }
        }
        return matriz;
    }

    private static void escreverSolucaoEmArquivo(int numGrafo, String nomeArquivo, List solucao, long tempo,
            String algoritmo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true))) {

            if (algoritmo == "guloso") {
                writer.write("Guloso: ");
                writer.write(solucao + " - ");
                writer.write(" tempo: " + tempo);
                writer.newLine();
            } else {
                writer.write("Força Bruta: ");
                writer.write(solucao + " - ");
                writer.write(" tempo: " + tempo);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void escreverGrafoEmArquivo(int i, int j, int grafo, String nomeArquivo) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo, true))) {

            writer.write((i + ", " + j + ", " + grafo));
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int vertices = 10, tamanhoGrupo = 1000;
        boolean continuar = true;
        long elapsedTimeFB = 0, mediaTempo = 0, elapsedTotalTimeFB = 0, elapsedTotalTimeGuloso = 0;
        int caminhosIguais = 0;
        String nomeArquivo = "caixeiro-viajante.txt";
        List<Integer> melhorCaminho = null;
        List<Integer> melhorCaminhoGuloso = null;

        while (continuar) {
            for (int tamanhoAtual = 0; tamanhoAtual < tamanhoGrupo; tamanhoAtual++) {

                int[][] grafo = grafoCompletoPonderado(vertices);
                CaixeiroViajanteForcaBruta caixeiroViajante = new CaixeiroViajanteForcaBruta(grafo);

                // Este bloco calcula o tempo gasto pro grafo em Força Bruta e escreve o arquivo
                long startTimeFB = System.currentTimeMillis();
                caixeiroViajante.encontrarMelhorCaminho();
                melhorCaminho = caixeiroViajante.getMelhorCaminho();
                elapsedTimeFB = System.currentTimeMillis() - startTimeFB;
                elapsedTotalTimeFB += elapsedTimeFB;
                if (melhorCaminho.get(0) != melhorCaminho.get(melhorCaminho.size() - 1)) {
                    melhorCaminho.add(melhorCaminho.get(0));
                }

                escreverSolucaoEmArquivo(tamanhoAtual, nomeArquivo, melhorCaminho, elapsedTimeFB, "forca-bruta");

                // Este bloco calcula o tempo gasto pro grafo em Guloso e escreve o arquivo
                long startTimeGuloso = System.nanoTime();
                melhorCaminhoGuloso = caixeiroViajanteGuloso(grafo);
                long elapsedTimeGuloso = System.nanoTime() - startTimeGuloso;
                elapsedTotalTimeGuloso += elapsedTimeGuloso;
                escreverSolucaoEmArquivo(tamanhoAtual, nomeArquivo, melhorCaminhoGuloso, elapsedTimeGuloso, "guloso");

                if (melhorCaminhoGuloso.equals(melhorCaminho)) {
                    escreverSolucaoEmArquivo(tamanhoAtual, "iguais.txt", melhorCaminhoGuloso, elapsedTimeGuloso,
                            "guloso");
                    caminhosIguais++;
                }
                System.out.println("Melhor caminho encontrado para tamanho " + tamanhoAtual + " do vertice " + vertices
                        + ". Seguindo...");
                // mediaTempo = (elapsedTime / tamanhoGrupo);

                System.out.println("-------------------------------------------------------");
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
            System.out.println("Tempo médio de força bruta: " + mediaTempo + " millisegundos.");
            mediaTempo = (elapsedTotalTimeGuloso / tamanhoGrupo);
            System.out.println("Tempo médio guloso: " + mediaTempo + " nano segundos.");
            System.out.println("Execução finalizada, foram encontrados " + caminhosIguais + " caminhos iguais.");
            System.out.println("-------------------------------------------------------");
        }
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
