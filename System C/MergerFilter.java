import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MergerFilter extends SystemFilter{

    public void run() {
        boolean sourcesExist = true;

        ArrayList<Frame> mergedSortedFrames = new ArrayList<>();

        Frame currentFrame;

        Map<Integer, Boolean> portsToClose = new HashMap<>();

        while (sourcesExist) {
            for (int portNum = 0; portNum < this.getTotalNumberOfInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                currentFrame = this.readCurrentFrame(portNum);
                //collecting and sorting all frames into ArrayList
                Utils.InsertIntoSortedList(mergedSortedFrames, currentFrame, Frame.TIME_ID);

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
            }
        }

        //sending sorted and merged frames to next filter
        mergedSortedFrames.forEach(this::transmitCurrentFrame);

        mergedSortedFrames.clear();

        // close all ports
        for (Map.Entry<Integer, Boolean> entry : portsToClose.entrySet()) {
            this.closeInputPort(entry.getKey());
        }

        System.out.print("\n" + this.getClass().getName() + "::Exiting; bytes read: " +
                bytesRead + " bytes written: " + bytesWritten);

    }
}
