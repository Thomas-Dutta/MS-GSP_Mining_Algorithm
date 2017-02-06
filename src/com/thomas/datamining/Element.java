package com.thomas.datamining;

import java.util.*;

/**
 * Created by Thomas Dutta on 19-09-2015.
 * This class is used to represent an individual element. Each item is represented as an integer.
 */
public class Element {
    private ArrayList<Integer> mArrayOfItems;
    private ArrayList<Item> mArrayOfObjectItems;                //Kept for future use
    private HashMap<Integer, Boolean> mHashMapOfItems;
    private int mNumberOfItems;
    private float mMinValueMIS;
    private float mMaxValueMIS;

    public Element() {
        this.mArrayOfItems = new ArrayList<Integer>();
        this.mArrayOfObjectItems = new ArrayList<Item>();
        this.mHashMapOfItems = new HashMap<Integer, Boolean>();
        this.mNumberOfItems =0;
        mMinValueMIS=0.0f;
        mMaxValueMIS=0.0f;
    }

    public Element(ArrayList<Integer> arrayOfItems) {
        this.mArrayOfItems = new ArrayList<Integer>();
        this.mHashMapOfItems = new HashMap<Integer, Boolean>();
        for (Integer item : arrayOfItems ) {
            this.mArrayOfItems.add(item);
            mHashMapOfItems.put(item, true);
        }
//        Changed to resolve the modification of array of Elements in Level3CandidateGeneration
//        this.mArrayOfItems = arrayOfItems;

        this.mNumberOfItems = this.mArrayOfItems.size();
        mMinValueMIS=0.0f;
        mMaxValueMIS=0.0f;
    }

    public Element(Integer item) {
        this.mArrayOfItems = new ArrayList<Integer>();
        this.mArrayOfItems.add(item);
        mHashMapOfItems = new HashMap<Integer, Boolean>();
        mHashMapOfItems.put(item, true);
        this.mNumberOfItems = this.mArrayOfItems.size();
        mMinValueMIS=0.0f;
        mMaxValueMIS=0.0f;
    }

    public Element(Element element) {
        this.mArrayOfItems = new ArrayList<Integer>();
        this.mHashMapOfItems = new HashMap<Integer, Boolean>();
        for(Integer item : element.getArrayOfItems()){
            this.mArrayOfItems.add(item);
            this.mHashMapOfItems.put(item, true);
        }
        this.mNumberOfItems = element.getNumberOfItems();
        this.mMinValueMIS = element.getMinValueMIS();
        this.mMaxValueMIS = element.getMaxValueMIS();
    }

    public ArrayList<Integer> getArrayOfItems() {
        return mArrayOfItems;
    }

    public void setArrayOfItems(ArrayList<Integer> arrayOfItems) {
        this.mArrayOfItems = arrayOfItems;
        mHashMapOfItems = new HashMap<Integer, Boolean>();
        for (Integer item : this.mArrayOfItems ) {
            mHashMapOfItems.put(item, true);
        }
        this.mNumberOfItems = this.mArrayOfItems.size();
    }

    public int getNumberOfItems() {
        return mNumberOfItems;
    }

    private void setNumberOfItems(int numberOfItems) {
        this.mNumberOfItems = numberOfItems;
    }

    public float getMinValueMIS() {
        return mMinValueMIS;
    }

    public void setMinValueMIS(float mMinValueMIS) {
        this.mMinValueMIS = mMinValueMIS;
    }

    public float getMaxValueMIS() {
        return mMaxValueMIS;
    }

    public void setMaxValueMIS(float mMaxValueMIS) {
        this.mMaxValueMIS = mMaxValueMIS;
    }

    public boolean containsItem(Integer itemNumber) {
        if(mHashMapOfItems.containsKey(itemNumber))
            return true;
        return false;
    }

    public void appendItem(Integer item ) {
        if(!this.containsItem(item)) {
            mArrayOfItems.add(item);
            mHashMapOfItems.put(item, true);
            mNumberOfItems++;
        }
    }

    public boolean isEqual(Element element) {
        if (this.mNumberOfItems == element.mNumberOfItems) {
            for (int i = 0; i < this.mNumberOfItems; ++i ) {
                if (this.mArrayOfItems.get(i) != element.mArrayOfItems.get(i)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public boolean containsElement(Element subElement) {
        if (this.mNumberOfItems >= subElement.mNumberOfItems) {
            for (Integer item: subElement.getArrayOfItems()  ) {
                if(!this.containsItem(item)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String printFormattedElementItems() {

        String tempString = "{";
        for (Integer item : mArrayOfItems) {
            tempString = tempString.concat(item.toString()).concat(",");
        }
        tempString = tempString.substring(0,tempString.length()-1);
        tempString = tempString.concat("}");
        return tempString;
    }

    public Integer getItemByIndex(int position) {
        return mArrayOfItems.get(position - 1);
    }

    public boolean removeItemByIndex(int position) {
        if (position > 0) {
            Integer item = mArrayOfItems.get(position - 1);
            mArrayOfItems.remove(position - 1);
            mHashMapOfItems.remove(item);
            mNumberOfItems = mNumberOfItems - 1;
            return true;
        }
        return false;
    }

    public void inPlaceSort(HashMap<Integer, Item> hmapItemMetaData) {

        ArrayList<SortedDataObjects> tempSortedItemTreeMap = new ArrayList<>();
        for (Integer item : this.getArrayOfItems()) {
            tempSortedItemTreeMap.add(new SortedDataObjects(hmapItemMetaData.get(item).getMinimumItemSupport(), item));
        }
        Collections.sort(tempSortedItemTreeMap);

        this.mArrayOfItems = new ArrayList<Integer>();
        TreeMap<Float, Boolean>  uniqueMISValues = new  TreeMap<Float, Boolean> ();
        ArrayList<Integer> tempArray = new ArrayList<>();

        for (SortedDataObjects sortedItem : tempSortedItemTreeMap) {
            uniqueMISValues.put(sortedItem.getmMinimumItemSupport(), true);
        }
        for(Map.Entry<Float,Boolean> entry : uniqueMISValues.entrySet()) {
            for(SortedDataObjects sortedItem : tempSortedItemTreeMap) {
                if(entry.getKey() == sortedItem.getmMinimumItemSupport()) {
                    tempArray.add(sortedItem.getmItemNumber());
                }
            }
            Collections.sort(tempArray);
            for (Integer item : tempArray) {
                mArrayOfItems.add(item);
            }
            tempArray = new ArrayList<>();

        }
    }
}
