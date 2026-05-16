package entidades;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AviaoTest {
  @Test
  public void deveCriarAviaoComIdentificadorCorreto() {
    Aviao aviao = new Aviao(1, 5);
    assertEquals(new Integer(5), aviao.getCombustivel());
    assertFalse(aviao.emSituacaoCritica());
    aviao.decrementarCombustivel();
    aviao.decrementarCombustivel();
    aviao.decrementarCombustivel();
    aviao.decrementarCombustivel();
    aviao.decrementarCombustivel();
    assertTrue(aviao.emSituacaoCritica());
  }
}
