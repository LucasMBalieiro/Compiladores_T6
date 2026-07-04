package org.ufscar.compiladores;
import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {

    // Enum com os tipos de blocos que a nossa linguagem reconhece
    public enum TipoMiau {
        INGREDIENTE,
        PEDIDO,
        SPAWNER,
        INVALIDO
    }

    // Estrutura interna para guardar os dados de cada símbolo
    class EntradaTabelaDeSimbolos {
        String nome;
        TipoMiau tipo;

        private EntradaTabelaDeSimbolos(String nome, TipoMiau tipo) {
            this.nome = nome;
            this.tipo = tipo;
        }
    }

    private Map<String, EntradaTabelaDeSimbolos> tabela;

    public TabelaDeSimbolos() {
        this.tabela = new HashMap<>();
    }

    // Adiciona um novo identificador na tabela
    public void adicionar(String nome, TipoMiau tipo) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }

    // Verifica se um ID já foi declarado antes (Útil para evitar duplicidade)
    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    // Retorna o tipo do identificador (Útil para saber se um ID é realmente um Ingrediente)
    public TipoMiau verificar(String nome) {
        if (tabela.containsKey(nome)) {
            return tabela.get(nome).tipo;
        }
        return TipoMiau.INVALIDO;
    }
}