import java.util.Random;

public class CaixeiroViajanteProgDinamica_Marcos {

   /* Criar uma tabela de instâncias x valores;
    Inicializar primeira linha e primeira coluna;
     Para cada linha,
        Para cada coluna,
            Comparar o resultado anterior com o resultado ao incluir a instância atual;
            Armazenar o melhor resultado;
    O resultado estará no final da tabela;
    Reconstruir o caminho do resultado (ou consultar objeto);*/

    /*

            n= aresta atual
     */
    //C(S, i) = min { C(n-{i}, j) + dis(j, i)}


    /**
     * Aleatório "fixo" para geração de testes repetitíveis
     */
    static Random aleatorio = new Random(42);

    /**
     * Retorna uma matriz quadrada de "vertices" x "vertices" com números inteiros,
     * representando um grafo completo. A diagonal principal está preenchida com
     * valor -1, indicando que não há aresta.
     * @param vertices A quantidade de vértices do grafo.
     * @return Matriz quadrada com custos de movimentação entre os vértices.
     */
    public static int[][] grafoCompletoPonderado(int vertices){
        int[][] matriz = new int[vertices][vertices];
        int valor;
        for (int i = 0; i < matriz.length; i++) {
            matriz[i][i]=-1;
            for (int j = i+1; j < matriz.length; j++) {
                valor = aleatorio.nextInt(25)+1;
                matriz[i][j] = valor;
                matriz[j][i] = valor;
            }
        }
        return matriz;
    }


    public static void printMatrix(int[][]matrix){

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[i][j]+" ");
            }
            System.out.println(" ");
        }

    }

    public static int[][] tspPD (int[][] grafoCidades){

        int[][] resp = new int[grafoCidades.length][grafoCidades.length];

        // iniciar a matrix com 0 para inserção das cidades
        for (int i = 0; i < resp.length; i++) {
            for (int j = 0; j < resp.length; j++) {
                resp[i][j]=0;
            }
        }


        for (int i = 0; i < resp.length; i++) {
            for (int j = 0; j < resp.length; j++) {
                resp[i][j]=0;
            }
        }

        return  resp;
    }


    public static void main(String[] args) {

            printMatrix(grafoCompletoPonderado(5));


    }
}
