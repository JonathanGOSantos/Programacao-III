package simulacao;

import enums.Estagio;
import enums.Operacao;

public record RegistroTorreDeControle(Integer instante, Integer idAviao, Integer combustivel, Estagio estagio, Operacao operacao,
    Integer pista, Integer prateleira) implements Registro {
}