/******************************************************************************************************************
 * File: Utils.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 * <p>
 * Description:
 * <p>
 * This class contains supporting methods
 *
 ******************************************************************************************************************/

import java.util.ArrayList;

public class Utils {
    /**
     * Method for insertion sort realization
     * @param list - sorted list of frame objects
     * @param element - object that have to be inserted
     * @param sortParam - id of field in frame. We sort by this field
     */
    public static void InsertIntoSortedList(ArrayList<Frame> list, Frame element, int sortParam){
        boolean isInserted = false;

        for (int i=0; i < list.size(); i++){
           if(list.get(i).getData().get(sortParam) > element.getData().get(sortParam)){
               list.add(element);
               isInserted=true;
               break;
           }else {
               continue;
           }
        }

        if (!isInserted) {
            list.add(element);
        }
    }
}
