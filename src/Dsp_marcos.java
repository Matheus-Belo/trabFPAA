import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dsp_marcos {
    private static int INFINITO = 999999;
    private static int[][] matrizDistancias;
    private static int[][] tabela;

    public static int caixeiroViajante(int cidadeAtual, List<Integer> cidadesVisitadas) {
        if (cidadesVisitadas.size() == matrizDistancias.length) {
            return matrizDistancias[cidadeAtual][0];
        }

        if (tabela[cidadeAtual][cidadesVisitadas.size()] != -1) {
            return tabela[cidadeAtual][cidadesVisitadas.size()];
        }

        int resultado = INFINITO;

        for (int proximaCidade = 0; proximaCidade < matrizDistancias.length; proximaCidade++) {
            if (!cidadesVisitadas.contains(proximaCidade)) {
                cidadesVisitadas.add(proximaCidade);
                int custo = matrizDistancias[cidadeAtual][proximaCidade] + caixeiroViajante(proximaCidade, cidadesVisitadas);
                resultado = Math.min(resultado, custo);
                cidadesVisitadas.remove(cidadesVisitadas.size() - 1);
            }
        }

        tabela[cidadeAtual][cidadesVisitadas.size()] = resultado;
        return resultado;
    }

    public static void printMatrix(int[][]matrix){

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println(" ");
        }

    }

    public static void main(String[] args) {
        matrizDistancias = new int[][] {
                { 0, 10, 15, 20 },
                { 10, 0, 35, 25 },
                { 15, 35, 0, 30 },
                { 20, 25, 30, 0 }
        };

        tabela = new int[matrizDistancias.length][matrizDistancias.length];
        for (int i = 0; i < matrizDistancias.length; i++) {
            Arrays.fill(tabela[i], -1);
        }

        List<Integer> cidadesVisitadas = new ArrayList<>();
        cidadesVisitadas.add(0); // Adicione a cidade inicial à lista de cidades visitadas

        int menorCaminho = caixeiroViajante(0, cidadesVisitadas);
        System.out.println("O menor caminho é: " + menorCaminho);
        printMatrix(tabela);

    }
}
