/**
 * Created by Aidar on 08.02.2016.
 */
public class MergerFilter extends SystemFilter{
    public void run() {
        //int portCount = getNumberOfOpenedInputPorts();
        try {
            Frame lastAFrame;// = inputPortIsAlive(0) ? this.readCurrentFrame(0) : null;
            Frame lastBFrame;// = inputPortIsAlive(1) ? this.readCurrentFrame(1) : null;
            while((lastAFrame = this.readCurrentFrame(0)) != null
                & (lastBFrame = this.readCurrentFrame(1)) != null){

                if (lastAFrame == null || lastBFrame == null) {
                    break;
                }

                if (lastAFrame.getData().get(0) <= lastBFrame.getData().get(0)) {
                    transmitCurrentFrame(lastAFrame);
                    //lastAFrame= this.readCurrentFrame(0);
                } else {
                    transmitCurrentFrame(lastBFrame);
                    //lastBFrame = this.readCurrentFrame(1);
                }
            }

            if (lastAFrame == null){
                while (true){
                    transmitCurrentFrame(lastBFrame);
                    transmitCurrentFrame(readCurrentFrame(1));
                }
            }
            else {
                while (true){
                    transmitCurrentFrame(lastAFrame);
                    transmitCurrentFrame(readCurrentFrame(0));
                }
            }
        } catch (Exception e) {
            System.out.println("\n" + this.getName() + " MergerFilter:Merging error::" + e);
        }
    }
}
