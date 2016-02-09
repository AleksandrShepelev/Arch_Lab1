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
        SystemBSink sink = new SystemBSink();
        SinkWildPointsFilter sinkWild = new SinkWildPointsFilter();
        FrameFilter frameFilter = new FrameFilter();
        AboveFilter aboveFilter = new AboveFilter();
        BelowFilter belowFilter = new BelowFilter();
        SinkAttitudeFilter sinkAttitude = new SinkAttitudeFilter();
        MergerFilter merger = new MergerFilter();

        //test
        merger.connect(sourceA);
        merger.connect(sourceB);
        sink.connect(merger);

        sourceA.start();
        sourceB.start();
        merger.start();
        sink.start();
        //end test

        /****************************************************************************
         * Here we connect the filters starting with the sink filter (filter1) which we connect to
         * filter2 the middle filter. Then we connect Filter2 to the source filter (filter3).
         ****************************************************************************/
/*        sinkWild.connect(extrapolator);
        sink.connect(extrapolator); // This esstially says, "connect sink input port to attitude output port
        extrapolator.connect(attitude);
        attitude.connect(temperature); // This esstially says, "connect attitude input port to temperature output port
        temperature.connect(frameFilter); // This esstially says, "connect temperature intput port to source output port
        frameFilter.connect(aboveFilter);
        merger.connect(sourceA);
        merger.connect(sourceB);
        belowFilter.connect(merger);
        aboveFilter.connect(merger);
        sinkAttitude.connect(belowFilter);
        *//****************************************************************************
         * Here we start the filters up. All-in-all,... its really kind of boring.
         ****************************************************************************//*
        sinkAttitude.start();
        belowFilter.start();
        aboveFilter.start();
        sinkWild.start();
        extrapolator.start();
        sourceA.start();
        sourceB.start();
        frameFilter.start();
        temperature.start();
        attitude.start();
        sink.start();*/
    } // main
} // Plumber
