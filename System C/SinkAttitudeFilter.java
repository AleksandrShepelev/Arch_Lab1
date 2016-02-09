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

import java.io.PrintWriter;

public class SinkAttitudeFilter extends SinkFilter {

    protected String getHeader(int id) {
        switch (id) {
            case Frame.TIME_ID:
                return "Time:";
            case Frame.VELOCITY_ID:
                return "Velocity (sec):";
            case Frame.ATTITUDE_ID:
                return "Attitude (feet):";
            case Frame.PRESSURE_ID:
                return "Pressure(psi):";
            case Frame.TEMPERATURE_ID:
                return "Temperature(C):";
            case Frame.BANK_ID:
                return "Bank(m):";
            default:
                return "Undefined header:";
        }
    }

    @Override
    protected String getFileName() {
        return "LessThan10K.dat";
    }

    @Override
    protected int[] getOutputColumns() {
        return new int[] {
            Frame.TIME_ID,
            Frame.ATTITUDE_ID
        };
    }

    @Override
    protected void writeFileData(PrintWriter fileWriter, Frame currentFrame) {
        fileWriter.write("\n");
        for (Integer anOutputColumn : this.getOutputColumns()) {
            fileWriter.write(String.format("%-24s",
                    convertToOutput(anOutputColumn, currentFrame.getData().get(anOutputColumn))));
        }
    }

} //  SinkFilter
