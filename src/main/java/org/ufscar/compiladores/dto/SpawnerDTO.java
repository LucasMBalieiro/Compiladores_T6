package org.ufscar.compiladores.dto;

import java.util.List;

public class SpawnerDTO {
    public String id;
    public List<DiaDTO> dias;

    public SpawnerDTO(String id, List<DiaDTO> dias) {
        this.id = id;
        this.dias = dias;
    }
}