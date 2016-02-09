/******************************************************************************************************************
 * File: SystemFilter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 * <p>
 * Description:
 * <p>
 * This class represents the abstract class for our system specific system to work with our system specific data
 * All other filter implementations should inherit this class
 * It can read the data and buffer it by frames
 * Then it can resend the frame to the next filter
 ******************************************************************************************************************/

import java.util.HashMap;
import java.util.Map;

public abstract class SystemFilter extends FilterFramework {

    protected static final int measurementLength = 8; // This is the length of all measurements (including time) in bytes
    protected static final int idLength = 4; // This is the length of IDs in the byte stream

    protected int bytesRead = 0; // Number of bytes read from the input file.
    protected int bytesWritten = 0; // Number of bytes written to the stream.

    private Map<Integer, Frame> nextFrames = new HashMap<>();

    private Frame nextFrame = null; // Next frame container...we need it when we stop reading to the first and return the data

    protected Integer portToClose = null; // contains the port that should be closed now

    /**
     *  This method takes as an argument the frame to be transmitted and sends it byte-by-byte starting
     *  from package ID to the next filter
     *  We don't care about the order of the packets inside the frame, therefore we can iterate thorugh the HashMap
     *
     * @param frameToTransmit Frame the frame to be transmitted to the next filter
     */
    protected void transmitCurrentFrame(Frame frameToTransmit) {

        int i;
        byte dataByte;
        long measurement;

        for (Map.Entry<Integer, Double> entry : frameToTransmit.getData().entrySet()) {
            // send further byte by byte
            // we should start from higher bites to lower to preserve the order
            for (i = 0; i < Frame.ID_LENGTH; i++) {
                dataByte = (byte) ((entry.getKey() >> 8 * (Frame.ID_LENGTH - 1 - i)) & 0xFF);
                //transmit data further to the next filter
                writeFilterOutputPortsAll(dataByte);
                bytesWritten++;
            }

            // send further byte by byte
            // we should start from higher bites to lower to preserve the order
            measurement = Double.doubleToLongBits(entry.getValue());
            for (i = 0; i < Frame.DATA_LENGTH; i++) {
                dataByte = (byte) ((measurement >> 8 * (Frame.DATA_LENGTH - 1 - i)) & 0xFF);
                //transmit data further to the next filter
                writeFilterOutputPortsAll(dataByte);
                bytesWritten++;
            }
        }
    }

    /**
     * This method collects the data from stream byte by byte and puts it into the Frame structure
     * This helps to work with the frame any way we need and want and then resend the data further
     *
     * @return Frame The frame that is built according to the received data
     */
    protected Frame readCurrentFrame(int portNumber) {
        int currentId; //Current measurement data id (also for time)

        long measurement; // This is the word used to store all measurements - conversions are illustrated.

        Frame currentFrame = new Frame();

        Frame nextFrame;
        if (!nextFrames.containsKey(portNumber)) {
            nextFrames.put(portNumber, null);
        }

        nextFrame = nextFrames.get(portNumber);

        if (nextFrame != null) {
            currentFrame = Frame.copyFrom(nextFrame);
        }

        while (true) {

            try {

                // read ID first
                currentId = this.getPacketId(portNumber);

                // read the measurement data
                measurement = this.getPacketData(portNumber);

                //
                if (currentId == Frame.TIME_ID) {
                    if (nextFrame != null) {
                        // pack data
                        nextFrame.getData().put(currentId, Double.longBitsToDouble(measurement));
                        return currentFrame;
                    } else {
                        nextFrame = new Frame();
                        nextFrames.put (portNumber, nextFrame);
                    }
                }

                currentFrame.getData().put(currentId, Double.longBitsToDouble(measurement));

            } catch (EndOfStreamException e) {
                //fire event for closing port outside!
                this.portToClose = portNumber;

                return currentFrame;
            } // try-catch
        }
    }

    /**
     * @TODO Comments
     * @param portNum
     */
    protected void checkInputPortForClose(int portNum) {
        if (this.endOfStreamInPort(portNum)) {
            System.out.print("\n" + this.getClass().getName() + " closing port " + portNum);
            this.portToClose = null;
            this.closeInputPort(portNum);
        }
    }

    /**
     * @TODO Comments
     *
     * @param portNum
     * @return
     */
    protected boolean endOfStreamInPort(int portNum) {
        return this.portToClose != null && this.portToClose == portNum;
    }

    /**
     * This methods reads the packet id inside the frame
     *
     * @param portNumber int the port number for the stream to read data from
     * @return int the ID for the packet
     * @throws EndOfStreamException
     */
    private int getPacketId(int portNumber) throws EndOfStreamException {

        int currentId = 0; //Current measurement data id (also for time)
        byte dataByte; // This is the data byte read from the stream

        int i; // This is a loop counter

        //read ID first
        for (i = 0; i < Frame.ID_LENGTH; i++) {
            dataByte = readFilterInputPort(portNumber); // This is where we read the byte from the stream...

            currentId |= dataByte & 0xFF; // We append the byte on to ID...

            // If this is not the last byte, then slide the
            if (i != idLength - 1) { // previously appended byte to the left by one byte
                currentId = currentId << 8; // to make room for the next byte we append to the ID
            } // if

            bytesRead++; // Increment the byte count

        } // for

        return currentId;
    }

    /**
     * This method reads data from the stream byte by byte for the packet data
     *
     * @param portNumber int number of input port to read data from
     * @return long data in the packet represented in long bits
     * @throws EndOfStreamException
     */
    private long getPacketData(int portNumber) throws EndOfStreamException {

        byte dataByte; // This is the data byte read from the stream

        long measurement = 0; // This is the word used to store all measurements - conversions are illustrated.
        int i; // This is a loop counter

        //read ID first
        for (i = 0; i < Frame.DATA_LENGTH; i++) {
            dataByte = readFilterInputPort(portNumber);
            bytesRead++; // Increment the byte count

            measurement |= dataByte & 0xFF; // We append the byte on to measurement...

            // If this is not the last byte, then slide the
            if (i != measurementLength - 1) { // previously appended byte to the left by one byte
                measurement = measurement << 8; // to make room for the next byte we append to the
                // measurement
            } // if

        } // for

        return measurement;
    }

}
