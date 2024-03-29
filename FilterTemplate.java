/******************************************************************************************************************
 * File: FilterTemplate.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 *
 * Description:
 *
 * This is filter for unnecessary data
 *
 *
 ******************************************************************************************************************/

public class FilterTemplate extends SystemFilter {

    public void run() {

        boolean sourcesExist = true;
        Frame currentFrame;

        while (sourcesExist) {
            /*************************************************************
             * Here we read the data byte by byte and buffer them
             * inside the Frame structure
             *
             **************************************************************/

            for (int portNum = 0; portNum < this.getTotalNumberOfInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                // this is the frame read from input port
                currentFrame = this.readCurrentFrame(portNum);

                /**
                 * YOUR CODE GOES HERE
                 */

                // some code
                // another code

                // we ALWAYS should transmit frame before closing port, because if it is the last port
                // it will also close the output port and it can hurt...
                // so if you need to process a lot of data here you'd better collect it (for instance many frames)
                // then process and only after all data is processed transmit frames one by one
                // and don't forget to take the towell and check for ports to close
                this.transmitCurrentFrame (currentFrame);

                // actually this closes the port
                this.checkInputPortForClose (portNum);

                // checks if we're done here
                if (this.getNumberOfOpenedInputPorts() < 1) {

                    System.out.print("\n" + this.getClass().getName() + "::Exiting; bytes read: " +
                            bytesRead + " bytes written: " + bytesWritten);

                    sourcesExist = false;
                    break;
                }
            }

        } // while
    } // run
}
