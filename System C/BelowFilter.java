/******************************************************************************************************************
 * File: FilterTemplate.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 *
 * Description:
 *
 * This filter remove data from dataflow that has attitude more or equal then 10K feet
 *
 *
 ******************************************************************************************************************/

public class BelowFilter extends SystemFilter {

    public void run() {
        final int level = 10000;
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

                // this is the frame read from input port
                currentFrame = this.readCurrentFrame(portNum);

                if (currentFrame.getData().containsKey(Frame.ATTITUDE_ID) &&
                        currentFrame.getData().get(Frame.ATTITUDE_ID) < level) {
                    this.transmitCurrentFrame (currentFrame);
                }

                // actually this closes the port
                this.checkInputPortForClose (portNum);

                // checks if we're done here
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
