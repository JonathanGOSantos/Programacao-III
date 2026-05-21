package aeroporto.simulacao;

import aeroporto.enums.Estagio;
import aeroporto.enums.Operacao;

public record Registro(
        Integer instante,
        Integer idAviao,
        Integer combustivel, Estagio estagio, Operacao operacao,
        Integer pista, Integer prateleira) {
}
