package com.theo.formais.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by theolm on 10/05/16.
 */
public class StructureD {

    public int groupIndex;
    public List<StructureOne> dList;

    public StructureD(){
        dList = new ArrayList<>();
    }

    public StructureD(int groupIndex, List<StructureOne> structureOneList) {
        this.groupIndex = groupIndex;
        this.dList = structureOneList;

    }
}
