import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CaixeiroViajanteForcaBruta {

    private int[][] grafo;
    private int numCidades;
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
            for (int j = i + 1; j < matriz.length; j++) {
                valor = aleatorio.nextInt(25) + 1;
                matriz[i][j] = valor;
                matriz[j][i] = valor;
                //System.out.println("[" + i + "][" + j + "] - " + matriz[i][j]);
            }
        }
        return matriz;
    }
    public static void main(String[] args) {
        int vertices = 5, tamanhoGrupo = 70;
        boolean continuar = true;
        long elapsedTime = 0;
        long startTime = System.currentTimeMillis();
        while (continuar) {
            for (int tamanhoAtual = 0; tamanhoAtual < tamanhoGrupo; tamanhoAtual++) {
                int[][] grafo = grafoCompletoPonderado(vertices);
                CaixeiroViajanteForcaBruta caixeiroViajante = new CaixeiroViajanteForcaBruta(grafo);
                caixeiroViajante.encontrarMelhorCaminho();
                List<Integer> melhorCaminho = caixeiroViajante.getMelhorCaminho();
                System.out.println("Melhor caminho encontrado para tamanho " + tamanhoAtual + " do vertice " + vertices + ". Seguindo...");
                System.out.println("-------------------------------------------------------");
                elapsedTime = System.currentTimeMillis() - startTime;
                if (elapsedTime > 240000) {
                    System.out.println("O número de vértices que executa iterando por 4min é " + vertices);
                    System.out.println("-----------------------------------------");
                    System.out.println("Execução interrompida");
                    System.out.println("-----------------------------------------");
                    System.out.println("N-1: " + (vertices-1));
                    continuar = false;
                    break;
                }
                /*for (Integer cidade : melhorCaminho) {
                //    System.out.print(cidade + " ");
                }*/
            }
            long mediaTempo = (elapsedTime / tamanhoGrupo);
            System.out.println("Tempo total de iteração: " + elapsedTime + " millisegundos.");
            System.out.println("Tempo médio de iteração para grupo de " + vertices + " vertices: " + mediaTempo + " millisegundos.");
            vertices++;
        }
    }
}
