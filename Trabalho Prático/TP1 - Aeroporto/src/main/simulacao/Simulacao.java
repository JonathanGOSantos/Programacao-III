package simulacao;

public abstract class Simulacao<E, R extends Registro> {
  protected Integer instante;
  protected Estatisticas<E, R> estatisticas;

  {
    instante = 0;
  }

  public Estatisticas<E, R> getEstatisticas() {
    return estatisticas;
  }

  public Integer getInstante() {
    return instante;
  }

  public void passarTempo() {
    instante++;
  }
}
