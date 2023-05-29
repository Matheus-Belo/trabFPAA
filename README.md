# Trabalho Fundamentos de Projeto e Análise de Algoritmos
**Professor:** João Caram

**Alunos:** Matheus Belo, Marcos Felipe, Natanael Gonzales, Leonardo Augusto

As tarefas preparatórias do seu grupo de trabalho são:

* **a)** Projetar e implementar uma solução para o problema do Caixeiro Viajante utilizando força
bruta. O professor fornecerá um “gerador de dados” que criará uma matriz VxV com os custos
de viagem. O grupo pode decidir usar ou não este método.

* **b)** Projetar e implementar uma solução para o problema do Caixeiro Viajante utilizando algoritmo
guloso. O grupo deve decidir se vai utilizar o critério demonstrado em aula ou outro à escolha.

A tarefa principal tem duas partes:

* **a)** Criar um teste automatizado. Este teste deve gerar grafos com a quantidade V de vértices
crescente, iniciando em 5, e tentar resolver o problema do Caixeiro Viajante com estes grafos.
O teste de cada tamanho de conjunto deve ser repetido 70 vezes, registrando o tempo médio
de solução para cada tamanho de conjunto. O teste será interrompido quando uma iteração
exceder o tempo de 4 minutos. Você então saberá o número N-1, que representa a quantidade
de vértices de um grafo no qual o algoritmo de força bruta consegue fornecer a resposta em
até aproximadamente 3,5 segundos.

* **b)** A partir dos resultados de (a), criar um relatório contendo o que é pedido:
  * **a.** Gere 1000 grafos aleatórios, cada um com (N-1) vértices.
  * **b.** Para cada um destes grafos, execute a solução do Caixeiro Viajante utilizando força
  bruta. Armazene a solução obtida e o tempo médio para encontrar cada solução.
  * **c.** Para cada um destes grafos, execute a solução do Caixeiro Viajante utilizando o
  algoritmo guloso. Verifique em quantas destas vezes ele encontrou a solução obtida
  pela força bruta e armazene o tempo médio para encontrar cada solução.
  * **d.** Comente as respostas encontradas.
