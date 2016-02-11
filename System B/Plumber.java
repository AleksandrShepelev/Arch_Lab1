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
 * Here we use seven filters - a source which reads data, frameFilter - removes velocity, pressure and bank data from stream,
 * temperature - converts temperature from Farenhaits to Celcius, attitude - converts attitude from feet to meters,
 * extrapolator - check pressure for invalid data and exhanges invalid data by calculated valid data,
 * sinkB - writes correct results to file, sinkWild - writes invalid results to separate file
 * All filters connected sequentially except for two sinks (sinkB and sinkWild) that are connected to extrapolator simultaneously
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

        SourceFilter source = new SourceFilter();
        ExtrapolatorFilter extrapolator = new ExtrapolatorFilter();
        TemperatureFilter temperature = new TemperatureFilter();
        AttitudeFilter attitude = new AttitudeFilter();
        SystemBSink sinkB = new SystemBSink("OutputB.dat");
        SinkWildPointsFilter sinkWild = new SinkWildPointsFilter("WildPoints.dat");
        FrameFilter frameFilter = new FrameFilter();


        /****************************************************************************
         * Here we connect the filters
         ****************************************************************************/
        sinkWild.connect(extrapolator);  // This essentially says, "connect sinkWild input port to extrapolator output port
        sinkB.connect(extrapolator); // This essentially says, "connect sinkB input port to extrapolator output port
        extrapolator.connect(attitude); // This essentially says, "connect extrapolator input port to attitude output port
        attitude.connect(temperature); // This essentially says, "connect attitude input port to temperature output port
        temperature.connect(frameFilter); // This essentially says, "connect temperature input port to frameFilter output port
        frameFilter.connect(source); // This essentially says, "connect frameFilter input port to source output port

        /****************************************************************************
         * Here we start the filters up. All-in-all,... its really kind of boring.
         ****************************************************************************/
        sinkWild.start();
        extrapolator.start();
        source.start();
        frameFilter.start();
        temperature.start();
        attitude.start();
        sinkB.start();
    } // main
} // Plumber
