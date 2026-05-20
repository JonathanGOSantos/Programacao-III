package aeroporto.entidades;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import aeroporto.enums.Operacao;

public class AviaoTest {

  @Test
  public void deveCriarAviaoComValoresCorretos() {
    Aviao aviao = new Aviao(1, 10, Operacao.POUSO);
    assertEquals(1, (int) aviao.getId());
    assertEquals(10, (int) aviao.getCombustivel());
    assertEquals(Operacao.POUSO, aviao.getOperacao());
  }

  @Test
  public void deveDecrementarCombustivel() {
    Aviao aviao = new Aviao(1, 1, Operacao.POUSO);
    aviao.decrementarCombustivel();
    assertEquals(0, (int) aviao.getCombustivel());
  }

  @Test
  public void naoDeveDecrementarCombustivelAbaixoDeZero() {
    Aviao aviao = new Aviao(1, 0, Operacao.POUSO);
    aviao.decrementarCombustivel();
    assertEquals(0, (int) aviao.getCombustivel());
  }

  @Test
  public void deveRetornarSeEstaEmSituacaoCritica() {
    Aviao aviaoNormal = new Aviao(1, 5, Operacao.POUSO);
    assertFalse(aviaoNormal.emSituacaoCritica());

    Aviao aviaoCritico = new Aviao(2, 0, Operacao.POUSO);
    assertTrue(aviaoCritico.emSituacaoCritica());
  }
}
