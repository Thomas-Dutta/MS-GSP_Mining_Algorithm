package com.thomas.datamining;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Thomas Dutta on 9/20/15.
 */
public class GenerateSequences {

    //method level1CandidateGenLPreProcess - sorts item as per MIS value
    public static ArrayList<SortedDataObjects> level1CandidateGenLPreProcess(HashMap<Integer, Item> hmapItemMetaData) {
        ArrayList<SortedDataObjects> tempSortedItemTreeMap = new ArrayList<>();
        for (Map.Entry<Integer, Item> entry : hmapItemMetaData.entrySet()) {
            tempSortedItemTreeMap.add(new SortedDataObjects(entry.getValue().getMinimumItemSupport(), entry.getValue().getItemNumber()));
        }

        //debug
//        for(int i=0 ; i<tempSortedItemTreeMap.size(); ++i) {
//            System.out.println("Method: level1CandidateGenLPreProcess" + "MIS: " + tempSortedItemTreeMap.get(i).getmMinimumItemSupport()
//                    + " Item number: "+ tempSortedItemTreeMap.get(i).getmItemNumber());
//        }
//        System.out.println("Method: level1CandidateGenLPreProcess" + "------------------------------");

        //debug
        Collections.sort(tempSortedItemTreeMap);

        //debug
//        for(int i=0 ; i<tempSortedItemTreeMap.size(); ++i) {
//            System.out.println("Method: level1CandidateGenLPreProcess" + "MIS: " + tempSortedItemTreeMap.get(i).getmMinimumItemSupport()
//                    + " Item number: "+ tempSortedItemTreeMap.get(i).getmItemNumber());
//        }
//        System.out.println("Method: level1CandidateGenLPreProcess" + "------------------------------");
//
//        //manually sort the arraylist
//        ArrayList<SortedDataObjects> tempSortedItemTreeMap2 = new ArrayList<>();
//        for(int i=0 ; i<tempSortedItemTreeMap.size()-1; ++i) {
//            SortedDataObjects tempSortedDataObject = tempSortedItemTreeMap.get(i);
//            int position =0;
//            for(int j=i; j < tempSortedItemTreeMap.size(); ++j) {
//                if(Float.compare(tempSortedDataObject.getmMinimumItemSupport(),tempSortedItemTreeMap.get(j).getmMinimumItemSupport()) > 0 ) {
//                    tempSortedDataObject = tempSortedItemTreeMap.get(j);
//                    position =j;
//                }
//            }
//            if (i != position) {
//                SortedDataObjects tempSortedDataObjectI = new SortedDataObjects(tempSortedItemTreeMap.get(i).getmMinimumItemSupport(),
//                        tempSortedItemTreeMap.get(i).getmItemNumber());
//                SortedDataObjects tempSortedDataObjectJ = new SortedDataObjects(tempSortedItemTreeMap.get(position).getmMinimumItemSupport(),
//                        tempSortedItemTreeMap.get(position).getmItemNumber());
//
//                tempSortedItemTreeMap.remove(i);
//                tempSortedItemTreeMap.add(i, tempSortedDataObjectJ);
//                tempSortedItemTreeMap.remove(position);
//                tempSortedItemTreeMap.add(position, tempSortedDataObjectI);
//            }
//        }

        //Debug -- print the items
//        for(int i=0 ; i<tempSortedItemTreeMap.size(); ++i) {
//            System.out.println("Method: level1CandidateGenLPreProcess" + "MIS: " + tempSortedItemTreeMap.get(i).getmMinimumItemSupport()
//                    + " Item number: "+ tempSortedItemTreeMap.get(i).getmItemNumber());
//        }
        return tempSortedItemTreeMap;
    }


    //method - genLevel1CandidateL : generates the level 1 candidate set
    public static FrequentItemSetList genLevel1CandidateL(HashMap<Integer, Item> hmapItemMetaData,
                                                          ArrayList<SortedDataObjects> sortedItemTreeMap, SequenceList aSequenceMetaData) {

        //get the counts of individual items
        FrequentItemSetList level1CandidateL = new FrequentItemSetList();
        HashMap<Integer, Integer> hmapCountOfAllSingleItem = aSequenceMetaData.getAllDistinctItems();

        Integer itemNumber;
        Element element;
        FrequentItemSet frequentItemSet;
        //get total transaction size
        int inputSize = aSequenceMetaData.getNumberOfSequence();

        //populate the hashmap with the item details
        for ( SortedDataObjects sortedItem : sortedItemTreeMap) {
            if (hmapItemMetaData.containsKey(sortedItem.getmItemNumber())) {
                if(hmapCountOfAllSingleItem.containsKey(sortedItem.getmItemNumber())) {
                    //record the occurrence count and support value
                    hmapItemMetaData.get(sortedItem.getmItemNumber()).setOccurrenceCount(hmapCountOfAllSingleItem.get(sortedItem.getmItemNumber()));
                    hmapItemMetaData.get(sortedItem.getmItemNumber()).setSupportValue(hmapItemMetaData.get(sortedItem.getmItemNumber()).getOccurrenceCount() * 1.0f / inputSize);
                }
            } else {
                System.out.println("Method: genLevel1CandidateL - " + "Inconsistent data in genLevel1CandidateL for Item: " + sortedItem.getmItemNumber());
            }
/*
            itemNumber = entry.getValue();
            element = new Element(itemNumber);
            frequentItemSet = new FrequentItemSet(element);
            frequentItemSet.setCount(hmapItemMetaData.get(entry.getValue()).getItemCount());
            frequentItemSet.setSupportValue(hmapItemMetaData.get(entry.getValue()).getSupportCount());
            level1CandidateL.addFrequentItemSet(frequentItemSet);
*/
        }

        //get the position of the item which has satisfies item.getSupportValue() >= minimumSupport
        float minimumSupport = 1111.0f;
        float startItemMinSup = 0.0f;
        int position = 0;
        for ( SortedDataObjects sortedItem : sortedItemTreeMap) {
            if(hmapCountOfAllSingleItem.containsKey(sortedItem.getmItemNumber())) {
                minimumSupport = Math.min(minimumSupport, sortedItem.getmMinimumItemSupport());
                Item item = hmapItemMetaData.get(sortedItem.getmItemNumber());

                //Need to check if we need to actually implement this stringent condition where the first element meets its mis
                //if (item.getSupportValue() >= minimumSupport) {
                if (Float.compare(minimumSupport, item.getMinimumItemSupport()) <= 0) {
                    startItemMinSup = item.getMinimumItemSupport();
                    break;
                    //remove comment from lines 69 -77 if the position fix does not work
                    //                startItemMinSup = item.getMinimumItemSupport();
                    //                itemNumber = entry.getValue();
                    //                element = new Element(itemNumber);
                    //                frequentItemSet = new FrequentItemSet(element);
                    //                frequentItemSet.setOccurrenceCount(hmapItemMetaData.get(entry.getValue()).getOccurrenceCount());
                    //                frequentItemSet.setSupportValue(hmapItemMetaData.get(entry.getValue()).getSupportValue());
                    //                frequentItemSet.setMinimumSupport((hmapItemMetaData.get(entry.getValue())).getMinimumItemSupport());
                    //                level1CandidateL.addFrequentItemSet(frequentItemSet);
                    //                break;
                }
            }
            position++;
        }

        //do processing for the remaining items
        for (int i = position; i < sortedItemTreeMap.size(); ++i) {
            if(hmapCountOfAllSingleItem.containsKey(sortedItemTreeMap.get(i).getmItemNumber())) {
                SortedDataObjects sortedItem2 = sortedItemTreeMap.get(i);
                Item item = hmapItemMetaData.get(sortedItem2.getmItemNumber());

                //REMOVE comments from line 82 - 91
                //            if (item.getMinimumItemSupport() < startItemMinSup) {
                //                continue;
                //            }
                //            boolean skip = true;
                //            if (skip && (item.getMinimumItemSupport() == startItemMinSup)) {
                //                skip = false;
                //                continue;
                //            }
                //if (item.getSupportValue() >= startItemMinSup) {
                if (Float.compare(item.getSupportValue(),startItemMinSup) >= 0) {
                    itemNumber = sortedItem2.getmItemNumber();
                    element = new Element(itemNumber);
                    frequentItemSet = new FrequentItemSet(element);
                    frequentItemSet.setOccurrenceCount(hmapItemMetaData.get(sortedItem2.getmItemNumber()).getOccurrenceCount());
                    frequentItemSet.setSupportValue(hmapItemMetaData.get(sortedItem2.getmItemNumber()).getSupportValue());
                    frequentItemSet.setMinimumSupport((hmapItemMetaData.get(sortedItem2.getmItemNumber())).getMinimumItemSupport());
                    level1CandidateL.addFrequentItemSet(frequentItemSet);
                }
            }
        }

//        level1CandidateL.printFrequentItemSetList();

        return level1CandidateL;
    }

    //method - genLevel1FrequentItemSet : generates the level 1 Frequent set
    public static FrequentItemSetList genLevel1FrequentItemSet(FrequentItemSetList candidateSet) {

        FrequentItemSetList level1FrequentItemSetF = new FrequentItemSetList();

        //compare the support with individual MIS value
        for (FrequentItemSet frequentItemSet : candidateSet.getArrayOfFrequentItemSet()) {
            //if (frequentItemSet.getSupportValue() >= frequentItemSet.getMinimumSupport()) {
            if (Float.compare(frequentItemSet.getSupportValue(), frequentItemSet.getMinimumSupport()) >= 0 ) {
                level1FrequentItemSetF.addFrequentItemSet(frequentItemSet);
//                System.out.println("Accepted: " + frequentItemSet.printFormattedFrequentItemSet() +
//                        " Count: " + frequentItemSet.getOccurrenceCount() +
//                        " MinMIS: " + frequentItemSet.getMininumSupport() +
//                        " Support: " + frequentItemSet.getActualSupportSupport());
            }
//            else {
//                System.out.println("Rejected: " + frequentItemSet.printFormattedFrequentItemSet() +
//                        " Count: " + frequentItemSet.getOccurrenceCount() +
//                        " MinMIS: " + frequentItemSet.getMininumSupport() +
//                        " Support: " + frequentItemSet.getActualSupportSupport());
//            }
        }

        return level1FrequentItemSetF;
    }

    //method - genLevel2CandidateSet : generates the level 2 Candidate set
    public static FrequentItemSetList genLevel2CandidateSet(FrequentItemSetList level1FrequentItemSetF,
                                                            HashMap<Integer, Item> hmapItemMetaData,
                                                            ArrayList<SortedDataObjects> sortedItemTreeMap,
                                                            SequenceList aSequenceMetaData,
                                                            Float supportDifferenceConstant) {

        //level1FrequentItemSetF is actually candidate set
        FrequentItemSetList level2CandidateSet = new FrequentItemSetList();
        FrequentItemSetList finalLevel2CandidateSet = new FrequentItemSetList();

        ArrayList<Integer> sortedItemListByMISValue = new ArrayList<Integer>();
        // removed as a part of sortedItemTreeMap problem
//        for (Map.Entry<Float, Integer> entry : sortedItemTreeMap.entrySet()) {
//            sortedItemListByMISValue.add(entry.getValue());
//        }

        for (int i = 0; i < sortedItemTreeMap.size(); ++i) {
            Integer itemI = sortedItemTreeMap.get(i).getmItemNumber();
            if (level1FrequentItemSetF.containsItem(itemI)) {
                Element elementI = new Element(itemI);
                // for (int j= i+1; j< sortedItemListByMISValue.size(); ++j ) {
                for (int j = 0; j < sortedItemTreeMap.size(); ++j) {
//                    if (i == j) continue;
                    Integer itemJ = sortedItemTreeMap.get(j).getmItemNumber();
                    if (level1FrequentItemSetF.containsItem(itemJ)) {
                        Element elementJ = new Element(itemJ);
                        ArrayList<Element> tempArrayOfElements = new ArrayList<>(2);
                        tempArrayOfElements.add(elementI);
                        tempArrayOfElements.add(elementJ);
                        //add {x}{y} as separate elements
                        FrequentItemSet frequentItemSet = new FrequentItemSet(tempArrayOfElements);
                        level2CandidateSet.addFrequentItemSet(frequentItemSet);

                        //we can have {x,y} also but here x !=y
                        if (i != j) {
                            ArrayList<Integer> tempArrayOfItems = new ArrayList<>(2);
                            tempArrayOfItems.add(itemI);
                            tempArrayOfItems.add(itemJ);
                            elementJ = new Element(tempArrayOfItems);
                            frequentItemSet = new FrequentItemSet(elementJ);

                            //add as a single element  {x,y}
                            level2CandidateSet.addFrequentItemSet(frequentItemSet);
                        }
                    }
                }
            }
        }

        //Debug
//        level2CandidateSet.printFrequentItemSetList();


        //reorganize the frequent-itemset, check for SDC satisfaction and remove duplicates
        for (FrequentItemSet candidateItemSet : level2CandidateSet.getArrayOfFrequentItemSet()) {
            GenerateSequences.recalculateData(candidateItemSet,hmapItemMetaData);
            //Debug
//            System.out.println(candidateItemSet.printFormattedFrequentItemSet());

            //check for unique items
            if(GenerateSequences.checkIfElementsAreUnique(candidateItemSet)) {

                if(GenerateSequences.isSDCConditionTrue(candidateItemSet,hmapItemMetaData,supportDifferenceConstant)) {
                    if (!GenerateSequences.isFrequentItemSetAlreadyPresent(finalLevel2CandidateSet, candidateItemSet)) {
                        finalLevel2CandidateSet.addFrequentItemSet(candidateItemSet);
                    }
                }
            }
            //Debug SDC false -- comment the else block
//            else {
//
//                int countOfTransactions = aSequenceMetaData.getNumberOfSequence();
//                int count = aSequenceMetaData.getSequenceCount(candidateItemSet);
//                candidateItemSet.setOccurrenceCount(count);
//                float minMIS = GenerateSequences.getMinimumMISValueOfSequence(candidateItemSet, hmapItemMetaData);
//                candidateItemSet.setMinimumSupport(minMIS);
//                float maxMIS = GenerateSequences.getMaximumMISValueOfSequence(candidateItemSet, hmapItemMetaData);
//                float tempSupport = (count * 1.0f) / countOfTransactions;
//                candidateItemSet.setSupportValue(tempSupport);
//
//                System.out.println("SDC false: "+candidateItemSet.printFormattedFrequentItemSet()
//                        +" Count: " + candidateItemSet.getOccurrenceCount()
//                        +" MinMIS: " + candidateItemSet.getMinimumSupport()
//                        +" Support: " + candidateItemSet.getSupportValue()
//                        +" SDC: " + (maxMIS - minMIS)
//                        );
//            }
        }

        //Debug
//        finalLevel2CandidateSet.printFrequentItemSetList();

        return finalLevel2CandidateSet;
    }

    //method - genLevel2FrequentSet : generates the level N Frequent set - generic and can be reused
    public static FrequentItemSetList genLevel2FrequentSet(FrequentItemSetList level2CandidateSet,
                                                           HashMap<Integer, Item> hmapItemMetaData,
                                                           ArrayList<SortedDataObjects> sortedItemTreeMap,
                                                           SequenceList aSequenceMetaData) {

        FrequentItemSetList level2FrequentSet = new FrequentItemSetList();
        int countOfTransactions = aSequenceMetaData.getNumberOfSequence();
        //Debug
        DecimalFormat decimalFormat = new DecimalFormat("#.00");


        /**
         *
         * ADD COPY CONSTRUCTOR for FrequentItemSet and create a new object when adding to FrequentItemSet
         *
         */

        for (FrequentItemSet frequentItemSet : level2CandidateSet.getArrayOfFrequentItemSet()) {
            int count = aSequenceMetaData.getSequenceCount(frequentItemSet);
            frequentItemSet.setOccurrenceCount(count);
            float minMIS = GenerateSequences.getMinimumMISValueOfSequence(frequentItemSet, hmapItemMetaData);
            frequentItemSet.setMinimumSupport(minMIS);
            float maxMIS = GenerateSequences.getMaximumMISValueOfSequence(frequentItemSet, hmapItemMetaData);
            /*
            Need logic to introduce the concept of SDC and add the member variable maxMIS in the    FrequentItemSet Class
             */
            //Debug
            float tempSupport = (count * 1.0f) / countOfTransactions;

            //Debug
//            tempSupport= Float.valueOf(decimalFormat.format(tempSupport));
            frequentItemSet.setSupportValue(tempSupport);
            //      if (tempSupport > minMIS && (maxMIS - minMIS) < 0.05) {

            /* not correct implementation.. need to change this..




            */
            //if(tempSupport >= minMIS) {
            if(Float.compare(tempSupport, minMIS) >= 0) {
//            if (tempSupport > minMIS && Math.abs(hmapItemMetaData.get(sortedItemTreeMap.get(maxMIS)).getSupportValue()
  //                  - hmapItemMetaData.get(sortedItemTreeMap.get(minMIS)).getSupportValue()) < 0.05) {
                level2FrequentSet.addFrequentItemSet(frequentItemSet);
//                   System.out.println("Accepted: " + frequentItemSet.printFormattedFrequentItemSet() +
//                        " Count: " + frequentItemSet.getOccurrenceCount() +
//                        " MinMIS: " + frequentItemSet.getMininumSupport() +
//                        " Support: " + frequentItemSet.getActualSupportSupport());
            }
            //Debug - Comment the else block this is only for debugging
//            else {
//                System.out.println("Rejected: " + frequentItemSet.printFormattedFrequentItemSet() +
//                        " Count: " + frequentItemSet.getOccurrenceCount() +
//                        " MinMIS: " + frequentItemSet.getMinimumSupport() +
//                        " Support: " + frequentItemSet.getSupportValue() +
//                        " SDC: " + (maxMIS - minMIS));
//            }
        }
        return level2FrequentSet;
    }

    //method - genLevelNCandidateSet : generates the level N Candidate set - generic and can be reused
    public static FrequentItemSetList genLevelNCandidateSet(FrequentItemSetList levelN_1FrequentItemSet,
                                                            HashMap<Integer, Item> hmapItemMetaData,
                                                            ArrayList<SortedDataObjects>  sortedItemTreeMap,
                                                            SequenceList aSequenceMetaData,
                                                            float supportDifferenceConstant ) {
        FrequentItemSetList levelNCandidateSet = new FrequentItemSetList();
        FrequentItemSetList finalLevelNCandidateSet = new FrequentItemSetList();

        FrequentItemSet frequentItemSetS1 = new FrequentItemSet();
        FrequentItemSet frequentItemSetS2 = new FrequentItemSet();
        FrequentItemSet tempFrequentItemSet = null;

        for (int i = 0; i < levelN_1FrequentItemSet.getArrayOfFrequentItemSet().size(); ++i) {
            frequentItemSetS1 = levelN_1FrequentItemSet.getArrayOfFrequentItemSet().get(i);
            frequentItemSetS1.populateArrayMIS(hmapItemMetaData);
            for (int j = 0; j < levelN_1FrequentItemSet.getArrayOfFrequentItemSet().size(); ++j) {
                frequentItemSetS2 = levelN_1FrequentItemSet.getArrayOfFrequentItemSet().get(j);
                frequentItemSetS2.populateArrayMIS(hmapItemMetaData);

                //Debug
//                System.out.println(frequentItemSetS1.printFormattedFrequentItemSet() + " ---- " + frequentItemSetS2.printFormattedFrequentItemSet());
//                if (i != j ) {
                    float misValue1 = frequentItemSetS1.getMISValueByPosition(1);
                    float misValue2 = frequentItemSetS2.getMISValueByPosition(frequentItemSetS2.getNumberOfItems());
                    float misValue3 = frequentItemSetS1.getMISValueByPosition(frequentItemSetS1.getNumberOfItems());
                    float misValue4 =frequentItemSetS2.getMISValueByPosition(1);

//                    if (frequentItemSetS1.ifFirstItemHasStrictlyLeastMIS()
//                            && frequentItemSetS1.getMISValueByPosition(1) <= frequentItemSetS2.getMISValueByPosition(frequentItemSetS2.getNumberOfItems())) {
                    if (frequentItemSetS1.ifFirstItemHasStrictlyLeastMIS()
                            && Float.compare(misValue1,misValue2) <= 0 ) {
                        if (frequentItemSetS1.getFrequentItemSetWithoutNthItem(2)
                                .isEqual(frequentItemSetS2.getFrequentItemSetWithoutNthItem(frequentItemSetS2.getNumberOfItems()))) {
//                            System.out.println("Type 1 found");
//                            System.out.println(frequentItemSetS1.printFormattedFrequentItemSet() + "   " + frequentItemSetS2.printFormattedFrequentItemSet());
                            levelNCandidateSet = GenerateSequences.joinFrequentItemSetCondition1(frequentItemSetS1, frequentItemSetS2, levelNCandidateSet);
                        }

//                    } else if (frequentItemSetS1.ifLastItemHasStrictlyLeastMIS()
//                            && frequentItemSetS1.getMISValueByPosition(frequentItemSetS1.getNumberOfItems()) <= frequentItemSetS2.getMISValueByPosition(1)) {
                    } else if (frequentItemSetS1.ifLastItemHasStrictlyLeastMIS()
                            && Float.compare(misValue3, misValue4) <= 0 ) {
//                        System.out.println("Type 2 found");
//                        System.out.println(frequentItemSetS1.printFormattedFrequentItemSet() + "   " + frequentItemSetS2.printFormattedFrequentItemSet());
                        levelNCandidateSet = GenerateSequences.joinFrequentItemSetCondition2(frequentItemSetS1, frequentItemSetS2, levelNCandidateSet);
                    } else {
//                        System.out.println("Type 3 found");
//                        System.out.println(frequentItemSetS1.printFormattedFrequentItemSet() + "   " + frequentItemSetS2.printFormattedFrequentItemSet());
                        levelNCandidateSet = GenerateSequences.joinFrequentItemSetCondition3(frequentItemSetS1, frequentItemSetS2, levelNCandidateSet);
                    }
//                }
            }
        }
        for (FrequentItemSet candidateItemSet : levelNCandidateSet.getArrayOfFrequentItemSet()) {
            ////reorganize the frequent-itemset
            GenerateSequences.recalculateData(candidateItemSet,hmapItemMetaData);
            //check if the items within an elements are unique
            if(GenerateSequences.checkIfElementsAreUnique(candidateItemSet)) {
                //if sdc condition is true
                if (GenerateSequences.isSDCConditionTrue(candidateItemSet, hmapItemMetaData, supportDifferenceConstant)) {
                    //if not already added
                    if (!GenerateSequences.isFrequentItemSetAlreadyPresent(finalLevelNCandidateSet, candidateItemSet)) {
                        finalLevelNCandidateSet.addFrequentItemSet(candidateItemSet);
                    }
                }
            }
        }
        finalLevelNCandidateSet = GenerateSequences.pruneLevelNCandidateSet(levelN_1FrequentItemSet,
                               finalLevelNCandidateSet,hmapItemMetaData,sortedItemTreeMap,aSequenceMetaData);
        return finalLevelNCandidateSet;
    }


    //method - joinFrequentItemSetCondition1 : implement the first join condition
    public static FrequentItemSetList joinFrequentItemSetCondition1(FrequentItemSet frequentItemSetS1,
                                                                    FrequentItemSet frequentItemSetS2, FrequentItemSetList frequentItemSetList) {

        FrequentItemSet frequentItemSet = new FrequentItemSet();

        // if last item l in s2 is a separate element
        if (frequentItemSetS2.getArrayOfElements().get(frequentItemSetS2.getNumberOfElements() - 1).getNumberOfItems() == 1) {

            //if length and size of s1 are both 2
            if (frequentItemSetS1.getNumberOfItems() == 2 && frequentItemSetS1.getNumberOfElements() == 2) {

                //if length and size of s2 are both 2  -- > this condition will always be true based on above conditions
                if (frequentItemSetS2.getNumberOfItems() == 2 && frequentItemSetS2.getNumberOfElements() == 2) {
                    frequentItemSet = new FrequentItemSet(frequentItemSetS1);
                    //add last element of s2 as the last element in s1
                    frequentItemSet.appendElement(frequentItemSetS2.getArrayOfElements().get(frequentItemSetS2.getNumberOfElements() - 1));
                    frequentItemSetList.addFrequentItemSet(frequentItemSet);

                    //20151003 -- added the concept that the last element in s2 must have a greater or
                    // equal value of MIS than last element in s1
                    //add last element of s2 as last item in last element of s1
                    float misValue1 = frequentItemSetS1.getMISValueByPosition(frequentItemSetS1.getNumberOfItems());
                    float misValue2 = frequentItemSetS2.getMISValueByPosition(frequentItemSetS2.getNumberOfItems());

                    if (Float.compare(misValue1,misValue2) < 0) {
                        frequentItemSet = null;
                        frequentItemSet = new FrequentItemSet(frequentItemSetS1);
                        //                    //checking
                        //                    System.out.println("Checking Element Count: " + frequentItemSetS2.getNumberOfElements());
                        //                    System.out.println("Checking Item: " + frequentItemSetS2.getArrayOfElements()
                        //                            .get(frequentItemSetS2.getNumberOfElements() - 1)
                        //                            .getArrayOfItems().get(0));

                        frequentItemSet.appendItemToLastElement(frequentItemSetS2.getArrayOfElements()
                                .get(frequentItemSetS2.getNumberOfElements() - 1)
                                .getArrayOfItems().get(0));

                        frequentItemSetList.addFrequentItemSet(frequentItemSet);
                    }
                }
            } else if ((frequentItemSetS1.getNumberOfItems() == 2 && frequentItemSetS1.getNumberOfElements() == 1)) {
                frequentItemSet = new FrequentItemSet(frequentItemSetS1);
                //append last element of s2 as the last element in s1  <<- Need to verify this
                frequentItemSet.appendElement(frequentItemSetS2.getArrayOfElements().get(frequentItemSetS2.getNumberOfElements() - 1));
                frequentItemSetList.addFrequentItemSet(frequentItemSet);
                //20151003 -- added the concept that the last element in s2 must have a greater or
                // equal value of MIS than last element in s1
                //add last element of s2 as last item in last element of s1
                float misValue1 = frequentItemSetS1.getMISValueByPosition(frequentItemSetS1.getNumberOfItems());
                float misValue2 = frequentItemSetS2.getMISValueByPosition(frequentItemSetS2.getNumberOfItems());

                if (Float.compare(misValue1,misValue2) < 0) {
                    frequentItemSet = null;
                    frequentItemSet = new FrequentItemSet(frequentItemSetS1);
                    //                    //checking
                    //                    System.out.println("Checking Element Count: " + frequentItemSetS2.getNumberOfElements());
                    //                    System.out.println("Checking Item: " + frequentItemSetS2.getArrayOfElements()
                    //                            .get(frequentItemSetS2.getNumberOfElements() - 1)
                    //                            .getArrayOfItems().get(0));

                    frequentItemSet.appendItemToLastElement(frequentItemSetS2.getArrayOfElements()
                            .get(frequentItemSetS2.getNumberOfElements() - 1)
                            .getArrayOfItems().get(0));

                    frequentItemSetList.addFrequentItemSet(frequentItemSet);
                }

            } else {
                frequentItemSet = new FrequentItemSet(frequentItemSetS1);
                //append last element of s2 as the last element in s1
                frequentItemSet.appendElement(frequentItemSetS2.getArrayOfElements().get(frequentItemSetS2.getNumberOfElements() - 1));
                frequentItemSetList.addFrequentItemSet(frequentItemSet);
            }
        } else {            //the last element is s2 is not a separate element
            //append  the last item of s2 as the last item in s1
            frequentItemSet = new FrequentItemSet(frequentItemSetS1);
            frequentItemSet.appendItemToLastElement(frequentItemSetS2.getArrayOfElements()
                    .get(frequentItemSetS2.getNumberOfElements() - 1)
                    .getArrayOfItems().get(frequentItemSetS2.getArrayOfElements()
                            .get(frequentItemSetS2.getNumberOfElements() - 1)
                            .getNumberOfItems()-1));

            frequentItemSetList.addFrequentItemSet(frequentItemSet);
        }

        return frequentItemSetList;
    }

    //method - joinFrequentItemSetCondition2 : implement the second join condition
    public static FrequentItemSetList joinFrequentItemSetCondition2(FrequentItemSet frequentItemSetS1,
                                                                    FrequentItemSet frequentItemSetS2, FrequentItemSetList frequentItemSetList) {


        FrequentItemSet frequentItemSet = new FrequentItemSet();
        //if the first item in s2 is a separate element
        //add first element of s2 before s1
        if(frequentItemSetS2.getArrayOfElements().get(0).getArrayOfItems().size() == 1 ){

            //special case for 2 items
            if (frequentItemSetS1.getNumberOfItems() == 2 ) {
                float misValue1 = frequentItemSetS1.getMISValueByPosition(frequentItemSetS1.getNumberOfItems());
                float misValue2 = frequentItemSetS2.getMISValueByPosition(1);

                //if the first element in s2 is less than the first element of s1
                if (Float.compare(misValue1, misValue2) > 0) {

                    //add the first item of s2 as the first item in s1
                    ArrayList<Integer> aItem =  new ArrayList<Integer>();
                    //get the first item of s2
                    Integer item = frequentItemSetS2.getArrayOfElements().get(0).getArrayOfItems().get(0);
                    aItem.add(item);
                    //add the items from the first element of s1
                    for (Integer tempItem : frequentItemSetS1.getArrayOfElements().get(0).getArrayOfItems()) {
                        aItem.add(tempItem);
                    }
                    Element element = new Element(aItem);
                    frequentItemSet = new FrequentItemSet(element);
                    //add the remaining elements of s1
                    for (int i = 1; i < frequentItemSetS1.getArrayOfElements().size(); ++i ) {
                        Element tempElement = frequentItemSetS1.getArrayOfElements().get(i);
                        Element tempElement2 = new Element(tempElement);
                        frequentItemSet.appendElement(tempElement2);
                    }
                    frequentItemSetList.addFrequentItemSet(frequentItemSet);
                }
            }


            //add as a separate element
            frequentItemSet = new FrequentItemSet(frequentItemSetS2.getArrayOfElements().get(0));
            for (Element element : frequentItemSetS1.getArrayOfElements() ) {
                frequentItemSet.appendElement(element);
            }
            frequentItemSetList.addFrequentItemSet(frequentItemSet);
        } else {
            //the first item in s2 is not a separate element
            //add the first item of s2 as the first item in s1
            ArrayList<Integer> aItem =  new ArrayList<Integer>();
            //get the first item of s2
            Integer item = frequentItemSetS2.getArrayOfElements().get(0).getItemByIndex(1);
            aItem.add(item);
            //add the items from the first element of s1
            for (Integer tempItem : frequentItemSetS1.getArrayOfElements().get(0).getArrayOfItems()) {
                aItem.add(tempItem);
            }
            Element element = new Element(aItem);
            frequentItemSet = new FrequentItemSet(element);
            //add the remaining elements of s1
            for (int i = 1; i < frequentItemSetS1.getArrayOfElements().size(); ++i ) {
                Element tempElement = frequentItemSetS1.getArrayOfElements().get(i);
                Element tempElement2 = new Element(tempElement);
                frequentItemSet.appendElement(tempElement2);
            }
            frequentItemSetList.addFrequentItemSet(frequentItemSet);

        }
        return frequentItemSetList;
    }

    //method - joinFrequentItemSetCondition3 : implement the third join condition
    public static FrequentItemSetList joinFrequentItemSetCondition3(FrequentItemSet frequentItemSetS1,
                                                                FrequentItemSet frequentItemSetS2, FrequentItemSetList frequentItemSetList) {
        FrequentItemSet frequentItemSet = new FrequentItemSet();
        // if last item in s2 is a separate element
        if (frequentItemSetS2.getArrayOfElements().get(frequentItemSetS2.getNumberOfElements() - 1).getNumberOfItems() == 1) {

            //append last element of s2 as the last element in s1
            frequentItemSet = new FrequentItemSet(frequentItemSetS1);
            frequentItemSet.appendElement(frequentItemSetS2.getArrayOfElements().get(frequentItemSetS2.getNumberOfElements() - 1));
            frequentItemSetList.addFrequentItemSet(frequentItemSet);
        } else {
            //the last element is s2 is not a separate element
            //append  the last item of s2 as the last item in s1
            frequentItemSet = new FrequentItemSet(frequentItemSetS1);
            frequentItemSet.appendItemToLastElement(frequentItemSetS2.getArrayOfElements()
                    .get(frequentItemSetS2.getNumberOfElements() - 1)
                    .getArrayOfItems().get(frequentItemSetS2.getArrayOfElements()
                            .get(frequentItemSetS2.getNumberOfElements() - 1)
                            .getNumberOfItems() - 1));

            frequentItemSetList.addFrequentItemSet(frequentItemSet);
        }


        return frequentItemSetList;


    }

    //method - pruneLevelNCandidateSet : prune the candidate sets which are not frequent
    public static FrequentItemSetList pruneLevelNCandidateSet(FrequentItemSetList levelN_1FrequentItemSet,
                                                              FrequentItemSetList levelNCandidateSet,
                                                              HashMap<Integer, Item> hmapItemMetaData,
                                                              ArrayList<SortedDataObjects>  sortedItemTreeMap,
                                                            SequenceList aSequenceMetaData) {

        FrequentItemSetList prunedLevelNCandidateSet = new FrequentItemSetList();

        for (FrequentItemSet frequentItemSet : levelNCandidateSet.getArrayOfFrequentItemSet()) {
            int minMISposition = -1;
            int count = 0;
            float minMIS = 11111.00f;
            for (int i = 0; i < frequentItemSet.getArrayOfElements().size(); ++i) {
                for (int j = 0; j < frequentItemSet.getArrayOfElements().get(i).getArrayOfItems().size(); ++j) {
                    Integer item = frequentItemSet.getArrayOfElements().get(i).getArrayOfItems().get(j);
                    if (minMIS > hmapItemMetaData.get(item).getMinimumItemSupport()) {
                        minMIS = Math.min(hmapItemMetaData.get(item).getMinimumItemSupport(), minMIS);
                        minMISposition = count;
                    }
                    count++;
                }
            }
            count = 0;
            for (int i = 0; i < frequentItemSet.getNumberOfItems(); ++i) {
                if(i != minMISposition) {
                    FrequentItemSet subSequence = frequentItemSet.getFrequentItemSetWithoutNthItem(i+1);

                    //Debug
//                    System.out.println("Sequence: " + frequentItemSet.printFormattedFrequentItemSet()
//                            + " -- Subsequence: " + subSequence.printFormattedFrequentItemSet());

                    for(FrequentItemSet levelN_1Candidate : levelN_1FrequentItemSet.getArrayOfFrequentItemSet()) {
                        if (subSequence.isEqual(levelN_1Candidate)) {
                            //Debug
//                            System.out.println("Match found: " + subSequence.printFormattedFrequentItemSet() + " -- "
//                                            + levelN_1Candidate.printFormattedFrequentItemSet());
                            count++;
                            break;
                        }
                    }
                }
            }
            if(count == frequentItemSet.getNumberOfItems() - 1){
                prunedLevelNCandidateSet.addFrequentItemSet(frequentItemSet);
            }

        }
        return prunedLevelNCandidateSet;
    }

    //method - isSDCConditionTrue : Validate the SDC condition for a given candidate key
    public static boolean isSDCConditionTrue(FrequentItemSet frequentItemSet, HashMap<Integer, Item> hmapItemMetaData, float SDC) {

        frequentItemSet.populateArray();
        frequentItemSet.populateArrayMIS(hmapItemMetaData);

        float minMIS = 11111.00f;
        float maxMIS = 0.00f;
        Item minItem = null;
        Item maxItem = null;

        for (Element element : frequentItemSet.getArrayOfElements()) {
            for (Integer item : element.getArrayOfItems()) {
                if(minMIS >= hmapItemMetaData.get(item).getMinimumItemSupport() ) {
                    if(minMIS > hmapItemMetaData.get(item).getMinimumItemSupport() ) {
                        minMIS = Math.min(hmapItemMetaData.get(item).getMinimumItemSupport(), minMIS);
                        minItem = hmapItemMetaData.get(item);
                    } else {
                        if(minItem.getSupportValue() > hmapItemMetaData.get(item).getSupportValue()) {
                            minItem = hmapItemMetaData.get(item);
                        }
                    }
                }
                if(maxMIS <= hmapItemMetaData.get(item).getMinimumItemSupport()) {
                    if (maxMIS < hmapItemMetaData.get(item).getMinimumItemSupport()) {
                        maxMIS = Math.max(hmapItemMetaData.get(item).getMinimumItemSupport(), maxMIS);
                        maxItem = hmapItemMetaData.get(item);
                    } else {
                        if(maxItem.getSupportValue() < hmapItemMetaData.get(item).getSupportValue()) {
                            maxItem = hmapItemMetaData.get(item);
                        }
                    }
                }
            }
        }
//        for (Element element : frequentItemSet.getArrayOfElements()) {
//            for (Integer item : element.getArrayOfItems()) {
//                if(minMIS > hmapItemMetaData.get(item).getMinimumItemSupport() ) {
//                    minMIS = Math.min(hmapItemMetaData.get(item).getMinimumItemSupport(), minMIS);
//                    minItem = hmapItemMetaData.get(item);
//                }
//                if(maxMIS < hmapItemMetaData.get(item).getMinimumItemSupport()) {
//                    maxMIS = Math.max(hmapItemMetaData.get(item).getMinimumItemSupport(), maxMIS);
//                    maxItem = hmapItemMetaData.get(item);
//                }
//            }
//        }

        DecimalFormat decimalFormat = new DecimalFormat("#.000");
        //Debug -- comment the print statement
//        System.out.println("SDC Check : " + frequentItemSet.printFormattedFrequentItemSet()
//                    + " Max Item: " + maxItem.getItemNumber()
//                    + " Max Support: " + maxItem.getSupportValue()
//                    + " Min Item: " + minItem.getItemNumber()
//                    + " Min Support: " + minItem.getSupportValue()
//                    + " Difference: " + Math.abs(maxItem.getSupportValue() - minItem.getSupportValue())
//                    + "Formatted diff: " + decimalFormat.format(Math.abs(maxItem.getSupportValue() - minItem.getSupportValue())));

//        if(Math.abs(maxItem.getSupportValue() - minItem.getSupportValue()) <= SDC ) {
//        Debug
//        if(Float.valueOf(decimalFormat.format(Math.abs(maxItem.getSupportValue() - minItem.getSupportValue()))) <= SDC ) {
//        if((Float.valueOf(decimalFormat.format(maxItem.getSupportValue())) -
//                Float.valueOf(decimalFormat.format(minItem.getSupportValue()))) <= SDC ) {
        BigDecimal bd1 = new BigDecimal(maxItem.getSupportValue(), MathContext.DECIMAL32);
        BigDecimal bd2 = new BigDecimal(minItem.getSupportValue(), MathContext.DECIMAL32);
        BigDecimal bdSDC = new BigDecimal(SDC, MathContext.DECIMAL32);
        BigDecimal diff = bd1.subtract(bd2);
        diff = diff.abs();

//        System.out.println("SDC Check : " + frequentItemSet.printFormattedFrequentItemSet()
//                + "BigDecimal diff: " + diff);

//        Float diff= Math.abs(maxItem.getSupportValue()- minItem.getSupportValue());
        if (diff.compareTo(bdSDC) <= 0 ) {
//            System.out.println("validated");
            return true;
        }

       return false;
    }


    //method - ifFirstItemHasLowestMIS : If firstItem has LowestMIS value
    public static boolean ifFirstItemHasLowestMIS(FrequentItemSet frequentItemSet,
                                                  HashMap<Integer, Item> hmapItemMetaData) {
        if (hmapItemMetaData.get(frequentItemSet.getArrayRepresentationOfEntireSequence().get(1)).getMinimumItemSupport()
                == frequentItemSet.getMinimumSupport()) {
            return true;
        }
        return false;
    }

    //method - ifLastItemHasLowestMIS : If last item has LowestMIS value
    public static boolean ifLastItemHasLowestMIS(FrequentItemSet frequentItemSet,
                                                 HashMap<Integer, Item> hmapItemMetaData) {
        if (hmapItemMetaData.get(frequentItemSet.getArrayRepresentationOfEntireSequence().get(frequentItemSet.getArrayRepresentationOfEntireSequence().size() - 1)).getMinimumItemSupport()
                == frequentItemSet.getMinimumSupport()) {
            return true;
        }
        return false;
    }

    //method - isFrequentItemSetAlreadyPresent : Checks for equality of frequent Itemset
    public static boolean isFrequentItemSetAlreadyPresent(FrequentItemSetList frequentItemSetList, FrequentItemSet frequentItemSet) {
        for (FrequentItemSet frequentItemSet1 : frequentItemSetList.getArrayOfFrequentItemSet()){
            if(frequentItemSet1.isEqual(frequentItemSet)){
                return true;
            }
        }
        return false;
    }

    //method - recalculateData : sort the elements as per mis values
    public static void recalculateData(FrequentItemSet frequentItemSet, HashMap<Integer, Item> hmapItemMetaData) {
        FrequentItemSet tempFrequentItemSet = new FrequentItemSet();
        for (Element element : frequentItemSet.getArrayOfElements()) {
            if(element.getArrayOfItems().size()>1) {
                element.inPlaceSort(hmapItemMetaData);
            }
        }
        frequentItemSet.populateArray();
        frequentItemSet.populateArrayMIS(hmapItemMetaData);
    }

    //method - getMinimumMISValueOfSequence : get minimum value of a sequence
    public static float getMinimumMISValueOfSequence(FrequentItemSet frequentItemSet,
                                                     HashMap<Integer, Item> hmapItemMetaData) {
        float minMIS = 11111.00f;
        for (Element element : frequentItemSet.getArrayOfElements()) {
            for (Integer item : element.getArrayOfItems()) {
                minMIS = Math.min(hmapItemMetaData.get(item).getMinimumItemSupport(), minMIS);
            }
        }
        return minMIS;
    }

    //method - getMaximumMISValueOfSequence : get maximum value of a sequence
    public static float getMaximumMISValueOfSequence(FrequentItemSet frequentItemSet,
                                                     HashMap<Integer, Item> hmapItemMetaData) {
        float maxMIS = 0.00f;
        for (Element element : frequentItemSet.getArrayOfElements()) {
            for (Integer item : element.getArrayOfItems()) {
                maxMIS = Math.max(hmapItemMetaData.get(item).getMinimumItemSupport(), maxMIS);
            }
        }
        return maxMIS;
    }

    //method - checkIfElementsAreUnique : check if items in an elements are unique
    public static boolean checkIfElementsAreUnique(FrequentItemSet frequentItemSet) {
        HashMap<Integer, Boolean> mHashMapOfItems = new HashMap<>();
        mHashMapOfItems.put(-100,false);
        for (Element element : frequentItemSet.getArrayOfElements()) {
            for(Integer item : element.getArrayOfItems()) {
                if(mHashMapOfItems.containsKey(item)) {
                    return false;
                }
                mHashMapOfItems.put(item,true);
            }
            mHashMapOfItems = new HashMap<>();
            mHashMapOfItems.put(-100,false);
        }
        return true;
    }

}


