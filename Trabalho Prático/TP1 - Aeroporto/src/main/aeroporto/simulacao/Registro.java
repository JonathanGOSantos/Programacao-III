package aeroporto.simulacao;

import aeroporto.enums.Estagio;
import aeroporto.enums.Operacao;

/**
 * Registro individual de um evento na simulação (ex: avião entrou na fila, decolou, etc).
 *
 * @param instante Instante de tempo em que o evento ocorreu.
 * @param idAviao ID do avião envolvido no evento.
 * @param combustivel Quantidade de combustível do avião no momento do registro.
 * @param estagio Estágio da operação (INICIOU, FINALIZOU, etc).
 * @param operacao Tipo da operação (POUSO ou DECOLAGEM).
 * @param pista ID da pista alocada (-1 se nenhuma).
 * @param prateleira ID da prateleira (fila) em que o avião se encontra.
 */
public record Registro(
        Integer instante,
        Integer idAviao,
        Integer combustivel, Estagio estagio, Operacao operacao,
        Integer pista, Integer prateleira) {
}
