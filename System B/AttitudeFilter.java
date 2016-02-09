import java.util.Map;

/******************************************************************************************************************
 * File: AttitudeFilter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 *
 * Description:
 *
 * This class represents the filter responsible for converting temperature from feet to meters
 * by means of implementing parent method convertData
 *
 *
 ******************************************************************************************************************/

public class AttitudeFilter extends DataConverter {

    @Override
    protected void convertData(Frame frameToProcess) {
        double value = frameToProcess.getData().get(this.getMeasurementId());
        value *= 0.3048;
        frameToProcess.getData().put(this.getMeasurementId(), value);
    }

    @Override
    protected int getMeasurementId() {
        return Frame.ATTITUDE_ID;
    }

} // AttitudeFilter
