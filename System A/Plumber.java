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
 * Here we use five filters - a source which reads data, frameFilter - removes velocity, pressure and bank data from stream,
 * temperature - converts temperature from Farenhaits to Celcius, attitude - converts attitude from feets to meters
 * and sink - writes results to file
 * All filters connected sequentally
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
        TemperatureFilter temperature = new TemperatureFilter();
        AttitudeFilter attitude = new AttitudeFilter();
        SystemASink sink = new SystemASink();
        FrameFilter frameFilter = new FrameFilter();

        /****************************************************************************
         * Here we connect the filters .
         ****************************************************************************/

        //System A test
        sink.connect(attitude); // This esstially says, "connect sink input port to attitude output port
        attitude.connect(temperature); // This esstially says, "connect attitude input port to temperature output port
        temperature.connect(frameFilter); // This esstially says, "connect temperature intput port to frameFilter output port
        frameFilter.connect(source);  // This esstially says, "connect frameFilter intput port to source output port

        /****************************************************************************
         * Here we start the filters up. All-in-all,... its really kind of boring.
         ****************************************************************************/
        //System A start
        source.start();
        frameFilter.start();
        temperature.start();
        attitude.start();
        sink.start();

    } // main
} // Plumber
