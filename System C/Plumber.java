/******************************************************************************************************************
 * File: Plumber.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 * <p>
 * Description:
 * <p>
 * This class serves as an example to illustrate how to use the PlumberTemplate to create a main
 * thread that instantiates and connects a set of filters. This example consists of three filters: a
 * source, a middle filter that acts as a pass-through filter (it does nothing to the data), and a
 * sink filter which illustrates all kinds of useful things that you can do with the input stream of
 * data.
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
        SystemCSink sinkC = new SystemCSink();
        SinkWildPointsCFilter sinkWild = new SinkWildPointsCFilter();
        FrameFilter frameFilter = new FrameFilter();
        AboveFilter aboveFilter = new AboveFilter();
        BelowFilter belowFilter = new BelowFilter();
        SinkAttitudeFilter sinkAttitude = new SinkAttitudeFilter();
        MergerFilter merger = new MergerFilter();


        /****************************************************************************
         * Here we connect the filters starting with the sink filter (filter1) which we connect to
         * filter2 the middle filter. Then we connect Filter2 to the source filter (filter3).
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
