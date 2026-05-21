package aeroporto.entidades;

import aeroporto.enums.Operacao;

/**
 * Representa um avião no sistema, mantendo informações
 * sobre seu ID, combustível restante e operação desejada (pouso ou decolagem).
 */
public class Aviao {
  private final Integer id;
  private Integer combustivel;
  private Integer tempoDeOperacao;
  private Operacao operacao;

  /**
   * Cria um novo avião.
   *
   * @param id Identificador único do avião.
   * @param combustivel Quantidade inicial de combustível.
   * @param operacao Operação pretendida (POUSO ou DECOLAGEM).
   */
  public Aviao(Integer id, Integer combustivel, Operacao operacao) {
    this.id = id;
    this.tempoDeOperacao = 0;
    this.combustivel = combustivel;
    this.operacao = operacao;
  }

  public void incrementarTempoDeOperacao() {
    this.tempoDeOperacao++;
  }

  /**
   * Decrementa em 1 a quantidade de combustível atual do avião.
   * O combustível nunca fica menor que zero.
   */
  public void decrementarCombustivel() {
    if (this.operacao == Operacao.POUSO && combustivel > 0) {
      combustivel--;
    }
  }

  /**
   * Verifica se o avião está em situação crítica, ou seja, sem combustível.
   *
   * @return true se o combustível for zero.
   */
  public Boolean emSituacaoCritica() {
    return combustivel != null && combustivel == 0;
  }

  public Integer getId() {
    return id;
  }

  public Integer getCombustivel() {
    return combustivel;
  }

  public Operacao getOperacao() {
    return operacao;
  }

  public Integer getTempoDeOperacao() {
    return tempoDeOperacao;
  }
}