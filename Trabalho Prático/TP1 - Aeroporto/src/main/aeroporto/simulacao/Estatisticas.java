package aeroporto.simulacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import aeroporto.enums.Operacao;

/**
 * Classe responsável por gerenciar e calcular estatísticas da simulação,
 * incluindo tempo médio de espera e quantidade de aviões em situação crítica.
 */
public class Estatisticas {
  private Map<Integer, List<Registro>> registros;

  /**
   * Construtor de Estatísticas.
   */
  public Estatisticas() {
    this.registros = new TreeMap<>();
  }

  /**
   * Adiciona um novo registro ao histórico.
   *
   * @param registro O registro contendo os dados do evento.
   */
  public void novoRegistro(Registro registro) {
    registros.putIfAbsent(registro.instante(), new ArrayList<>());
    registros.get(registro.instante()).add(registro);
  }

  public List<Registro> getRegistros() {
    return registros.values().stream().flatMap(List::stream).collect(Collectors.toList());
  }

  public List<Registro> getRegistrosEm(Integer instante) {
    return registros.getOrDefault(instante, new ArrayList<>());
  }

  /**
   * Calcula o tempo médio de espera para todas as operações de pouso concluídas.
   *
   * @return Tempo médio (em unidades de tempo), ou 0.0 se não houver pousos.
   */
  public Double tempoMedioDePouso() {
    List<Registro> registrosPouso = getRegistros().stream()
        .filter(r -> r.operacao() == Operacao.POUSO)
        .toList();

    Map<Integer, List<Registro>> historicoPorAviao = registrosPouso.stream()
        .collect(Collectors.groupingBy(Registro::idAviao));

    return historicoPorAviao.values().stream()
        .filter(historico -> historico.size() >= 2)
        .mapToInt(historico -> {
          int inicio = historico.stream().mapToInt(Registro::instante).min().orElse(0);
          int fim = historico.stream().mapToInt(Registro::instante).max().orElse(0);
          return fim - inicio;
        })
        .average()
        .orElse(0.0);
  }

  /**
   * Calcula o tempo médio de espera para todas as operações de decolagem concluídas.
   *
   * @return Tempo médio (em unidades de tempo), ou 0.0 se não houver decolagens.
   */
  public Double tempoMedioDeDecolagem() {
    List<Registro> registrosPouso = getRegistros().stream()
        .filter(r -> r.operacao() == Operacao.DECOLAGEM)
        .toList();

    Map<Integer, List<Registro>> historicoPorAviao = registrosPouso.stream()
        .collect(Collectors.groupingBy(Registro::idAviao));

    return historicoPorAviao.values().stream()
        .filter(historico -> historico.size() >= 2)
        .mapToInt(historico -> {
          int inicio = historico.stream().mapToInt(Registro::instante).min().orElse(0);
          int fim = historico.stream().mapToInt(Registro::instante).max().orElse(0);
          return fim - inicio;
        })
        .average()
        .orElse(0.0);
  }

  /**
   * Calcula o tempo total de operação de um determinado avião.
   *
   * @param idAviao O identificador do avião.
   * @return O tempo total decorrido entre a entrada e a saída do avião.
   */
  public Integer tempoDeOperacao(Integer idAviao) {
    List<Registro> registros = getRegistros().stream().filter(r -> r.idAviao().equals(idAviao)).toList();
    if (registros.isEmpty()) return 0;
    Registro inicio = registros.getFirst();
    if (registros.size() == 1) {
      return 0;
    }

    Registro fim = registros.getLast();
    return fim.instante() - inicio.instante();
  }

  /**
   * Conta a quantidade total de aviões que chegaram à situação de ausência de combustível
   * em qualquer momento da simulação.
   *
   * @return O número de aviões sem combustível.
   */
  public Long avioesSemCombustivel() {
    return registros.values().stream()
        .flatMap(List::stream)
        .filter(r -> r.combustivel() == 0)
        .map(Registro::idAviao)
        .distinct()
        .count();
  }
}