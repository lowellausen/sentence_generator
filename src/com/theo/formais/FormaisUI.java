package com.theo.formais;

import com.theo.formais.adapter.GrammarAdapter;
import com.theo.formais.models.*;
import com.theo.formais.utils.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theolm on 08/05/16.
 */
public class FormaisUI {
    private JPanel panelMain;
    private JButton btnOpenGrammar;
    private JTextField txtInput;
    private JButton btnGo;
    private JButton btnGenerateDialog;

    private Grammar grammar;
    private String generatedPhrase;

    public FormaisUI() {
        btnOpenGrammar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = IOUtils.openFile(panelMain);
                if (file != null){
                    grammar = GrammarAdapter.parseFile(file);
                }

                showOnGrammarLoadMessage();
            }
        });
        btnGo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    StructureD d = grammar.parseInputText(txtInput.getText());
                    if(accept(d)){
                        for (StructureOne x : d.dList) {
                            int i=1;
                            if(x.pointer == -1 && x.group == 0) {
                                if(x.rule.ini.variable.contentEquals(grammar.initialVar.variable)) {
                                    JOptionPane.showMessageDialog(panelMain, "Arvore " + i++ +"   " +grammar.initialVar.variable +x.history);
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(panelMain, "A gramática NÃO aceita essa entrada.");
                    }
                } catch (Exception e1){
                    JOptionPane.showMessageDialog(panelMain, "A gramática NÃO aceita essa entrada.");
                }
            }
        });

        btnGenerateDialog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (grammar != null) {
                    generatedPhrase = grammar.generateRandomText();
                    JOptionPane.showMessageDialog(panelMain, generatedPhrase);

                }
                else {
                    JOptionPane.showMessageDialog(panelMain, "Primeiro é necessário carregar uma gramática válida");
                }
            }
        });
    }

    public boolean accept (StructureD d){
        for (StructureOne x : d.dList){
            if (x.pointer == -1 && x.group == 0) {
                if (x.rule.ini.variable.contentEquals(grammar.initialVar.variable)){
                    return true;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("FormaisUI");
        frame.setContentPane(new FormaisUI().panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void showOnGrammarLoadMessage(){
        if (grammar != null){
            String msg = "A gramática contém "
                    + String.valueOf(grammar.terminals.size()) + " terminais, "
                    + String.valueOf(grammar.rules.size()) + " regras, "
                    + "e começa pela variavel " + String.valueOf(grammar.initialVar.variable);
            JOptionPane.showMessageDialog(panelMain, msg);
        } else {
            String msg = "Gramática não reconhecida";
            JOptionPane.showMessageDialog(panelMain, msg);
        }
    }


}
