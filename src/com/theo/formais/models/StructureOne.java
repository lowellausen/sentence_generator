package com.theo.formais.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theolm on 10/05/16.
 */
public class StructureOne {

    public Rule rule;
    public int pointer;
    public int group;
    public String history;

    public StructureOne(Rule rule, int pointer, int group){
        this.rule = rule;
        this.group = group;
        this.pointer = pointer;
    }

}
