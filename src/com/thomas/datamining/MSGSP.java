package com.thomas.datamining;
/**
 * Created by Thomas Dutta on 19-09-2015.
 */
import java.util.*;

public class MSGSP {
    public static void main( String args []) {

        //Read the contents of para.txt, parse the data and save into a HashMap
        final String dir = System.getProperty("user.dir");
//        String inputParaFileName = dir + "/files/para1-1.txt";
        String inputParaFileName = dir + "/files/para1-2.txt";
//       String inputParaFileName = dir + "/files/para2-1.txt";
//        String inputParaFileName = dir + "/files/para2-2.txt";


        ReadWriteFile readWriteFile = new ReadWriteFile();
        HashMap<Integer, Item> hmapItemMetaData = readWriteFile.getParaDetails(inputParaFileName);

        //Debug -- print the contents of the hashmap
//        System.out.println("Method: main - Number of Items read: " + hmapItemMetaData.size());
//        for(Map.Entry<Integer,Item> entry : hmapItemMetaData.entrySet()) {
//            System.out.println("Item No: " + entry.getKey() + " MIS: " + entry.getValue().getMinimumItemSupport());
//        }

        //Extract the SupportDifferenceConstant from para.txt file
        float supportDifferenceConstant = readWriteFile.getSupportDifferenceConstant(inputParaFileName);

        //Debug -- print the value of SupportDifferenceConstant
        System.out.println("Method: main - SupportDifferenceConstant: " + supportDifferenceConstant);

        //Read and parse the contents of data.txt file
        String inputDataFileName = dir + "/files/data-1.txt";
//        String inputDataFileName = dir + "/files/data2.txt";


        SequenceList aSequenceMetaData = new SequenceList();
        aSequenceMetaData.setArrayOfSequence(readWriteFile.getDataDetails(inputDataFileName));

        //Debug -- print the content of the array of sequences
//        for(Sequence sequence : aSequenceMetaData.getArrayOfSequence() ){
//            System.out.println("Method: main - " + sequence.printFormattedSequence());
//        }


        //Generate the frequentItemSets
//        ArrayList<FrequentItemSetList> arrayOfCandidateSet = new ArrayList<FrequentItemSetList>();
//        ArrayList<FrequentItemSetList> arrayOfFrequentItemSetList = new ArrayList<FrequentItemSetList>();

//        //Generate Level1
//        //Generate Candidate set
//        //Preprocess the data, sort according to MIS values
        ArrayList<SortedDataObjects> sortedItemTreeMap = GenerateSequences.level1CandidateGenLPreProcess(hmapItemMetaData);



        //Generate level 1 candidate set
        ArrayList<FrequentItemSetList> arrayOfCandidateSet = new ArrayList<FrequentItemSetList>();
        FrequentItemSetList tempCandidateSet = GenerateSequences.genLevel1CandidateL(hmapItemMetaData,
                sortedItemTreeMap, aSequenceMetaData);
        arrayOfCandidateSet.add(tempCandidateSet);

        //Generate level 1 frequent itemset
        ArrayList<FrequentItemSetList> arrayOfFrequentItemSetList = new ArrayList<FrequentItemSetList>();
        FrequentItemSetList tempFrequentItemSetList1 = GenerateSequences.genLevel1FrequentItemSet(tempCandidateSet);
        arrayOfFrequentItemSetList.add(tempFrequentItemSetList1);

        //now iteratively generate the level n frequent itemset
        for (int i=1 ; arrayOfFrequentItemSetList.get(i-1).getArrayOfFrequentItemSet().size() > 0; ++i ) {

            //in case of level 2 read the input from level 1 candidate set
            if(i==1) {
                tempCandidateSet =  new FrequentItemSetList();
                tempCandidateSet = GenerateSequences.genLevel2CandidateSet(arrayOfCandidateSet.get(i-1),
                        hmapItemMetaData, sortedItemTreeMap, aSequenceMetaData,supportDifferenceConstant);
                arrayOfCandidateSet.add(tempCandidateSet);
                //Debug
//                tempCandidateSet.printFrequentItemSetList();
            } else {
                tempCandidateSet =  new FrequentItemSetList();
                tempCandidateSet = GenerateSequences.genLevelNCandidateSet(arrayOfFrequentItemSetList.get(i - 1),
                        hmapItemMetaData, sortedItemTreeMap, aSequenceMetaData, supportDifferenceConstant);
                arrayOfCandidateSet.add(tempCandidateSet);

            }
            tempFrequentItemSetList1 = new FrequentItemSetList();
            tempFrequentItemSetList1 = GenerateSequences.genLevel2FrequentSet(arrayOfCandidateSet.get(i),
                    hmapItemMetaData, sortedItemTreeMap, aSequenceMetaData);
            arrayOfFrequentItemSetList.add(tempFrequentItemSetList1);
        }

        //write the formatted output to a text file
        String outputFileName = dir + "/files/results.txt";
        readWriteFile.writeResultToOutput(outputFileName, arrayOfFrequentItemSetList);


        //Remove the below comment asap (only the for loop). This has been kept to debug an error
        System.out.println("-------------------------------------------");
        int count = 1;
        for (FrequentItemSetList frequentItemSetList : arrayOfFrequentItemSetList) {
            if(frequentItemSetList.getArrayOfFrequentItemSet().size() < 1) {break;}
            System.out.println("The number of length "+count+" sequential patterns is "+ frequentItemSetList.getArrayOfFrequentItemSet().size());
            frequentItemSetList.printFrequentItemSetList();
            System.out.println("");
            count++;
        }

    }

}

