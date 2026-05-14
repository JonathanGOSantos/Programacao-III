import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import enums.FaseOperacao;
import enums.OperacaoAeroporto;
import enums.OperacaoAviao;
import records.LedgerAeroporto;

public class Aeroporto extends Simulador {
  private Integer idAviao = 1;
  
  private List<Pista> pistas;
  private Queue<Aviao> avioes;

  private List<LedgerAeroporto> logs;

  public Aeroporto() {
    this.pistas = new ArrayList<>();
    this.avioes = new LinkedList<>();
    this.logs = new ArrayList<>();

    this.pistas.add(new Pista(this, true));
    this.pistas.add(new Pista(this, true));
    this.pistas.add(new Pista(this, false));
  }

  public void adicionarAviao(Aviao aviao, OperacaoAeroporto operacao) {
    avioes.add(aviao);

    Integer pista = 0;    
    logs.add(new LedgerAeroporto(tick, pista, aviao.getRecord(), operacao));
  }

  public void processar(Aviao aviao) {
    removerAviao(aviao);
    logs.add(new LedgerAeroporto(tick, 0, aviao.getRecord(), OperacaoAeroporto.from(aviao.getOperacao(), FaseOperacao.FIM)));
  }
  
  public void emergencia(Aviao aviao) {
    removerAviao(aviao);
  }

  private void removerAviao(Aviao aviao) {
    Iterator<Aviao> avioes = this.avioes.iterator();
    while (avioes.hasNext()) {
      Aviao a = avioes.next();
      if (a.equals(aviao)) {
        avioes.remove();
        break;
      }
    }
  }

  private Aviao novoAviao(OperacaoAviao operacao) {
    Aviao aviao;
    aviao = new Aviao(idAviao++, null);
    if (operacao == OperacaoAviao.DECOLAR)
      idAviao++;
    return aviao;
  }

  @Override
  public void runTick() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'runTick'");
  }
}