import enums.OperacaoAviao;
import records.AviaoRecord;

public class Aviao extends Simulador {
  private final Integer id;
  private Integer combustivel;
  private OperacaoAviao operacao;

  public Aviao(Integer id, Integer combustivel) {
    this.id = id;
    this.combustivel = combustivel;
    if (id % 2 == 0) {
      this.operacao = OperacaoAviao.DECOLAR;
    } else {
      this.operacao = OperacaoAviao.ATERRISSAR;
    }
  }

  public Integer getId() {
    return id;
  }

  public Integer getCombustivel() {
    return combustivel;
  }

  public Boolean isCombustivelCritico() {
    return combustivel == 0;
  }

  public OperacaoAviao getOperacao() {
    return operacao;
  }

  public AviaoRecord getRecord() {
    return new AviaoRecord(tick, id, combustivel);
  }

  @Override
  public void runTick() {
    if (operacao == OperacaoAviao.ATERRISSAR)
      combustivel--;
    if (combustivel < 0)
      throw new RuntimeException("O avião " + id + " caiu por falta de combustível.");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Aviao other = (Aviao) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

}