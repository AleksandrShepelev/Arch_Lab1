/******************************************************************************************************************
 * File: DataConverter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 *
 * Description:
 *
 * This class represents the abstract filter responsible for converting any data from one unit to another
 * The binary data comes to the input as a sequence of bytes one-by-one
 * The output data is sent as a sequence of bytes one-by-one
 *
 * To process specific units children classes should implement the method convertData
 *
 *
 ******************************************************************************************************************/

public abstract class DataConverter extends SystemFilter {

    public void run() {

        Frame currentFrame;

        while (true) {
            /*************************************************************
             * Here we read the data byte by byte
             * Buffer inside the Frame structure
             * And then convert the data applying necessary filter
             **************************************************************/

            for (int portNum = 0; portNum < this.getNumberOfInputPorts(); portNum++) {
                try {

                    currentFrame = this.readCurrentFrame(portNum);

                    if (currentFrame.getData().containsKey(this.getMeasurementId())) {
                        System.out.println(this.getClass().getName() + " converted value " + currentFrame.getData().get(this.getMeasurementId()));
                        this.convertData(currentFrame);
                        System.out.println(" to " + currentFrame.getData().get(this.getMeasurementId()));
                    }

                    this.transmitCurrentFrame (currentFrame);

                } catch (EndOfStreamException e) {
                    closeInputPort(portNum);
                    System.out.print("\n" + this.getName() + "::Middle Exiting; bytes read: " +
                            bytesRead + " bytes written: " + bytesWritten);
                    break;
                } // try-catch
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
