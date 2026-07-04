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

    // =========================================================================
    // 1. ANÁLISE DE INGREDIENTES
    // =========================================================================
    @Override
    public Void visitDecIngrediente(MiauDataParser.DecIngredienteContext ctx) {
        String nome = ctx.ID().getText();

        // Verificação 1: Duplicidade global de identificadores
        if (tabela.existe(nome)) {
            errosSemanticos.add("Erro Semantico: O identificador '" + nome + "' ja foi declarado anteriormente.");
        } else {
            // Verificação 2: A restrição rígida de design (Tier de 0 a 3)
            int tier = Integer.parseInt(ctx.NUM_INT().getText());
            if (tier < 0 || tier > 3) {
                errosSemanticos.add("Erro Semantico: O tier do ingrediente '" + nome + "' deve ser um valor entre 0 e 3.");
            }

            // Adiciona na tabela se passou pelas validações básicas
            tabela.adicionar(nome, TabelaDeSimbolos.TipoMiau.INGREDIENTE);
        }

        return super.visitDecIngrediente(ctx);
    }

    // =========================================================================
    // 2. ANÁLISE DE PEDIDOS
    // =========================================================================
    @Override
    public Void visitDecPedido(MiauDataParser.DecPedidoContext ctx) {
        String nome = ctx.ID().getText();

        if (tabela.existe(nome)) {
            errosSemanticos.add("Erro Semantico: O identificador '" + nome + "' ja foi declarado anteriormente.");
        } else {
            tabela.adicionar(nome, TabelaDeSimbolos.TipoMiau.PEDIDO);
        }

        // Visita os itens (ingredientes) dentro deste pedido
        if (ctx.itemPedido() != null) {
            for (MiauDataParser.ItemPedidoContext itemCtx : ctx.itemPedido()) {
                String nomeItem = itemCtx.ID().getText();

                // Verificação 3: Referência fantasma e Verificação 4: Checagem de Tipo
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

    // =========================================================================
    // 3. ANÁLISE DO SPAWNER E DIAS
    // =========================================================================
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
        // Itera sobre todos os IDs que foram colocados na lista de clientes do Dia
        for (org.antlr.v4.runtime.tree.TerminalNode idNode : ctx.ID()) {
            String nomeCliente = idNode.getText();
            TabelaDeSimbolos.TipoMiau tipoCliente = tabela.verificar(nomeCliente);

            // Reaproveitamos a lógica da Verificação 3 e 4 para o contexto de Spawners
            if (tipoCliente == TabelaDeSimbolos.TipoMiau.INVALIDO) {
                errosSemanticos.add("Erro Semantico: O pedido '" + nomeCliente + "' referenciado no Dia nao foi declarado.");
            } else if (tipoCliente != TabelaDeSimbolos.TipoMiau.PEDIDO) {
                errosSemanticos.add("Erro Semantico: Identificador '" + nomeCliente + "' e invalido para um Dia. Dias so podem receber listas de Pedidos.");
            }
        }

        return super.visitDia(ctx);
    }
}