import java.io.IOException;

import java.util.Scanner;

/**
 * 
 * IMPORTANT:
 * 
 * O nome da classe deve ser "Main" para que a sua solução execute
 * 
 * Class name must be "Main" for your solution to execute
 * 
 * El nombre de la clase debe ser "Main" para que su solución ejecutar
 * 
 */

public class MainBurra {

    public static void main(String[] args) throws IOException {

        Scanner read = new Scanner(System.in);

        Integer tamanho = read.nextInt();

        do {

            Vetor numeros = new Vetor(tamanho);

            for (Integer i = 0; i < tamanho; i++) {

                numeros.adicionar(i, read.nextInt());

            }
            System.out.println(new Vencedor(numeros) + " : " + numeros.mudancas);

        } while ((tamanho = read.nextInt()) != 0);

        read.close();

    }

    static class Vetor {

        private Integer[] elementos;
        public int mudancas = 0;

        public Vetor(Integer tamanho) {

            this.elementos = new Integer[tamanho];

        }

        public void adicionar(Integer posicao, Integer valor) {

            this.elementos[posicao] = valor;

        }

        public boolean isOrdenado() {

            for (Integer i = 0; i < elementos.length - 1; i++) {

                if (elementos[i] > elementos[i + 1])

                    return false;

            }

            return true;

        }

        public void trocarPrimeiroPar() {

            if (isOrdenado())

                return;
            mudancas++;
            for (Integer i = 0; i < elementos.length - 1; i++) {

                if (elementos[i] > elementos[i + 1]) {

                    Integer aux = elementos[i];

                    elementos[i] = elementos[i + 1];

                    elementos[i + 1] = aux;

                    return;

                }

            }

        }

    }

    static class Vencedor {

        private final String nome;

        public Vencedor(Vetor vetor) {

            String[] participantes = { "Marcelo", "Carlos" };

            Integer participante = 0;

            while (!vetor.isOrdenado()) {

                vetor.trocarPrimeiroPar();

                participante = (participante + 1) % participantes.length;

            }

            Integer vencedor = participante = (participante + 1) % participantes.length;

            nome = participantes[vencedor];

        }

        public String getNome() {

            return nome;

        }

        @Override

        public String toString() {

            return nome;

        }

    }
}
