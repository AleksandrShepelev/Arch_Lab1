/******************************************************************************************************************
 * File: Plumber.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 06.02.16
 *
 * Description:
 *
 * This class serves as an example to illustrate how to use the PlumberTemplate to create a main
 * thread that instantiates and connects a set of filters. This example consists of three filters: a
 * source, a middle filter that acts as a pass-through filter (it does nothing to the data), and a
 * sink filter which illustrates all kinds of useful things that you can do with the input stream of
 * data.
 *
 * Parameters: None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/
public class Plumber {
    public static void main(String argv[]) {
        /****************************************************************************
         * Here we instantiate four filters.
         ****************************************************************************/

        SourceFilter source = new SourceFilter();
        TemperatureFilter temperature = new TemperatureFilter();
        AttitudeFilter attitude = new AttitudeFilter();
        SinkFilter sink = new SinkFilter();

        /****************************************************************************
         * Here we connect the filters starting with the sink filter (filter1) which we connect to
         * filter2 the middle filter. Then we connect Filter2 to the source filter (filter3).
         ****************************************************************************/

        sink.connect(attitude); // This esstially says, "connect sink input port to attitude output port
        attitude.connect(temperature); // This esstially says, "connect attitude input port to temperature output port
        temperature.connect(source); // This esstially says, "connect temperature intput port to source output port

        /****************************************************************************
         * Here we start the filters up. All-in-all,... its really kind of boring.
         ****************************************************************************/

        source.start();
        temperature.start();
        attitude.start();
        sink.start();
    } // main
} // Plumber
