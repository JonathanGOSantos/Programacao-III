package aeroporto.entidades;

import java.util.Random;

import aeroporto.enums.Operacao;

public class Aeroporto {
  private TorreDeControle torreDeControle;
  private Random random;
  private Integer contadorIdAviao;

  public Aeroporto() {
    this.torreDeControle = new TorreDeControle();
    this.random = new Random();
    this.contadorIdAviao = 1;
  }

  public void passarTempo() {
    System.out.println("Instante: " + torreDeControle.getInstante());
    Integer qtdAvioesPousar = random.nextInt(4);
    Integer qtdAvioesDecolar = random.nextInt(4);

    for (int i = 0; i < qtdAvioesPousar; i++) {
      Integer combustivel = random.nextInt(20) + 1;
      Aviao aviao = new Aviao(contadorIdAviao++, combustivel, Operacao.POUSO);
      torreDeControle.processarAviao(aviao);
    }

    for (int i = 0; i < qtdAvioesDecolar; i++) {
      Integer combustivel = random.nextInt(20) + 1;
      Aviao aviao = new Aviao(contadorIdAviao++, combustivel, Operacao.DECOLAGEM);
      torreDeControle.processarAviao(aviao);
    }

    torreDeControle.processarPistas();
    torreDeControle.imprimirRelatorio();
    torreDeControle.passarTempo();
    System.out.println("========================");
    System.out.println();
  }
}
