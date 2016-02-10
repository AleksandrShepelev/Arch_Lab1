/******************************************************************************************************************
 * File: SinkAttitudeFilter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 09.02.16
 * <p>
 * Description:
 * <p>
 *
 * this class is realization of abstract Sink class which writes data that have less than 10k feet attitude in separate file
 *
 ******************************************************************************************************************/

import java.io.PrintWriter;

public class SinkAttitudeFilter extends SinkFilter {

    /*
    this procedure overrides getHeader method in sinkFilter because it returns 'attitude(m)' header but we need feets here
     */
    @Override
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
    /*
    name of file to write
    */
    @Override
    protected String getFileName() {
        return "LessThan10K.dat";
    }

    /*
    columns to be written
     */
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
