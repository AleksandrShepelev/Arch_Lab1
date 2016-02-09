/******************************************************************************************************************
 * File: DataConverter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 * <p>
 * Description:
 * <p>
 * This class represents the abstract filter responsible for converting any data from one unit to another
 * The binary data comes to the input as a sequence of bytes one-by-one
 * The output data is sent as a sequence of bytes one-by-one
 * <p>
 * To process specific units children classes should implement the method convertData
 ******************************************************************************************************************/

public abstract class DataConverter extends SystemFilter {

    public void run() {

        boolean sourcesExist = true;

        Frame currentFrame;

        while (sourcesExist) {
            /*************************************************************
             * Here we read the data byte by byte and buffer them
             * inside the Frame structure
             * And then convert the data applying necessary data converter
             *
             * We read from all input ports we have until all input pipes are closed
             * only after that we also close other ports and break the while loop
             *
             **************************************************************/

            for (int portNum = 0; portNum < this.getNumberOfOpenedInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                currentFrame = this.readCurrentFrame(portNum);

                if (currentFrame.getData().containsKey(this.getMeasurementId())) {
                    this.convertData(currentFrame);
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

    /**
     * Converts data from one unit to another
     *
     * @param frameToProcess Frame input frame to process
     */
    protected abstract void convertData(Frame frameToProcess);

    /**
     * Returns ID for the measurement data to be converted
     *
     * @return int the ID for the measurement
     */
    protected abstract int getMeasurementId();
}
