package org.ufscar.compiladores.dto;

import java.util.ArrayList;
import java.util.List;

public class MiauDataOutput {
    public List<IngredienteDTO> ingredientes = new ArrayList<>();
    public List<PedidoDTO> pedidos = new ArrayList<>();
    public List<SpawnerDTO> spawners = new ArrayList<>();
}