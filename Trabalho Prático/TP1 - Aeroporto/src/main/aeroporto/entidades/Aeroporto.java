package aeroporto.entidades;

import java.util.Random;

import aeroporto.enums.Operacao;

/**
 * Representa o Aeroporto, gerando tráfego aéreo e controlando
 * a passagem do tempo da simulação através da Torre de Controle.
 */
public class Aeroporto {
  private TorreDeControle torreDeControle;
  private Random random;
  private Integer contadorIdAviao;

  /**
   * Inicializa o Aeroporto, criando a Torre de Controle e o gerador de números aleatórios.
   */
  public Aeroporto() {
    this.torreDeControle = new TorreDeControle();
    this.random = new Random();
    this.contadorIdAviao = 1;
  }

  /**
   * Avança uma unidade de tempo na simulação.
   * Gera novos aviões (pouso e decolagem) de forma aleatória,
   * solicita processamento pela torre de controle e imprime o relatório atual.
   */
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
