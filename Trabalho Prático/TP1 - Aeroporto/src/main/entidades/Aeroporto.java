package entidades;

import java.util.Random;

import enums.Operacao;

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
    Integer qtdAvioesPousar = random.nextInt(4);
    Integer qtdAvioesDecolar = random.nextInt(4);

    for (int i = 0; i < qtdAvioesPousar; i++) {
      Integer combustivel = random.nextInt(30) + 1;
      Aviao aviao = new Aviao(contadorIdAviao++, combustivel, Operacao.POUSAR);
      torreDeControle.processarAviao(aviao);
    }

    for (int i = 0; i < qtdAvioesDecolar; i++) {
      Integer combustivel = random.nextInt(30) + 1;
      Aviao aviao = new Aviao(contadorIdAviao++, combustivel, Operacao.DECOLAR);
      torreDeControle.processarAviao(aviao);
    }

    torreDeControle.processarPistas();
  }
}
