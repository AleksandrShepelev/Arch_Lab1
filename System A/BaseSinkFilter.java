/******************************************************************************************************************
 * File: BaseSinkFilter.java
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

public abstract class BaseSinkFilter extends SystemFilter {

    protected abstract String convertToOutput(int id, double measurement);

    protected abstract String getHeader(int id);
    
    public void run() {
        /************************************************************************************
         * timeStamp is used to compute time using java.util's Calendar class. timeStampFormat is
         * used to format the time value so that it can be easily printed to the terminal.
         *************************************************************************************/
        String fileName = getFileName(); // Input data file.
        String encoding = "UTF-8";
        PrintWriter fileWriter;
        Calendar timeStamp = Calendar.getInstance();
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy MM dd::hh:mm:ss:SSS");

        /* here should be put ID's of data that should be output */
        int[] outputColumn = getOutputColumns();

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

        for (Integer anOutputColumn1 : outputColumn) {
            fileWriter.write(String.format("%-24s", getHeader(anOutputColumn1)));
        }

        /*************************************************************
         * First we announce to the world that we are alive...
         **************************************************************/

        System.out.print("\n" + this.getName() + "::Sink Reading ");

        boolean sourcesExist = true;
        Frame currentFrame;

        while (sourcesExist) {
            for (int portNum = 0; portNum < this.getNumberOfOpenedInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                currentFrame = this.readCurrentFrame(portNum);

                fileWriter.write("\n");
                for (Integer anOutputColumn : outputColumn) {
                    fileWriter.write(String.format("%-24s", convertToOutput(anOutputColumn, currentFrame.getData().get(anOutputColumn))));
                }

                this.checkInputPortForClose(portNum);

                if (this.getNumberOfOpenedInputPorts() < 1) {
                    System.out.print("\n" + this.getName() + "::Sink Exiting; bytes read: " + bytesRead);
                    fileWriter.close();
                    sourcesExist = false;
                    break;
                }

            }
        } // while
    } // run

    protected abstract String getFileName();

    protected abstract int[] getOutputColumns();
} // BaseSinkFilter
