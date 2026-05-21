## Análise de Complexidade Algorítmica (Notação Big-O)

### 1. Classe `Prateleira` (Lista Duplamente Encadeada)
Nossa implementação de final manipula ponteiros diretos para a `cabeca` e `cauda`, além de manter o `tamanho` de forma dinâmica.

| Método | Complexidade | Cenário |
| :--- | :---: | :--- |
| `adicionar(Aviao aviao)` | **O(1)** | Inserção imediata no final da fila utilizando o ponteiro `cauda`. O tempo de execução independe da quantidade de aviões na fila. |
| `removerPrimeiroAviao()` | **O(1)** | Remoção direta no início da fila utilizando o ponteiro `cabeca`. Apenas altera as referências de memória do nó, sem necessidade de deslocamento. |
| `verProximoAviao()` | **O(1)** | Operação de leitura direta da propriedade do nó `cabeca`, sem realizar iterações. |
| `tamanho()` / `isVazia()` | **O(1)** | Retorna o valor de variáveis primitivas mantidas e controladas pela própria classe a cada inserção/remoção. |
| `remover(Aviao aviao)` | **O(N)** | Pior cenário: o avião a ser removido  está no meio ou fim da fila, exigindo uma busca linear nó por nó. |
| `obterEmergencias()` | **O(N)** | Obrigatoriamente passa pela fila inteira para avaliar o estado crítico de combustível de todos os $N$ aviões ali presentes. |
| `atualizarTempoDeEspera()`| **O(N)** | Executa um laço de repetição iterando por cada um dos $N$ nós para decrementar combustível e envelhecer o tempo de operação das aeronaves. |

### 2. Classe `Estatisticas`

| Método | Complexidade | Cenário |
| :--- | :---: | :--- |
| `novoRegistro(...)` | **O(1)** | Realiza apenas avaliações condicionais `if/else`, calculos matemáticos e uma saída formatada de log no console. Custo fixo. |
| `tempoMedioDeDecolagem()`| **O(1)** | Divisão aritmética elementar entre duas variáveis de tipo primitivo mantidas de forma estática em memória: `tempoTotal / totalAvioes`. |
| `tempoMedioDePouso()` | **O(1)** | Semelhante ao `tempoMedioDeDecolagem()`, ele só faz uma divisão de variáveis já presentes na memória. |

### 3. Classe `TorreDeControle`
A Torre de Controle atua como o cérebro do sistema, coordenando o fluxo entre aviões, prateleiras e pistas. Como o número de pistas (3) e o número de prateleiras (3 de decolagem e 4 de pouso) são valores fixos e constantes mapeados na inicialização, os laços de repetição que iteram sobre essas coleções não escalam com o tempo, garantindo alta eficiência.

| Método | Complexidade | Justificativa Técnica / Cenário |
| :--- | :---: | :--- |
| `processarAviao(Aviao aviao)` | **O(1)** | O algoritmo de balanceamento percorre um arranjo fixo de pistas e prateleiras para descobrir a menor fila. Como os tamanhos dessas coleções são constantes e a inserção na ponta da `Prateleira` é $O(1)$, o custo total é constante. |
| `alocarAviaoParaPista(Pista pista)` | **O(1)** | Avalia no máximo as 3 prateleiras associadas à pista informada. A verificação do id do próximo avição e a remoção do topo da fila são resolvidas em tempo constante. |
| `alocarPistas()` | **O(1)** | Apenas gerencia a distribuição dos fluxos regulares para as 3 pistas fixas do aeroporto. |
| `alocarPistasParaEmergencias()` | **O(N²)** | O método `obterEmergencias()` realiza uma busca completa na fila. Em seguida, o código itera sobre o array de emergências encontradas e remove cada avião da prateleira. Como a remoção no meio da lista encadeada exige uma busca linear de custo $O(N)$, o custo do laço passa a ser de $O(N \cdot N)$ se todos os aviões estiverem em situação crítica e com a lista a partir da cauda até a cabeça. Portanto, a complexidade atinge o nível quadrático $O(N^2)$. |
| `processarPistas()` | **O(N²)** | É o método principal disparado a cada ciclo. No pior cenário teórico, a complexidade é ditada pelo gargalo de $O(N^2)$ da remoção de emergências. Em um fluxo operacional sem emergências, o método opera em **O(N)**, tempo necessário para atualizar o combustível e a espera de todos os $N$ aviões distribuídos nas prateleiras. |
| `imprimirRelatorio()` | **O(N)** | Percorre os arranjos de prateleiras fixas e dispara o método `formatarFila()`. Como a formatação precisa inspecionar e concatenar os dados de cada nó individualmente, o tempo total é estritamente proporcional à soma de todos os $N$ aviões presentes no aeroporto. |