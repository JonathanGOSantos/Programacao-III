public class Nodo {

   private int chave;
   private Nodo anterior;
   private Nodo proximo;
   
   public Nodo(int chave) {
      this.chave = chave;
      anterior = null;
      proximo = null;
   }
   
   public int getChave() { return chave; }
   public Nodo getAnterior() { return anterior; }
   public Nodo getProximo() { return proximo; }
   
   public void setAnterior(Nodo anterior) {
      this.anterior = anterior;
   }
   
   public void setProximo(Nodo proximo) {
      this.proximo = proximo;
   }
}