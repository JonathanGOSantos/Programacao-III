package aeroporto.simulacao;

import aeroporto.entidades.Aviao;
import aeroporto.enums.Estagio;
import aeroporto.enums.Operacao;

public class Estatisticas {
  private Long totalAvioesDecolagem;
  private Long totalAvioesPouso;
  private Long tempoTotalParaPouso;
  private Long tempoTotalParaDecolagem;
  private Long totalAvioesPousaramSemCombustivel;

  public Estatisticas() {
    totalAvioesPouso = 0L;
    totalAvioesDecolagem = 0L;
    tempoTotalParaPouso = 0L;
    tempoTotalParaDecolagem = 0L;
    totalAvioesPousaramSemCombustivel = 0L;
  }

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

    System.out.printf("- Avião %d %s %d da pista %d com %d un. de combustível.\n", 
        aviao.getId(), fraseAcao, prateleira, pista, aviao.getCombustivel());
  }

  public Double tempoMedioDeDecolagem() {
    if (totalAvioesDecolagem.compareTo(0L) == 0L)
      return 0.0;
    return tempoTotalParaDecolagem / totalAvioesDecolagem.doubleValue();
  }

  public Double tempoMedioDePouso() {
    if (totalAvioesPouso.compareTo(0L) == 0L)
      return 0.0;
    return tempoTotalParaPouso / totalAvioesPouso.doubleValue();
  }

  public Long avioesSemCombustivel() {
    return totalAvioesPousaramSemCombustivel;
  }
}