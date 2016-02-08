/******************************************************************************************************************
 * File: DataConverter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 *
 * Description:
 *
 * This class represents the abstract class for our system specific system to work with our system specific data
 * All other filter implementations should inherit this class
 * It can read the data and buffer it by frames
 * Then it can resend the frame to the next filter
 *
 *
 ******************************************************************************************************************/

import java.util.Map;

public abstract class SystemFilter extends FilterFramework {

    protected static final int measurementLength = 8; // This is the length of all measurements (including time) in bytes
    protected static final int idLength = 4; // This is the length of IDs in the byte stream

    protected int bytesRead = 0; // Number of bytes read from the input file.
    protected int bytesWritten = 0; // Number of bytes written to the stream.


    /**
     *  This method takes as an argument the frame to be transmitted and sends it byte-by-byte starting
     *  from package ID to the next filter
     *  We don't care about the order of the packets inside the frame, therefore we can iterate thorugh the HashMap
     *
     * @param frameToTransmit Frame the frame to be transmitted to the next filter
     */
    protected void transmitCurrentFrame (Frame frameToTransmit) {

        int i;
        byte dataByte;
        long measurement;

        for (Map.Entry<Integer, Double> entry : frameToTransmit.getData().entrySet())
        {
            // send further byte by byte
            // we should start from higher bites to lower to preserve the order
            for (i = 0; i < Frame.ID_LENGTH; i++) {
                dataByte = (byte)((entry.getKey() >> 8 * (Frame.ID_LENGTH-1-i)) & 0xFF);
                //transmit data further to the next filter
                writeFilterOutputPort(dataByte);
                bytesWritten++;
            }

            // send further byte by byte
            // we should start from higher bites to lower to preserve the order
            measurement = Double.doubleToLongBits(entry.getValue());
            for (i = 0; i < Frame.DATA_LENGTH; i++) {
                dataByte = (byte)((measurement >> 8 * (Frame.DATA_LENGTH-1-i)) & 0xFF);
                //transmit data further to the next filter
                writeFilterOutputPort(dataByte);
                bytesWritten++;
            }
        }
    }

    /**
     * This method collects the data from stream byte by byte and puts it into the Frame structure
     * This helps to work with the frame any way we need and want and then resend the data further
     *
     * @return Frame The frame that is built according to the received data
     * @throws EndOfStreamException
     */
    protected Frame readCurrentFrame () throws EndOfStreamException {
        int currentId; //Current measurement data id (also for time)
        byte dataByte; // This is the data byte read from the stream

        long measurement; // This is the word used to store all measurements - conversions are illustrated.
        int i, j; // This is a loop counter

        Frame currentFrame = new Frame();

        for (i = 0; i < Frame.PACKETS; i++) {

            currentId = 0;

            //read ID first
            for (j = 0; j < Frame.ID_LENGTH; j++) {
                dataByte = readFilterInputPort(); // This is where we read the byte from the stream...

                currentId |= dataByte & 0xFF; // We append the byte on to ID...

                // If this is not the last byte, then slide the
                if (j != idLength - 1) { // previously appended byte to the left by one byte
                    currentId = currentId << 8; // to make room for the next byte we append to the ID
                } // if

                bytesRead++; // Increment the byte count

            } // for

            // read the measurement
            measurement = 0;
            for (j = 0; j < Frame.DATA_LENGTH; j++) {
                dataByte = readFilterInputPort();
                bytesRead++; // Increment the byte count

                measurement |= dataByte & 0xFF; // We append the byte on to measurement...

                // If this is not the last byte, then slide the
                if (j != measurementLength - 1) { // previously appended byte to the left by one byte
                    measurement = measurement << 8; // to make room for the next byte we append to the
                    // measurement
                } // if
            } // if
            currentFrame.getData().put(currentId, Double.longBitsToDouble(measurement));
        }

        return currentFrame;
    }

}
