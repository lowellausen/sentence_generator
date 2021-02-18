package com.theo.formais.models;

/**
 * Created by theolm on 08/05/16.
 */
public class Variable {
    public int id;
    public boolean isTerminal = false;
    public String variable;

    public Variable (String var, boolean isTerminal) {
        this.isTerminal = isTerminal;
        this.variable = var;
    }
}
