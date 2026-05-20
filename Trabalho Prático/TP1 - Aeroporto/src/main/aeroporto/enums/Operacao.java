package aeroporto.enums;

/**
 * Enumeração que representa o tipo de operação desejada por um avião.
 */
public enum Operacao {
  /**
   * Procedimento de descida e aterrissagem.
   * Aviões com esta operação devem ser direcionados às prateleiras de pouso.
   */
  POUSO,

  /**
   * Procedimento de partida e voo.
   * Aviões com esta operação devem ser direcionados às prateleiras de decolagem.
   */
  DECOLAGEM
}
