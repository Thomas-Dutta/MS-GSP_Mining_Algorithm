package com.thomas.datamining;

/**
 * Created by Thomas on 25-09-2015.
 * This class was created to quickfix the sorting issue.
 * Treemap was failing due to duplicate MIS values.
 */
public class SortedDataObjects implements Comparable<SortedDataObjects> {
    private float mMinimumItemSupport;
    private int mItemNumber;

    public SortedDataObjects() {
        this.mMinimumItemSupport = 0.0f;
        this.mItemNumber = 0;
    }

    public SortedDataObjects(float mMinimumItemSupport, int mItemNumber) {
        this.mMinimumItemSupport = mMinimumItemSupport;
        this.mItemNumber = mItemNumber;
    }

    public float getmMinimumItemSupport() {
        return mMinimumItemSupport;
    }

    public void setmMinimumItemSupport(float mMinimumItemSupport) {
        this.mMinimumItemSupport = mMinimumItemSupport;
    }

    public int getmItemNumber() {
        return mItemNumber;
    }

    public void setmItemNumber(int mItemNumber) {
        this.mItemNumber = mItemNumber;
    }

    @Override
    public int compareTo(SortedDataObjects o) {
        return (int)((this.getmMinimumItemSupport() - o.getmMinimumItemSupport())*10000);
    }
}
