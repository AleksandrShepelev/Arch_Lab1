import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/******************************************************************************************************************
 * File: FilterTemplate.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 * <p>
 * Description:
 * <p>
 * This filter checks pressure data on invalid numbers
 * number is invalid when it is negative or it differs from previous sample on more then 10 psi
 * if invalid number in the beginning then it is exchanged to first meet valid.
 * If in file is no valid data, then all data exchanged by 0
 * if invalid number in the end of stream it is exchanged by last valid data
 * if invalid number in the middle than it exchanged on extrapolated number (between two valids) - in this case data consider
 * valid only when next pair of positive data with low delta (<10 psi) is found
 * (in this case first number in pair marked as invalid, second - valid)
 *
 ******************************************************************************************************************/

public class ExtrapolatorFilter extends SystemFilter {

    public void run() {

        boolean sourcesExist = true;
        Frame currentFrame;
        final int maxVariance = 10;
        Frame lastValidFrame = null;
        Frame previousFrame = null;
        List<Frame> invalidFrames = new ArrayList<>();
        List<Frame> validFrames = new ArrayList<>();

        Map<Integer, Boolean> portsToClose = new HashMap<>();

        while (sourcesExist) {

            for (int portNum = 0; portNum < this.getTotalNumberOfInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                // this is the frame read from input port
                currentFrame = this.readCurrentFrame(portNum);

                //check frame
                if (currentFrame.getData().get(Frame.PRESSURE_ID) < 0) {
                    invalidFrames.add(Frame.copyFrom(currentFrame));
                } else {
                    if (lastValidFrame == null) { // DONE !
                        lastValidFrame = Frame.copyFrom(currentFrame);

                        // to preserve the order we should put to the valid list all previously invalid
                        for (Frame frame : invalidFrames) {
                            frame.getData().put(Frame.EXTRAPOLATED_PRESSURE, lastValidFrame.getData().get(Frame.PRESSURE_ID));
                            validFrames.add(frame);
                        }

                        // clear the list
                        invalidFrames.clear();

                        validFrames.add(Frame.copyFrom(currentFrame));

                    } else {
                        double currentValue = currentFrame.getData().get(Frame.PRESSURE_ID);
                        double previousValue = previousFrame.getData().get(Frame.PRESSURE_ID);
                        if (Math.abs(previousValue - currentValue) > maxVariance) {
                            invalidFrames.add(Frame.copyFrom(currentFrame));
                        } else {
                            double lastValidValue = lastValidFrame.getData().get(Frame.PRESSURE_ID);
                            double delta = Math.abs(currentValue - lastValidValue) / (invalidFrames.size() + 1);

                            for (int i = 0; i < invalidFrames.size(); i++) {
                                Frame frame = invalidFrames.get(i);
                                frame.getData().put(Frame.EXTRAPOLATED_PRESSURE, lastValidFrame.getData().get(Frame.PRESSURE_ID) + delta * (i+1));
                                validFrames.add(frame);
                            }

                            // clear the list
                            invalidFrames.clear();

                            lastValidFrame = Frame.copyFrom(currentFrame);

                            validFrames.add(Frame.copyFrom(currentFrame));
                        }
                    }
                }

                previousFrame = Frame.copyFrom(currentFrame);

                if (this.endOfStreamInPort(portNum)) {
                    portsToClose.put(portNum, true);
                }

                sourcesExist = false;
                for (int j = 0; j < this.getTotalNumberOfInputPorts(); j++) {
                    if (!portsToClose.containsKey(j)) {
                        sourcesExist = true;
                        break;
                    }
                }

            } // for
        } // while

        //check for invalids
        double lastCorrectValue = 0;
        if (lastValidFrame != null) {
            lastCorrectValue = lastValidFrame.getData().get(Frame.PRESSURE_ID);
        }

        // to preserve the order we should put to the valid list all previously invalid
        for (Frame frame : invalidFrames) {
            frame.getData().put(Frame.EXTRAPOLATED_PRESSURE, lastCorrectValue);
            validFrames.add(frame);
        }

        // clear the list
        invalidFrames.clear();

        validFrames.forEach(this::transmitCurrentFrame);

        validFrames.clear();

        // close all ports
        for (Map.Entry<Integer, Boolean> entry : portsToClose.entrySet()) {
            this.closeInputPort(entry.getKey());
        }

        System.out.print("\n" + this.getClass().getName() + "::Exiting; bytes read: " +
                bytesRead + " bytes written: " + bytesWritten);

    } // run
}
