import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * IMPORTANT:
 * O nome da classe deve ser "Main" para que a sua solução execute
 * Class name must be "Main" for your solution to execute
 * El nombre de la clase debe ser "Main" para que su solución ejecutar
 */
public class Main {

    public static void main(String[] args) throws IOException {

        Scanner read = new Scanner(System.in);
        Integer tamanho = read.nextInt();
        do {
            Vetor numeros = new Vetor(tamanho);
            for (Integer i = 0; i < tamanho; i++) {
                numeros.adicionar(i, read.nextInt());
            }
            System.out.println(new Vencedor(numeros));
        } while ((tamanho = read.nextInt()) != 0);
        read.close();
    }

    static class Vetor {
        private Integer[] elementos;
        private Long mudancas;

        public Vetor(Integer tamanho) {
            this.elementos = new Integer[tamanho];
            mudancas = 0L;
        }

        public void adicionar(Integer posicao, Integer valor) {
            this.elementos[posicao] = valor;
        }

        public Vetor sort() {
            mudancas = MergeSort.sort(elementos);
            return this;
        }

        public Stream<Integer> stream() {
            return Arrays.stream(elementos);
        }

        public Long getMudancas() {
            return mudancas;
        }

    }

    static class Vencedor {
        private final String nome;

        public Vencedor(Vetor vetor) {
            Long mudancas = vetor.sort().getMudancas();
            if (mudancas % 2 != 0) {
                nome = "Marcelo";
            } else {
                nome = "Carlos";
            }
        }

        public String getNome() {
            return nome;
        }

        @Override
        public String toString() {
            return nome;
        }
    }

    static class MergeSort {
        public static Long sort(Integer[] vetor) {
            if (vetor.length < 2) {
                return 0L;
            }
            Integer meio = vetor.length / 2;
            Integer[] esquerda = new Integer[meio];
            Integer[] direita = new Integer[vetor.length - meio];

            for (int i = 0; i < esquerda.length; i++) {
                esquerda[i] = vetor[i];
            }
            for (int i = 0; i < direita.length; i++) {
                direita[i] = vetor[meio + i];
            }
            Long mudancasLocais = 0L;
            mudancasLocais += sort(esquerda);
            mudancasLocais += sort(direita);
            mudancasLocais += merge(vetor, esquerda, direita);
            
            return mudancasLocais;
        }

        public static Long merge(Integer[] vetor, Integer[] esquerda, Integer[] direita) {
            Integer i = 0, j = 0, k = 0;
            Long mudancasNoMerge = 0L;
            while (i < esquerda.length && j < direita.length) {
                if (esquerda[i] <= direita[j]) {
                    vetor[k++] = esquerda[i++];
                } else {
                    vetor[k++] = direita[j++];
                    mudancasNoMerge += (esquerda.length - i);
                }
            }
            while (i < esquerda.length) {
                vetor[k++] = esquerda[i++];
            }
            while (j < direita.length) {
                vetor[k++] = direita[j++];
            }
            return mudancasNoMerge;
        }
    }
}
