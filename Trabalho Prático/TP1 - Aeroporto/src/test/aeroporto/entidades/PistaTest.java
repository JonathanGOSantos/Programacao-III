package aeroporto.entidades;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;

public class PistaTest {

  @Test
  public void deveOcuparELiberarPista() {
    Pista pista = new Pista(1);
    
    assertFalse(pista.emUso());
    
    pista.ocupar();
    assertTrue(pista.emUso());
    
    pista.liberar();
    assertFalse(pista.emUso());
  }

  @Test
  public void deveRetornarIdCorreto() {
    Pista pista = new Pista(2);
    assertEquals(2, (int) pista.getId());
  }
}
