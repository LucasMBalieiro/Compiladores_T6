package org.ufscar.compiladores.dto;

import java.util.List;

public class PedidoDTO {
    public String id;
    public int valor;
    public float tempo;
    public List<ItemPedidoDTO> itens;

    public PedidoDTO(String id, int valor, float tempo, List<ItemPedidoDTO> itens) {
        this.id = id;
        this.valor = valor;
        this.tempo = tempo;
        this.itens = itens;
    }
}