/******************************************************************************************************************
 * File: ExtrapolatorSink.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 09.02.16
 * <p>
 * Description:
 * <p>
 *
 * this class writes correct data to file. If pressure were extrapolated than it writes extrapolated value and
 * and '*' symbol to this data
 ******************************************************************************************************************/
import java.io.PrintWriter;

public class ExtrapolatorSink extends SinkFilter {

    public ExtrapolatorSink(String fileName) {
        super(fileName);
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

    /**
     *in this method we write data to file. If extrapolated data exists we write them with "*"
     * otherwise we write original pressure data
     */
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
