package aeroporto.entidades;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import aeroporto.enums.Operacao;

public class TorreDeControleTest {

  private TorreDeControle torre;

  @Test
  public void deveProcessarAviaoEColocarNaFila() {
    torre = new TorreDeControle();
    Aviao aviao = new Aviao(1, 10, Operacao.POUSO);
    torre.processarAviao(aviao);

    assertNotNull(torre.getEstatisticas());
    assertEquals(1, torre.getEstatisticas().getRegistros().size());
  }

  @Test
  public void devePassarOTempoEAtualizarInstante() {
    torre = new TorreDeControle();
    assertEquals(0, (int) torre.getInstante());
    torre.passarTempo();
    assertEquals(1, (int) torre.getInstante());
  }
}
