   /**
     * Aleatório "fixo" para geração de testes repetitíveis
     */

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
     * Retorna uma matriz quadrada de "vertices" x "vertices" com números inteiros,
     * representando um grafo completo. A diagonal principal está preenchida com 
     * valor -1, indicando que não há aresta.
     * @param vertices A quantidade de vértices do grafo.
     * @return Matriz quadrada com custos de movimentação entre os vértices.
     */
    
public class AplicacaoForcaBruta{

    static Random aleatorio = new Random(42); // repete os mesmos valores da matriz, toda vez que roda o código. Colocar essa parte comentada quando quiser valores diferentes

    public static void main(String[] args) {
// caixeiro viajante - > visitar n cidades, passando por todas elas e encontrar o caminho que torna mínima a viagem total.
// força bruta testar todos os caminhos possíveis 


       int vertices = 6;

       int [][] grafo = grafoCompletoPonderado(vertices);

       List<Integer> caminhoAtual = new ArrayList<>();
        caminhoAtual.add(0); // Adicione o vértice inicial ao caminho atual

        List<Integer> melhorCaminho = new ArrayList<>();
        AtomicInteger menorDistancia = new AtomicInteger(Integer.MAX_VALUE);

        encontrarMelhorCaminho(grafo, caminhoAtual, melhorCaminho, menorDistancia);

        System.out.println("Melhor caminho: " + melhorCaminho);
        System.out.println("Menor distância: " + menorDistancia.get());

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

    public static void encontrarMelhorCaminho(int[][] grafo, List<Integer> caminhoAtual, List<Integer> melhorCaminho, AtomicInteger menorDistancia) {
        if (caminhoAtual.size() == grafo.length) {
            int distancia = calcularDistanciaCaminho(grafo, caminhoAtual);
            if (distancia < menorDistancia.get()) {
                menorDistancia.set(distancia);
                melhorCaminho.clear();
                melhorCaminho.addAll(caminhoAtual);
            }
        } else {
            for (int i = 0; i < grafo.length; i++) {
                if (!caminhoAtual.contains(i)) {
                    caminhoAtual.add(i);
                    encontrarMelhorCaminho(grafo, caminhoAtual, melhorCaminho, menorDistancia);
                    caminhoAtual.remove(caminhoAtual.size() - 1);
                }
            }
        }
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

