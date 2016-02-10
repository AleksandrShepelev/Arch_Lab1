/******************************************************************************************************************
 * File: SinkFilter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 09.02.16
 * <p>
 * Description:
 * <p>
 *
 * This is a parent class for any sinks to be used in the system
 * It is responsible for final data representation - it has methods for header creating and formatting data in needed format
 *
 ******************************************************************************************************************/

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.*;
import java.text.SimpleDateFormat;

abstract public class SinkFilter extends SystemFilter {

    /**
     * This methods formats the final representation of the measurements according to the data type (by ID)
     *
     * @param id int packet ID inside the Frame
     * @param measurement double data value inside the packet
     * @return String data representation depending on data type
     */
    protected String convertToOutput(int id, double measurement) {
        Calendar timeStamp = Calendar.getInstance();
        SimpleDateFormat timeStampOutputFormat = new SimpleDateFormat("yyyy:MM:dd:hh:mm:ss");
        DecimalFormat attitudeFormat = new DecimalFormat("000000.00000");
        DecimalFormat tempFormat = new DecimalFormat("000.00000");
        DecimalFormat pressureFormat = new DecimalFormat("00.00000");
        DecimalFormat velocityFormat = new DecimalFormat("000000.00000");
        DecimalFormat bankFormat = new DecimalFormat("000000.00000");
        /*
        time data needs a bit different processing therefore we need to transform them to timeStamp
         */
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

    /**
     * This method returns the header name for each packet inside the frame
     *
     * @param id int the ID of the packet inside the frame
     * @return String header name
     */
    protected String getHeader(int id) {
        switch (id) {
            case Frame.TIME_ID:
                return "Time:";
            case Frame.VELOCITY_ID:
                return "Velocity (sec):";
            case Frame.ATTITUDE_ID:
                return "Attitude(m):";
            case Frame.PRESSURE_ID:
                return "Pressure (PSI):";
            case Frame.TEMPERATURE_ID:
                return "Temperature(C):";
            case Frame.BANK_ID:
                return "Bank(m):";
            default:
                return "Undefined header:";
        }
    }

    /**
     * Returns file name to output the final stream
     *
     * @return String file name to write the output
     */
    protected abstract String getFileName();

    /**
     * @return Default file encoding
     */
    protected String getFileEncoding() {
        return "UTF-8";
    }

    protected abstract int[] getOutputColumns();

    protected abstract void writeFileData (PrintWriter fileWriter, Frame currentFrame);

    public void run() {
        String fileName = this.getFileName(); // Input data file.

        /* here should be put ID's of data that should be output */
        int[] outputColumn = this.getOutputColumns();

        /************************************************************************************
         * timeStamp is used to compute time using java.util's Calendar class. timeStampFormat is
         * used to format the time value so that it can be easily printed to the terminal.
         *************************************************************************************/
        String encoding = this.getFileEncoding();
        PrintWriter fileWriter;

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

        for (Integer anOutputColumn : outputColumn) {
            fileWriter.write(String.format("%-24s", getHeader(anOutputColumn)));
        }

        /*************************************************************
         * First we announce to the world that we are alive...
         **************************************************************/

        System.out.print("\n" + this.getName() + "::Sink Reading ");

        boolean sourcesExist = true;
        Frame currentFrame;

        while (sourcesExist) {
            for (int portNum = 0; portNum < this.getTotalNumberOfInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                currentFrame = this.readCurrentFrame(portNum);

                this.writeFileData(fileWriter, currentFrame);

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

}
