/******************************************************************************************************************
 * File: SystemASink.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 09.02.16
 * <p>
 * Description:
 * <p>
 *This filter is realisation of abstract sink filter. it specifies name of the file where we have to write
 * which columns we need to write and algoritm of writing
 ******************************************************************************************************************/

import java.io.PrintWriter;

public class SystemASink extends SinkFilter {

    /*
    name of output file
     */
    private static final String FILE_NAME = "OutputA.dat";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

    /*
    columns id's that have to be written
     */
    @Override
    protected int[] getOutputColumns() {
        return new int[] {
            Frame.TIME_ID,
            Frame.TEMPERATURE_ID,
            Frame.ATTITUDE_ID
        };
    }

    /*
    how we should write data to output file (sometimes order of writing might be different from standard
     or some additional symbols need to be added)
     in this case, format of writing is standard: for each column data is written without any changes
     */
    @Override
    protected void writeFileData(PrintWriter fileWriter, Frame currentFrame) {

        fileWriter.write("\n");

        for (Integer anOutputColumn : this.getOutputColumns()) {
            fileWriter.write(String.format("%-24s",
                    convertToOutput(anOutputColumn, currentFrame.getData().get(anOutputColumn))));
        }
    }
}
