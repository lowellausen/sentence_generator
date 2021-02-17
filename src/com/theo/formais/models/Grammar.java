package com.theo.formais.models;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by theolm on 08/05/16.
 */
public class Grammar {
    public Variable initialVar;
    public List<Variable> terminals;
    public List<Variable> variables;
    public List<Rule> rules;

    public Grammar(){
        terminals = new ArrayList<>();
        variables = new ArrayList<>();
        rules = new ArrayList<>();
    }

    public void addVariable(String line, boolean isTerminal){
        String inputString = line.replace(" ", "").replaceAll("#.*", "").replaceAll("\t", ""); //remove espaços em branco e todos os comentarios
        inputString = inputString.replace("{", "").replace("}", ""); // remove os brackets

        String [] arr = inputString.split(",");

        for (int i = 0; i < arr.length; i++) {
            Variable var = new Variable(arr[i], isTerminal);
            if (isTerminal)
                this.terminals.add(var);
            else
                this.variables.add(var);
        }

        if(isTerminal)
            System.out.print("ADICIONOU TODOS OS TERMINAIS");
        else
            System.out.print("ADICIONOU TODAS AS VARIAVEIS");

    }

    public void addInitialVar(String line){
        String inputString = line.replace(" ", "").replaceAll("#.*", "").replaceAll("\t", ""); //remove espaços em branco e todos os comentarios
        inputString = inputString.replace("{", "").replace("}", ""); // remove os brackets

        Variable var = new Variable(inputString, false);
        this.initialVar = var;

    }

    public void addRule(String line){
        Rule rule = new Rule();

        String inputString = line.replace(" ", "").replaceAll("#.*", "").replaceAll("\t", ""); //remove espaços em branco e todos os comentarios
        inputString = inputString.replace("{", "").replace("}", "").replace(";", ""); // remove os brackets

        String[] arr = inputString.split(">", 2);
        String[] rightVars = arr[1].split(",");

        Variable leftVar = new Variable(arr[0], false);
        rule.ini = leftVar;

        for (int i = 0; i < rightVars.length; i++) {
            String s = rightVars[i];
            Variable v = new Variable(s, false);

            if(Character.isLowerCase(s.charAt(0))){
                v.isTerminal = true;
            }

            rule.gList.add(v);
        }

        rules.add(rule);
    }

    public StructureD parseInputText(String txtInput){

        String inputString = txtInput.toLowerCase();
        String[] arrString = inputString.split(" ");

        StructureD d = createD(arrString);

        return d;
    }

    public String generateRandomText(){
        String output = "";

        StructureD zero = createDZero();
        output = getFirstWord(zero);



        output = getWords(output, zero);




        return output;
    }

    // auxiliar functions

    private StructureD createD(String[] input){
        int n = input.length;
        List<StructureD> groups = new ArrayList<>();

        StructureD d_zero = createDZero();
        d_zero.groupIndex = 0;
        groups.add(d_zero);

        for (int j = 1; j <= n ; j++) {
            StructureD dn = createDN(j, groups, input[j-1]); //chama func que gera Dn
            groups.add(dn);
        }

        return groups.get(n);
    }

    private StructureD createDZero(){
        int i = 0;
        int size;
        StructureD zero = new StructureD();
        zero.groupIndex = 0;

        for (Rule r : this.rules){
            if(this.initialVar.variable.contentEquals(r.ini.variable)){
                StructureOne s = new StructureOne(r, 0, 0);
                zero.dList.add(s);
            }
        }

        do{
            StructureOne r = zero.dList.get(i);
            i++;
            size = zero.dList.size();

            if(!r.rule.gList.get(r.pointer).isTerminal) {
                for (Rule s : this.rules) {
                    if (r.rule.gList.get(r.pointer).variable.contentEquals(s.ini.variable)) {
                        StructureOne t = new StructureOne(s, 0, 0);
                        zero.dList.add(t);
                    }
                }
            }

        }while ((size - zero.dList.size()) != 0 );  // enquanto a diferença de tamanho for diferente de zero;

        return zero;
    }

    private StructureD createDN(int groupIndex, List<StructureD> groups, String input){
        int pointer = groupIndex - 1; // por causa do d0
        StructureD d_n = new StructureD();
        d_n.groupIndex = groupIndex;

        for (StructureOne previousGroup : groups.get(pointer).dList) {  //acha quem aponta  para o terminal sendo parseado, no conjunto anterior
            if (previousGroup.pointer != -1) {
                if (previousGroup.rule.gList.get(previousGroup.pointer).isTerminal && previousGroup.rule.gList.get(previousGroup.pointer).variable.contentEquals(input)) {
                    StructureOne k2;
                    if (previousGroup.rule.gList.size() > previousGroup.pointer + 1)
                        k2 = new StructureOne(previousGroup.rule, previousGroup.pointer + 1, pointer);   // -1 simboliza ponteiro no final
                    else
                        k2 = new StructureOne(previousGroup.rule, -1, pointer);
                    k2.history = "{".concat(input).concat("}");
                    d_n.dList.add(k2);
                    
                }
            }
        }

        int size;

        do{

            size = d_n.dList.size();

            for (int i = 0; i <size ; i++) {
                StructureOne currentRule = d_n.dList.get(i);

                if(currentRule.pointer == -1){  //regras que já completaram
                    for(StructureOne pointedGroup : groups.get(currentRule.group).dList){
                        if(pointedGroup.pointer != -1){
                            if(pointedGroup.rule.gList.get(pointedGroup.pointer).variable.contentEquals(currentRule.rule.ini.variable) && !isOngroup(pointedGroup.rule, d_n.dList, givePointer(pointedGroup), pointedGroup.group)){
                                StructureOne newStruct;
                                if (pointedGroup.rule.gList.size()>pointedGroup.pointer+1){
                                    newStruct = new StructureOne(pointedGroup.rule, pointedGroup.pointer + 1 , pointedGroup.group);
                                }
                                else{
                                    newStruct = new StructureOne(pointedGroup.rule, -1, pointedGroup.group);
                                }
                                if (pointedGroup.history!=null) {

                                    newStruct.history = "{".concat(pointedGroup.history).concat(pointedGroup.rule.gList.get(pointedGroup.pointer).variable).concat(currentRule.history).concat("}");
                                }
                                else{
                                    newStruct.history = "{".concat(pointedGroup.rule.gList.get(pointedGroup.pointer).variable).concat(currentRule.history).concat("}");
                                }
                                d_n.dList.add(newStruct);
                            }
                        }
                    }
                }
                else{                //regras que não completaram
                    if(!currentRule.rule.gList.get(currentRule.pointer).isTerminal){
                        for (Rule grammarRule : this.rules){
                            if(grammarRule.ini.variable.contentEquals(currentRule.rule.gList.get(currentRule.pointer).variable) && !isOngroup(grammarRule,d_n.dList,0, d_n.groupIndex)){
                                StructureOne  newStruct = new StructureOne(grammarRule, 0, groupIndex);
                                d_n.dList.add(newStruct);

                            }
                        }
                    }
                }

            }

        } while (size - d_n.dList.size() != 0);

        return d_n;
    }

    private int givePointer (StructureOne current){
        int out;
        if(current.rule.gList.size() > current.pointer + 1){
            out =current.pointer + 1 ;
        }
        else{
            out = - 1;
        }

        return out;
    }

    private boolean compareRules(Rule one, Rule two){

        boolean out = true;

        if (one.ini.variable.contentEquals(two.ini.variable) && one.gList.size()==two.gList.size()){
            for (int i = 0; i <one.gList.size() ; i++) {
                if(!one.gList.get(i).variable.contentEquals(two.gList.get(i).variable))
                    out = false;
            }
        }
        else{
            out = false;
        }

        return out;
    }

    private boolean isOngroup (Rule current, List<StructureOne> group, int pointer, int groupi){
        int cont = 0;

        for (StructureOne r : group)
            if ( compareRules(r.rule, current) && r.pointer==pointer && groupi == r.group)
                cont++;
        if (cont!=0)
            return true;
        else
            return false;
    }

    private String getFirstWord(StructureD zero){
        List<String> terminals = new ArrayList<>();

        for(StructureOne x : zero.dList){
            if(x.rule.gList.get(x.pointer).isTerminal){
                terminals.add(x.rule.gList.get(x.pointer).variable);
            }
        }

        Random rand = new Random();
        int  n = rand.nextInt(terminals.size());


        return terminals.get(n);
    }

    private String getWords(String firstWord, StructureD zero){
        String tempWord = firstWord, output;
        int i=1;
        List<StructureOne> list = new ArrayList<>();
        List<StructureD> groups = new ArrayList<>();
        List<String> terminals = new ArrayList<>();


        groups.add(zero);

        boolean loopFlag;
        int loopControl = 4;

        do {
            loopFlag = false;

            StructureD d = createDN(i, groups,tempWord);
            groups.add(d);
            i++;

            for (StructureOne x : d.dList) {

                if (x.pointer == -1 && x.group == 0) {
                    if (x.rule.ini.variable.contentEquals(this.initialVar.variable)) {
                        list.add(x);
                        loopControl --;
                    }
                }

                if (x.pointer != -1) {
                    if (x.rule.gList.get(x.pointer).isTerminal  ) {
                        terminals.add(x.rule.gList.get(0).variable);
                        loopFlag = true;
                    }
                }

            }

            if(terminals.size()>0) {
                Random rand = new Random();
                int n = rand.nextInt(terminals.size());
                tempWord = terminals.get(n);
                terminals.clear();
            }


        } while ((loopControl > 0 && loopFlag));

        Random rand = new Random();
        int n = rand.nextInt(list.size());
        output = list.get(n).history;
        output = output.replace("{", "").replace("}"," ").replaceAll("[A-Z]","").replaceAll("( )+"," ");

        return output+"\n"+this.initialVar.variable+list.get(n).history;
    }

}
