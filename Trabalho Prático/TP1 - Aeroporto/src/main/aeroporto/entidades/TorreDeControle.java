package aeroporto.entidades;

import aeroporto.enums.Estagio;
import aeroporto.enums.Operacao;
import aeroporto.simulacao.Estatisticas;


public class TorreDeControle {
  private Integer instante;
  private Estatisticas estatisticas;

  private Pista[] pistas;
  private Pista[] pistasDeDecolagem;
  private Pista[] pistasDePouso;

  private Prateleira[] prateleirasDeDecolagem;
  private Prateleira[] prateleirasDePouso;

  public TorreDeControle() {
    this.instante = 1;
    estatisticas = new Estatisticas();

    popularPistas();
    popularPrateleirasDeDecolagem();
    popularPrateleirasDePouso();
  }

  private void popularPistas() {
    pistas = new Pista[3];
    pistas[0] = new Pista(1);
    pistas[1] = new Pista(2);
    pistas[2] = new Pista(3);

    pistasDePouso = new Pista[2];
    pistasDePouso[0] = pistas[0];
    pistasDePouso[1] = pistas[1];

    pistasDeDecolagem = new Pista[3];
    pistasDeDecolagem[0] = pistas[0];
    pistasDeDecolagem[1] = pistas[1];
    pistasDeDecolagem[2] = pistas[2];
  }

  private void popularPrateleirasDeDecolagem() {
    prateleirasDeDecolagem = new Prateleira[3];

    prateleirasDeDecolagem[0] = new Prateleira(1, Operacao.DECOLAGEM);
    prateleirasDeDecolagem[1] = new Prateleira(2, Operacao.DECOLAGEM);
    prateleirasDeDecolagem[2] = new Prateleira(3, Operacao.DECOLAGEM);
  }

  private void popularPrateleirasDePouso() {
    prateleirasDePouso = new Prateleira[4];

    prateleirasDePouso[0] = new Prateleira(1, Operacao.POUSO);
    prateleirasDePouso[1] = new Prateleira(2, Operacao.POUSO);
    prateleirasDePouso[2] = new Prateleira(3, Operacao.POUSO);
    prateleirasDePouso[3] = new Prateleira(4, Operacao.POUSO);
  }

  public void processarAviao(Aviao aviao) {
    switch (aviao.getOperacao()) {
      case DECOLAGEM -> {
        Pista pista = pistasDeDecolagem[0];
        Integer totalAvioes = null;
        for (Pista p : pistasDeDecolagem) {
          Prateleira[] prateleiras = prateleirasDe(p);
          Integer total = 0;
          for (Prateleira prateleira : prateleiras) {
            total += prateleira.tamanho();
          }
          if (totalAvioes == null || total < totalAvioes) {
            pista = p;
            totalAvioes = total;
          }
        }

        Prateleira prateleira = prateleirasDe(pista)[0];
        prateleira.adicionar(aviao);
        estatisticas.novoRegistro(instante, aviao, Estagio.INICIOU, Operacao.DECOLAGEM,
            pista.getId(), prateleira.getId());
      }
      case POUSO -> {
        Pista pista = pistasDePouso[0];
        Integer totalAvioes = null;
        for (Pista p : pistasDePouso) {
          Prateleira[] prateleiras = prateleirasDe(p);
          Integer total = 0;
          for (Prateleira prateleira : prateleiras) {
            total += prateleira.tamanho();
          }
          if (totalAvioes == null || total < totalAvioes) {
            pista = p;
            totalAvioes = total;
          }
        }

        Prateleira[] prateleiras = prateleirasDe(pista);
        Prateleira prateleira;
        if (prateleiras[1].tamanho() <= prateleiras[2].tamanho()) {
          prateleira = prateleiras[1];
        } else {
          prateleira = prateleiras[2];
        }
        prateleira.adicionar(aviao);

        estatisticas.novoRegistro(instante, aviao, Estagio.INICIOU, Operacao.POUSO,
            pista.getId(), prateleira.getId());

      }
    }
  }

  public void processarPistas() {
    for (Pista pista : pistas) {
      pista.liberar();
    }

    alocarPistasParaEmergencias();
    alocarPistas();

    for (Prateleira prateleira : prateleirasDeDecolagem) {
      prateleira.atualizarTempoDeEspera();
    }

    for (Prateleira prateleira : prateleirasDePouso) {
      prateleira.atualizarTempoDeEspera();
    }
  }

  private void alocarPistasParaEmergencias() {
    for (Prateleira prateleira : prateleirasDePouso) {
      Aviao[] emergencias = prateleira.obterEmergencias();
      for (Aviao emergencia : emergencias) {
        Pista pista;
        if (!pistas[2].emUso()) {
          pista = pistas[2];
        } else if (!pistas[0].emUso()) {
          pista = pistas[0];
        } else if (!pistas[1].emUso()) {
          pista = pistas[1];
        } else {
          prateleira.remover(emergencia);
          estatisticas.novoRegistro(instante, emergencia,
              Estagio.CAIU, Operacao.POUSO, -1, prateleira.getId());
          continue;
        }

        pista.ocupar();
        prateleira.remover(emergencia);
        estatisticas.novoRegistro(instante, emergencia, Estagio.FINALIZOU, Operacao.POUSO,
            pista.getId(), prateleira.getId());
      }
    }
  }

  private void alocarPistas() {
    if (!pistas[0].emUso()) {
      alocarAviaoParaPista(pistas[0]);
    }
    if (!pistas[1].emUso()) {
      alocarAviaoParaPista(pistas[1]);
    }
    if (!pistas[2].emUso()) {
      prateleirasDeDecolagem[2].removerPrimeiroAviao().ifPresent(a -> {
        estatisticas.novoRegistro(instante, a, Estagio.FINALIZOU, Operacao.DECOLAGEM,
            3, 3);
      });
    }
  }

  private void alocarAviaoParaPista(Pista pista) {
    Prateleira[] prateleiras = prateleirasDe(pista);
    Prateleira prateleira = null;
    for (Prateleira p : prateleiras) {
      if (p.isVazia())
        continue;
      if (prateleira == null || p.verProximoAviao().get().getId() < prateleira.verProximoAviao().get().getId()) {
        prateleira = p;
      }
    }

    if (prateleira == null) {
      // Pista ociosa
      return; 
    }

    if (prateleira.getOperacaoPermitida() == Operacao.DECOLAGEM) {
      Aviao aviao = prateleira.removerPrimeiroAviao().get();
      estatisticas.novoRegistro(instante, aviao, Estagio.FINALIZOU, Operacao.DECOLAGEM,
          pista.getId(), prateleira.getId());
    } else {
      Aviao aviao = prateleira.removerPrimeiroAviao().get();
      estatisticas.novoRegistro(instante, aviao, Estagio.FINALIZOU, Operacao.POUSO,
          pista.getId(), prateleira.getId());
    }
  }

  private Prateleira[] prateleirasDe(Pista pista) {
    Integer posicao = pista.getId() - 1;
    if (pista.getId() == 3) {
      Prateleira[] prateleiras = { prateleirasDeDecolagem[posicao] };
      return prateleiras;
    }

    Prateleira[] prateleiras = { prateleirasDeDecolagem[posicao], prateleirasDePouso[posicao * 2 + 1],
        prateleirasDePouso[posicao * 2] };
    return prateleiras;
  }

  public Estatisticas getEstatisticas() {
    return estatisticas;
  }

  public Integer getInstante() {
    return instante;
  }

  public void passarTempo() {
    instante++;
  }

  public void imprimirRelatorio() {
    System.out.println("\nConteúdo de cada fila:");
    
    for (Prateleira prateleira : prateleirasDePouso) {
        System.out.println("- Fila de aterrissagem " + prateleira.getId() + ": " + prateleira.formatarFila());
    }
    
    for (Prateleira prateleira : prateleirasDeDecolagem) {
        System.out.println("- Fila de decolagem " + prateleira.getId() + ": " + prateleira.formatarFila());
    }

    System.out.println("\nEstatísticas Periódicas:");
    System.out.printf("- Tempo médio de espera para decolagem: %.2f%n", estatisticas.tempoMedioDeDecolagem());
    System.out.printf("- Tempo médio de espera para aterrissagem: %.2f%n", estatisticas.tempoMedioDePouso());
    System.out.println("- Número de aviões que aterrissam sem reserva de combustível: " + estatisticas.avioesSemCombustivel());
}
}