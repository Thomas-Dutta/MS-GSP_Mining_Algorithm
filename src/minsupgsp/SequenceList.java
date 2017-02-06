package minsupgsp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Thomas Dutta on 19-09-2015.
 * This class is used to represent a list of sequences, which are read from the input file.
 * It contains an arraylist of Sequence
 * and counter for number of sequence
 */

public class SequenceList {
    private ArrayList<Sequence> arrayOfSequence;
    private int mNumberOfSequence;

    public ArrayList<Sequence> getArrayOfSequence() {
        return arrayOfSequence;
    }

    public void setArrayOfSequence(ArrayList<Sequence> arrayOfSequence) {
        this.arrayOfSequence = arrayOfSequence;
        this.mNumberOfSequence =arrayOfSequence.size();
    }

    public int getNumberOfSequence() {
        return mNumberOfSequence;
    }

    public void setNumberOfSequence(int mNumberOfSequence) {
        this.mNumberOfSequence = mNumberOfSequence;
    }

    public int getItemCount(Integer itemNumber) {
        int count = 0;
        for ( Sequence sequence : arrayOfSequence) {
            if (sequence.containsItem(itemNumber)) {
                count++;
            }
        }
        return count;
    }

    public HashMap<Integer,Integer> getAllDistinctItems () {
        HashMap<Integer,Integer> hmapTotalCountOfItems = new HashMap<>();
        Integer count;
        for(Sequence  sequence : arrayOfSequence) {
            ArrayList <Integer> tempArrayList = sequence.getDistinctItems();
            for ( Integer item : tempArrayList) {
                if (hmapTotalCountOfItems.containsKey(item)) {
                    //increase the count
                    count = hmapTotalCountOfItems.get(item) + 1;
                    //remove the count
                    hmapTotalCountOfItems.remove(item);
                    hmapTotalCountOfItems.put(item, count);
                }
                else{
                    hmapTotalCountOfItems.put(item, 1);
                }
            }
        }
        return hmapTotalCountOfItems;
    }

    public int getSequenceCount(Sequence searchSequence) {
        int count =0;
        for (Sequence sequence : arrayOfSequence) {
            if (sequence.isSuperSequenceOf(searchSequence)) {
                count ++;
            }
        }
        return count;
    }

}
