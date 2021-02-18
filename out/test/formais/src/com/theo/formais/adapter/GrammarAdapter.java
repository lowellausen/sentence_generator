package com.theo.formais.adapter;

import com.theo.formais.models.Grammar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * Created by Theo on 08/05/2016.
 */
public class GrammarAdapter {

    private final static int TYPE_TERMINAL = 0;
    private final static int TYPE_VARIABLE = 1;
    private final static int TYPE_INITIAL = 2;
    private final static int TYPE_RULE = 3;

    public static Grammar parseFile(File txt) {

        try {
            Grammar grammar = new Grammar();

            FileReader input = new FileReader(txt);
            BufferedReader bufRead = new BufferedReader(input);
            String line = null;

            int currentType = TYPE_TERMINAL;


            while ( (line = bufRead.readLine()) != null)
            {
                //se começar por { tem informação útil para a gramatica caso contrario indica tipo de conteudo a ser inserido
                if(line.charAt(0) != '{'){

                    String teste = line;

                    String arr[] = line.split(" ", 2);
                    String firstWord = arr[0].replace("#", "").replaceAll("\t", "");

                    if(firstWord.toLowerCase().contentEquals("terminais")){
                        currentType = TYPE_TERMINAL;
                    } else if (firstWord.toLowerCase().contentEquals("variaveis")){
                        currentType = TYPE_VARIABLE;
                    } else if (firstWord.toLowerCase().contentEquals("inicial")){
                        currentType = TYPE_INITIAL;
                    } else {
                        currentType = TYPE_RULE;
                    }
                } else if (line.charAt(0) == '{'){

                    switch (currentType){
                        case TYPE_TERMINAL:
                            grammar.addVariable(line, true);
                            break;
                        case TYPE_VARIABLE:
                            grammar.addVariable(line, false);
                            break;
                        case TYPE_INITIAL:
                            grammar.addInitialVar(line);
                            break;
                        case TYPE_RULE:
                            grammar.addRule(line);
                            break;
                        default:
                            break;
                    }

                }

            }

            return grammar;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
