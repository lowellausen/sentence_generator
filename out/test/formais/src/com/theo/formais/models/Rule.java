package com.theo.formais.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theolm on 08/05/16.
 */
public class Rule {
    public int id;
    public Variable ini; //variavel ini
    public List<Variable> gList; // gera essa lista de var

    public Rule(){
        gList = new ArrayList<>();
    }
}
