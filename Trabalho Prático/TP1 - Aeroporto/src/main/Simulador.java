public abstract class Simulador {
  protected Integer tick;

  {
    tick = 0;
  }

  public Integer getTick() {
    return tick;
  }

  public abstract void runTick();
}