package org.ufscar.compiladores.dto;

public class ItemPedidoDTO {
    public String ingredienteId;
    public int quantidade;

    public ItemPedidoDTO(String ingredienteId, int quantidade) {
        this.ingredienteId = ingredienteId;
        this.quantidade = quantidade;
    }
}