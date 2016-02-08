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

public abstract class DataConverter extends FilterFramework {

    protected static final int measurementLength = 8; // This is the length of all measurements (including time) in bytes
    protected static final int idLength = 4; // This is the length of IDs in the byte stream

    public void run() {
        int bytesRead = 0; // Number of bytes read from the input file.
        int bytesWritten = 0; // Number of bytes written to the stream.

        int currentId = 0; //Current measurement data id (also for time)
        byte dataByte = 0; // This is the data byte read from the stream

        long measurement; // This is the word used to store all measurements - conversions are illustrated.
        int i; // This is a loop counter

        boolean needToConvert = false; // flag states if there is a need to convert the data block

        // Next we write a message to the terminal to let the world know we are alive...
        System.out.print("\n" + this.getName() + "::" + this.getClass().getName() + " Reading ");

        while (true) {
            /*************************************************************
             * Here we read a byte and write a byte
             * if we meet the necessary data ID to be converted than we stop the output
             * convert the data and then continue transmitting to the output
             **************************************************************/

            try {

                currentId = 0;

                //read ID first
                for (i = 0; i < idLength; i++) {
                    dataByte = readFilterInputPort(); // This is where we read the byte from the stream...

                    currentId = currentId | (dataByte & 0xFF); // We append the byte on to ID...

                    // If this is not the last byte, then slide the
                    if (i != idLength - 1) { // previously appended byte to the left by one byte
                        currentId = currentId << 8; // to make room for the next byte we append to the ID
                    } // if

                    bytesRead++; // Increment the byte count

                    //transmit data further to the next filter
                    writeFilterOutputPort(dataByte);
                    bytesWritten++;

                } // for

                needToConvert = (currentId == this.getMeasurementId());

                //very difficult logic: if we don't need to convert the data
                //then just resend byte further
                measurement = 0;
                for (i = 0; i < measurementLength; i++) {
                    dataByte = readFilterInputPort();
                    bytesRead++; // Increment the byte count

                    if (needToConvert) {
                        measurement |= dataByte & 0xFF; // We append the byte on to measurement...

                        // If this is not the last byte, then slide the
                        if (i != measurementLength - 1) { // previously appended byte to the left by one byte
                            measurement = measurement << 8; // to make room for the next byte we append to the
                            // measurement
                        } // if
                    } else {
                        writeFilterOutputPort(dataByte);
                        bytesWritten++;
                    }

                } // if

                // if this is the ID to be converted, then get the data and convert it
                if (currentId == this.getMeasurementId()) {

                    System.out.print("\n" + this.getName() + "::" + this.getClass().getName() + " Converted " +
                            "" + Double.longBitsToDouble(measurement) + " to ");
                    double converted = this.convertData(Double.longBitsToDouble(measurement));

                    System.out.print(converted + "\n");
                    measurement = Double.doubleToLongBits(converted);

                } // if

                // if something was converted, than data should be sent this way
                if (needToConvert) {
                    // send further byte by byte
                    // we should start from higher bites to lower to preserve the order
                    for (i = 0; i < measurementLength; i++) {
                        dataByte = (byte)((measurement >> 8 * (7-i)) & 0xFF);
                        //transmit data further to the next filter
                        writeFilterOutputPort(dataByte);
                        bytesWritten++;
                    }
                } // if

            } catch (EndOfStreamException e) {
                closePorts();
                System.out.print("\n" + this.getName() + "::Middle Exiting; bytes read: " +
                        bytesRead + " bytes written: " + bytesWritten);
                break;
            } // try-catch
        } // while
    } // run

    /**
     * Converts data from one unit to another
     *
     * @param inputValue double input value
     * @return double converted value
     */
    protected abstract double convertData(double inputValue);

    /**
     * Returns ID for the measurement data to be converted
     *
     * @return int the ID for the measurement
     */
    protected abstract int getMeasurementId();
}
