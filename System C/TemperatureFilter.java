/******************************************************************************************************************
 * File: TemperatureFilter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 * <p>
 * Description:
 * <p>
 * This class represents the filter responsible for converting temperature from Fahrenheit to Celsius
 * by means of implementing parent method convertData
 ******************************************************************************************************************/

public class TemperatureFilter extends DataConverter {

    @Override
    protected void convertData(Frame frameToProcess) {
        double value = Frame.MISSED_VALUE_INSIDE_PACKET;
        if (frameToProcess.getData().containsKey(this.getMeasurementId())) {
            value = frameToProcess.getData().get(this.getMeasurementId());
            value = (value - 32) / 1.8;
        }
        frameToProcess.getData().put(this.getMeasurementId(), value);
    }

    @Override
    protected int getMeasurementId() {
        return Frame.TEMPERATURE_ID;
    }

} // TemperatureFilter
