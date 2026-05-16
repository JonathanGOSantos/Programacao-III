package simulacao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public abstract class Estatisticas<E, R extends Registro> {
  protected E entidade;
  protected Map<Integer, List<R>> registros; // Lista já guarda o tipo correto!

  protected Estatisticas(E entidade) {
    this.entidade = entidade;
    this.registros = new TreeMap<>();
  }

  public abstract void novoRegistro(R registro);

  public abstract List<R> getRegistros();
  
  public abstract List<R> getRegistrosEm(Integer instante);
}