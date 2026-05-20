package aeroporto.entidades;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import aeroporto.enums.Operacao;

public class PrateleiraTest {

  private Prateleira prateleira;

  @BeforeEach
  public void setUp() {
    prateleira = new Prateleira(1, Operacao.POUSO);
  }

  @Test
  public void deveAdicionarERemoverAviao() {
    Aviao aviao = new Aviao(1, 10, Operacao.POUSO);
    prateleira.adicionarAviao(aviao);

    assertEquals(1, prateleira.getTamanho());
    assertFalse(prateleira.isVazia());

    Aviao removido = prateleira.removerPrimeiroAviao().orElse(null);
    assertNotNull(removido);
    assertEquals(1, removido.getId());
    assertTrue(prateleira.isVazia());
  }

  @Test
  public void deveObterEmergencias() {
    Aviao normal = new Aviao(1, 10, Operacao.POUSO);
    Aviao critico1 = new Aviao(2, 0, Operacao.POUSO);
    Aviao critico2 = new Aviao(3, 0, Operacao.POUSO);

    prateleira.adicionarAviao(normal);
    prateleira.adicionarAviao(critico1);
    prateleira.adicionarAviao(critico2);

    var emergencias = prateleira.obterEmergencias();
    assertEquals(2, emergencias.size());
    assertTrue(emergencias.contains(critico1));
    assertTrue(emergencias.contains(critico2));
  }

  @Test
  public void deveAtualizarTempoDeEspera() {
    Aviao aviao1 = new Aviao(1, 5, Operacao.POUSO);
    Aviao aviao2 = new Aviao(2, 0, Operacao.POUSO);

    prateleira.adicionarAviao(aviao1);
    prateleira.adicionarAviao(aviao2);

    prateleira.atualizarTempoDeEspera();

    assertEquals(4, aviao1.getCombustivel());
    assertEquals(0, aviao2.getCombustivel());
  }
}
