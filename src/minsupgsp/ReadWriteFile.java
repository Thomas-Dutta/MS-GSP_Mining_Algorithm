package minsupgsp;

import minsupgsp.Item;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thomas Dutta on 9/20/15.
 */
public class ReadWriteFile {

    public HashMap<Integer, Item> getParaDetails(String inputParaFileName) {
        File inputParaFile = null;
        HashMap<Integer, Item> hmapItemMetaData = new HashMap<Integer, Item>();
        Item tempItem = null;
        try {
            inputParaFile = new File(inputParaFileName);
            BufferedReader br = new BufferedReader(new FileReader(inputParaFile));
            String line;
            Pattern pattern = Pattern.compile("([a-zA-Z]+\\()(\\d+)(\\)\\s*=\\s*)(.*)");
            Matcher matcher = null;

            while ((line = br.readLine()) != null) {
                //Debug -- print the line
//                System.out.println("Line: " + line);

                matcher = pattern.matcher(line);
                if (matcher.find()) {

                    tempItem = new Item(Integer.valueOf(matcher.group(2)), Float.valueOf(matcher.group(4)));
                    hmapItemMetaData.put(Integer.valueOf(matcher.group(2)), tempItem);

                    //Debug -- print the values
//                    System.out.println("Found value: " + matcher.group(0) + matcher.group(1) + matcher.group(2) + matcher.group(3) + matcher.group(4));

                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(inputParaFile+" File not found exception occurred! " + e);
        } catch (IOException e) {
            System.out.println("I/O exception occurred: " + e);
        }
        return hmapItemMetaData;
    }

    public float getSupportDifferenceConstant(String inputParaFileName) {

        File inputParaFile = null;
        float supportDifferenceConstant = 0.0f;
        Item tempItem = null;
        try {
            inputParaFile = new File(inputParaFileName);
            BufferedReader br = new BufferedReader(new FileReader(inputParaFile));
            String line;
            Pattern pattern = Pattern.compile("([a-zA-Z]+\\s*=\\s*)(.*)");
            Matcher matcher = null;

            while ((line = br.readLine()) != null) {

                //Debug -- print the line
//                System.out.println("Line: " + line);

                if(line.contains("SDC")) {
                    matcher = pattern.matcher(line);
                    if (matcher.find()) {

                        //Debug -- print the values
//                        System.out.println("Found value: " + matcher.group(0) + matcher.group(1) + matcher.group(2));

                        supportDifferenceConstant = Float.valueOf(matcher.group(2));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(inputParaFile+" File not found exception occurred! " + e);
        } catch (IOException e) {
            System.out.println("I/O exception occurred: " + e);
        }
        return supportDifferenceConstant;

    }
    public ArrayList<Sequence> getDataDetails(String inputDataFileName) {

        ArrayList<Sequence> aSequenceMetaData = new ArrayList<Sequence>();

        File inputDataFile = null;
        try {
            inputDataFile = new File(inputDataFileName);
            BufferedReader br = new BufferedReader(new FileReader(inputDataFile));
            String line;
            Pattern pattern = Pattern.compile("(<)(.*?)(>)");
            Matcher matcher = null;

            while ((line = br.readLine()) != null) {
                //Debug -- print the line
//                System.out.println("Line: " + line);
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String sSequence = new String(matcher.group(2).replaceAll("\\s+", ""));
                    //replace all "}{" with ";"  remove { and } completely
                    String tempSSequence = sSequence.replaceAll("\\}\\{", ";").replaceAll("\\{", "").replaceAll("\\}", "");

                    //Debug -- print the trimmed sequence
//                    System.out.println("Method: getDataDetails - TempSequence : " + tempSSequence);

                    ArrayList<Element> tempElementArray = new ArrayList<Element>();
                    for (String sElement : tempSSequence.split(";")) {
                        ArrayList<Integer> tempItemArray = new ArrayList<>();
                        for (String sItem : sElement.split(",")) {
                            tempItemArray.add(Integer.valueOf(sItem));
                        }
                        Element tempElement = new Element(tempItemArray);
                        tempElementArray.add(tempElement);
                    }

                    Sequence tempSequence = new Sequence(tempElementArray);

                    //Debug -- print the trimmed sequence
//                    System.out.println("Method: getDataDetails - Sequence : " + tempSequence.printFormattedSequence());
                    aSequenceMetaData.add(tempSequence);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(inputDataFileName+" File not found exception occurred! " + e);
        } catch (IOException e) {
            System.out.println("I/O exception occurred: " + e);
        }

        return aSequenceMetaData;
    }

    public void writeResultToOutput(String outputFileName, ArrayList<FrequentItemSetList> arrayOfFrequentItemSetList) {
        File outputFile = new File(outputFileName);
        PrintWriter pw = null;
        try {
            int count = 1 ;
            pw = new PrintWriter(outputFile);
            for (FrequentItemSetList frequentItemSetList : arrayOfFrequentItemSetList) {
                if(frequentItemSetList.getArrayOfFrequentItemSet().size() < 1) {break;}
                pw.println("The number of length "+count+" sequential patterns is "+ frequentItemSetList.getArrayOfFrequentItemSet().size());
                pw.println(frequentItemSetList.writeFrequentItemSetList());
//                pw.println("");
                count++;
            }
        } catch (FileNotFoundException e) {
            System.out.println(outputFileName+" File not found exception occurred! " + e);
        } catch (IOException e) {
            System.out.println("I/O exception occurred: " + e);
        } finally {
            pw.close();
        }
    }
}