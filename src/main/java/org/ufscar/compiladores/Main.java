package org.ufscar.compiladores;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso esperado: java -jar T6-MiauCafe.jar <entrada.txt> <saida.json>");
            System.exit(1);
        }

        String arquivoEntrada = args[0];
        String arquivoSaida = args[1];

        try (PrintWriter pw = new PrintWriter(arquivoSaida)) {

            // LEXICO E SINTATICO
            CharStream cs = CharStreams.fromFileName(arquivoEntrada);
            MiauDataLexer lexer = new MiauDataLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiauDataParser parser = new MiauDataParser(tokens);


            MiauDataParser.ProgramaContext arvore = parser.programa();

            // SEMANTICO
            SemanticoVisitor semantico = new SemanticoVisitor();
            semantico.visit(arvore);

            // GERADOR DE CODIGO
            if (!semantico.errosSemanticos.isEmpty()) {
                for (String erro : semantico.errosSemanticos) {
                    pw.println(erro);
                    System.out.println(erro);
                }
                pw.println("Fim da compilacao por erros.");
            } else {
                GeradorJsonVisitor gerador = new GeradorJsonVisitor();
                gerador.visit(arvore);

                String jsonFinal = gerador.getJson();
                pw.print(jsonFinal);

                System.out.println("JSON gerado em: " + arquivoSaida);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}