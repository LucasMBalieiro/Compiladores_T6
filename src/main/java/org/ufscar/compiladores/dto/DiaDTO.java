package org.ufscar.compiladores.dto;

import java.util.List;

public class DiaDTO {
    public List<String> clientesPedidosIds;

    public DiaDTO(List<String> clientesPedidosIds) {
        this.clientesPedidosIds = clientesPedidosIds;
    }
}