package org.ufscar.compiladores;

import java.util.ArrayList;
import java.util.List;

public class SemanticoVisitor extends MiauDataBaseVisitor<Void> {

    TabelaDeSimbolos tabela;
    public List<String> errosSemanticos;

    public SemanticoVisitor() {
        this.tabela = new TabelaDeSimbolos();
        this.errosSemanticos = new ArrayList<>();
    }

    @Override
    public Void visitDecIngrediente(MiauDataParser.DecIngredienteContext ctx) {
        String nome = ctx.ID().getText();

        // Duplicidade de identificadores
        if (tabela.existe(nome)) {
            errosSemanticos.add("Erro Semantico: O identificador '" + nome + "' ja foi declarado anteriormente.");
        } else {
            // Verifica tier
            int tier = Integer.parseInt(ctx.NUM_INT().getText());
            if (tier < 0 || tier > 3) {
                errosSemanticos.add("Erro Semantico: O tier do ingrediente '" + nome + "' deve ser um valor entre 0 e 3.");
            }

            tabela.adicionar(nome, TabelaDeSimbolos.TipoMiau.INGREDIENTE);
        }

        return super.visitDecIngrediente(ctx);
    }

    @Override
    public Void visitDecPedido(MiauDataParser.DecPedidoContext ctx) {
        String nome = ctx.ID().getText();

        if (tabela.existe(nome)) {
            errosSemanticos.add("Erro Semantico: O identificador '" + nome + "' ja foi declarado anteriormente.");
        } else {
            tabela.adicionar(nome, TabelaDeSimbolos.TipoMiau.PEDIDO);
        }

        if (ctx.itemPedido() != null) {
            for (MiauDataParser.ItemPedidoContext itemCtx : ctx.itemPedido()) {
                String nomeItem = itemCtx.ID().getText();

                // Referencia fantasma e checagem de tipo
                TabelaDeSimbolos.TipoMiau tipoItem = tabela.verificar(nomeItem);

                if (tipoItem == TabelaDeSimbolos.TipoMiau.INVALIDO) {
                    errosSemanticos.add("Erro Semantico: O ingrediente '" + nomeItem + "' referenciado no pedido '" + nome + "' nao foi declarado.");
                } else if (tipoItem != TabelaDeSimbolos.TipoMiau.INGREDIENTE) {
                    errosSemanticos.add("Erro Semantico: Identificador '" + nomeItem + "' no pedido '" + nome + "' e invalido. Pedidos so podem conter Ingredientes.");
                }
            }
        }

        return super.visitDecPedido(ctx);
    }

    @Override
    public Void visitDecSpawner(MiauDataParser.DecSpawnerContext ctx) {
        String nome = ctx.ID().getText();

        if (tabela.existe(nome)) {
            errosSemanticos.add("Erro Semantico: O identificador '" + nome + "' ja foi declarado anteriormente.");
        } else {
            tabela.adicionar(nome, TabelaDeSimbolos.TipoMiau.SPAWNER);
        }

        return super.visitDecSpawner(ctx);
    }

    @Override
    public Void visitDia(MiauDataParser.DiaContext ctx) {
        for (org.antlr.v4.runtime.tree.TerminalNode idNode : ctx.ID()) {
            String nomeCliente = idNode.getText();
            TabelaDeSimbolos.TipoMiau tipoCliente = tabela.verificar(nomeCliente);

            if (tipoCliente == TabelaDeSimbolos.TipoMiau.INVALIDO) {
                errosSemanticos.add("Erro Semantico: O pedido '" + nomeCliente + "' referenciado no Dia nao foi declarado.");
            } else if (tipoCliente != TabelaDeSimbolos.TipoMiau.PEDIDO) {
                errosSemanticos.add("Erro Semantico: Identificador '" + nomeCliente + "' e invalido para um Dia. Dias so podem receber listas de Pedidos.");
            }
        }

        return super.visitDia(ctx);
    }
}