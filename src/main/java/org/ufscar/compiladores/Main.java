package org.ufscar.compiladores;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Uso esperado: java -jar T6-MiauCafe-jar-with-dependencies.jar <arquivo_entrada> <arquivo_saida>");
            System.exit(1);
        }

        String arquivoEntrada = args[0];
        String arquivoSaida = args[1];

        try (PrintWriter pw = new PrintWriter(arquivoSaida)) {

            //SINTATICO E LEXICO
            CharStream cs = CharStreams.fromFileName(arquivoEntrada);
            MiauDataLexer lexer = new MiauDataLexer(cs);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            MiauDataParser parser = new MiauDataParser(tokens);

            MiauErrorListener errorListener = new MiauErrorListener();

            //ERROS
            lexer.removeErrorListeners();
            lexer.addErrorListener(errorListener);
            parser.removeErrorListeners();
            parser.addErrorListener(errorListener);

            MiauDataParser.ProgramaContext arvore = parser.programa();

            if (!errorListener.errosSintaticos.isEmpty()) {
                for (String erro : errorListener.errosSintaticos) {
                    pw.println(erro);
                    System.out.println(erro);
                }
                pw.println("Fim da compilacao. Erros sintaticos encontrados.");
                return;
            }

            // SEMANTICO
            SemanticoVisitor semantico = new SemanticoVisitor();
            semantico.visit(arvore);

            // CODIGO
            if (!semantico.errosSemanticos.isEmpty()) {
                for (String erro : semantico.errosSemanticos) {
                    pw.println(erro);
                    System.out.println(erro);
                }
                pw.println("Fim da compilacao. Erros semanticos encontrados.");
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