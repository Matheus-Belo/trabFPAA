
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
 * 
 * @param vertices A quantidade de vértices do grafo.
 * @return Matriz quadrada com custos de movimentação entre os vértices.
 */

public class AplicacaoForcaBruta {

    // static Random aleatorio = new Random(42); // repete os mesmos valores da
    // matriz, toda vez que roda o código. Colocar essa parte comentada quando
    // quiser valores diferentes

    public static void main(String[] args) {
        // caixeiro viajante - > visitar n cidades, passando por todas elas e encontrar
        // o caminho que torna mínima a viagem total.
        // força bruta testar todos os caminhos possíveis

        int vertices = 5;

        int nMinusOne = -1; // Valor N-1 que satisfaz o requisito

        

        boolean continua = true;

        // long tempoInicialCodigo = System.currentTimeMillis();
        // long tempoFinalCodigo = System.currentTimeMillis(); // Obtém o tempo de
        // término em milissegundos
        // long tempoTotalCodigo = (tempoFinalCodigo - tempoInicialCodigo) / tamanho; //
        // Calcula o tempo total em
        // milissegundos
        // System.out.println("Tempo Médio Gasto no código: " + tempoTotalCodigo + "
        // ms");

        long startTime = System.currentTimeMillis();

        while (continua) {
            int quantidadeTotalDeVertices = vertices;
            long tamanho = 70;
            int testeCadaTamanho;
            long tempoTotalTamanho = 0;

            // for (vertices = 5; vertices < quantidadeTotalDeVertices; vertices++) {
                long tamanhoInicioTempo = System.currentTimeMillis();
            for (testeCadaTamanho = 0; testeCadaTamanho < tamanho; testeCadaTamanho++) {
                
                int[][] grafo = grafoCompletoPonderado(quantidadeTotalDeVertices);

                List<Integer> caminhoAtual = new ArrayList<>();
                caminhoAtual.add(0); // Adicione o vértice inicial ao caminho atual

                List<Integer> melhorCaminho = new ArrayList<>();
                AtomicInteger menorDistancia = new AtomicInteger(Integer.MAX_VALUE);

                
                encontrarMelhorCaminho(grafo, caminhoAtual, melhorCaminho, menorDistancia);
                
System.out.println("vertice - "+vertices);
                System.out.println("Teste " + (testeCadaTamanho + 1) + " Melhor caminho: " + melhorCaminho);
                System.out.println("Teste " + (testeCadaTamanho + 1) + " Menor distância: " + menorDistancia.get());
                // Verifica se o tempo total excedeu 4 minutos
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 4 * 60 * 1000) {
                System.out.println("chegou na segunda condicao");
                continua = false;
                break;
            }
            }

            // }
            
            long tamanhoFinalTempo = System.currentTimeMillis();

            long tamanhoTempo = tamanhoFinalTempo - tamanhoInicioTempo;
            
            tempoTotalTamanho += tamanhoTempo;

            long tempoMedio = tempoTotalTamanho / tamanho;

            System.out.println("Para N vertices = " + vertices + ", Tempo Médio Gasto: " + tempoMedio + " ms");

            // Verifica se o tempo médio está dentro do requisito de aproximadamente 3,5
            // segundos
            if (tempoMedio >= 3500) {
                System.out.println("chegou na primeira condicao");
                nMinusOne = vertices - 1;
                continua = false;
                
            }

            vertices++;

            // Verifica se o tempo total excedeu 4 minutos
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime > 4 * 60 * 1000) {
                System.out.println("chegou na segunda condicao");
                continua = false;
                break;
            }
        }
        

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
                System.out.println("[" + i + "][" + j + "] - " + matriz[i][j]);
            }
        }
        return matriz;
    }

    public static void encontrarMelhorCaminho(int[][] grafo, List<Integer> caminhoAtual, List<Integer> melhorCaminho,
            AtomicInteger menorDistancia) {
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
