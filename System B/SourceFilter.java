/******************************************************************************************************************
 * File: SourceFilter.java
 * Course: MSIT-SE-M-04
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions: 1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *           1.1 February 2016 -  SKB Kontur Team (MSIT SE) - adopting of source filter to be used with different source files
 * <p>
 * Description:
 *This particular filter is a source filter that reads input from the FlightData.dat
 * file and writes the bytes up stream.
 * <p>
 * Parameters: name of file to read - optionally
 * <p>
 * Internal Methods: None
 *
 *
 ******************************************************************************************************************/

import java.io.*; // note we must add this here since we use BufferedReader class to read from the
// keyboard

public class SourceFilter extends FilterFramework {
    private String fileName = "FlightData.dat";

    /**
     * empty constructor source file for filter will be FlightData
     */
    public SourceFilter() {

    }

    /**
     * With non-empty constructor we might read from custom file
     *
     * @param fileName String source file name
     */
    public SourceFilter(String fileName) {
        this.fileName = fileName;
    }

    public void run() {
        int bytesRead = 0; // Number of bytes read from the input file.
        int bytesWritten = 0; // Number of bytes written to the stream.
        DataInputStream in = null; // File stream reference.
        byte dataByte = 0; // The byte of data read from the file

        try {
            /***********************************************************************************
             * Here we open the file and write a message to the terminal.
             ***********************************************************************************/

            in = new DataInputStream(new FileInputStream(fileName));
            System.out.println("\n" + this.getName() + "::Source reading file...");

            /***********************************************************************************
             * Here we read the data from the file and send it out the filter's output port one byte
             * at a time. The loop stops when it encounters an EOFExecption.
             ***********************************************************************************/

            while (true) {
                dataByte = in.readByte();
                bytesRead++;
                writeFilterOutputPortsAll(dataByte);
                bytesWritten++;
            } // while

        } catch (EOFException eoferr) {

            /***********************************************************************************
             * The following exception is raised when we hit the end of input file. Once we reach this
             * point, we close the input file, close the filter ports and exit.
             ***********************************************************************************/

            System.out.println("\n" + this.getName() + "::End of file reached...");
            try {
                in.close();
                closeOutputPorts();
                System.out.println("\n" + this.getName() + "::Read file complete, bytes read::" +
                        bytesRead + " bytes written: " + bytesWritten);
            } catch (Exception closeerr) {

                /***********************************************************************************
                 * The following exception is raised should we have a problem closing the file.
                 ***********************************************************************************/

                System.out.println(
                        "\n" + this.getName() + "::Problem closing input data file::" + closeerr);
            } // try-catch
        } catch (IOException iox) {

            /***********************************************************************************
             * The following exception is raised should we have a problem openinging the file.
             ***********************************************************************************/

            System.out.println("\n" + this.getName() + "::Problem reading input data file::" + iox);
        } // try-catch
    } // run
} // SourceFilter
