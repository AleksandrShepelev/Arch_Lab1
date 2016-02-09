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

        double value;

        for (Integer anOutputColumn : this.getOutputColumns()) {
            value = 0;
            if ((anOutputColumn == Frame.PRESSURE_ID)
                            && (currentFrame.getData().containsKey(Frame.EXTRAPOLATED_PRESSURE))) {
                if (currentFrame.getData().containsKey(Frame.EXTRAPOLATED_PRESSURE)) {
                    value = currentFrame.getData().get(Frame.EXTRAPOLATED_PRESSURE);
                }
                fileWriter.write(String.format("%-24s", convertToOutput(anOutputColumn, value) + "*"));
            } else {
                if (currentFrame.getData().containsKey(anOutputColumn)) {
                    value = currentFrame.getData().get(anOutputColumn);
                }
                fileWriter.write(String.format("%-24s", convertToOutput(anOutputColumn, value)));
            }
        }
    }
}
