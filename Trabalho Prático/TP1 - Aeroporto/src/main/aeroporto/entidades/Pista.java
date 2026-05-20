package aeroporto.entidades;

/**
 * Representa uma pista do aeroporto.
 * Uma pista pode ser ocupada por um avião para realizar o pouso ou decolagem.
 */
public class Pista {
  private final Integer id;
  private Boolean emUso;

  /**
   * Cria uma nova pista de pouso ou decolagem.
   *
   * @param id O identificador numérico da pista.
   */
  public Pista(Integer id) {
    this.id = id;
    this.emUso = false;
  }

  public Integer getId() {
    return id;
  }

  /**
   * Verifica se a pista está atualmente ocupada por um avião.
   *
   * @return true se a pista estiver ocupada.
   */
  public Boolean emUso() {
    return emUso;
  }

  /**
   * Define o status da pista como ocupada.
   */
  public void ocupar() {
    emUso = true;
  }

  /**
   * Libera a pista, deixando-a disponível para a próxima unidade de tempo.
   */
  public void liberar() {
    emUso = false; 
  }
}
