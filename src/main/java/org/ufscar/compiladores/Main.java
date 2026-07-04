package org.ufscar.compiladores;

public class Main {
    public static void main(String[] args) {
        
        SemanticoVisitor semantico = new SemanticoVisitor();
        semantico.visit(arvore);

        if (!semantico.errosSemanticos.isEmpty()) {
            for (String erro : semantico.errosSemanticos) {
                System.out.println(erro);
                // Opcional: Escrever no arquivo de saída
            }
            System.out.println("Fim da compilacao. Erros encontrados.");
        } else {
            // SUCESSO! Iniciar a geração do código JSON.
            GeradorJsonVisitor gerador = new GeradorJsonVisitor();
            gerador.visit(arvore);
        }
    }
}