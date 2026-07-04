package org.ufscar.compiladores;

import org.ufscar.compiladores.dto.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class GeradorJsonVisitor extends MiauDataBaseVisitor<Void> {

    // A raiz do nosso documento que será serializada para JSON
    private MiauDataOutput output;

    public GeradorJsonVisitor() {
        this.output = new MiauDataOutput();
    }

    @Override
    public Void visitDecIngrediente(MiauDataParser.DecIngredienteContext ctx) {
        String id = ctx.ID().getText();
        String tipo = ctx.TIPO_INGREDIENTE().getText();
        int tier = Integer.parseInt(ctx.NUM_INT().getText());

        IngredienteDTO ingrediente = new IngredienteDTO(id, tipo, tier);
        output.ingredientes.add(ingrediente);

        return null; // Não precisamos chamar o super, pois já extraímos tudo que precisávamos deste nó
    }

    @Override
    public Void visitDecPedido(MiauDataParser.DecPedidoContext ctx) {
        String id = ctx.ID().getText();
        int valor = Integer.parseInt(ctx.NUM_INT().getText());
        float tempo = Float.parseFloat(ctx.NUM_FLOAT().getText());

        List<ItemPedidoDTO> itensLista = new ArrayList<>();

        // Itera sobre os filhos (itens do pedido) manualmente
        if (ctx.itemPedido() != null) {
            for (MiauDataParser.ItemPedidoContext itemCtx : ctx.itemPedido()) {
                String ingredienteId = itemCtx.ID().getText();
                int quantidade = Integer.parseInt(itemCtx.NUM_INT().getText());
                itensLista.add(new ItemPedidoDTO(ingredienteId, quantidade));
            }
        }

        PedidoDTO pedido = new PedidoDTO(id, valor, tempo, itensLista);
        output.pedidos.add(pedido);

        return null;
    }

    @Override
    public Void visitDecSpawner(MiauDataParser.DecSpawnerContext ctx) {
        String id = ctx.ID().getText();

        List<DiaDTO> diasLista = new ArrayList<>();

        // Itera sobre os blocos de Dias
        if (ctx.dia() != null) {
            for (MiauDataParser.DiaContext diaCtx : ctx.dia()) {
                List<String> clientes = new ArrayList<>();

                // Itera sobre a lista de clientes dentro de cada Dia
                if (diaCtx.ID() != null) {
                    for (org.antlr.v4.runtime.tree.TerminalNode clienteNode : diaCtx.ID()) {
                        clientes.add(clienteNode.getText());
                    }
                }
                diasLista.add(new DiaDTO(clientes));
            }
        }

        SpawnerDTO spawner = new SpawnerDTO(id, diasLista);
        output.spawners.add(spawner);

        return null;
    }

    // =========================================================================
    // MÉTODO AUXILIAR PARA A SAÍDA FINAL
    // =========================================================================
    public String getJson() {
        // O GsonBuilder com PrettyPrinting garante que o JSON saia formatado
        // com quebras de linha e indentação, facilitando a leitura humana.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(output);
    }
}