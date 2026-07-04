package org.ufscar.compiladores.dto;

public class IngredienteDTO {
    public String id; // Nome do ScriptableObject na Unity
    public String tipo;
    public int tier;

    public IngredienteDTO(String id, String tipo, int tier) {
        this.id = id;
        this.tipo = tipo;
        this.tier = tier;
    }
}