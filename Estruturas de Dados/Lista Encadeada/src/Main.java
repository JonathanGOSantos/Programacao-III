public class Main {

   public static void main(String[] args) {
      testeInserir();
      testeBuscar();
   }
   
   public static void testeInserir() {
      System.out.println("Teste Inserir: ");
      Lista lista = new Lista();
      System.out.println("Inserindo 10");
      lista.inserir(new Nodo(10));      
      System.out.println("Mostrando a lista");
      lista.mostrar();
      System.out.println("Inserindo 20");
      lista.inserir(new Nodo(20));
      System.out.println("Mostrando a lista de novo");
      lista.mostrar();
   }

   public static void testeBuscar() {
      System.out.println("Teste Buscar: ");
      Lista lista = new Lista();
      System.out.println("Inserindo 10");
      lista.inserir(new Nodo(10));      
      System.out.println("Mostrando a lista");
      lista.mostrar();
      System.out.println("Inserindo 20");
      lista.inserir(new Nodo(20));
      System.out.println("Mostrando a lista de novo");
      lista.mostrar();

      Nodo nodo = lista.buscar(lista.getCabeca(), 10);
      System.out.println(nodo != null ? "Encontrou: " + nodo.getChave() : "Não encontrou");
      nodo = lista.buscar(lista.getCabeca(), 20);
      System.out.println(nodo != null ? "Encontrou: " + nodo.getChave() : "Não encontrou");
      nodo = lista.buscar(lista.getCabeca(), 30);
      System.out.println(nodo != null ? "Encontrou: " + nodo.getChave() : "Não encontrou");
   }
}