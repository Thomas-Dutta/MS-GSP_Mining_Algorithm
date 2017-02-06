package com.thomas.datamining;

import java.util.ArrayList;

/**
 * Created by Thomas Dutta on 9/20/15.
 * This class is used to represent a list of FrequentItemSet, (inherited from Sequence class)
 * It is used to represent the output of candidateset and frequent item set at each level
 */

public class FrequentItemSetList {

    private ArrayList<FrequentItemSet> arrayOfFrequentItemSet;
    private int count;

    public FrequentItemSetList() {
        arrayOfFrequentItemSet = new ArrayList<>();
        count = 0;
    }

    public void setArrayOfSequence(ArrayList<FrequentItemSet> arrayOfSequence) {
        this.arrayOfFrequentItemSet = arrayOfSequence;
        this.count =arrayOfSequence.size();
    }

    public ArrayList<FrequentItemSet> getArrayOfFrequentItemSet() {
        return arrayOfFrequentItemSet;
    }

    public void addFrequentItemSet(FrequentItemSet frequentItemSet) {
        this.arrayOfFrequentItemSet.add(frequentItemSet);
        this.count++;
    }

    public void printFrequentItemSetList() {

        for (FrequentItemSet frequentItemSet : arrayOfFrequentItemSet ) {
            System.out.println("Pattern : " +  frequentItemSet.printFormattedFrequentItemSet() + " Count : " + frequentItemSet.getOccurrenceCount());
        }
    }
    public String writeFrequentItemSetList() {
        StringBuffer tempString =  new StringBuffer();
        for (FrequentItemSet frequentItemSet : arrayOfFrequentItemSet ) {
            tempString.append("Pattern : " +  frequentItemSet.printFormattedFrequentItemSet() + "  Count : " + frequentItemSet.getOccurrenceCount());
            tempString.append("\r\n");
        }
        return tempString.toString();
    }

    public boolean containsItem(Integer item) {
        for (FrequentItemSet frequentItemSet : arrayOfFrequentItemSet) {
                if (frequentItemSet.containsItem(item)) {
                    return true;
                }
        }
        return false;
    }
}

////Pattern : <{7}> Count : 2
//Pattern : <{8}> Count : 3