package aeroporto;

import aeroporto.entidades.Aeroporto;

public class App {
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
