package aeroporto.simulacao;

import aeroporto.entidades.Aviao;
import aeroporto.enums.Estagio;
import aeroporto.enums.Operacao;

/**
 * Gerencia e consolida as métricas e estatísticas da simulação do aeroporto.
 * Calcula dados como tempo médio de espera e quantidade de pousos emergenciais,
 * utilizando estruturas de dados otimizadas para processamento em tempo real.
 */
public class Estatisticas {
  private Long totalAvioesDecolagem;
  private Long totalAvioesPouso;
  private Long tempoTotalParaPouso;
  private Long tempoTotalParaDecolagem;
  private Long totalAvioesPousaramSemCombustivel;

  /**
   * Construtor padrão da classe Estatísticas.
   * Inicializa os mapas de histórico e zera todos os contadores de métricas.
   */
  public Estatisticas() {
    totalAvioesPouso = 0L;
    totalAvioesDecolagem = 0L;
    tempoTotalParaPouso = 0L;
    tempoTotalParaDecolagem = 0L;
    totalAvioesPousaramSemCombustivel = 0L;
  }

  /**
   * Processa e armazena um novo registro da simulação.
   * Acumula o tempo de operação e atualiza as contagens gerais de decolagens e
   * pousos no momento exato em que o registro é inserido, evitando cálculos
   * pesados posteriores.
   *
   * @param registro O evento/registro a ser processado e adicionado ao histórico.
   */
  public void novoRegistro(Integer instante, Aviao aviao, Estagio estagio, Operacao operacao,
      Integer pista, Integer prateleira) {
    Registro registro = new Registro(instante, aviao.getId(), aviao.getCombustivel(), estagio, operacao, pista,
        prateleira);

    if (registro.estagio() == Estagio.INICIOU) {
      if (registro.operacao() == Operacao.DECOLAGEM) {
        totalAvioesDecolagem++;
      } else if (registro.operacao() == Operacao.POUSO) {
        totalAvioesPouso++;
      }
    } else if (registro.estagio() == Estagio.FINALIZOU) {

      if (registro.operacao() == Operacao.DECOLAGEM) {
        tempoTotalParaDecolagem += aviao.getTempoDeOperacao();
      } else if (registro.operacao() == Operacao.POUSO) {
        tempoTotalParaPouso += aviao.getTempoDeOperacao();
        if (registro.combustivel() == 0) {
          totalAvioesPousaramSemCombustivel++;
        }
      }
    }
    
    if (estagio == Estagio.CAIU) {
      System.out.printf("- [ALERTA] Avião %d CAIU por falta de combustível na prateleira %d (Pista %d)!\n", 
          aviao.getId(), prateleira, pista);
      return;
    }

    String fraseAcao = switch (estagio) {
      case INICIOU -> (operacao == Operacao.POUSO) 
          ? "chegou para pousar e entrou em órbita na prateleira" 
          : "entrou na fila de espera para decolar na prateleira";
          
      case FINALIZOU -> (operacao == Operacao.POUSO) 
          ? "pousou com sucesso a partir da prateleira" 
          : "decolou com sucesso a partir da prateleira";
          
      default -> "";
    };

    // 3. Print final com a gramática e a semântica da aviação perfeitas
    System.out.printf("- Avião %d %s %d da pista %d com %d un. de combustível.\n", 
        aviao.getId(), fraseAcao, prateleira, pista, aviao.getCombustivel());
  }

  /**
   * Calcula o tempo médio geral de espera das aeronaves nas filas de solo para
   * decolar.
   *
   * @return O tempo médio de espera para decolagens em unidades de tempo. Retorna
   *         0.0 se nenhuma decolagem for registrada.
   */
  public Double tempoMedioDeDecolagem() {
    if (totalAvioesDecolagem.compareTo(0L) == 0L)
      return 0.0;
    return tempoTotalParaDecolagem / totalAvioesDecolagem.doubleValue();
  }

  /**
   * Calcula o tempo médio geral de espera das aeronaves para realizar operações
   * de aterrissagem.
   *
   * @return O tempo médio de espera para pousos em unidades de tempo. Retorna 0.0
   *         se nenhum pouso for registrado.
   */
  public Double tempoMedioDePouso() {
    if (totalAvioesPouso.compareTo(0L) == 0L)
      return 0.0;
    return tempoTotalParaPouso / totalAvioesPouso.doubleValue();
  }

  /**
   * Informa a quantidade total de aeronaves que atingiram o nível zero de
   * combustível e pousaram sob condições de emergência durante todo o período da
   * simulação.
   *
   * @return O número de aviões que aterrissaram sem reserva de combustível.
   */
  public Long avioesSemCombustivel() {
    return totalAvioesPousaramSemCombustivel;
  }
}