/******************************************************************************************************************
 * File: SinkWildPointsCFilter.java
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

public class SinkWildPointsCFilter extends SinkWildPointsFilter {

    private static final String FILE_NAME = "PressureWildPoints.dat";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }

} //  SinkFilter
