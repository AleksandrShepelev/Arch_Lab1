import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aidar on 08.02.2016.
 */
public class MergerFilter extends SystemFilter{

    public void run() {
        boolean sourcesExist = true;

        ArrayList<Frame> mergedSortedFrames = new ArrayList<>();

        Frame currentFrame;

        while (sourcesExist) {
            for (int portNum = 0; portNum < this.getNumberOfOpenedInputPorts(); portNum++) {

                if (!this.inputPortIsAlive(portNum)) {
                    continue;
                }

                currentFrame = this.readCurrentFrame(portNum);
                //System.out.println("\nFrame: " + currentFrame.getData());
                //collecting and sorting all frames into ArrayList
                Utils.InsertIntoSortedList(mergedSortedFrames, currentFrame, Frame.TIME_ID);

                this.checkInputPortForClose (portNum);

                if (this.getNumberOfOpenedInputPorts() < 1) {

                    System.out.print("\n" + this.getClass().getName() + "::Exiting; bytes read: " +
                            bytesRead + " bytes written: " + bytesWritten);

                    sourcesExist = false;
                    break;
                }
            }
        }

        //sending sorted and merged frames to next filter
        System.out.println("size: " + mergedSortedFrames);
        for(Frame item : mergedSortedFrames){
            System.out.println("\nFrame: " + item.getData());
        }
        for(Frame item : mergedSortedFrames){
            //System.out.println("\nFrame: " + item.getData());
            transmitCurrentFrame(item);
        }


    }
}
