package aeroporto;

import aeroporto.entidades.Aeroporto;

/**
 * Classe principal que inicia a simulação do Aeroporto.
 * Contém o método main que executa o loop infinito de passagem de tempo.
 */
public class App {
  /**
   * Método de entrada do programa.
   * Inicializa o aeroporto e avança o tempo a cada 1 segundo.
   *
   * @param args Argumentos de linha de comando (não utilizados).
   */
  public static void main(String[] args) {
    try {
      Aeroporto aeroporto = new Aeroporto();
      int i = 0;
      while (i++ < 25) {
        aeroporto.passarTempo();
        Thread.sleep(100);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
