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

        int currentId = 0; //Current measurement data id (also for time)
        byte dataByte = 0; // This is the data byte read from the stream

        long measurement; // This is the word used to store all measurements - conversions are illustrated.
        int i, j; // This is a loop counter

        boolean needToConvert = false; // flag states if there is a need to convert the data block

        Frame currentFrame = null;

        // Next we write a message to the terminal to let the world know we are alive...
        //System.out.print("\n" + this.getName() + "::" + this.getClass().getName() + " Reading ");

        while (true) {
            /*************************************************************
             * Here we read a byte and write a byte
             * if we meet the necessary data ID to be converted than we stop the output
             * convert the data and then continue transmitting to the output
             **************************************************************/

            try {

                currentFrame = this.readCurrentFrame();

                if (currentFrame.getData().containsKey(this.getMeasurementId())) {
                    System.out.println(this.getClass().getName() + " converted value " + currentFrame.getData().get(this.getMeasurementId()));
                    this.convertData(currentFrame);
                    System.out.print(" to " + currentFrame.getData().get(this.getMeasurementId()));
                }

                this.transmitCurrentFrame (currentFrame);

            } catch (EndOfStreamException e) {
                //closePorts();
                System.out.print("\n" + this.getName() + "::Middle Exiting; bytes read: " +
                        bytesRead + " bytes written: " + bytesWritten);
                break;
            } // try-catch
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
