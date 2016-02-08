/******************************************************************************************************************
 * File: FilterFramework.java
 * Course: MSIT-SE-M-04
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions: 1.0 November 2008 - Initial rewrite of original assignment 1 (ajl).
 *
 * Description:
 * This superclass defines a skeletal filter framework that defines a filter in terms of the input
 * and output ports. All filters must be defined in terms of this framework - that is, filters must
 * extend this class in order to be considered valid system filters. Filters as standalone threads
 * until the input port no longer has any data - at which point the filter finishes up any work it
 * has to do and then terminates.
 *
 * Parameters:
 * inputReadPort: This is the filter's input port. Essentially this port is connected to another
 * filter's piped output steam. All filters connect to other filters by connecting their input ports
 * to other filter's output ports. This is handled by the Connect() method.
 * outputWritePort: This the filter's output port. Essentially the filter's job is to read data from
 * the input port, perform some operation on the data, then write the transformed data on the output
 * port.
 * inputFilter: This is a reference to the filter that is connected to the instance filter's
 * input port. This reference is to determine when the upstream filter has stopped sending data
 * along the pipe.
 *
 * Internal Methods:
 * public void connect(FilterFramework filter)
 * public byte readFilterInputPort()
 * public void writeFilterOutputPort(byte datum)
 * public boolean endOfInputStream()
 *
 ******************************************************************************************************************/

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilterFramework extends Thread {
    // Define filter input and output ports
    private List<PipedInputStream> inputReadPorts = new ArrayList<>();
    private List<PipedOutputStream> outputWritePorts = new ArrayList<>();

    // The following reference to a filter is used because java pipes are able to reliably
    // detect broken pipes on the input port of the filter. This variable will point to
    // the previous filter in the network and when it dies, we know that it has closed its
    // output pipe and will send no more data.

    private List<FilterFramework> inputFilters = new ArrayList<>();

    /***************************************************************************
     * InnerClass:: EndOfStreamExeception
     * 
     * Purpose: This
     *
     * Arguments: none
     *
     * Returns: none
     *
     * Exceptions: none
     *
     ****************************************************************************/

    class EndOfStreamException extends Exception {
        static final long serialVersionUID = 0; // the version for serializing

        EndOfStreamException() {
            super();
        }

        EndOfStreamException(String s) {
            super(s);
        }
    } // class

    /***************************************************************************
     * CONCRETE METHOD:: connect
     * Purpose: This method connects filters to each other. All
     * connections are through the input port of each filter. That is each filter's
     * input port is connected to another filter's output port through this method.
     *
     * Arguments: FilterFramework - this is the filter that this filter will connect to.
     *
     * Returns: void
     *
     * Exceptions: IOException
     *
     ****************************************************************************/

    void connect(FilterFramework filter) {
        try {
            // Connect this filter's input to the upstream pipe's output stream
            PipedInputStream inputReadPort = new PipedInputStream();
            inputReadPort.connect(filter.createOutputPort());
            this.inputFilters.add(filter);
            this.inputReadPorts.add(inputReadPort);
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " FilterFramework error connecting::" + e);
        } // try-catch
    } // connect

    /**
     * @TODO WRITE COMMENTS
     * @return
     */
    private PipedOutputStream createOutputPort() {
        PipedOutputStream outputWritePort = new PipedOutputStream();
        this.outputWritePorts.add(outputWritePort);
        return outputWritePort;
    }

    /***************************************************************************
     * CONCRETE METHOD:: readFilterInputPort
     * 
     * Purpose: This method reads data from the input port one byte at a time.
     *
     * Arguments: void
     *
     * Returns: byte of data read from the input port of the filter.
     *
     * Exceptions: IOExecption, EndOfStreamException (rethrown)
     *
     ****************************************************************************/

    byte readFilterInputPort(int portNum) throws EndOfStreamException {
        byte datum = 0;

        /***********************************************************************
         * Since delays are possible on upstream filters, we first wait until there is data
         * available on the input port. We check,... if no data is available on the input port we
         * wait for a quarter of a second and check again. Note there is no timeout enforced here at
         * all and if upstream filters are deadlocked, then this can result in infinite waits in
         * this loop. It is necessary to check to see if we are at the end of stream in the wait
         * loop because it is possible that the upstream filter completes while we are waiting. If
         * this happens and we do not check for the end of stream, then we could wait forever on an
         * upstream pipe that is long gone. Unfortunately Java pipes do not throw exceptions when
         * the input pipe is broken.
         ***********************************************************************/

        PipedInputStream inputReadPort = this.inputReadPorts.get(portNum);

        try {
            while (inputReadPort.available() == 0) {
                if (endOfInputStream(portNum)) {
                    throw new EndOfStreamException("End of input stream reached");
                } //if
                sleep(250);
            } // while
        } catch (EndOfStreamException e) {
            throw e;
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " Error in read port wait loop::" + e);
        } // try-catch

        /***********************************************************************
         * If at least one byte of data is available on the input pipe we can read it. We read and
         * write one byte to and from ports.
         ***********************************************************************/

        try {
            datum = (byte) inputReadPort.read();
            return datum;
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " Pipe read error::" + e);
            return datum;
        } // try-catch

    } // readFilterPort

    /***************************************************************************
     * CONCRETE METHOD:: writeFilterOutputPort
     * 
     * Purpose: This method writes data to the output port one byte at a time.
     *
     * Arguments: byte datum - This is the byte that will be written on the output port
     * of the filter.
     *
     * Returns: void
     *
     * Exceptions: IOException
     *
     ****************************************************************************/

    private void writeFilterOutputPort(int portNum, byte datum) {
        try {
            PipedOutputStream outputWritePort = this.outputWritePorts.get(portNum);
            outputWritePort.write((int) datum);
            outputWritePort.flush();
        } catch (Exception Error) {
            System.out.println("\n" + this.getName() + " Pipe write error::" + Error);
        } // try-catch

        return;
    } // writeFilterPort

    /**
     * @TODO COMMENTS
     * @param datum
     */
    void writeFilterOutputPortsAll (byte datum) {
        for (int i = 0; i < this.outputWritePorts.size(); i++) {
            this.writeFilterOutputPort(i, datum);
        }
    }


    boolean inputPortIsAlive (int portNum) {
        return this.inputReadPorts.get(portNum) != null;
    }

    /***************************************************************************
     * CONCRETE METHOD:: endOfInputStream
     * 
     * Purpose: This method is used within this framework which
     * is why it is private It returns a true when there is no more data to read on the input port
     * of the instance filter. What it really does is to check if the upstream filter is still
     * alive. This is done because Java does not reliably handle broken input pipes and will often
     * continue to read (junk) from a broken input pipe.
     *
     * Arguments: void
     *
     * Returns: A value of true if the previous filter has stopped sending data, false if it is
     * still alive and sending data.
     *
     * Exceptions: none
     *
     ****************************************************************************/

    private boolean endOfInputStream(int portNum) {
        FilterFramework inputFilter = this.inputFilters.get(portNum);
        return !inputFilter.isAlive();
    } // endOfInputStream

    int getNumberOfInputPorts() {
        return this.inputReadPorts.size();
    }

    /***************************************************************************
     * CONCRETE METHOD:: closePorts
     * 
     * Purpose: This method is used to close the input and output ports of the filter.
     * It is important that filters close their ports before the filter thread exits.
     *
     * Arguments: void
     *
     * Returns: void
     *
     * Exceptions: IOExecption
     *
     ****************************************************************************/

    void closeOutputPorts() {
        try {
            for (PipedOutputStream outputWritePort : this.outputWritePorts) {
                outputWritePort.close();
            }
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " ClosePorts error::" + e);
        } // catch
    } // closePorts

    /**
     *
     * @TODO comments
     *
     * @param portNum
     */
    void closeInputPort(int portNum) {
        PipedInputStream inputReadPort = this.inputReadPorts.get(portNum);
        if (inputReadPort != null) {
            try {
                this.inputReadPorts.set(portNum, null);
                boolean alivePortExist = false;
                for (PipedInputStream inputPort : this.inputReadPorts) {
                    if (inputPort != null) {
                        alivePortExist = true;
                        break;
                    }
                }
                if (!alivePortExist) {
                    this.closeOutputPorts();
                }
            } catch (Exception e) {
                System.out.println("\n" + this.getName() + " ClosePorts error::" + e);
            } // catch
        }
    }

    /***************************************************************************
     * CONCRETE METHOD:: run
     * 
     * Purpose: This is actually an abstract method defined by Thread. It is
     * called when the thread is started by calling the Thread.start() method. In this case, the
     * run() method should be overridden by the filter programmer using this framework superclass
     *
     * Arguments: void
     *
     * Returns: void
     *
     * Exceptions: IOExecption
     *
     ****************************************************************************/

    public void run() {
        // The run method should be overridden by the subordinate class. Please
        // see the example applications provided for more details.

    } // run

} // FilterFramework class
