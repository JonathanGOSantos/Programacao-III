package aeroporto.entidades;

import aeroporto.enums.Operacao;

public class Aviao {
  private final Integer id;
  private Integer combustivel;
  private Integer tempoDeOperacao;
  private Operacao operacao;

  public Aviao(Integer id, Integer combustivel, Operacao operacao) {
    this.id = id;
    this.tempoDeOperacao = 0;
    this.combustivel = combustivel;
    this.operacao = operacao;
  }

  public void incrementarTempoDeOperacao() {
    this.tempoDeOperacao++;
  }

  public void decrementarCombustivel() {
    if (this.operacao == Operacao.POUSO && combustivel > 0) {
      combustivel--;
    }
  }

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