import java.io.File;

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
 * temperature - converts temperature from Farenhaits to Celcius, attitude - converts attitude from feet to meters
 * and sink - writes results to file
 * All filters connected sequentially
 * <p>
 * Parameters: None
 * <p>
 * Internal Methods: None
 ******************************************************************************************************************/
public class Plumber {

    public static void main(String argv[]) {
        /***
         * default output filename
         */
        String inputFile = "FlightData.dat";

        if (argv.length>0) {
            inputFile = argv[0];
        }

        if (!checkFile(inputFile)) {
            System.out.println("Application terminated");
            return;
        }
        /****************************************************************************
         * Here we instantiate four filters.
         ****************************************************************************/

        SourceFilter source = new SourceFilter(inputFile);
        TemperatureFilter temperature = new TemperatureFilter();
        AttitudeFilter attitude = new AttitudeFilter();
        SystemASink sink = new SystemASink("OutputA.dat");
        FrameFilter frameFilter = new FrameFilter();

        /****************************************************************************
         * Here we connect the filters .
         ****************************************************************************/

        //System A test
        sink.connect(attitude); // This essentially says, "connect sink input port to attitude output port
        attitude.connect(temperature); // This essentially says, "connect attitude input port to temperature output port
        temperature.connect(frameFilter); // This essentially says, "connect temperature input port to frameFilter output port
        frameFilter.connect(source);  // This essentially says, "connect frameFilter itput port to source output port

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

    private static boolean checkFile(String fileName) {
        File f = new File(fileName);
        if (!(f.exists() & !f.isDirectory())) {
            System.out.println("File " + fileName + " does not exist");
            return false;
        } else {
            return true;
        }
    }
} // Plumber
