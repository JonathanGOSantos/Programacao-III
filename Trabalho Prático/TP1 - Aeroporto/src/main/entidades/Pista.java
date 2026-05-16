package entidades;

public class Pista {
  private final Integer id;
  private Boolean emUso;

  public Pista(Integer id) {
    this.id = id;
    this.emUso = false;
  }

  public Integer getId() {
    return id;
  }

  public Boolean emUso() {
    return emUso;
  }

  public void ocupar() {
    emUso = true;
  }

  public void liberar() {
    emUso = false; 
  }
}
