package org.ufscar.compiladores;
import java.util.HashMap;
import java.util.Map;

public class TabelaDeSimbolos {

    public enum TipoMiau {
        INGREDIENTE,
        PEDIDO,
        SPAWNER,
        INVALIDO
    }

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

    public void adicionar(String nome, TipoMiau tipo) {
        tabela.put(nome, new EntradaTabelaDeSimbolos(nome, tipo));
    }

    public boolean existe(String nome) {
        return tabela.containsKey(nome);
    }

    public TipoMiau verificar(String nome) {
        if (tabela.containsKey(nome)) {
            return tabela.get(nome).tipo;
        }
        return TipoMiau.INVALIDO;
    }
}