package aeroporto.entidades;

import java.util.Optional;

import aeroporto.enums.Operacao;

/**
 * Representa uma prateleira (ou fila) de espera para aviões no aeroporto.
 * Esta fila gerencia os aviões que estão aguardando autorização para
 * realizar uma operação específica, como pouso ou decolagem.
 */
public class Prateleira {
  private int tamanho;
  private Node cabeca;
  private Node cauda;

  private final Integer id;
  private Operacao operacaoPermitida;

  /**
   * Construtor da Prateleira de Espera.
   * 
   * @param id                O identificador da prateleira.
   * @param operacaoPermitida O tipo de operação que os aviões desta
   *                          prateleira estão aguardando para realizar.
   */
  public Prateleira(Integer id, Operacao operacaoPermitida) {
    this.id = id;
    this.operacaoPermitida = operacaoPermitida;
    this.tamanho = 0;
  }

  public boolean isVazia() {
    return tamanho == 0;
  }

  public boolean remover(Aviao aviao) {
    Node node = cabeca;
    while (node != null) {
      if (node.aviao.equals(aviao)) {
        if (node == cabeca) {
          cabeca = node.next;
        } else {
          node.prev.next = node.next;
        }

        if (node == cauda) {
          cauda = node.prev;
        } else {
          node.next.prev = node.prev;
        }

        tamanho--;
        return true;
      }
      node = node.next;
    }
    return false;
  }

  public int tamanho() {
    return tamanho;
  }

  public void adicionar(Aviao aviao) {
    if (cabeca == null) {
      Node node = new Node(null, aviao, null);
      cabeca = node;
      cauda = node;
    } else {
      Node newNode = new Node(cauda, aviao, null);
      cauda.next = newNode;
      cauda = newNode;
    }
    tamanho++;
  }

  public Optional<Aviao> verProximoAviao() {
    if (cabeca == null) {
      return Optional.empty();
    }
    return Optional.of(cabeca.aviao);
  }

  public Optional<Aviao> removerPrimeiroAviao() {
    if (cabeca == null)
      return Optional.empty();

    tamanho--;

    if (cabeca == cauda) {
      Aviao aviao = cabeca.aviao;
      cabeca = null;
      cauda = null;
      return Optional.of(aviao);
    }

    Aviao aviao = cabeca.aviao;
    Node next = cabeca.next;
    cabeca.next = null;
    next.prev = null;
    cabeca = next;
    return Optional.of(aviao);
  }

  public Aviao[] obterEmergencias() {
    Aviao[] temporario = new Aviao[tamanho];
    Node node = cabeca;
    int qtdEmergencias = 0;

    while (node != null) {
      if (node.aviao.emSituacaoCritica()) {
        temporario[qtdEmergencias] = node.aviao;
        qtdEmergencias++;
      }
      node = node.next;
    }

    if (qtdEmergencias == 0) {
      return new Aviao[0];
    }

    Aviao[] resultado = new Aviao[qtdEmergencias];

    for (int j = 0; j < qtdEmergencias; j++) {
      resultado[j] = temporario[j];
    }

    return resultado;
  }

  public void atualizarTempoDeEspera() {
    Node node = cabeca;
    while (node != null) {
      node.aviao.decrementarCombustivel();
      node.aviao.incrementarTempoDeOperacao();
      node = node.next;
    }
  }

  public Integer getId() {
    return id;
  }

  public Operacao getOperacaoPermitida() {
    return operacaoPermitida;
  }

  public String formatarFila() {
    StringBuilder sb = new StringBuilder("[");
    Node atual = cabeca;

    while (atual != null) {
      sb.append(String.format("ID: %d (Comb: %d)", atual.aviao.getId(), atual.aviao.getCombustivel()));

      if (atual.next != null) {
        sb.append(", ");
      }
      atual = atual.next;
    }

    sb.append("]");
    return sb.toString();
  }

  private static class Node {
    Node prev;
    Aviao aviao;
    Node next;

    public Node(Node prev, Aviao aviao, Node next) {
      this.prev = prev;
      this.aviao = aviao;
      this.next = next;
    }
  }
}
