/******************************************************************************************************************
 * File: Plumber.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 * <p>
 * Description:
 * <p>
 * This class creates a main thread that instantiates and connects a set of filters.
 * Here we use twelve filters - a sourceA which reads data from one file, sourceB which reads data from second file,
 * merger - which merges data from two dataflows and sorts them
 * belowFilter - transmits data below 10k feet, aboveFilter - transmit data above 10k feet
 * frameFilter - removes velocity, pressure and bank data from stream,
 * temperature - converts temperature from Farenhaits to Celcius, attitude - converts attitude from feet to meters,
 * extrapolator - check pressure for invalid data and exhanges invalid data by calculated valid data,
 * sinkC - writes correct results to file, sinkWild - writes invalid results (with invalid pressure) to separate file,
 * sinkAttitude - writes data less then 10k feet in another file
 * <p>
 * Parameters: None
 * <p>
 * Internal Methods: None
 ******************************************************************************************************************/
public class Plumber {
    public static void main(String argv[]) {
        /****************************************************************************
         * Here we instantiate four filters.
         ****************************************************************************/

        SourceFilter sourceA = new SourceFilter("SubSetA.dat");
        SourceFilter sourceB = new SourceFilter("SubSetB.dat");

        ExtrapolatorFilter extrapolator = new ExtrapolatorFilter();
        TemperatureFilter temperature = new TemperatureFilter();
        AttitudeFilter attitude = new AttitudeFilter();
        ExtrapolatorSink sinkC = new ExtrapolatorSink("OutputC.dat");
        SinkWildPointsFilter sinkWild = new SinkWildPointsFilter("PressureWildPoints.dat");
        FrameFilter frameFilter = new FrameFilter();
        AboveFilter aboveFilter = new AboveFilter();
        BelowFilter belowFilter = new BelowFilter();
        SinkAttitudeFilter sinkAttitude = new SinkAttitudeFilter("LessThan10K.dat");
        MergerFilter merger = new MergerFilter();


        /****************************************************************************
         * Here we connect the filters
         ****************************************************************************/
        merger.connect(sourceA);
        merger.connect(sourceB);

        belowFilter.connect(merger);
        aboveFilter.connect(merger);

        sinkAttitude.connect(belowFilter);

        frameFilter.connect(aboveFilter);
        temperature.connect(frameFilter);
        attitude.connect(temperature);
        extrapolator.connect(attitude);
        sinkWild.connect(extrapolator);
        sinkC.connect(extrapolator);

        /****************************************************************************
         * Here we start the filters up. All-in-all,... its really kind of boring.
         ****************************************************************************/

        sourceA.start();
        sourceB.start();

        merger.start();
        belowFilter.start();
        aboveFilter.start();

        frameFilter.start();
        temperature.start();
        attitude.start();
        extrapolator.start();

        sinkC.start();
        sinkWild.start();
        sinkAttitude.start();
    } // main
} // Plumber
