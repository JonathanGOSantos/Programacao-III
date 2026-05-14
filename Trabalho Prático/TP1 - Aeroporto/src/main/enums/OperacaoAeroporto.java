package enums;

public enum OperacaoAeroporto {
  INICIO_ATERRISSAGEM, INICIO_DECOLAGEM, FIM_ATERRISAGEM, FIM_DECOLAGEM;

  public static OperacaoAeroporto from(OperacaoAviao operacao, FaseOperacao fase) {
    if (operacao == OperacaoAviao.ATERRISSAR) {
      if (fase == FaseOperacao.INICIO) {
        return OperacaoAeroporto.INICIO_ATERRISSAGEM;
      } else {
        return OperacaoAeroporto.FIM_DECOLAGEM;
      }
    } else {
      if (fase == FaseOperacao.INICIO) {
        return OperacaoAeroporto.INICIO_DECOLAGEM;
      } else {
        return OperacaoAeroporto.FIM_DECOLAGEM;
      }
    }
  }
}
