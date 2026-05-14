public class Lista {

   private Nodo cabeca;

   public Lista() {
      cabeca = null;
   }
   
   public void inserir(Nodo x) {
      x.setProximo(cabeca);
      if (cabeca != null) {
         cabeca.setAnterior(x);
      }
      cabeca = x;
      x.setAnterior(null);
   }
   
   /**
    * Exibe na tela todos os elementos atualmente
    * na lista.
    */
   public void mostrar() {
      Nodo x = cabeca;
      while (x != null) {
         System.out.printf("%d ", x.getChave());
         x = x.getProximo();
      }
      System.out.println();
   }

   public Nodo buscar(Nodo x, int k) {
      if (x == null || x.getChave() == k) return x;
      return buscar(x.getProximo(), k);
   }

   public Nodo getCabeca() {
      return cabeca;
   }
}