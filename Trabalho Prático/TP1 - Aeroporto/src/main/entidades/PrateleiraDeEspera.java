package entidades;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import enums.Operacao;

/**
 * Representa uma prateleira (ou fila) de espera para aviões no aeroporto.
 * Esta fila gerencia os aviões que estão aguardando autorização para
 * realizar uma operação específica, como pouso ou decolagem.
 */
public class PrateleiraDeEspera {
  private Queue<Aviao> avioes;
  private Operacao operacaoPermitida;

  /**
   * Construtor da Prateleira de Espera.
   * 
   * @param operacaoPermitida O tipo de operação que os aviões desta
   *                          prateleira estão aguardando para realizar.
   */
  public PrateleiraDeEspera(Operacao operacaoPermitida) {
    this.avioes = new LinkedList<>();
    this.operacaoPermitida = operacaoPermitida;
  }

  /**
   * Adiciona um avião ao final da fila de espera
   * 
   * @param aviao O avião a ser adicionado na prateleira.
   */
  public void adicionarAviao(Aviao aviao) {
    avioes.add(aviao);
  }

  /**
   * Visualiza o primeiro avião da fila sem removê-lo.
   * 
   * @return Um {@link Optional} contendo o primeiro {@link Aviao} da fila, ou
   *         vazio se a fila estiver vazia.
   */
  public Optional<Aviao> verPrimeiroAviao() {
    return Optional.ofNullable(avioes.peek());
  }

  /**
   * Obtém o ID do próximo avião da fila (o que tem maior prioridade para sair).
   * 
   * @return Um Optional contendo o ID do avião, ou Optional.empty() se a fila
   *         estiver vazia.
   */
  public Optional<Integer> getIdProximoAviao() {
    return verPrimeiroAviao().map(Aviao::getId);
  }

  /**
   * Remove e retorna o primeiro avião da fila de espera.
   * 
   * @return Um {@link Optional} contendo o primeiro {@link Aviao} da fila, ou
   *         vazio se a fila estiver vazia.
   */
  public Optional<Aviao> removerPrimeiroAviao() {
    return Optional.ofNullable(avioes.poll());
  }

  /**
   * Remove um avião específico da fila, independentemente de sua posição.
   * 
   * @param aviao O avião a ser removido.
   */
  public void removerAviao(Aviao aviao) {
    avioes.remove(aviao);
  }

  /**
   * Filtra e retorna todos os aviões desta prateleira que se encontram
   * em situação de emergência.
   * 
   * @return Uma lista contendo os aviões em situação crítica.
   */
  public List<Aviao> obterEmergencias() {
    return avioes.stream().filter(Aviao::emSituacaoCritica).toList();
  }

  /**
   * Obtém a quantidade atual de aviões aguardando nesta prateleira.
   * 
   * @return O número de aviões na fila.
   */
  public Integer getTamanho() {
    return avioes.size();
  }

  /**
   * Verifica se a prateleira de espera está vazia.
   * 
   * @return true se não houver aviões na fila, false caso contrário.
   */
  public Boolean isVazia() {
    return avioes.isEmpty();
  }

  /**
   * Obtém qual é a operação permitida para os aviões desta prateleira.
   * 
   * @return A {@link Operacao} vinculada a esta prateleira.
   */
  public Operacao getOperacaoPermitida() {
    return operacaoPermitida;
  }

  public void atualizarTempoDeEspera() {
    avioes.forEach(Aviao::decrementarCombustivel);
  }
}
