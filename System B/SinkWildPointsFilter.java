/******************************************************************************************************************
 * File: SinkWildPointsFilter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 09.02.16
 * <p>
 * Description:
 * <p>
 *
 * this class is realization of abstract Sink class which writes invalid data to separate file
 * It understand if data is invalid by existance of extrapolated pressure in frame
 * if extrapolated pressure is in frame than data is invalid and have to be written
 *
 ******************************************************************************************************************/

import java.io.PrintWriter;

public class SinkWildPointsFilter extends SinkFilter {
    /*
    name of file
     */
    private static final String FILE_NAME = "WildPoints.dat";

    /*
    check if extrapolated pressure exists in frame. If true then it writes original value to file
     */
    @Override
    protected void writeFileData(PrintWriter fileWriter, Frame currentFrame) {

        if (currentFrame.getData().containsKey(Frame.EXTRAPOLATED_PRESSURE)) {
            fileWriter.write("\n");
            for (Integer anOutputColumn : this.getOutputColumns()) {
                fileWriter.write(String.format("%-24s", convertToOutput(anOutputColumn,
                        currentFrame.getData().get(anOutputColumn))));
            }
        }
    }

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    /*
    columns that have to be written
     */
    @Override
    protected int[] getOutputColumns() {
        return new int[] {
            Frame.TIME_ID,
            Frame.PRESSURE_ID
        };
    }

} //  SinkFilter
