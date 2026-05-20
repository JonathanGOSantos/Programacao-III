# Relatório do Trabalho Prático 1 - Aeroporto

## 1. Decisões de Especificação e Estruturas de Dados

### 1.1 Decisão sobre a Identificação dos Aviões e Balanceamento
Conforme detalhe de especificação, optamos por utilizar um ID sequencial contínuo e sem distinção fixa par/ímpar entre aviões de pouso ou decolagem. Para diferenciar o objetivo de cada avião e permitir um balanceamento mais justo na distribuição das filas, preferimos o uso do enum `Operacao` que permite `POUSO` ou `DECOLAGEM`. 

Dessa forma, a Torre de Controle decide para qual prateleira enviar o avião avaliando dinamicamente a menor fila disponível para aquela `Operacao` específica naquele instante. Isso diminui a criação de gargalos não naturais e não restringe o sistema a assumir a natureza da operação com base em propriedades numéricas do ID, tornando a arquitetura mais orientada a objetos e autoexplicativa.

### 1.2 Estruturas de Dados Utilizadas
* **Prateleiras:** Implementadas utilizando a interface `Queue` mas internamente uma `LinkedList`. Esta escolha é essencial para o algoritmo, pois assegura que a entrada de novos aviões no fim da fila e a remoção dos aviões priorizados no início ocorram de forma eficiente e mantenham a ordem de chegada íntegra (comportamento FIFO - First-In, First-Out).
* **Torre de Controle:** Utilizamos dicionários `Map<Integer, Prateleira>` e `Map<Integer, Pista>` para organizar o acesso. Maps facilitam o acesso direto de leitura através da chave da entidade.
* **Registros e Estatísticas:** Para armazenar os registros de acontecimentos temporais, utilizamos `record`, tornando os dados encapsulados e imutaveís por padrão. O histórico é agrupado com `List` e filtrado utilizando a `Stream API` do Java.

---

## 2. Listagem dos Testes Executados

Os testes unitários focaram nas lógicas de domínio primárias de cada entidade, assegurando a confiabilidade da simulação de tempo e alocação. Os testes, desenvolvidos sob o escopo do **JUnit 5**, são:

1. **AviaoTest:** 
   - Valida a instanciação sem perda de integridade dos dados.
   - Verifica o decréscimo sucessivo de combustível e as barreiras que impedem combustível negativo.
   - Testa a flag booleana de estado de emergência.
2. **PrateleiraTest:** 
   - Testes de FIFO: garantir que o primeiro avião inserido é impreterivelmente o primeiro a ser resgatado.
   - Avaliação da ação de passagem do tempo envelhecendo/reduzindo em -1 o tanque de toda a fila simultaneamente.
   - Filtro de busca seletiva dentro da fila para detecção isolada de aeronaves em emergência.
3. **PistaTest:** 
   - Garante o controle de bloqueio.
4. **TorreDeControleTest:** 
   - Confirma a integração que avança o (`instante`), roteia novos aviões para a fila correta e processa a alocação.
5. **EstatisticasTest:** 
   - Comprova a precisão dos cálculos matemáticos responsáveis por agregar multiplos `Registros`, convertendo-os em médias através das etapas do ciclo de vida da simulação.

---

## 3. Estudo da Complexidade de Tempo (Notação Big-O)

A análise abaixo reflete o comportamento assintótico de pior caso, com a seguinte legenda das variáveis independentes:
* **F** = Número total de filas (Pouso ou Decolagem).
* **A** = Número de aviões armazenados dentro de uma fila no momento avaliado.
* **R** = Quantidade de Registros salvos no sistema.

### Inserção e Remoção em Filas
* `Prateleira.adicionarAviao(Aviao)` e `Prateleira.removerPrimeiroAviao()`: **O(1)**
  * A utilização de `LinkedList` faz com que a realocação de ponteiros para adicionar ou extrair aeronaves nas pontas da fila não dependa do tamanho da fila, sendo resolvida sempre em tempo constante.

### Processamento e Balanceamento na Torre
* `TorreDeControle.processarAviao(Aviao)`: **O(F)**
  * Para efetuar o balanceamento natural, a Torre avalia o tamanho da fila em cada uma das opções para localizar a opção menos superlotada. A complexidade é linear em relação a quantidade de prateleiras.

### Avanço Temporal e Varredura de Emergência
* `TorreDeControle.processarPistas()` e `alocarPistasParaEmergencias()`: **O(F * A)**
  *  A cada instante, o código passa por todas as prateleiras de pouso para decrementar o combustível e localizar aviões em situação de emergência. A complexidade é linear em relação a quantidade de aviões nas prateleiras.

### Cálculo Retrospectivo de Estatísticas
* `Estatisticas.tempoMedioDePouso()` e `tempoMedioDeDecolagem()`: **O(R)**
  * Quando requisitada, as médias processam o acúmulo das transações e eventos. A operação percorre todos os registros, tornando a operação uma leitura linear total sobre a base de dados em memória.
