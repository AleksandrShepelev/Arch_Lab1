/******************************************************************************************************************
 * File: Frame.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 * <p>
 * Description:
 * <p>
 * This class represents the frame with all data inside it
 * It helps to manage data in a systematic structured way and access any frame packet in case
 * of data processing necessities. By "Packet" we mean here the structure represented by ID + DATA BYTES (4 + 8)
 ******************************************************************************************************************/

import java.util.Map;
import java.util.HashMap;

public class Frame {

    /**
     * used to indicate empty value inside packet in case if packet is broken
     * for instance, if there is no temperature data inside the packet
     */
    public static final double MISSED_VALUE_INSIDE_PACKET = -999999999;

    public static final int DATA_LENGTH = 8; // This is the length of all measurements (including time) in bytes per one data packet
    public static final int ID_LENGTH = 4; // This is the length of IDs in the byte stream

    public final static int TIME_ID = 0; // Time identity
    public final static int VELOCITY_ID = 1; // Velocity identity
    public final static int ATTITUDE_ID = 2; // Attitude identity
    public final static int PRESSURE_ID = 3; // Pressure identity
    public final static int TEMPERATURE_ID = 4; // Temperature identity
    public final static int BANK_ID = 5; // Bank identity (Like "kren" in Russian analogues)
    public final static int EXTRAPOLATED_PRESSURE = 6; // Pressure that was extrapolated

    private Map<Integer, Double> data = new HashMap<>(); // Hash map with the data

    public Frame() {
    }

    /**
     * This method clones the frame content
     *
     * @param sourceFrame Frame to be copied
     * @return Frame copy of the frame
     */
    public static Frame copyFrom (Frame sourceFrame) {
        Frame copiedFrame = new Frame();

        for (Map.Entry<Integer, Double> entry : sourceFrame.getData().entrySet()) {
            copiedFrame.getData().put(entry.getKey(), entry.getValue());
        }

        return copiedFrame;
    }

    /**
     *
     * @return Map Key-Value storage with data by its ID
     */
    public Map<Integer, Double> getData() {
        return this.data;
    }
}
