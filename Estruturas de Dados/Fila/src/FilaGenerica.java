public class FilaGenerica<E> {
  private Object[] elementos;
  private int cauda;
  private int cabeca;

  public FilaGenerica(int tamanho) {
    elementos = new Object[tamanho];

    cauda = 0;
    cabeca = 0;
  }

  public void enfileirar(E valor) {
    // Verifica se a fila está cheia
    if ((cauda + 1) % elementos.length == cabeca) {
      throw new IllegalStateException("overflow");
    }

    elementos[cauda] = valor;

    if (cauda == elementos.length - 1) {
      cauda = 0;
    } else {
      cauda = cauda + 1;
    }
  }

  public E desenfileirar() {
    // Verifica se a fila está vazia
    if (cabeca == cauda) {
      throw new IllegalStateException("underflow");
    }

    E x = (E) elementos[cabeca];

    if (cabeca == elementos.length - 1) {
      cabeca = 0;
    } else {
      cabeca = cabeca + 1;
    }

    return x;
  }

  public void printar() {
    int pos = cabeca;
    while (pos != cauda) {
      System.out.printf(elementos[pos] + " ");

      if (pos == elementos.length - 1) {
        pos = 0;
      } else {
        pos = pos + 1;
      }
    }
    System.out.println();
  }
}
