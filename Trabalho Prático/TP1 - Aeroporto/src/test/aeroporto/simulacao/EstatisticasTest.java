package aeroporto.simulacao;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import aeroporto.enums.Estagio;
import aeroporto.enums.Operacao;

public class EstatisticasTest {

  private Estatisticas estatisticas;

  @Test
  public void deveCalcularTempoMedioDePouso() {
    estatisticas = new Estatisticas();
    // Avião 1 levou 2 instantes
    estatisticas.novoRegistro(new Registro(1, 100, 10, Estagio.INICIOU, Operacao.POUSO, 1, 1));
    estatisticas.novoRegistro(new Registro(3, 100, 8, Estagio.FINALIZOU, Operacao.POUSO, 1, 1));

    // Avião 2 levou 4 instantes
    estatisticas.novoRegistro(new Registro(1, 101, 10, Estagio.INICIOU, Operacao.POUSO, 2, 2));
    estatisticas.novoRegistro(new Registro(5, 101, 6, Estagio.FINALIZOU, Operacao.POUSO, 2, 2));

    // Média = 3.0
    assertEquals(3.0, estatisticas.tempoMedioDePouso(), 0.01);
  }

  @Test
  public void deveCalcularTempoMedioDeDecolagem() {
    estatisticas = new Estatisticas();
    // Avião 1 levou 1 instante
    estatisticas.novoRegistro(new Registro(1, 200, 10, Estagio.INICIOU, Operacao.DECOLAGEM, 1, 1));
    estatisticas.novoRegistro(new Registro(2, 200, 9, Estagio.FINALIZOU, Operacao.DECOLAGEM, 1, 1));

    // Avião 2 levou 3 instantes
    estatisticas.novoRegistro(new Registro(2, 201, 10, Estagio.INICIOU, Operacao.DECOLAGEM, 2, 2));
    estatisticas.novoRegistro(new Registro(5, 201, 7, Estagio.FINALIZOU, Operacao.DECOLAGEM, 2, 2));

    // Média = (1 + 3) / 2 = 2.0
    assertEquals(2.0, estatisticas.tempoMedioDeDecolagem(), 0.01);
  }

  @Test
  public void deveContarAvioesSemCombustivel() {
    estatisticas = new Estatisticas();
    estatisticas.novoRegistro(new Registro(1, 100, 1, Estagio.INICIOU, Operacao.POUSO, 1, 1));
    estatisticas.novoRegistro(new Registro(2, 100, 0, Estagio.FINALIZOU, Operacao.POUSO, 1, 1));
    
    estatisticas.novoRegistro(new Registro(1, 101, 5, Estagio.INICIOU, Operacao.POUSO, 2, 2));
    estatisticas.novoRegistro(new Registro(2, 101, 4, Estagio.FINALIZOU, Operacao.POUSO, 2, 2));

    // Apenas o aviao 100 zerou o combustivel
    assertEquals(1L, (long) estatisticas.avioesSemCombustivel());
  }
}
