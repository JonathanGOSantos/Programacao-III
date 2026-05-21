package aeroporto.simulacao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import aeroporto.enums.Estagio;
import aeroporto.enums.Operacao;

/**
 * Gerencia e consolida as métricas e estatísticas da simulação do aeroporto.
 * Calcula dados como tempo médio de espera e quantidade de pousos emergenciais,
 * utilizando estruturas de dados otimizadas para processamento em tempo real
 * O(1).
 */
public class Estatisticas {
  private Map<Integer, List<Registro>> registros;

  private Map<Integer, Integer> tempoInicioPorAviao;

  private Long totalAvioesDecolagem;
  private Long totalAvioesPouso;
  private Long tempoTotalParaPouso;
  private Long tempoTotalParaDecolagem;
  private Long totalAvioesPousaramSemCombustivel;

  /**
   * Construtor padrão da classe Estatísticas.
   * Inicializa os mapas de histórico e zera todos os contadores de métricas.
   */
  public Estatisticas() {
    this.registros = new TreeMap<>();
    this.tempoInicioPorAviao = new HashMap<>();

    totalAvioesPouso = 0L;
    totalAvioesDecolagem = 0L;
    tempoTotalParaPouso = 0L;
    tempoTotalParaDecolagem = 0L;
    totalAvioesPousaramSemCombustivel = 0L;
  }

  /**
   * Processa e armazena um novo registro da simulação.
   * Acumula o tempo de operação e atualiza as contagens gerais de decolagens e
   * pousos no momento exato em que o registro é inserido, evitando cálculos
   * pesados posteriores.
   *
   * @param registro O evento/registro a ser processado e adicionado ao histórico.
   */
  public void novoRegistro(Registro registro) {
    registros.putIfAbsent(registro.instante(), new ArrayList<>());
    registros.get(registro.instante()).add(registro);

    if (registro.estagio() == Estagio.INICIOU) {
      tempoInicioPorAviao.put(registro.idAviao(), registro.instante());

      if (registro.operacao() == Operacao.DECOLAGEM) {
        totalAvioesDecolagem++;
      } else if (registro.operacao() == Operacao.POUSO) {
        totalAvioesPouso++;
      }
    } else if (registro.estagio() == Estagio.FINALIZOU) {
      Integer instanteInicio = tempoInicioPorAviao.remove(registro.idAviao());
      int tempoDecorrido = registro.instante() - instanteInicio;

      if (registro.operacao() == Operacao.DECOLAGEM) {
        tempoTotalParaDecolagem += tempoDecorrido;
      } else if (registro.operacao() == Operacao.POUSO) {
        tempoTotalParaPouso += tempoDecorrido;

        if (registro.combustivel() == 0) {
          totalAvioesPousaramSemCombustivel++;
        }
      }
    }
  }

  /**
   * Recupera o histórico completo de todos os registros da simulação,
   * ordenados cronologicamente pelo instante de ocorrência.
   *
   * @return Uma lista unificada contendo todos os registros.
   */
  public List<Registro> getRegistros() {
    return registros.values().stream().flatMap(List::stream).collect(Collectors.toList());
  }

  /**
   * Recupera a lista de eventos e registros que ocorreram em um instante de tempo
   * específico.
   *
   * @param instante O momento exato (unidade de tempo) da simulação.
   * @return Uma lista de registros daquele instante, ou uma lista vazia se não
   *         houver eventos.
   */
  public List<Registro> getRegistrosEm(Integer instante) {
    return registros.getOrDefault(instante, new ArrayList<>());
  }

  /**
   * Calcula o tempo médio geral de espera das aeronaves nas filas de solo para
   * decolar.
   *
   * @return O tempo médio de espera para decolagens em unidades de tempo. Retorna
   *         0.0 se nenhuma decolagem for registrada.
   */
  public Double tempoMedioDeDecolagem() {
    if (totalAvioesDecolagem.compareTo(0L) == 0L)
      return 0.0;
    return tempoTotalParaDecolagem / totalAvioesDecolagem.doubleValue();
  }

  /**
   * Calcula o tempo médio geral de espera das aeronaves para realizar operações
   * de aterrissagem.
   *
   * @return O tempo médio de espera para pousos em unidades de tempo. Retorna 0.0
   *         se nenhum pouso for registrado.
   */
  public Double tempoMedioDePouso() {
    if (totalAvioesPouso.compareTo(0L) == 0L)
      return 0.0;
    return tempoTotalParaPouso / totalAvioesPouso.doubleValue();
  }

  /**
   * Informa a quantidade total de aeronaves que atingiram o nível zero de
   * combustível e pousaram sob condições de emergência durante todo o período da
   * simulação.
   *
   * @return O número de aviões que aterrissaram sem reserva de combustível.
   */
  public Long avioesSemCombustivel() {
    return totalAvioesPousaramSemCombustivel;
  }
}