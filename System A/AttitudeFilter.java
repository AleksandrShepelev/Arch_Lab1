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
    protected double convertData(double inputValue) {
        return inputValue * 0.3048;
    }

    @Override
    protected int getMeasurementId() {
        return ATTITUDE_ID;
    }

} // AttitudeFilter
