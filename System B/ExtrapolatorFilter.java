/******************************************************************************************************************
 * File: DataConverter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 *
 * Description:
 *
 * This class represents the filter responsible for converting extrapolating data of any type
 * The binary data comes to the input as a sequence of bytes one-by-one
 * The output data is sent as a sequence of bytes one-by-one
 *
 * To extrapolate specific units children classes should define the ID from data to be extrapolated
 *
 *
 ******************************************************************************************************************/

public abstract class ExtrapolatorFilter extends SystemFilter {

    public void run() {
        int currentId = 0; //Current measurement data id (also for time)
        byte dataByte = 0; // This is the data byte read from the stream

        long measurement; // This is the word used to store all measurements - conversions are illustrated.
        int i; // This is a loop counter

        boolean needToConvert = false; // flag states if there is a need to convert the data block

        // Next we write a message to the terminal to let the world know we are alive...
        System.out.print("\n" + this.getName() + "::" + this.getClass().getName() + " Reading ");

        while (true) {
            /*************************************************************
             * Here we read a byte and write a byte
             * if we meet the necessary data ID to be converted than we stop the output
             * convert the data and then continue transmitting to the output
             **************************************************************/

        } // while
    } // run
}
