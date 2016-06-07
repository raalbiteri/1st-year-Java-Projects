/**
 * CS2852- 031
 * Spring 2016
 * Lab 3 - Connect the Dots Generator Revisited
 * Name: Raunel Albiter
 * Email: albiterri@msoe.edu
 * modified: 4/05/2016
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Keeps track of the dots from file that ultimately is
 * formed into a shape either as just dots or through lines
 * @author albiterri
 * @version 1.0
 */
public class Shape{

    private List<Dot> dotList = new ArrayList<>();

    public int getNumberOfDots() {

        return dotList.size();
    }

    public List<Dot> getDotList() {

        return dotList;
    }

    /**
     * Constructor for creating a shape from dots
     */
    public Shape(){

    }

    /**
     * Clears out previous file dot data.
     * Reads in point count from file which holds a
     * horizontal and vertical component separated by a comma.
     * Both components are floats between 0 and 1.
     * @param chosenFile The file that has the .pnt coordinates
     * @throws FileNotFoundException If file could not be found
     */
    public void pointReadIn(File chosenFile) throws FileNotFoundException {

        dotList.clear();
        Scanner scanFile = new Scanner(chosenFile);
        while(scanFile.hasNext()) {

            String tempPointHolder = scanFile.nextLine();
            String[] coordinatesInList = tempPointHolder.split(",");
            Dot newDot = new Dot(coordinatesInList[0], coordinatesInList[1]);
            addToDotList(newDot);
        }
        scanFile.close();
    }

    /**
     * Accepts a list of dots, original, and copies the dots into a list,
     * result, that starts out empty.  The method then uses the technique described
     * in the lab assignment to remove all but the numDesired number of dots.
     * <br />
     * If fewer than numDesired dots are found in original, then a copy of all
     * the dots in original is returned.
     * @param original The list of dots read in from the data file
     * @param result An empty list that will contain the numDesired most critical
     *        dots from the original list
     * @param numDesired The number of dots desired in the resulting list, must be
     *        at least 3
     * @return The number of nanoseconds required to execute
     */
    public static long getDesiredDots2(List<Dot> original, Collection<Dot> result, int numDesired){

        long startTime = System.currentTimeMillis();
        final int MINIMUM_DESIRED_DOTS = 3;

        if(original == null || original.size() <= MINIMUM_DESIRED_DOTS
                || numDesired < MINIMUM_DESIRED_DOTS) {
            throw new IllegalArgumentException();
        }

        copyDotsInList(result, original);
        List<Float> critValList = new ArrayList<>();

        while(result.size() != numDesired) {

            critValList.clear();
            Dot firstDotInList = (Dot) result.toArray()[0];
            Dot secondDotInList = (Dot) result.toArray()[1];
            Dot lastDotInList = (Dot) result.toArray()[result.size() - 1];
            Dot secondToLastDotInList = (Dot) result.toArray()[result.size() - 2];

            Dot dotOne;
            Dot dotTwo;
            Dot dotThree;

            for(int i = 0; i < result.size() - 2; i++){

                dotOne = (Dot) result.toArray()[i];
                dotTwo = (Dot) result.toArray()[i + 1];
                dotThree = (Dot) result.toArray()[i + 2];
                calculateAndStoreCritVal(dotOne, dotTwo, dotThree, critValList);
            }

            //Case where I had to compare the second to last, last, and
            //first dot in list
            dotOne = (Dot) result.toArray()[findIndexOfDot(secondToLastDotInList, result)];
            dotTwo = (Dot) result.toArray()[findIndexOfDot(lastDotInList, result)];
            dotThree = (Dot) result.toArray()[findIndexOfDot(firstDotInList, result)];
            calculateAndStoreCritVal(dotOne, dotTwo, dotThree, critValList);

            //Case where I had to compare last, first, and
            //second dot in list
            dotOne = (Dot) result.toArray()[findIndexOfDot(lastDotInList, result)];
            dotTwo = (Dot) result.toArray()[findIndexOfDot(firstDotInList, result)];
            dotThree = (Dot) result.toArray()[findIndexOfDot(secondDotInList, result)];
            calculateAndStoreCritVal(dotOne, dotTwo, dotThree, critValList);

            collectRemoveLowCritVal(critValList, result);
        }

        return (System.currentTimeMillis() - startTime);
    }

    /**
     * Accepts a list of dots, original, and copies the dots into a list,
     * result, that starts out empty.  The method then uses the technique described
     * in the lab assignment to remove all but the numDesired number of dots.
     * <br />
     * If fewer than numDesired dots are found in original, then a copy of all
     * the dots in original is returned.
     * @param original The list of dots read in from the data file
     * @param result An empty list that will contain the numDesired most critical
     *        dots from the original list
     * @param numDesired The number of dots desired in the resulting list, must be
     *        at least 2
     * @return The number of nanoseconds required to execute
     */
    public static long getDesiredDots(List<Dot> original, List<Dot> result, int numDesired){

        long startTime = System.currentTimeMillis();
        final int MINIMUM_DESIRED_DOTS = 3;

        if(original == null || original.size() <= MINIMUM_DESIRED_DOTS
                || numDesired < MINIMUM_DESIRED_DOTS) {
            throw new IllegalArgumentException();
        }

        copyDotsInList(result, original);
        List<Float> critValList = new ArrayList<>();

        while(result.size() != numDesired) {

            critValList.clear();
            Dot firstDotInList = result.get(0);
            Dot secondDotInList = result.get(1);
            Dot lastDotInList = result.get(result.size() - 1);
            Dot secondToLastDotInList = result.get(result.size() - 2);

            Dot dotOne;
            Dot dotTwo;
            Dot dotThree;

            for(int i = 0; i < result.size() - 2; i++){

                dotOne = result.get(i);
                dotTwo = result.get(i + 1);
                dotThree = result.get(i + 2);
                calculateAndStoreCritVal(dotOne, dotTwo, dotThree, critValList);
            }

            //Case where I had to compare the second to last, last, and
            //first dot in list
            dotOne = result.get(result.indexOf(secondToLastDotInList));
            dotTwo = result.get(result.indexOf(lastDotInList));
            dotThree = result.get(result.indexOf(firstDotInList));
            calculateAndStoreCritVal(dotOne, dotTwo, dotThree, critValList);

            //Case where I had to compare last, first, and
            //second dot in list
            dotOne = result.get(result.indexOf(lastDotInList));
            dotTwo = result.get(result.indexOf(firstDotInList));
            dotThree = result.get(result.indexOf(secondDotInList));
            calculateAndStoreCritVal(dotOne, dotTwo, dotThree, critValList);

            listRemoveLowCritVal(critValList, result);
        }
        return (System.currentTimeMillis() - startTime);
    }

    /**
     * Helps to copy over all dot data from original list
     * to another in order to be used for modification
     * @param destList Destination of items in list
     * @param sourceList Source where items are being copied from
     */
    public static void copyDotsInList(Collection<Dot> destList, List<Dot> sourceList) {

        for(Dot d: sourceList) {
            destList.add(d);
        }
    }

    /**
     * Helps to remove lowest critical value according to lab
     * algorithm
     * @param cValList Critical Value list
     * @param resultList List to be modified fewer data points
     */
    private static void listRemoveLowCritVal(List<Float> cValList, List<Dot> resultList) {

        float lowestVal = 1;
        final float CRITICAL_VALUE = 0;
        int removeAtIndex = 0;
        for(Float fT: cValList) {
            if(fT < lowestVal && fT != CRITICAL_VALUE) {
                lowestVal = fT;
                removeAtIndex = cValList.indexOf(fT);
            }
        }
        cValList.remove(removeAtIndex);

        //Since I first determined the critical value of dot at
        //index #1, I prevented removal of a null object with this.
        if(removeAtIndex == resultList.size() - 1) {
            resultList.remove(resultList.size() - 1);
        } else {
            resultList.remove(removeAtIndex + 1);
        }
    }

    private static void collectRemoveLowCritVal(List<Float> cValList, Collection<Dot> resultList) {

        float lowestVal = 1;
        final float CRITICAL_VALUE = 0;
        int removeAtIndex = 0;
        for(Float fT: cValList) {
            if(fT < lowestVal && fT != CRITICAL_VALUE) {
                lowestVal = fT;
                removeAtIndex = cValList.indexOf(fT);
            }
        }
        cValList.remove(removeAtIndex);

        //Since I first determined the critical value of dot at
        //index #1, I prevented removal of a null object with this.
        if(removeAtIndex == resultList.size() - 1) {
            resultList.remove(resultList.remove(resultList.toArray()[resultList.size() - 1]));
        } else {
            resultList.remove(resultList.remove(resultList.toArray()[removeAtIndex + 1]));
        }
    }

    /**
     * Helps to calculate and store the Critical value of dotTwo based
     * on neighbor dots one and three
     * @param dotOne First dot that is compared for finding critical value
     * @param dotTwo Second dot that is compared for finding critical value
     * @param dotThree Third dot that is compared for finding critical value
     * @param cValList List where critical values are stored to be compared later
     */
    private static void calculateAndStoreCritVal(Dot dotOne, Dot dotTwo,
                                                 Dot dotThree, List<Float> cValList) {

        float distanceTwoToOne = (float) Math.sqrt(Math.pow(dotOne.getXComponent() -
                dotTwo.getXComponent(), 2) + Math.pow(dotOne.getYComponent() -
                dotTwo.getYComponent(), 2));
        float distanceTwoToThree = (float) Math.sqrt(Math.pow(dotThree.getXComponent() -
                dotTwo.getXComponent(), 2) + Math.pow(dotThree.getYComponent() -
                dotTwo.getYComponent(), 2));
        float distanceOneToThree = (float) Math.sqrt(Math.pow(dotThree.getXComponent() -
                dotOne.getXComponent(), 2) + Math.pow(dotThree.getYComponent() -
                dotOne.getYComponent(), 2));

        cValList.add(distanceTwoToOne + distanceTwoToThree - distanceOneToThree);
    }

    private static int findIndexOfDot(Dot desiredDot, Collection<Dot> sourceList) {

        Iterator<Dot> itemIterator = sourceList.iterator();
        int indexCount = 0;
        while(itemIterator.hasNext()) {
            if(itemIterator.next() == desiredDot) {
                break;
            }
            indexCount++;
        }
        return indexCount;
    }

    /**
     * Used to add dot to list of dots
     * @param someDot Dot to be added
     */
    private void addToDotList(Dot someDot) {

        dotList.add(someDot);
    }
}
