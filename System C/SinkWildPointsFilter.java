/******************************************************************************************************************
 * File: SystemBSink.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 09.02.16
 * <p>
 * Description:
 * <p>
 *
 * @TODO comments
 *
 ******************************************************************************************************************/

import java.io.PrintWriter;

public class SinkWildPointsFilter extends SinkFilter {

    private static final String FILE_NAME = "WildPoints.dat";

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

    @Override
    protected int[] getOutputColumns() {
        return new int[] {
            Frame.TIME_ID,
            Frame.PRESSURE_ID
        };
    }

} //  SinkFilter
