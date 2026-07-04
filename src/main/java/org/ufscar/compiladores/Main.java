package org.ufscar.compiladores;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        // Valida se os caminhos dos arquivos foram passados no terminal
        if (args.length < 2) {
            System.err.println("Uso esperado: java -jar MiauCompiler.jar <arquivo_entrada> <arquivo_saida>");
            System.exit(1);
        }

        String arquivoEntrada = args[0];
        String arquivoSaida = args[1];

        // O try-with-resources garante o fechamento do arquivo .out/.json no fim da execução
        try (PrintWriter pw = new PrintWriter(arquivoSaida)) {

            // =================================================================
            // 1. ANÁLISE LÉXICA E SINTÁTICA
            // =================================================================
            CharStream cs = CharStreams.fromFileName(arquivoEntrada);
            MiauDataLexer lexer = new MiauDataLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiauDataParser parser = new MiauDataParser(tokens);

            // Inicia o parsing a partir da regra principal definida no .g4
            MiauDataParser.ProgramaContext arvore = parser.programa();

            // =================================================================
            // 2. ANÁLISE SEMÂNTICA
            // =================================================================
            SemanticoVisitor semantico = new SemanticoVisitor();
            semantico.visit(arvore);

            // =================================================================
            // 3. GERAÇÃO DE CÓDIGO (JSON) OU EXIBIÇÃO DE ERROS
            // =================================================================
            if (!semantico.errosSemanticos.isEmpty()) {
                // Caso existam falhas lógicas no balanceamento (ex: IDs duplicados)
                for (String erro : semantico.errosSemanticos) {
                    pw.println(erro);
                    System.out.println(erro); // Opcional: print no terminal para feedback imediato
                }
                pw.println("Fim da compilacao. Erros semanticos encontrados.");
            } else {
                // Caso a árvore seja 100% válida, gera o JSON final
                GeradorJsonVisitor gerador = new GeradorJsonVisitor();
                gerador.visit(arvore);

                String jsonFinal = gerador.getJson();
                pw.print(jsonFinal);

                System.out.println("Sucesso! JSON gerado em: " + arquivoSaida);
            }

        } catch (IOException e) {
            System.err.println("Erro grave de I/O ao manipular os arquivos: " + e.getMessage());
        }
    }
}