public class App {
    public static void main(String[] args) {
        FilaGenerica<String> atendimentos = new FilaGenerica<>(20);
        FilaGenerica<Integer> sla = new FilaGenerica<>(4);

        atendimentos.enfileirar("Pierre");
        atendimentos.enfileirar("Enzo");
        atendimentos.printar();

        // Atendeu o 1º
        atendimentos.desenfileirar();
        atendimentos.printar();

        // Chegou mais 2
        atendimentos.enfileirar("Filipe");
        atendimentos.enfileirar("Jonathan");
        atendimentos.printar();

        // Terminou de atender
        atendimentos.desenfileirar();
        atendimentos.desenfileirar();
        atendimentos.desenfileirar();
        atendimentos.printar();

        sla.enfileirar(1);
        sla.enfileirar(2);
        sla.printar();
    }
}
