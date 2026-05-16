package entidades;
import java.util.Optional;

import enums.Operacao;

public class Aviao {
  private final Integer id;
  private Integer combustivel;
  private Optional<Operacao> operacao;
  
  public Aviao(Integer id, Integer combustivel) {
    this.id = id;
    this.combustivel = combustivel;
  }

  public Aviao(Integer id, Integer combustivel, Operacao operacao) {
    this.id = id;
    this.combustivel = combustivel;
    this.operacao = Optional.ofNullable(operacao);
  }

  public void decrementarCombustivel() {
    combustivel--;
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

  public Optional<Operacao> getOperacao() {
    return operacao;
  }
}