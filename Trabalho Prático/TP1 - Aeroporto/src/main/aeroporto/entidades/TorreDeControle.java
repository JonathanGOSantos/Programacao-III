package aeroporto.entidades;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import aeroporto.enums.Estagio;
import aeroporto.enums.Operacao;
import aeroporto.simulacao.Estatisticas;
import aeroporto.simulacao.Registro;

/**
 * Classe responsável por gerenciar todo o tráfego do aeroporto.
 * Controla as pistas, as prateleiras de espera e direciona os aviões
 * para decolagem, pouso ou situações de emergência.
 */
public class TorreDeControle {
  private Integer instante;
  private Estatisticas estatisticas;

  private Map<Integer, Pista> pistas;
  private Map<Integer, Pista> pistasDeDecolagem;
  private Map<Integer, Pista> pistasDePouso;

  private Map<Integer, Prateleira> prateleirasDeDecolagem;
  private Map<Integer, Prateleira> prateleirasDePouso;

  /**
   * Inicializa a torre de controle, criando e populando as pistas
   * e as prateleiras de pouso e decolagem.
   */
  public TorreDeControle() {
    this.instante = 0;
    estatisticas = new Estatisticas();
    pistasDeDecolagem = new HashMap<>();
    pistasDePouso = new HashMap<>();
    prateleirasDeDecolagem = new HashMap<>();
    prateleirasDePouso = new HashMap<>();
    pistas = new HashMap<>();

    popularPistas();
    popularPrateleirasDeDecolagem();
    popularPrateleirasDePouso();
  }

  private void popularPistas() {
    pistas.put(1, new Pista(1));
    pistas.put(2, new Pista(2));
    pistas.put(3, new Pista(3));

    pistasDePouso.put(1, pistas.get(1));
    pistasDePouso.put(2, pistas.get(2));

    pistasDeDecolagem.put(1, pistas.get(1));
    pistasDeDecolagem.put(2, pistas.get(2));
    pistasDeDecolagem.put(3, pistas.get(3));
  }

  private void popularPrateleirasDeDecolagem() {
    prateleirasDeDecolagem.put(1, new Prateleira(1, Operacao.DECOLAGEM));
    prateleirasDeDecolagem.put(2, new Prateleira(2, Operacao.DECOLAGEM));
    prateleirasDeDecolagem.put(3, new Prateleira(3, Operacao.DECOLAGEM));
  }

  private void popularPrateleirasDePouso() {
    prateleirasDePouso.put(1, new Prateleira(1, Operacao.POUSO));
    prateleirasDePouso.put(2, new Prateleira(2, Operacao.POUSO));
    prateleirasDePouso.put(3, new Prateleira(3, Operacao.POUSO));
    prateleirasDePouso.put(4, new Prateleira(4, Operacao.POUSO));
  }

  /**
   * Processa a chegada de um novo avião no espaço aéreo ou pátio,
   * direcionando-o para a fila correta com base na sua operação.
   *
   * @param aviao O avião a ser processado.
   */
  public void processarAviao(Aviao aviao) {
    switch (aviao.getOperacao()) {
      case DECOLAGEM -> {
        Prateleira prateleira = prateleirasDeDecolagem.values().stream()
            .min(Comparator.comparing(Prateleira::getTamanho))
            .orElseThrow();

        prateleira.adicionarAviao(aviao);
        estatisticas.novoRegistro(new Registro(instante, aviao.getId(), aviao.getCombustivel(),
            Estagio.INICIOU, Operacao.DECOLAGEM, prateleira.getId(), prateleira.getId()));
      }

      case POUSO -> {
        List<Prateleira> prateleirasMenorPista = new ArrayList<>();
        Pista menorPista = null;
        Integer tamanhoMenorPista = null;

        for (Pista pista : pistasDePouso.values()) {
          List<Prateleira> prateleirasPistaAtual = prateleirasDePousoDe(pista);
          Integer tamanhoPistaAtual = prateleirasPistaAtual.stream().map(Prateleira::getTamanho).reduce(0,
              Integer::sum);

          if (tamanhoMenorPista == null || tamanhoPistaAtual < tamanhoMenorPista) {
            prateleirasMenorPista = prateleirasPistaAtual;
            tamanhoMenorPista = tamanhoPistaAtual;
            menorPista = pista;
          }
        }

        Prateleira prateleira = prateleirasMenorPista.stream()
            .min(Comparator.comparing(Prateleira::getTamanho)).orElseThrow();

        estatisticas.novoRegistro(new Registro(instante, aviao.getId(), aviao.getCombustivel(),
            Estagio.INICIOU, Operacao.POUSO, menorPista.getId(), prateleira.getId()));

        prateleira.adicionarAviao(aviao);
      }
    }

  }

  /**
   * Processa a alocação das pistas na unidade de tempo atual.
   * Libera as pistas usadas anteriormente, aloca pistas para emergências,
   * aloca pistas para usos normais e atualiza o tempo de espera nas filas.
   */
  public void processarPistas() {
    pistas.values().forEach(Pista::liberar);

    alocarPistasParaEmergencias();
    alocarPistas();
    prateleirasDePouso.values().forEach(Prateleira::atualizarTempoDeEspera);
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
          prateleira.removerAviao(emergencia);
          estatisticas.novoRegistro(new Registro(instante, emergencia.getId(), emergencia.getCombustivel(),
              Estagio.CAIU, Operacao.POUSO, -1, id));
          continue;
        }

        pista.ocupar();
        prateleira.removerAviao(emergencia);
        estatisticas.novoRegistro(new Registro(instante, emergencia.getId(), emergencia.getCombustivel(),
            Estagio.FINALIZOU, Operacao.POUSO, pista.getId(), id));
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
          estatisticas.novoRegistro(new Registro(instante, a.getId(), a.getCombustivel(),
              Estagio.INICIOU, Operacao.DECOLAGEM, 3, 3));
        });
      }
    }
  }

  private void alocarAviaoParaPista(Pista pista) {
    Prateleira prateleiraDeDecolagem = prateleirasDeDecolagem.get(pista.getId());
    Prateleira prateleiraDePouso = prateleirasDePousoDe(pista).stream().filter(p -> !p.isVazia())
        .min(Comparator.comparing(Prateleira::getTamanho)).orElse(prateleirasDePousoDe(pista).get(0));

    Optional<Aviao> decolagem = prateleiraDeDecolagem.verPrimeiroAviao();
    Optional<Aviao> pouso = prateleiraDePouso.verPrimeiroAviao();

    if (pouso.isEmpty() && decolagem.isEmpty()) {
      return;
    }

    if (decolagem.isEmpty()) {
      Aviao aviao = prateleiraDePouso.removerPrimeiroAviao().get();
      estatisticas.novoRegistro(new Registro(instante, aviao.getId(), aviao.getCombustivel(),
          Estagio.FINALIZOU, Operacao.POUSO, pista.getId(), prateleiraDePouso.getId()));
      return;
    }

    if (pouso.isEmpty()) {
      Aviao aviao = prateleiraDeDecolagem.removerPrimeiroAviao().get();
      estatisticas.novoRegistro(new Registro(instante, aviao.getId(), aviao.getCombustivel(),
          Estagio.FINALIZOU, Operacao.DECOLAGEM, pista.getId(), prateleiraDePouso.getId()));
      return;
    }

    if (pouso.get().getId() < decolagem.get().getId()) {
      Aviao aviao = prateleiraDePouso.removerPrimeiroAviao().get();
      estatisticas.novoRegistro(new Registro(instante, aviao.getId(), aviao.getCombustivel(),
          Estagio.FINALIZOU, Operacao.POUSO, pista.getId(), prateleiraDePouso.getId()));
    } else {
      Aviao aviao = prateleiraDeDecolagem.removerPrimeiroAviao().get();
      estatisticas.novoRegistro(new Registro(instante, aviao.getId(), aviao.getCombustivel(),
          Estagio.FINALIZOU, Operacao.DECOLAGEM, pista.getId(), prateleiraDePouso.getId()));
    }
  }

  private List<Prateleira> prateleirasDePousoDe(Pista pista) {
    List<Prateleira> prateleiras = new ArrayList<>();
    prateleiras.add(prateleirasDePouso.get(pista.getId() * 2 - 1));
    prateleiras.add(prateleirasDePouso.get(pista.getId() * 2));
    return prateleiras;
  }

  public Estatisticas getEstatisticas() {
    return estatisticas;
  }

  public Integer getInstante() {
    return instante;
  }

  /**
   * Avança uma unidade de tempo no relógio interno da Torre de Controle.
   */
  public void passarTempo() {
    instante++;
  }

  /**
   * Imprime um relatório completo contendo o estado atual das filas
   * de aterrissagem e decolagem, além de estatísticas acumuladas.
   */
  public void imprimirRelatorio() {
    System.out.println("===============================================================");
    System.out.println("UNIDADE DE TEMPO: " + instante);
    System.out.println("\nConteúdo de cada fila:");
    for (Map.Entry<Integer, Prateleira> entry : prateleirasDePouso.entrySet()) {
      String avioesStr = entry.getValue().getAvioes().stream()
          .map(a -> String.format("ID: %d (Comb: %d)", a.getId(), a.getCombustivel()))
          .toList().toString();
      System.out.println("- Fila de aterrissagem " + entry.getKey() + ": " + avioesStr);
    }
    for (Map.Entry<Integer, Prateleira> entry : prateleirasDeDecolagem.entrySet()) {
      String avioesStr = entry.getValue().getAvioes().stream()
          .map(a -> String.format("ID: %d (Comb: %d)", a.getId(), a.getCombustivel()))
          .toList().toString();
      System.out.println("- Fila de decolagem " + entry.getKey() + ": " + avioesStr);
    }

    System.out.println("\nEstatísticas Periódicas:");
    System.out.printf("- Tempo médio de espera para decolagem: %.2f%n", estatisticas.tempoMedioDeDecolagem());
    System.out.printf("- Tempo médio de espera para aterrissagem: %.2f%n", estatisticas.tempoMedioDePouso());
    System.out.println("- Número de aviões que aterrissam sem reserva de combustível: " + estatisticas.avioesSemCombustivel());
  }
}