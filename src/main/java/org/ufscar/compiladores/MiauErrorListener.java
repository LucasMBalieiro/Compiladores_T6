package org.ufscar.compiladores;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

import java.util.ArrayList;
import java.util.List;

public class MiauErrorListener extends BaseErrorListener {

    public List<String> errosSintaticos = new ArrayList<>();

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        String erroFormatado = "Linha " + line + ":" + charPositionInLine + " - Erro sintatico: " + msg;
        errosSintaticos.add(erroFormatado);
    }
}