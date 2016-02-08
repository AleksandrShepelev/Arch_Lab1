/******************************************************************************************************************
 * File: SinkFilter.java
 * Course: MSIT-SE-M-04
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions: 1.0 November 2008 - Sample Pipe and Filter code (ajl).
 * <p>
 * Description:
 * <p>
 * This class serves as an example for using the SinkFilterTemplate for creating a sink filter. This
 * particular filter reads some input from the filter's input port and does the following:
 * <p>
 * 1) It parses the input stream and "decommutates" the measurement ID 2) It parses the input steam
 * for measurements and "decommutates" measurements, storing the bits in a long word.
 * <p>
 * This filter illustrates how to convert the byte stream data from the upstream filter into usable
 * data found in the stream: namely time (long type) and measurements (double type).
 * <p>
 * <p>
 * Parameters: None
 * <p>
 * Internal Methods: None
 ******************************************************************************************************************/

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*; // This class is used to interpret time words
import java.text.SimpleDateFormat; // This class is used to format and write time in a string
// format.

public class SinkFilter extends SystemFilter {
    private String convertToOutput(int id, double measurement) {
        Calendar timeStamp = Calendar.getInstance();
        SimpleDateFormat timeStampOutputFormat = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
        DecimalFormat attitudeFormat = new DecimalFormat("000000.00000");
        DecimalFormat tempFormat = new DecimalFormat("000.00000");
        DecimalFormat pressureFormat = new DecimalFormat("00.00000");
        DecimalFormat velocityFormat = new DecimalFormat("000000.00000");
        DecimalFormat bankFormat = new DecimalFormat("000000.00000");
        if (id == Frame.TIME_ID) {
            timeStamp.setTimeInMillis(Double.doubleToLongBits(measurement));
        }

        switch (id) {
            case Frame.TIME_ID:
                return timeStampOutputFormat.format(timeStamp.getTime());
            case Frame.VELOCITY_ID:
                return velocityFormat.format(measurement);
            case Frame.ATTITUDE_ID:
                return attitudeFormat.format(measurement);
            case Frame.PRESSURE_ID:
                return pressureFormat.format(measurement);
            case Frame.TEMPERATURE_ID:
                return tempFormat.format(measurement);
            case Frame.BANK_ID:
                return bankFormat.format(measurement);
            default:
                return Double.toString(measurement);
        }
    }

    public void run() {
        /************************************************************************************
         * timeStamp is used to compute time using java.util's Calendar class. timeStampFormat is
         * used to format the time value so that it can be easily printed to the terminal.
         *************************************************************************************/
        String fileName = "OutputA.dat"; // Input data file.
        String encoding = "UTF-8";
        PrintWriter fileWriter;
        Calendar timeStamp = Calendar.getInstance();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        /* here should be put ID's of data that should be output */
        Integer[] outputColumn = {Frame.TIME_ID, Frame.TEMPERATURE_ID, Frame.ATTITUDE_ID};




        /*here we initialize file reader. If something goes wrong we'll get exception here*/
        try {
            fileWriter = new PrintWriter(fileName, encoding);
        } catch (FileNotFoundException e) {
            System.out.print("\n" + fileName + " is not found or locked by other process");
            return;
        } catch (UnsupportedEncodingException e) {
            System.out.print("\n" + encoding + " is not supported");
            return;
        }

        /*
        TODO:Is header needed in file?
         */

        /*************************************************************
         * First we announce to the world that we are alive...
         **************************************************************/

        System.out.print("\n" + this.getName() + "::Sink Reading ");
        Frame currentFrame = null;
        while (true) {
            try {
                currentFrame = this.readCurrentFrame();
                fileWriter.write("\n");
                for (int i = 0; i < outputColumn.length; i++) {
                    fileWriter.write(convertToOutput(outputColumn[i], currentFrame.data.get(outputColumn[i])) + " ");
                }

            } catch (EndOfStreamException e) {

                /*******************************************************************************
                 * The EndOfStreamExeception below is thrown when you reach end of the input stream
                 * (duh). At this point, the filter ports are closed and a message is written letting
                 * the user know what is going on.
                 ********************************************************************************/
                fileWriter.close();
                closePorts();
                System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesRead);
                break;
            } // catch
        } // while
    } // run
} // SinkFilter
