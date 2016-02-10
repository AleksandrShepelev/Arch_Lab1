/******************************************************************************************************************
 * File: FrameFilter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 * <p>
 * Description:
 * <p>
 * This filter removes unnececary data from dataflow
 ******************************************************************************************************************/

public class FrameFilter extends SystemFilter {
    /*
    in filterElements array determined id-s of elements that should be skipped
    */
    private int[] filterElements = new int[]{Frame.BANK_ID, Frame.EXTRAPOLATED_PRESSURE, Frame.VELOCITY_ID, Frame.PRESSURE_ID};

    public void run() {

        boolean sourcesExist = true;
        Frame currentFrame;

        while (sourcesExist) {
            /*************************************************************
             * Here we read the data byte by byte and buffer them
             * inside the Frame structure
             *
             **************************************************************/

            for (int portNum = 0; portNum < this.getTotalNumberOfInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                currentFrame = this.readCurrentFrame(portNum);

                for (int element : this.filterElements) {
                    if (currentFrame.getData().containsKey(element)) {
                        currentFrame.getData().remove(element);
                    }
                }

                this.transmitCurrentFrame(currentFrame);

                this.checkInputPortForClose(portNum);

                if (this.getNumberOfOpenedInputPorts() < 1) {

                    System.out.print("\n" + this.getClass().getName() + "::Exiting; bytes read: " +
                            bytesRead + " bytes written: " + bytesWritten);

                    sourcesExist = false;
                    break;
                }
            }

        } // while
    } // run
}
