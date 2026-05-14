public class Fila {
  private String[] elementos;
  private int cauda;
  private int cabeca;

  public Fila(int tamanho) {
    elementos = new String[tamanho];

    cauda = 0;
    cabeca = 0;
  }

  public void enfileirar(String valor) {
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

  public String desenfileirar() {
    // Verifica se a fila está vazia
    if (cabeca == cauda) {
      throw new IllegalStateException("underflow");
    }

    String x = elementos[cabeca];

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
