public class Fila {
    private int cabeca;
    private int cauda;
    private int[] vetor;

    public Fila (int tamanho) {
        vetor = new int[tamanho];
        cabeca = 0;
        cauda = 0;
    }

    public boolean vazio () {
        return cabeca == cauda;
    }

    public boolean cheia () {
        return (cauda +1) % vetor.length == cabeca;
           
    }

    public void enfileirar (int x) {
        if (cheia()) 
            throw new RuntimeException ("Overflow");
        vetor[cauda] = x;
        cauda = (cauda +1) % vetor.length;
    }

    public int desenfileirar() {
        if (vazio())
            throw new RuntimeException("Underflow");
        int x = vetor[cabeca];
        cabeca = (cabeca +1) % vetor.length;
        return x;
    }

    public void mostrar() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int i = cabeca;
        while (i != cauda) {
            sb.append(vetor[i]);
            i = (i + 1) % vetor.length;
            if (i != cauda) 
                sb.append(", ");
        }
        sb.append("]");
        System.out.println(sb);
    }
}