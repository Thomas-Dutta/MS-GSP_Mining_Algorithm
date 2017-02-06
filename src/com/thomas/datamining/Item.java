package com.thomas.datamining;

/**
 * Created by Thomas Dutta on 19-09-2015.
 * This class is used to represent an individual item in an itemset
 */
public class Item {
    private int mItemNumber;
    private float mMinimumItemSupport;
    private int mOccurrenceCount;
    private float mSupportValue;

    public Item(Item clone) {
        this.mItemNumber = clone.getItemNumber();
        this.mMinimumItemSupport = clone.getMinimumItemSupport();
        this.mOccurrenceCount = clone.getOccurrenceCount();
        this.mSupportValue = clone.getSupportValue();
    }

    public Item(int itemNumber, float minimumItemSupport, int occurrenceCount, float supportValue) {
        this.mItemNumber = mItemNumber;
        this.mMinimumItemSupport = mMinimumItemSupport;
        this.mOccurrenceCount = mOccurrenceCount;
        this.mSupportValue = mSupportValue;
    }

    public Item(int itemNumber, float minimumItemSupport) {
        this.mItemNumber = itemNumber;
        this.mMinimumItemSupport = minimumItemSupport;
        this.mOccurrenceCount = 0;
        this.mSupportValue = 0.0f;
    }

    public void setItemNumber(int iItemNumber) {
        this.mItemNumber = iItemNumber;
    }

    public int getItemNumber() {
        return mItemNumber;
    }

    public void setMinimumItemSupport(float iMinimumItemSupport) {
        this.mMinimumItemSupport = iMinimumItemSupport;
    }

    public float getMinimumItemSupport() {
        return mMinimumItemSupport;
    }

    public void setOccurrenceCount(int occurrenceCount) {
        this.mOccurrenceCount = occurrenceCount;
    }

    public int getOccurrenceCount() {
        return mOccurrenceCount;
    }

    public void setSupportValue(float supportValue) {
        this.mSupportValue = supportValue;
    }

    public float getSupportValue() {
        return mSupportValue;
    }

    public boolean isHavingGreaterMISValue(Item item) {
        if(mMinimumItemSupport > item.getMinimumItemSupport()) {
            return true;
        }
        return false;
    }
}

