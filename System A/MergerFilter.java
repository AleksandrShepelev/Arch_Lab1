/**
 * Created by Aidar on 08.02.2016.
 */
public class MergerFilter extends SystemFilter{
    public void transmitWholeStream(int portNum) {
        try {
            Frame frame;
            while (true){
                frame = readCurrentFrame(portNum);
                System.out.println("\nframe: " + frame.getData());

                transmitCurrentFrame(frame);
            }
        }catch (Exception e){
            System.out.println("Stream was transmitted");
        }

    }

    public void run() {
        //int portCount = getNumberOfOpenedInputPorts();
        Frame lastAFrame;
        Frame lastBFrame;
        lastAFrame = inputPortIsAlive(0) ? this.readCurrentFrame(0) : null;
        lastBFrame = inputPortIsAlive(1) ? this.readCurrentFrame(1) : null;

        while(lastAFrame!=null && lastBFrame!=null){

            if(lastAFrame.getData().get(0) <= lastBFrame.getData().get(0)){
                System.out.println("\nframe: " + lastAFrame.getData());
                transmitCurrentFrame(lastAFrame);
                lastAFrame = readCurrentFrame(0);
            }else {
                System.out.println("\nframe: " + lastBFrame.getData());
                transmitCurrentFrame(lastBFrame);
                lastBFrame = readCurrentFrame(1);
            }
        }
        if(lastBFrame != null){
            transmitWholeStream(1);
        }else {
            if(lastAFrame != null){
                transmitWholeStream(0);
            }
        }
    }
}
