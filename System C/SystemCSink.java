/******************************************************************************************************************
 * File: SystemCSink.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 09.02.16
 * <p>
 * Description:
 * <p>
 *
 * in this class we change name of file of reused from previous system sink filter
 ******************************************************************************************************************/

public class SystemCSink extends SystemBSink {

    private static final String FILE_NAME = "OutputC.dat";

    @Override
    protected String getFileName() {
        return FILE_NAME;
    }
}
