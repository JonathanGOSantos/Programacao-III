package entidades;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import enums.Estagio;
import enums.Operacao;
import simulacao.EstatisticasTorreDeControle;
import simulacao.RegistroTorreDeControle;
import simulacao.Simulacao;

public class TorreDeControle extends Simulacao<TorreDeControle, RegistroTorreDeControle> {
  private Map<Integer, PrateleiraDeEspera> prateleirasDeDecolagem;
  private Map<Integer, PrateleiraDeEspera> prateleirasDePouso;
  private Map<Integer, Pista> pistas;

  public TorreDeControle() {
    estatisticas = new EstatisticasTorreDeControle(this);
    prateleirasDeDecolagem = new HashMap<>();
    prateleirasDePouso = new HashMap<>();
    pistas = new HashMap<>();

    for (int i = 1; i <= 3; i++) {
      prateleirasDeDecolagem.put(i, new PrateleiraDeEspera(Operacao.DECOLAR));
    }
    for (int i = 1; i <= 4; i++) {
      prateleirasDePouso.put(i, new PrateleiraDeEspera(Operacao.POUSAR));
    }
    for (int i = 1; i <= 3; i++) {
      pistas.put(i, new Pista(i));
    }
  }

  public void processarAviao(Aviao aviao) {
    if (aviao.getOperacao().isEmpty()) {
      return;
    }
    PrateleiraDeEspera prateleira = switch (aviao.getOperacao().get()) {
      case POUSAR -> {
        Integer menorPista = null;
        Integer tamanhoPista = null;
        for (Integer pista = 1; pista <= prateleirasDePouso.size() / 2; pista++) {
          Integer tamanhoPistaAtual = prateleirasDePouso.get(pista * 2 - 1).getTamanho();
          tamanhoPistaAtual += prateleirasDePouso.get(pista * 2).getTamanho();

          if (tamanhoPista == null || tamanhoPistaAtual < tamanhoPista) {
            tamanhoPista = tamanhoPistaAtual;
            menorPista = pista;
          }
        }

        yield prateleirasDePousoDe(pistas.get(menorPista)).values()
            .stream()
            .min(Comparator.comparing(PrateleiraDeEspera::getTamanho)).orElseThrow();
      }

      case DECOLAR -> {
        yield prateleirasDeDecolagem.values().stream().min(Comparator.comparing(PrateleiraDeEspera::getTamanho))
            .orElseThrow();
      }
    };
    prateleira.adicionarAviao(aviao);
  }

  public void processarPistas() {
    pistas.values().forEach(Pista::liberar);

    alocarPistasParaEmergencias();
    alocarPistas();
    prateleirasDeDecolagem.values().forEach(PrateleiraDeEspera::atualizarTempoDeEspera);
  }

  private void alocarPistasParaEmergencias() {
    prateleirasDePouso.forEach((id, prateleira) -> {
      List<Aviao> emergencias = prateleira.obterEmergencias();
      for (Aviao emergencia : emergencias) {
        Pista pista;
        if (!pistas.get(3).emUso()) {
          pista = pistas.get(3);
        } else if (!pistas.get(1).emUso()) {
          pista = pistas.get(1);
        } else if (!pistas.get(2).emUso()) {
          pista = pistas.get(2);
        } else {
          return;
        }

        pista.ocupar();
        prateleira.removerAviao(emergencia);
        estatisticas.novoRegistro(new RegistroTorreDeControle(instante, emergencia.getId(), emergencia.getCombustivel(),
            Estagio.FINALIZOU, Operacao.POUSAR, pista.getId(), id));
      }
    });
  }

  private void alocarPistas() {
    if (!pistas.get(1).emUso()) {
      alocarAviaoParaPista(pistas.get(1));
    }
    if (!pistas.get(2).emUso()) {
      alocarAviaoParaPista(pistas.get(2));
    }
    if (!pistas.get(3).emUso()) {
      if (!prateleirasDeDecolagem.isEmpty()) {
        prateleirasDeDecolagem.get(3).removerPrimeiroAviao().ifPresent(a -> {
          estatisticas.novoRegistro(new RegistroTorreDeControle(instante, a.getId(), a.getCombustivel(),
              Estagio.INICIOU, Operacao.DECOLAR, 3, 1));
        });
      }
    }
  }

  private void alocarAviaoParaPista(Pista pista) {
    PrateleiraComAviaoMaisAntigo prateleiraDePouso = prateleiraDePousoComAviaoMaisAntigo(pista);
    PrateleiraComAviaoMaisAntigo prateleiraDeDecolagem = prateleiraDeDecolagemComAviaoMaisAntigo(pista);

    Optional<Aviao> pouso = prateleiraDePouso.prateleira().verPrimeiroAviao();
    Optional<Aviao> decolagem = prateleiraDeDecolagem.prateleira().verPrimeiroAviao();

    if (pouso.isEmpty() && decolagem.isEmpty()) {
      return;
    }

    if (decolagem.isEmpty()) {
      Aviao aviao = prateleiraDePouso.prateleira().removerPrimeiroAviao().get();
      estatisticas.novoRegistro(new RegistroTorreDeControle(instante, aviao.getId(), aviao.getCombustivel(),
            Estagio.FINALIZOU, Operacao.POUSAR, pista.getId(), prateleiraDePouso.id()));
      return;
    }

    if (pouso.isEmpty()) {
      Aviao aviao = prateleiraDeDecolagem.prateleira().removerPrimeiroAviao().get();
      estatisticas.novoRegistro(new RegistroTorreDeControle(instante, aviao.getId(), aviao.getCombustivel(),
            Estagio.FINALIZOU, Operacao.DECOLAR, pista.getId(), prateleiraDePouso.id()));
      return;
    }

    if (pouso.get().getId() < decolagem.get().getId()) {
      Aviao aviao = prateleiraDePouso.prateleira().removerPrimeiroAviao().get();
      estatisticas.novoRegistro(new RegistroTorreDeControle(instante, aviao.getId(), aviao.getCombustivel(),
            Estagio.FINALIZOU, Operacao.POUSAR, pista.getId(), prateleiraDePouso.id()));
    } else {
      Aviao aviao = prateleiraDeDecolagem.prateleira().removerPrimeiroAviao().get();
      estatisticas.novoRegistro(new RegistroTorreDeControle(instante, aviao.getId(), aviao.getCombustivel(),
            Estagio.FINALIZOU, Operacao.DECOLAR, pista.getId(), prateleiraDePouso.id()));
    }
  }

  private Map<Integer, PrateleiraDeEspera> prateleirasDePousoDe(Pista pista) {
    Map<Integer, PrateleiraDeEspera> prateleiras = new HashMap<>();
    prateleiras.put(pista.getId() * 2 - 1, prateleirasDePouso.get(pista.getId() * 2 - 1));
    prateleiras.put(pista.getId() * 2, prateleirasDePouso.get(pista.getId() * 2));
    return prateleiras;
  }

  private PrateleiraComAviaoMaisAntigo prateleiraDePousoComAviaoMaisAntigo(Pista pista) {
    Map<Integer, PrateleiraDeEspera> prateleiras = prateleirasDePousoDe(pista);
    Integer idPrateleiraSelecionada = null;
    Integer idMenorAviao = null;

    for (Map.Entry<Integer, PrateleiraDeEspera> entry : prateleiras.entrySet()) {
      if (entry.getValue().isVazia()) continue;

      Integer idAviaoAtual = entry.getValue().getIdProximoAviao().orElseThrow();
      
      if (idMenorAviao == null || idAviaoAtual < idMenorAviao) {
        idMenorAviao = idAviaoAtual;
        idPrateleiraSelecionada = entry.getKey();
      }
    }

    return new PrateleiraComAviaoMaisAntigo(idMenorAviao, prateleirasDePouso.get(idPrateleiraSelecionada));
  }

  private PrateleiraComAviaoMaisAntigo prateleiraDeDecolagemComAviaoMaisAntigo(Pista pista) {
    Integer idPrateleiraSelecionada = null;
    Integer idMenorAviao = null;

    for (Map.Entry<Integer, PrateleiraDeEspera> entry : prateleirasDeDecolagem.entrySet()) {
      if (entry.getValue().isVazia()) continue;

      Integer idAviaoAtual = entry.getValue().getIdProximoAviao().orElseThrow();
      
      if (idMenorAviao == null || idAviaoAtual < idMenorAviao) {
        idMenorAviao = idAviaoAtual;
        idPrateleiraSelecionada = entry.getKey();
      }
    }

    return new PrateleiraComAviaoMaisAntigo(idMenorAviao, prateleirasDeDecolagem.get(idPrateleiraSelecionada));
  }
}

record PrateleiraComAviaoMaisAntigo(Integer id, PrateleiraDeEspera prateleira) {
}