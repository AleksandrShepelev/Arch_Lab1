/******************************************************************************************************************
 * File: MergerFilter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 11.02.16
 * <p>
 * Description:
 * <p>
 * This filter merge data from two dataflows in one. In addition, it sorts data by time in ascending order
 ******************************************************************************************************************/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MergerFilter extends SystemFilter{

    public void run() {
        boolean sourcesExist = true;

        ArrayList<Frame> mergedSortedFrames = new ArrayList<>();

        Frame currentFrame;

        Map<Integer, Boolean> portsToClose = new HashMap<>();

        while (sourcesExist) {
            for (int portNum = 0; portNum < this.getTotalNumberOfInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                currentFrame = this.readCurrentFrame(portNum);
                //collecting and sorting all frames into ArrayList
                InsertIntoSortedList(mergedSortedFrames, currentFrame, Frame.TIME_ID);

                if (this.endOfStreamInPort(portNum)) {
                    portsToClose.put(portNum, true);
                }

                sourcesExist = false;
                for (int j = 0; j < this.getTotalNumberOfInputPorts(); j++) {
                    if (!portsToClose.containsKey(j)) {
                        sourcesExist = true;
                        break;
                    }
                }
            }
        }

        //sending sorted and merged frames to next filter
        mergedSortedFrames.forEach(this::transmitCurrentFrame);

        mergedSortedFrames.clear();

        // close all ports
        for (Map.Entry<Integer, Boolean> entry : portsToClose.entrySet()) {
            this.closeInputPort(entry.getKey());
        }

        System.out.print("\n" + this.getClass().getName() + "::Exiting; bytes read: " +
                bytesRead + " bytes written: " + bytesWritten);

    }

    /**
     * Method for insertion sort realization
     * @param list - sorted list of frame objects
     * @param element - object that have to be inserted
     * @param sortParam - id of field in frame. We sort by this field
     */
    private void InsertIntoSortedList(ArrayList<Frame> list, Frame element, int sortParam){
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
