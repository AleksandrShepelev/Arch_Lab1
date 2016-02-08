/******************************************************************************************************************
 * File: Frame.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 *
 * Description:
 *
 * This class represents the frame with all data inside it
 * It helps to manage data in a systematic structured way and access any frame packet in case
 * of data processing necessities
 *
 *
 ******************************************************************************************************************/

import java.util.Map;
import java.util.HashMap;

public class Frame {

    //72 bytes per frame (4 + 8) * 6 = 72

    public static final int DATA_LENGTH = 8; // This is the length of all measurements (including time) in bytes per one data packet
    public static final int ID_LENGTH = 4; // This is the length of IDs in the byte stream
    public static final int PACKETS = 6; // How many data packets inside one frame (including time)

    public final static int TIME_ID = 0;
    public final static int VELOCITY_ID = 1;
    public final static int ATTITUDE_ID = 2;
    public final static int PRESSURE_ID = 3;
    public final static int TEMPERATURE_ID = 4;
    public final static int BANK_ID = 5;

    protected Map <Integer, Double> data = new HashMap<>();

    public Frame () {}

    public synchronized Map <Integer, Double> getData() {
        return this.data;
    }
}
