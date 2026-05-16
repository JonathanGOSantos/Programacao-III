package simulacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import entidades.TorreDeControle;
import enums.Operacao;

public class EstatisticasTorreDeControle extends Estatisticas<TorreDeControle, RegistroTorreDeControle> {

  public EstatisticasTorreDeControle(TorreDeControle torreDeControle) {
    super(torreDeControle);
  }

  @Override
  public void novoRegistro(RegistroTorreDeControle registro) { // Já recebe o tipo exato!
    Integer instante = entidade.getInstante();
    registros.putIfAbsent(instante, new ArrayList<>());
    registros.get(instante).add(registro);
  }

  @Override
  public List<RegistroTorreDeControle> getRegistros() {
    return registros.values().stream().flatMap(List::stream).collect(Collectors.toList());
  }

  @Override
  public List<RegistroTorreDeControle> getRegistrosEm(Integer instante) {
    return registros.getOrDefault(instante, new ArrayList<>());
  }

  public Double tempoMedioDePouso() {
    List<RegistroTorreDeControle> registrosPouso = getRegistros().stream()
        .filter(r -> r.operacao() == Operacao.POUSAR)
        .toList();

    Map<Integer, List<RegistroTorreDeControle>> historicoPorAviao = registrosPouso.stream()
        .collect(Collectors.groupingBy(RegistroTorreDeControle::idAviao));

    return historicoPorAviao.values().stream()
        .filter(historico -> historico.size() >= 2)
        .mapToInt(historico -> {
          int inicio = historico.stream().mapToInt(RegistroTorreDeControle::instante).min().orElse(0);
          int fim = historico.stream().mapToInt(RegistroTorreDeControle::instante).max().orElse(0);
          return fim - inicio;
        })
        .average()
        .orElse(0.0);
  }

  public Double tempoMedioDeDecolagem() {
    List<RegistroTorreDeControle> registrosPouso = getRegistros().stream()
        .filter(r -> r.operacao() == Operacao.DECOLAR)
        .toList();

    Map<Integer, List<RegistroTorreDeControle>> historicoPorAviao = registrosPouso.stream()
        .collect(Collectors.groupingBy(RegistroTorreDeControle::idAviao));

    return historicoPorAviao.values().stream()
        .filter(historico -> historico.size() >= 2)
        .mapToInt(historico -> {
          int inicio = historico.stream().mapToInt(RegistroTorreDeControle::instante).min().orElse(0);
          int fim = historico.stream().mapToInt(RegistroTorreDeControle::instante).max().orElse(0);
          return fim - inicio;
        })
        .average()
        .orElse(0.0);
  }

  public Integer tempoDeOperacao(Integer idAviao) {
    List<RegistroTorreDeControle> registros = getRegistros().stream().filter(r -> r.idAviao().equals(idAviao)).toList();
    RegistroTorreDeControle inicio = registros.getFirst();
    if (registros.size() == 1) {
      return 0;
    }

    RegistroTorreDeControle fim = registros.getLast();
    return fim.instante() - inicio.instante();
  }

  public Long avioesSemCombustivel() {
    return registros.values().stream()
        .flatMap(List::stream)
        .filter(r -> r.combustivel() == 0)
        .map(RegistroTorreDeControle::idAviao)
        .distinct() 
        .count();
  }

  public Long avioesSemCombustivelEm(Integer instante) {
    return registros.getOrDefault(instante, new ArrayList<>()).stream()
        .filter(r -> r.combustivel() == 0)
        .map(RegistroTorreDeControle::idAviao)
        .distinct()
        .count();
  }
}