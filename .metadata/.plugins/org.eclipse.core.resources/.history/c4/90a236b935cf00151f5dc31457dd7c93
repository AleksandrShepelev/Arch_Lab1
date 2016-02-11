/******************************************************************************************************************
 * File: DataConverter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 *
 * Description:
 *
 * This class represents the filter responsible for converting extrapolating data of any type
 * The binary data comes to the input as a sequence of bytes one-by-one
 * The output data is sent as a sequence of bytes one-by-one
 *
 * To extrapolate specific units children classes should define the ID from data to be extrapolated
 *
 *
 ******************************************************************************************************************/

public abstract class ExtrapolatorFilter extends SystemFilter {

    public void run() {

        boolean sourcesExist = true;

        while (sourcesExist) {
            /*************************************************************
             * EXTRAPOLATION DESCRIPTION
             **************************************************************/

            for (int portNum = 0; portNum < this.getNumberOfOpenedInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                try {

                    this.readCurrentFrame(portNum);

                    // DO YOUR EXTRAPOLATION HERE !!!

                    this.transmitCurrentFrame (this.currentFrame);

                } catch (EndOfStreamException e) {
                    this.transmitCurrentFrame (this.currentFrame);
                    closeInputPort(portNum);
                    System.out.print("\n" + this.getName() + "::Middle Exiting; bytes read: " +
                            bytesRead + " bytes written: " + bytesWritten);
                    if (this.getNumberOfOpenedInputPorts() < 1) {
                        sourcesExist = false;
                        break;
                    }
                } // try-catch
            }

        } // while
    } // run
}
