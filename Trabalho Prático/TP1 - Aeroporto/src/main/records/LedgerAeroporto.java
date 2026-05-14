package records;

import enums.OperacaoAeroporto;

public record LedgerAeroporto(Integer tick, Integer pista, AviaoRecord aviao, OperacaoAeroporto operacao) {
}