package minsupgsp;

import java.util.ArrayList;


/**
 * Created by Thomas Dutta on 19-09-2015.
 * This class is used to represent an individual Sequences read from the input file.
 * It contains an arraylist of elements
 * and counter for number of elements and items
 */
public class Sequence {
    protected ArrayList<Element> arrayOfElements;
    protected int mNumberOfElements;      // Number of Elements
    protected int mNumberOfItems;

    public Sequence() {
        this.arrayOfElements = null;
        this.mNumberOfElements = 0;
        this.mNumberOfItems = 0;
    }

    public Sequence(ArrayList<Element> arrayOfElements) {
//        this.arrayOfElements = arrayOfElements;
//        commented to resolve the modification of array of Elements in Level3CandidateGeneration
        this.arrayOfElements = new ArrayList<Element>();
        for (Element element : arrayOfElements ) {
            Element tempElement = new Element(element);
            this.arrayOfElements.add(tempElement);
        }
        this.mNumberOfElements = this.arrayOfElements.size();
        this.mNumberOfItems = 0;
        for (Element element : this.arrayOfElements) {
            mNumberOfItems = mNumberOfItems + element.getArrayOfItems().size();
        }
    }

    public Sequence(Element element) {
        this.arrayOfElements = new ArrayList<Element>();
        Element tempElement = new Element(element);
        this.arrayOfElements.add(tempElement);
        this.mNumberOfElements = this.arrayOfElements.size();
        this.mNumberOfItems = 0;
        for (Element tempElement2 : this.arrayOfElements) {
            mNumberOfItems = mNumberOfItems + tempElement2.getArrayOfItems().size();
        }
    }

    public Sequence(Sequence sequence) {
        this.arrayOfElements = new ArrayList<Element>();
        for (Element element : sequence.getArrayOfElements()) {
            Element tempElement = new Element(element);
            this.arrayOfElements.add(tempElement);
        }
        this.mNumberOfItems=sequence.getNumberOfItems();
        this.mNumberOfElements=sequence.getNumberOfElements();
    }

    public ArrayList<Element> getArrayOfElements() {
        return arrayOfElements;
    }

    public void setArrayOfElements(ArrayList<Element> iItems) {
        this.arrayOfElements = iItems;
    }

    public int getNumberOfElements() {
        return mNumberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.mNumberOfElements = numberOfElements;
    }

    public int getNumberOfItems() {
        this.mNumberOfItems = 0;
        for (Element element : this.arrayOfElements) {
            this.mNumberOfItems = this.mNumberOfItems + element.getArrayOfItems().size();
        }
        return mNumberOfItems;
    }

    public void setNumberOfItems(int mNumberOfItems) {
        this.mNumberOfItems = mNumberOfItems;
    }

    public boolean containsItem(Integer itemNumber) {
        for (Element element : arrayOfElements) {
            if(element.containsItem(itemNumber)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList <Integer> getDistinctItems() {
        ArrayList <Integer> distinctItems = new ArrayList <Integer> ();
        for (Element element : arrayOfElements) {
            for (Integer item : element.getArrayOfItems()) {
                if (!distinctItems.contains(item)) {
                    distinctItems.add(item);
                }
            }
        }
        return distinctItems;
    }

    public boolean isSuperSequenceOf (Sequence subSequence) {

        if (this.getNumberOfElements() < subSequence.getNumberOfElements()) {
            return false;
        }

        for (Element element : subSequence.getArrayOfElements() ) {
            for (Integer item : element.getArrayOfItems()) {
                if(!this.containsItem(item)) {
                    return false;
                }
            }
        }

        int matchedElementCount=0;
        for (int i = 0, j = 0; i < this.mNumberOfElements && j < subSequence.getNumberOfElements(); i++ ) {

            if (this.arrayOfElements.get(i).containsElement(subSequence.getArrayOfElements().get(j))) {
                j++;
                matchedElementCount++;
            }
        }
        if ( matchedElementCount != subSequence.getNumberOfElements())
            return false;

        return true;
    }

    public boolean isEqual(Sequence sequence){
        if(this.mNumberOfElements == sequence.getNumberOfElements()) {
            for(int i=0; i< this.mNumberOfElements; ++i) {
                if(!this.arrayOfElements.get(i).isEqual(sequence.getArrayOfElements().get(i))){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String printFormattedSequence() {
        String tempString = "<";
        for (Element element : this.arrayOfElements) {
            tempString = tempString.concat(element.printFormattedElementItems());
        }
        tempString = tempString.concat(">");
        return tempString;
    }

}