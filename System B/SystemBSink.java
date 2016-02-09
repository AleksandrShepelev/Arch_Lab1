import java.io.PrintWriter;

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
 ******************************************************************************************************************/

public class SystemBSink extends SinkFilter {

    private static final String FILE_NAME = "OutputB.dat";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    @Override
    protected int[] getOutputColumns() {
        return new int[] {
            Frame.TIME_ID,
            Frame.TEMPERATURE_ID,
            Frame.ATTITUDE_ID,
            Frame.PRESSURE_ID
        };
    }

    @Override
    protected void writeFileData(PrintWriter fileWriter, Frame currentFrame) {

        fileWriter.write("\n");

        for (Integer anOutputColumn : this.getOutputColumns()) {

            if ((anOutputColumn == Frame.PRESSURE_ID)
                            && (currentFrame.getData().containsKey(Frame.EXTRAPOLATED_PRESSURE))) {
                fileWriter.write(String.format("%-24s", convertToOutput(anOutputColumn,
                        currentFrame.getData().get(Frame.EXTRAPOLATED_PRESSURE)) + "*"));
            } else {
                fileWriter.write(String.format("%-24s", convertToOutput(anOutputColumn,
                        currentFrame.getData().get(anOutputColumn))));
            }
        }
    }
}
